package com.example.mangatool.MinorUI;

import com.example.mangatool.AbstractUI.TextFieldAndTwoButtonsHBox;

import java.util.List;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

public class FileChooser extends TextFieldAndTwoButtonsHBox {
    public FileChooser(String title, String extensionText, List<String> extensionList) {
        super(title, select_file_button_text, open_file_button_text);
        this.firstButton.setOnAction(_ -> {
            try {
                fileSelector(this.textField, extensionText, extensionList);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        this.secondButton.setOnAction(_ -> {
            try {
                openFolder(this.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
