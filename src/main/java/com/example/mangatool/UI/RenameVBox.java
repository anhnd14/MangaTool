package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.*;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;


public class RenameVBox extends VBox {

    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;

    public RenameVBox() {

        formatChooserVBox = new FormatChooserVBox();
        progressVBox = new ProgressVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        progressVBox.runButton.setOnAction(_ -> renameFileList(this));

        this.getChildren().addAll(formatChooserVBox, inputSelector, outputSelector, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }

    public void renameFileList(RenameVBox renameVBox) {

        String outputPath = renameVBox.outputSelector.textField.getText();
        String expectedType = renameVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = renameVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = renameVBox.formatChooserVBox.startIndex.textField.getText();
        List<File> files = renameVBox.inputSelector.fileList;

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
