package kz.hts.ce.controller.invoice;

import kz.hts.ce.config.PagesConfiguration;
import kz.hts.ce.controller.ControllerException;
import kz.hts.ce.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

import static kz.hts.ce.util.spring.SpringFxmlLoader.getPagesConfiguration;

@Controller
public class ReceiptPageController {

    @Autowired
    private MainController mainController;

    public void showReceiptsPage() {
        PagesConfiguration screens = getPagesConfiguration();
        try {
            mainController.getContentContainer().getChildren().setAll(screens.receipts());
        } catch (IOException e) {
            throw new ControllerException(e);
        }
    }
}
