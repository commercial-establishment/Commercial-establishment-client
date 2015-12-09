package kz.hts.ce.util.javafx;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import kz.hts.ce.model.dto.ProductDto;

import java.math.BigDecimal;

import static kz.hts.ce.util.JavaUtil.stringToBigDecimal;

public class EditingBigDecimalCell  extends TableCell<ProductDto, BigDecimal> {

    private final TextField mTextField;

    public EditingBigDecimalCell() {
        super();
        mTextField = new TextField();
        mTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                commitEdit(stringToBigDecimal(mTextField.getText()));
        });

        mTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                commitEdit(stringToBigDecimal(mTextField.getText()));
        });
        mTextField.textProperty().bindBidirectional(textProperty());
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setGraphic(mTextField);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }

    @Override
    public void updateItem(final BigDecimal item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item == null) {
                setGraphic(null);
            } else {
                if (isEditing()) {
                    setGraphic(mTextField);
                    setText(String.valueOf(getItem()));
                } else {
                    setGraphic(null);
                    setText(String.valueOf(getItem()));
                }
            }
        }
    }
}
