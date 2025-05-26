package com.example.mangatool.ui;

import static com.example.mangatool.common.TextConfig.*;

import com.example.mangatool.ui.component.FolderChooser;
import com.example.mangatool.ui.component.FormatChooserVBox;
import com.example.mangatool.ui.component.ImagesListChooser;
import com.example.mangatool.ui.component.ProgressVBox;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import static com.example.mangatool.common.CommonFunction.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class SplitImageInHalf extends VBox {


    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;

    public SplitImageInHalf() {

        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        progressVBox = new ProgressVBox();

        progressVBox.runButton.setOnAction(_ -> {
            try {
                splitImage();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.getChildren().addAll(formatChooserVBox, inputSelector, outputSelector, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    private void splitImage() {
        String outputPath = this.outputSelector.getText();
        String expectedType = this.formatChooserVBox.getFileFormat();
        String expectedName = this.formatChooserVBox.getNameFormat();
        String expectedStartIndex = this.formatChooserVBox.getStartIndex();
        List<File> files = this.inputSelector.fileList;


        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {


                if (outputPath.isEmpty()) {
                    updateMessage("Please choose input path and output path");
                    return null;
                }
                if (!Files.isDirectory(Paths.get(outputPath))) {
                    updateMessage("Please choose valid input path and output path");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }

                if (files == null) {
                    updateMessage("Found no image file in the input folder");
                    return null;
                }

                int counter;
                counter = Integer.parseInt(expectedStartIndex);



                for (int i = 0; i < files.size(); i++) {
                    updateProgress(i, files.size());
                    updateMessage(i + "/" + files.size());
                    File file = files.get(i);
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

        progressVBox.progressBar.progressProperty().bind(task.progressProperty());
        progressVBox.progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }

}
