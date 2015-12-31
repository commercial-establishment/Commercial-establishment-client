package kz.hts.ce.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.sale.CalculatorController;
import kz.hts.ce.model.entity.City;
import kz.hts.ce.model.entity.Employee;
import kz.hts.ce.model.entity.Shift;
import kz.hts.ce.service.EmployeeService;
import kz.hts.ce.service.ShiftService;
import kz.hts.ce.service.WarehouseProductHistoryService;
import kz.hts.ce.util.spring.SpringUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;
import static kz.hts.ce.util.spring.SpringUtil.getPrincipal;

@Controller
public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;

    @Autowired
    private ShiftService shiftService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CalculatorController calculatorController;

    @Autowired
    private SpringUtil springUtils;
    @Autowired
    private WarehouseProductHistoryService wphService;

    @FXML
    @Transactional
    private void loginAction(ActionEvent event) throws IOException {
        PagesConfiguration screens = getPagesConfiguration();
        try {
            springUtils.authorize(username.getText(), password.getText());
            Stage stage = new Stage();
            screens.setPrimaryStage(stage);

            Shift shiftEntity = new Shift();
            shiftEntity.setStart(new Date());
            Employee employee = employeeService.findByUsername(getPrincipal());
            springUtils.setEmployee(employee);
            shiftEntity.setEmployee(employee);
            Shift shift = shiftService.save(shiftEntity);
            springUtils.setShift(shift);

            /*GET*/
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders("yakov11", "yakov11");
            HttpEntity<String> request = new HttpEntity<>(headers);
            String url = "http://localhost:8080/employees11";
            String stringFromServer = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
            System.out.println(stringFromServer);
            /*GET*/

            /*POST*/
            City city = new City();
            city.setName("test name");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<City> requestEntity = new HttpEntity<>(city, headers);
            System.out.println(requestEntity);
            RestTemplate restTemplateForPost = new RestTemplate();
            restTemplateForPost.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplateForPost.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<City> responseEntity = restTemplateForPost.exchange(url, HttpMethod.POST, requestEntity, City.class);
            City result = responseEntity.getBody();
            System.out.println(result);
            /*POST*/

            screens.login().hide();
            screens.main().show();
            message.setText("");
            checkConnection();
            calculatorController.startEventHandler(screens.getPrimaryStage().getScene());
        } catch (NullPointerException | UsernameNotFoundException e) {
            message.setText("Неверное имя пользователя или пароль:");
        }
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                setContentType(MediaType.APPLICATION_JSON);
                set("Authorization", authHeader);
            }
        };
    }

    private boolean checkConnection() {
        try {
            URL url = new URL("http://commercial-establishment.rhcloud.com/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            return con.getResponseCode() == 200;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }
}
