package kz.hts.ce.util.spring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import kz.hts.ce.model.dto.ShopProductProviderDto;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.security.AuthenticationService;
import kz.hts.ce.security.CustomAuthenticationProvider;
import kz.hts.ce.service.*;
import kz.hts.ce.util.JavaUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static kz.hts.ce.util.JavaUtil.checkConnection;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Component
public class SpringHelper {

    public static Map<String, Role> roleMap;

    private String id;
    private Shift shift;
    private Employee employee;
    private boolean newInvoice;
    private String password;

    private static final Logger log = Logger.getLogger(SpringHelper.class.getName());

    @PostConstruct
    public void initialize() {
        List<Role> roles = roleService.findAll();
        roleMap = new HashMap<>();
        for (Role role : roles) roleMap.put(role.getName(), role);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private ProviderService providerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopProviderService shopProviderService;
    @Autowired
    private ProductProviderService productProviderService;
    @Autowired
    private WarehouseProductService warehouseProductService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TransferService transferService;
    @Autowired
    private EmployeeService employeeService;

    public static String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public void authorize(String username, String password) {
        UserDetails user = authenticationService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            Authentication authToken = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            customAuthenticationProvider.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else throw new NullPointerException();
    }

    private HttpHeaders createHeadersForAuthentication() {
        return new HttpHeaders() {
            {
                String auth = employee.getUsername() + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                setContentType(MediaType.APPLICATION_JSON);
                set("Authorization", authHeader);
            }
        };
    }

    private HttpEntity<Long> createHttpEntityWithAuthHeaders() {
        HttpHeaders headers = createHeadersForAuthentication();
        return new HttpEntity<>(headers);
    }

    private JsonNode getJsonNodeFromServer(long lastTransferDate, String urlPart) {
        HttpEntity<Long> requestEntity = createHttpEntityWithAuthHeaders();
        RestTemplate template = createRestTemplateWithMessageConverters();

        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("time", lastTransferDate);
        String url = JavaUtil.URL + urlPart;/*TODO fix '+'*/

        return template.exchange(url, HttpMethod.GET, requestEntity, JsonNode.class, uriVariables).getBody();
    }

    public void transmitAndReceiveData() {
        if (checkConnection()) {
            Transfer transfer = transferService.findByLastDate();
            long lastTransferDate = 0;
            if (transfer != null) lastTransferDate = transfer.getDate().getTime();
            checkAndUpdateNewDataFromServer(lastTransferDate);
            sendDataToServer(lastTransferDate);
            transferService.saveWithNewDate();
        } else {
            alert(Alert.AlertType.WARNING, "Проверьте интернет соединение", null, "Данные с сервера небыли подгружены.");
        }
    }

    private void sendNewProvidersDataToServer(long lastTransferDate) {
        HttpHeaders headers = createHeadersForAuthentication();

        List<Provider> providers;
        if (lastTransferDate == 0) providers = providerService.findAll();
        else providers = providerService.getHistory(lastTransferDate);
        log.info("Providers' data for server: " + providers);

        HttpEntity<List<Provider>> requestEntity = new HttpEntity<>(providers, headers);
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        template.getMessageConverters().add(new StringHttpMessageConverter());
        String url = JavaUtil.URL + "/replication/providers";
        template.exchange(url, HttpMethod.POST, requestEntity, providers.getClass());
    }

    private JsonNode getAllProvidersFromServer() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<ShopProvider>> request = new HttpEntity<>(headers);
        String url = JavaUtil.URL + "/json/providers";
        return restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class).getBody();
    }

    private void getNewCategoriesDataFromServer(long lastTransferDate) {
        try {
            String urlPart = "/replication/categories/time={time}";
            JsonNode categoriesJson = getJsonNodeFromServer(lastTransferDate, urlPart);
            log.info("category list response: " + categoriesJson);

            ObjectMapper mapper = new ObjectMapper();
            List<Category> categoryList = mapper.readValue(mapper.treeAsTokens(categoriesJson), new TypeReference<List<Category>>() {
            });

            categoryService.saveOrUpdateList(categoryList);
            categoryList.clear();
        } catch (IOException e) {
            alert(Alert.AlertType.WARNING, "Внутренняя ошибка", null, "Обратитесь в тех. поддержку");
        }
    }

    private void getNewProvidersDataFromServer(long lastTransferDate) {
        try {
            String urlPart = "/replication/providers/time={time}";
            JsonNode providersJson = getJsonNodeFromServer(lastTransferDate, urlPart);
            log.info("provider list response: " + providersJson);

            ObjectMapper mapper = new ObjectMapper();
            List<Provider> providerList = mapper.readValue(mapper.treeAsTokens(providersJson), new TypeReference<List<Provider>>() {
            });

            providerService.saveOrUpdateList(providerList);
            providerList.clear();
        } catch (IOException e) {
            alert(Alert.AlertType.WARNING, "Внутренняя ошибка", null, "Обратитесь в тех. поддержку");
        }
    }

    private RestTemplate createRestTemplateWithMessageConverters() {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        template.getMessageConverters().add(new StringHttpMessageConverter());
        return template;
    }

    private JsonNode getCategoryChangesFromServer() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<Category>> request = new HttpEntity<>(headers);
        String url = JavaUtil.URL + "/json/category-changes";
        return restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class).getBody();
    }

    private void sendResiduesToServer(long lastTransferDate) {
        List<WarehouseProduct> warehouseProducts;
        if (lastTransferDate == 0) warehouseProducts = warehouseProductService.findAll();
        else warehouseProducts = warehouseProductService.getHistory(lastTransferDate);

        HttpHeaders headers = createHeadersForAuthentication();
        List<ShopProductProviderDto> residues = new ArrayList<>();
        for (WarehouseProduct warehouseProduct : warehouseProducts) {
            List<ProductProvider> productProviderList = productProviderService.findByProductId(warehouseProduct.getProduct().getId());
            for (ProductProvider productProvider : productProviderList) {
                ShopProductProviderDto shopProductProviderDto = new ShopProductProviderDto();
                shopProductProviderDto.setProductId(warehouseProduct.getProduct().getId());
                shopProductProviderDto.setShopId(employee.getShop().getId());
                shopProductProviderDto.setResidue(warehouseProduct.getResidue());

                shopProductProviderDto.setProductProviderId(productProvider.getId());
                shopProductProviderDto.setProviderId(productProvider.getProvider().getId());
                residues.add(shopProductProviderDto);
            }
        }

        HttpEntity<List<ShopProductProviderDto>> requestEntity = new HttpEntity<>(residues, headers);
        RestTemplate template = createRestTemplateWithMessageConverters();
        String url = JavaUtil.URL + "/replication/residues";
        template.exchange(url, HttpMethod.POST, requestEntity, ArrayList.class);
    }

    private void sendNewProductsDataToServer(long transferDate) {
        List<Product> products;
        if (transferDate == 0) products = productService.findAll();
        else products = productService.getHistory(transferDate);
        log.info("Product data for server: " + products);

        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<Product>> requestEntity = new HttpEntity<>(products, headers);
        RestTemplate template = createRestTemplateWithMessageConverters();
        String url = JavaUtil.URL + "/replication/products";
        template.exchange(url, HttpMethod.POST, requestEntity, ArrayList.class);
    }

    private void sendNewProductProviderListDataToServer(long transferDate) {
        List<ProductProvider> productProviderList;
        if (transferDate == 0) productProviderList = productProviderService.findAll();
        else productProviderList = productProviderService.getHistory(transferDate);
        log.info("Product Provider List data for server: " + productProviderList);

        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<ProductProvider>> requestEntity = new HttpEntity<>(productProviderList, headers);
        RestTemplate template = createRestTemplateWithMessageConverters();
        String url = JavaUtil.URL + "/replication/product-provider-list";
        template.exchange(url, HttpMethod.POST, requestEntity, ArrayList.class);
    }

    private void sendNewEmployeesDataToServer(long transferDate) {
        List<Employee> employees;
        if (transferDate == 0) employees = employeeService.findAll();
        else employees = employeeService.getHistory(transferDate);
        log.info("Employees data for server: " + employees);

        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<Employee>> requestEntity = new HttpEntity<>(employees, headers);
        RestTemplate template = createRestTemplateWithMessageConverters();
        String url = JavaUtil.URL + "/replication/employees";
        template.exchange(url, HttpMethod.POST, requestEntity, ArrayList.class);
    }

    private void sendNewShopProviderListDataToServer(long transferDate) {
        List<ShopProvider> shopProviderList;
        if (transferDate == 0) shopProviderList = shopProviderService.findAll();
        else shopProviderList = shopProviderService.getHistory(transferDate);
        log.info("Shop Product List data for server: " + shopProviderList);

        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<ShopProvider>> requestEntity = new HttpEntity<>(shopProviderList, headers);
        RestTemplate template = createRestTemplateWithMessageConverters();
        String url = JavaUtil.URL + "/replication/shop-provider-list";
        template.exchange(url, HttpMethod.POST, requestEntity, ArrayList.class);
    }

    private void checkAndUpdateNewDataFromServer(long lastTransferDate) {
        getNewCategoriesDataFromServer(lastTransferDate);
        getNewProvidersDataFromServer(lastTransferDate);
        /*TODO shop_provider data from server*/
        /*TODO product_provider data from server*/
        /*TODO product data from server*/
        /*TODO shop data from server*/
    }

    private void sendDataToServer(long transferDate) {
        if (checkConnection()) {
            sendNewProvidersDataToServer(transferDate);
            sendNewProductsDataToServer(transferDate);
            sendNewProductProviderListDataToServer(transferDate);
            sendNewEmployeesDataToServer(transferDate);
            sendNewShopProviderListDataToServer(transferDate);
            sendResiduesToServer(transferDate);
        } else
            alert(Alert.AlertType.ERROR, "Проверьте интернет соединение", null, "Данные небыли переданы на сервер. Проверьте интернет соединение.");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isNewInvoice() {
        return newInvoice;
    }

    public void setNewInvoice(boolean newInvoice) {
        this.newInvoice = newInvoice;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}