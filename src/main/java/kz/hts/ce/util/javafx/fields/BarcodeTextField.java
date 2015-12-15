package kz.hts.ce.util.javafx.fields;

import javafx.scene.control.TextField;

public class BarcodeTextField extends TextField {

    public static final String REGEX = "^[a-zA-Z0-9]{0,20}$";

    @Override
    public void replaceText(int start, int end, String text) {
        if (matchTest(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (matchTest(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean matchTest(String text) {
        return text.isEmpty() || (text.matches(REGEX) && getText().length() <= 20);
    }
}