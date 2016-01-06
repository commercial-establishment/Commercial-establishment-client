package kz.hts.ce.util.spring;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.control.Alert;
import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.model.entity.*;
import kz.hts.ce.security.AuthenticationService;
import kz.hts.ce.security.CustomAuthenticationProvider;
import kz.hts.ce.service.BroadcastService;
import kz.hts.ce.service.CategoryService;
import kz.hts.ce.service.ProviderService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static kz.hts.ce.util.JavaUtil.checkConnection;
import static kz.hts.ce.util.javafx.JavaFxUtil.alert;

@Component
public class SpringUtil {

    private String id;
    private Shift shift;
    private Employee employee;
    private boolean newInvoice;
    private String password;

    private List<Provider> providers;
    private List<ShopProvider> shopProviders;

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
    private BroadcastService broadcastService;

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

    private void sendProvidersToServer() {
        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<Provider>> requestEntity = new HttpEntity<>(providers, headers);
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        template.getMessageConverters().add(new StringHttpMessageConverter());
        String url = JavaUtil.URL + "/json/providers/add";
        template.exchange(url, HttpMethod.POST, requestEntity, (Class<List<Provider>>) providers.getClass());
        providers.clear();
    }

    private void sendShopProvidersToServer() {
        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<ShopProvider>> requestEntity = new HttpEntity<>(shopProviders, headers);
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        template.getMessageConverters().add(new StringHttpMessageConverter());
        String url = JavaUtil.URL + "/json/shop-providers/add";
        template.exchange(url, HttpMethod.POST, requestEntity, (Class<List<ShopProvider>>) shopProviders.getClass());
        shopProviders.clear();
    }

    private JsonNode getAllProvidersFromServer() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<ShopProvider>> request = new HttpEntity<>(headers);
        String url = JavaUtil.URL + "/json/providers";
        return restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class).getBody();
    }
//
//    private JsonNode getAllCategoriesFromServer() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = createHeadersForAuthentication();
//        HttpEntity<List<Category>> request = new HttpEntity<>(headers);
//        String url = JavaUtil.URL + "/json/categories";
//        return restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class).getBody();
//    }

    private boolean sendLastBroadcastDateToServer() {
        try {
            HttpHeaders headers = createHeadersForAuthentication();
            List<Broadcast> broadcastDates = broadcastService.findByNearestDate(new Date());/*TODO get 1 date*/
            Date broadcastDate = broadcastDates.get(0).getDate();
            HttpEntity<Date> requestEntity = new HttpEntity<>(broadcastDate, headers);
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            template.getMessageConverters().add(new StringHttpMessageConverter());
            String url = JavaUtil.URL + "/json/last-broadcast-date";
            template.exchange(url, HttpMethod.POST, requestEntity, Date.class);
            return true;
        } catch (ControllerException e) {
            return false;
        }
    }

    private JsonNode getCategoryChangesFromServer() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeadersForAuthentication();
        HttpEntity<List<Category>> request = new HttpEntity<>(headers);
        String url = JavaUtil.URL + "/json/category-changes";
        return restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class).getBody();
    }

    public void checkAndUpdateNewDataFromServer() {
//        try {
//            JsonNode providersFromServerJson = getAllProvidersFromServer();
//            List<Provider> providers = providerService.findAll();
//            List<String> companyNames = providers.stream().map(Provider::getCompanyName).collect(Collectors.toList());
//            ObjectMapper mapper = new ObjectMapper();
//            List<Provider> providersFromServer = mapper.readValue(mapper.treeAsTokens(providersFromServerJson), new TypeReference<List<Provider>>() {
//            });
//            providersFromServer.stream().filter(provider -> !companyNames.contains(provider.getCompanyName()))
//                    .forEach(provider -> providerService.save(provider));
//
//            JsonNode categoriesFromServerJson = getAllCategoriesFromServer();
//            List<Category> categories = categoryService.findAll();
//            List<String> categoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());
//            List<Category> categoriesFromServer = mapper.readValue(mapper.treeAsTokens(categoriesFromServerJson), new TypeReference<List<Category>>() {
//            });
//            categoriesFromServer.stream().filter(category -> !categoryNames.contains(category.getName()))
//                    .forEach(category -> categoryService.save(category));
//        } catch (IOException e) {
//            alert(Alert.AlertType.WARNING, "Данные не синхронизированны", null, "Ошибка синхронизации данных. Обратитесь в службу поддержки.");
//        }
    }

    public void sendDataToServer() {
        if (checkConnection()) {
            if (!getProviders().isEmpty()) sendProvidersToServer();
            if (!getShopProviders().isEmpty()) sendShopProvidersToServer();
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

    public List<Provider> getProviders() {
        if (providers == null) providers = new ArrayList<>();
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public void addProviderInProviders(Provider provider) {
        if (providers == null) providers = new ArrayList<>();
        providers.add(provider);
    }

    public List<ShopProvider> getShopProviders() {
        if (shopProviders == null) shopProviders = new ArrayList<>();
        return shopProviders;
    }

    public void setShopProviders(List<ShopProvider> shopProviders) {
        this.shopProviders = shopProviders;
    }

    public void addShopProviderInShopProviders(ShopProvider shopProvider) {
        if (shopProviders == null) shopProviders = new ArrayList<>();
        shopProviders.add(shopProvider);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}