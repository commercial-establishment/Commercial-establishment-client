package kz.hts.ce.main;

import javafx.application.Application;
import javafx.stage.Stage;
import kz.hts.ce.config.PagesConfiguration;

import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        PagesConfiguration screens = getPagesConfiguration();
        screens.setPrimaryStage(stage);
        screens.login();
    }
}
