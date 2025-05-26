package com.example.mangatool.ui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import static com.example.mangatool.common.TextConfig.*;

public class SmallInputRow extends HBox {
    public TextField textField;
    public SmallInputRow(String title) {
        Text titleText = new Text(title);
        textField = new TextField();
        textField.setPrefWidth(small_text_field_pref_width);

        this.setPadding(new Insets(default_padding));
        this.setSpacing(default_spacing);
        this.setAlignment(Pos.BASELINE_CENTER);
        this.getChildren().addAll(titleText, textField);
    }

    public SmallInputRow(String title, String defaultTextFieldData) {
        this(title);
        this.textField.setText(defaultTextFieldData);
    }

    public String getText(){
        return this.textField.getText();
    }

}
