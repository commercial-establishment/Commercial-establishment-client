package kz.hts.ce.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kz.hts.ce.config.AppContext;
import kz.hts.ce.service.GenderService;
import org.springframework.context.ApplicationContext;

public class Main extends Application {

    private ApplicationContext context = AppContext.getInstance();
    private GenderService genderService = (GenderService) context.getBean("genderService");

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 300));
//        primaryStage.getScene().getStylesheets().add("css/JMetroDarkTheme.css");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
