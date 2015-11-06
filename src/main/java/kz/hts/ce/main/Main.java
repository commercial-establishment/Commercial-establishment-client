//package kz.hts.ce.main;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import kz.hts.ce.config.AppContext;
//import kz.hts.ce.service.GenderService;
//import org.springframework.context.ApplicationContext;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class Main extends Application {
//
//    private ApplicationContext context = AppContext.getInstance();
//    private GenderService genderService = (GenderService) context.getBean("genderService");
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
//            stage.setTitle("Hello World");
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
