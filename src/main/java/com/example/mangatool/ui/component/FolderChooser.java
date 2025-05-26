package com.example.mangatool.ui.component;

import static com.example.mangatool.common.CommonFunction.openFolder;
import static com.example.mangatool.common.CommonFunction.selectFolder;
import static com.example.mangatool.common.TextConfig.*;

public class FolderChooser extends InputActionRow {

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
