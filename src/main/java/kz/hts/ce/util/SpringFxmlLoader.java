package kz.hts.ce.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.config.PersistenceConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class SpringFxmlLoader {

    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PersistenceConfig.class);

    public Object load(String url) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        loader.setLocation(getClass().getResource(url));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showStage(Stage stage, String url){
        SpringFxmlLoader springFxmlLoader = new SpringFxmlLoader();
        Object loader = springFxmlLoader.load(url);
        stage.setScene(new Scene((Parent) loader));
        stage.show();
    }

    public static PagesConfiguration getPagesConfiguration() {
        ApplicationContext context = AppContextSingleton.getInstance();
        return context.getBean(PagesConfiguration.class);
    }
}