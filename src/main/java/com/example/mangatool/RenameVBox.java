package com.example.mangatool;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static com.example.mangatool.Reusable.saveImage;

public class RenameVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;
    public FormatAndFolderChooserVBox formatAndFolderChooserVBox;

    int defaultSpacing = Reusable.default_spacing;
    int defaultPadding = Reusable.default_padding;

    public RenameVBox() {
        formatAndFolderChooserVBox = new FormatAndFolderChooserVBox();

        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");

        runButton.setOnAction(_ -> {
            try {
                renameImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.getChildren().addAll(formatAndFolderChooserVBox, runButton, progressBar, progressLabel);
        this.setSpacing(defaultSpacing);
        this.setPrefSize(800,600);
        this.setPadding(new Insets(defaultPadding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    public void renameImage(RenameVBox renameVBox) {

        String inputPath = renameVBox.formatAndFolderChooserVBox.inputPathTextField.getText();
        String outputPath = renameVBox.formatAndFolderChooserVBox.outputPathTextField.getText();
        String expectedType = renameVBox.formatAndFolderChooserVBox.fileFormatCombo.getValue();
        String expectedName = renameVBox.formatAndFolderChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = renameVBox.formatAndFolderChooserVBox.startIndexTextField.getText();

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {


                if (inputPath.isEmpty() || outputPath.isEmpty()) {
                    updateMessage("Please choose input path and output path");
                    System.out.print("Please choose input path and output path");
                    return null;
                }
                if (!Reusable.isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                File[] files = new File(inputPath).listFiles();
                int counter;
                counter = Integer.parseInt(expectedStartIndex);
                List<File> fileList = Reusable.filterFiles(files);

                if (fileList.isEmpty()) {
                    updateMessage("Found no image file in the input folder");
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
                        System.out.println("Full image save successfully: " + file.getName());
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

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }
}
