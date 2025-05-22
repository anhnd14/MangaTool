package com.example.mangatool.AbstractUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import static com.example.mangatool.TextConfig.*;

public abstract class TextFieldAndTwoButtonsHBox extends HBox {
    public TextField textField;
    public Button firstButton;
    public Button secondButton;

    public TextFieldAndTwoButtonsHBox(String title, String firstButtonTitle, String secondButtonTitle) {

        Text titleText = new Text(title);

        textField = new TextField();
        textField.setPrefWidth(long_text_field_pref_width);

        firstButton = new Button(firstButtonTitle);
        secondButton = new Button(secondButtonTitle);

        this.setSpacing(default_spacing);
        this.setAlignment(Pos.BASELINE_CENTER);
        this.setPadding(new Insets(default_padding));
        this.getChildren().addAll(titleText, this.textField, firstButton, secondButton);

    }
}
