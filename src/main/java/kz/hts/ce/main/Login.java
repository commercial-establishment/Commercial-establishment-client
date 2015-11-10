package kz.hts.ce.main;

import javafx.application.Application;
import javafx.stage.Stage;
import kz.hts.ce.util.AppContextSingleton;
import kz.hts.ce.config.UrlConfiguration;
import org.springframework.context.ApplicationContext;

public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContext context = AppContextSingleton.getInstance();
        UrlConfiguration screens = context.getBean(UrlConfiguration.class);
        screens.setPrimaryStage(stage);
        screens.login();
    }
}
