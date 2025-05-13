package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.FormatChooserVBox;
import com.example.mangatool.MinorUI.ProgressVBox;
import com.example.mangatool.MinorUI.TextFieldAndTwoButtonsHBox;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RenameFileListVBox extends VBox {


    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public String inputFolderPath;
    public List<File> fileList;
    public TextFieldAndTwoButtonsHBox outputSelector;


    public RenameFileListVBox() {

        fileList = new ArrayList<>();

        formatChooserVBox = new FormatChooserVBox();

        progressVBox = new ProgressVBox();

        outputSelector = new TextFieldAndTwoButtonsHBox(output_path_text, select_folder_button_text, open_folder_button_text);
        outputSelector.firstButton.setOnAction(_ -> {
            try {
                selectFolder(outputSelector.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        outputSelector.secondButton.setOnAction(e -> {
            try {
                openFolder(outputSelector.textField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Text inputText = new Text(input_file_list_text);
        TextField inputFileListTextField = new TextField();
        inputFileListTextField.setPrefWidth(long_text_field_pref_width);

        Button inputFileListSelectButton = new Button(select_files_button_text);
        Button inputFileListOpenButton = new Button(open_folder_button_text);
        inputFileListSelectButton.setOnAction(e -> {
            try {
//                selectFiles(inputFileListTextField);
                fileList = filesSelector(inputFileListTextField);
                inputFolderPath = fileList.getFirst().getParent();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        inputFileListOpenButton.setOnAction(_ -> {
            try {
                openFolder(inputFolderPath);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        progressVBox.runButton.setOnAction(_ -> renameFileList(this));

        HBox hBoxInput = new HBox(default_spacing);
        hBoxInput.setSpacing(default_spacing);
        hBoxInput.setAlignment(Pos.BASELINE_CENTER);
        hBoxInput.setPadding(new Insets(default_padding));
        hBoxInput.getChildren().addAll(inputText, inputFileListTextField, inputFileListSelectButton, inputFileListOpenButton);


        this.getChildren().addAll(formatChooserVBox, hBoxInput, outputSelector, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    public void renameFileList(RenameFileListVBox renameFileListVBox) {


        String outputPath = renameFileListVBox.outputSelector.textField.getText();
        String expectedType = renameFileListVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = renameFileListVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = renameFileListVBox.formatChooserVBox.startIndexTextField.getText();
        List<File> files = renameFileListVBox.fileList;

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {

                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                int counter;
                counter = Integer.parseInt(expectedStartIndex);

                if (files.isEmpty()) {
                    updateMessage("No files had been chosen");
                    return null;
                }

                List<File> fileList = filterFiles(files);

                if (fileList.isEmpty()) {
                    updateMessage("Found no image file");
                    return null;
                }

                for (int i = 0;i < fileList.size();i++) {
                    updateProgress(i, fileList.size());
                    updateMessage(i + "/" + fileList.size());
                    File file = fileList.get(i);
                    try {
                        BufferedImage originalImage = ImageIO.read(file);
                        String imagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                        counter += 1;
                        saveImage(originalImage, imagePath, expectedType);
                        System.out.println("Image renamed successfully: " + file.getName());
                    } catch (Exception exception) {
                        System.err.println("Error processing image: " + file.getName());
                        exception.printStackTrace();
                    }
                }
                updateProgress(100, 100);
                updateMessage("Processing complete.");
                System.out.println("Processing complete.");

                return null;
            }
        };

        progressVBox.progressBar.progressProperty().bind(task.progressProperty());
        progressVBox.progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }


}
