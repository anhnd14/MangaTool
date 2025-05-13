package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;



public class FoldersChooserVBox extends VBox {


    public TextField inputPathTextField;
    public TextField outputPathTextField;

//    public FormatChooserVBox formatChooserVBox;


    public FoldersChooserVBox() {




        Text inputText = new Text(input_path_text);
        Text outputText = new Text(output_path_text);

        inputPathTextField = new TextField();
        inputPathTextField.setPrefWidth(long_text_field_pref_width);

        outputPathTextField = new TextField();
        outputPathTextField.setPrefWidth(long_text_field_pref_width);

        Button inputPathSelectButton = new Button(select_folder_button_text);
        Button inputPathOpenButton = new Button(open_folder_button_text);
        inputPathSelectButton.setOnAction(e -> {
            try {
                selectFolder(inputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        inputPathOpenButton.setOnAction(e -> {
            try {
                openFolder(inputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Button outputPathSelectButton = new Button(select_folder_button_text);
        Button outputPathOpenButton = new Button(open_folder_button_text);
        outputPathSelectButton.setOnAction(e -> {
            try {
                selectFolder(outputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        outputPathOpenButton.setOnAction(e -> {
            try {
                openFolder(outputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        HBox hBoxInput = new HBox(default_spacing);
        hBoxInput.setSpacing(default_spacing);
        hBoxInput.setAlignment(Pos.BASELINE_CENTER);
        hBoxInput.setPadding(new Insets(default_padding));
        hBoxInput.getChildren().addAll(inputText, this.inputPathTextField, inputPathSelectButton, inputPathOpenButton);

        HBox hBoxOutput = new HBox(default_spacing);
        hBoxOutput.setSpacing(default_spacing);
        hBoxOutput.setAlignment(Pos.BASELINE_CENTER);
        hBoxOutput.setPadding(new Insets(default_padding));
        hBoxOutput.getChildren().addAll(outputText, this.outputPathTextField, outputPathSelectButton, outputPathOpenButton);

        this.getChildren().addAll(hBoxInput, hBoxOutput);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.BASELINE_CENTER);

    }




}
