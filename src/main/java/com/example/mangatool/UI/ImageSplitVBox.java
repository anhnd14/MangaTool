package com.example.mangatool.UI;

import static com.example.mangatool.TextConfig.*;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import static com.example.mangatool.AppFunction.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class ImageSplitVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;
    public FormatChooserVBox formatChooserVBox;
    public FoldersChooserVBox foldersChooserVBox;




    public ImageSplitVBox() {

        formatChooserVBox = new FormatChooserVBox();

        foldersChooserVBox = new FoldersChooserVBox();

        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");

        runButton.setOnAction(_ -> {
            try {
                splitImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.getChildren().addAll(formatChooserVBox, foldersChooserVBox, runButton, progressBar, progressLabel);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    public void splitImage(ImageSplitVBox imageSplitVBox) {
        String inputPath = imageSplitVBox.foldersChooserVBox.inputPathTextField.getText();
        String outputPath = imageSplitVBox.foldersChooserVBox.outputPathTextField.getText();
        String expectedType = imageSplitVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = imageSplitVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = imageSplitVBox.formatChooserVBox.startIndexTextField.getText();

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {


                if (inputPath.equals("") || outputPath.equals("")) {
                    updateMessage("Please choose input path and output path");
                    return null;
                }
                if (!Files.isDirectory(Paths.get(inputPath)) || !Files.isDirectory(Paths.get(outputPath))) {
                    updateMessage("Please choose valid input path and output path");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                File[] files = new File(inputPath).listFiles();
                int counter;
                counter = Integer.parseInt(expectedStartIndex);
                List<File> fileList = filterFiles(files);

                if (fileList.size() == 0) {
                    updateMessage("Found no image file in the input folder");
                    return null;
                }

                for (int i = 0; i < fileList.size(); i++) {
                    updateProgress(i, fileList.size());
                    updateMessage(i + "/" + fileList.size());
                    File file = fileList.get(i);
                    try {
                        BufferedImage originalImage = ImageIO.read(file);
                        if (file.getName().contains("-full")) {
                            String imagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                            counter += 1;
                            saveImage(originalImage, imagePath, expectedType);
                            System.out.println("Full image save successfully: " + file.getName());
                        } else {
                            int width = originalImage.getWidth();
                            int height = originalImage.getHeight();

                            BufferedImage leftHalf = originalImage.getSubimage(0, 0, width / 2, height);
                            BufferedImage rightHalf = originalImage.getSubimage(width / 2, 0, width / 2, height);

                            String rightImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                            counter += 1;
                            String leftImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                            counter += 1;

                            saveImage(rightHalf, rightImagePath, expectedType);
                            saveImage(leftHalf, leftImagePath, expectedType);

                            System.out.println("Image split successfully: " + file.getName());
                        }
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
