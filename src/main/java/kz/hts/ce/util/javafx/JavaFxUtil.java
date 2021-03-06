package kz.hts.ce.util.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class JavaFxUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
    private static BigDecimal left;
    private static String selectedOperator;
    private static boolean numberInputting;

    public static void getWatch(Label dateLabel) {
        Timeline watch = new Timeline(new KeyFrame(Duration.seconds(1), event ->
                dateLabel.setText(simpleDateFormat.format(Calendar.getInstance().getTime()))));
        watch.setCycleCount(Timeline.INDEFINITE);
        watch.play();
    }

    public static void calculator(String buttonText, TextField txtDisplay, TextField txtAdditionalDisplay) {
        if (buttonText.equals("CE")) {
            selectedOperator = "";
            numberInputting = false;
            txtDisplay.setText("");
            txtAdditionalDisplay.setText("");
            return;
        }
        if (buttonText.matches("[0-9]")) {
            if (!txtDisplay.getText().startsWith("0") || txtDisplay.getText().startsWith("0.")) {
                if (txtAdditionalDisplay.getText().equals("")) {
                    if (!numberInputting) {
                        numberInputting = true;
                        txtDisplay.clear();
                    }
                    txtDisplay.appendText(buttonText);
                    return;
                } else {
                    String text = txtAdditionalDisplay.getText();
                    text += buttonText;
                    txtAdditionalDisplay.setText(text);
                    return;
                }
            }
        }
        if (buttonText.matches("[\\.]")) {
            if (txtAdditionalDisplay.getText().equals("")) {
                if (!txtDisplay.getText().contains(".")) {
                    if (!numberInputting) {
                        numberInputting = true;
                        txtDisplay.clear();
                    }
                    txtDisplay.appendText(buttonText);
                    return;
                }
            } else {
                if (!txtAdditionalDisplay.getText().contains(".")) {
                    String text = txtAdditionalDisplay.getText();
                    text += buttonText;
                    txtAdditionalDisplay.setText(text);
                    return;
                }
            }
        }
        if (buttonText.matches("[*]")) {
            if (txtDisplay.getText().equals("")) {
                txtDisplay.setText("0");
            }
            txtAdditionalDisplay.setText(buttonText);
            left = new BigDecimal(txtDisplay.getText());
            selectedOperator = buttonText;
            numberInputting = false;
            return;
        }
    }

    public static void additionalCalculator(String buttonText, TextField txtDisplay) {
        if (buttonText.equals("CE")) {
            selectedOperator = "";
            numberInputting = false;
            txtDisplay.setText("");
            return;
        }
        if (buttonText.matches("[0-9]")) {
            if (txtDisplay.getText().equals("0.00")) {
                txtDisplay.clear();
            }
            if (!numberInputting) {
                numberInputting = true;
                txtDisplay.clear();
            }
            txtDisplay.appendText(buttonText);
            return;
        }
        if (buttonText.matches("[\\.]")) {
            if (!txtDisplay.getText().contains(".")) {
                if (!numberInputting) {
                    numberInputting = true;
                    txtDisplay.clear();
                }
                txtDisplay.appendText(buttonText);
                return;
            }

        }
    }

    public static void alert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);

        if (headerText == null) alert.setHeaderText(null);
        else alert.setHeaderText(headerText);

        alert.setContentText(contentText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
