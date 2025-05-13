package com.example.mangatool.MinorUI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;




public class FoldersChooserVBox extends VBox {

    public TextFieldAndTwoButtonsHBox inputSelector;
    public TextFieldAndTwoButtonsHBox outputSelector;


    public FoldersChooserVBox() {

        inputSelector = new TextFieldAndTwoButtonsHBox(input_path_text, select_folder_button_text, open_folder_button_text);
        outputSelector = new TextFieldAndTwoButtonsHBox(output_path_text, select_folder_button_text, open_folder_button_text);


        inputSelector.firstButton.setOnAction(_ -> {
            try {
                selectFolder(inputSelector.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        inputSelector.secondButton.setOnAction(_ -> {
            try {
                openFolder(inputSelector.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        outputSelector.firstButton.setOnAction(_ -> {
            try {
                selectFolder(outputSelector.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        outputSelector.secondButton.setOnAction(_ -> {
            try {
                openFolder(outputSelector.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });



        this.getChildren().addAll(inputSelector, outputSelector);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.BASELINE_CENTER);

    }




}
