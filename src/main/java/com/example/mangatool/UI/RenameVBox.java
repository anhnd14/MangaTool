package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.FoldersChooserVBox;
import com.example.mangatool.MinorUI.FormatChooserVBox;
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



public class RenameVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;
    public FormatChooserVBox formatChooserVBox;
    public FoldersChooserVBox foldersChooserVBox;


    public RenameVBox() {
        formatChooserVBox = new FormatChooserVBox();
        foldersChooserVBox = new FoldersChooserVBox();

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

        this.getChildren().addAll(formatChooserVBox, foldersChooserVBox, runButton, progressBar, progressLabel);
        this.setSpacing(default_spacing);
        this.setPrefSize(800,600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    public void renameImage(RenameVBox renameVBox) {

        String inputPath = renameVBox.foldersChooserVBox.inputSelector.textField.getText();
        String outputPath = renameVBox.foldersChooserVBox.outputSelector.textField.getText();
        String expectedType = renameVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = renameVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = renameVBox.formatChooserVBox.startIndexTextField.getText();

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {


                if (inputPath.isEmpty() || outputPath.isEmpty()) {
                    updateMessage("Please choose input path and output path");
                    System.out.print("Please choose input path and output path");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                File[] files = new File(inputPath).listFiles();
                int counter;
                counter = Integer.parseInt(expectedStartIndex);
                assert files != null;
                List<File> fileList = filterFiles(files);

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
                        saveImage(originalImage, imagePath, expectedType);
                        System.out.println("Image save successfully: " + file.getName());
                        System.out.println("Image save as: " + String.format("%0" + expectedName + "d", counter) + "." + expectedType);
                        counter += 1;
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
