package com.example.mangatool.MinorUI;

import com.example.mangatool.AbstractUI.TextFieldAndTwoButtonsHBox;

import static com.example.mangatool.AppFunction.openFolder;
import static com.example.mangatool.AppFunction.selectFolder;
import static com.example.mangatool.TextConfig.*;

public class FolderChooser extends TextFieldAndTwoButtonsHBox {

    public FolderChooser(String title) {
        super(title, select_folder_button_text, open_folder_button_text);
        this.firstButton.setOnAction(_ -> {
            try {
                selectFolder(this.textField);
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
