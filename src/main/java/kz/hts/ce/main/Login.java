package kz.hts.ce.main;

import javafx.application.Application;
import javafx.stage.Stage;
import kz.hts.ce.config.AppContext;
import kz.hts.ce.config.ScreensConfiguration;
import org.springframework.context.ApplicationContext;

public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContext context = AppContext.getInstance();
        ScreensConfiguration screens = context.getBean(ScreensConfiguration.class);
        screens.setPrimaryStage(stage);
        screens.loginDialog().show();
    }
}
