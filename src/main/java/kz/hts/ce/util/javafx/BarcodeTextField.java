package kz.hts.ce.util.javafx;

import javafx.scene.control.TextField;

public class BarcodeTextField extends TextField {

    public BarcodeTextField(){
        this.setPromptText("Только цифры и буквы");
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("^[a-zA-Z0-9]{0,20}$") || text.isEmpty()) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}