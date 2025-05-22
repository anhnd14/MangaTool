package com.example.mangatool.MinorUI;

import com.example.mangatool.AbstractUI.TextFieldAndTwoButtonsHBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

public class ImagesListChooser extends TextFieldAndTwoButtonsHBox {
    public List<File> fileList;
    public String inputFolderPath;

    public ImagesListChooser(String title) {
        super(title, select_files_button_text, open_folder_button_text);
        fileList = new ArrayList<>();
        inputFolderPath = "";
        this.firstButton.setOnAction(e -> {
            try {
                fileList = filesSelector(this.textField, images_file_extension_text, image_extensions);
                inputFolderPath = fileList.getFirst().getParent();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        this.secondButton.setOnAction(_ -> {
            try {
                openFolder(inputFolderPath);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
