package kz.hts.ce.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kz.hts.ce.config.AppContext;
import kz.hts.ce.service.GenderService;
import org.springframework.context.ApplicationContext;

public class Main extends Application {

    private ApplicationContext context = AppContext.getInstance();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                GenderService genderService = (GenderService) context.getBean("genderService");
                System.out.println("Gender Name from DB: " + genderService.findById((long) 1).getName());
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
