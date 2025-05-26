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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.example.mangatool.common.CommonFunction.*;

public class ToGrayScaleImage extends VBox {
    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;




    public ToGrayScaleImage() {
        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        progressVBox = new ProgressVBox();
        progressVBox.runButton.setOnAction(_ -> {
            try {
                toGrayScale();
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

    private void toGrayScale() {
        String inputPath = this.inputSelector.getText();
        String outputPath = this.outputSelector.getText();
        String expectedType = this.formatChooserVBox.getFileFormat();
        String expectedName = this.formatChooserVBox.getNameFormat();
        String expectedStartIndex = this.formatChooserVBox.getStartIndex();
        List<File> files = this.inputSelector.fileList;

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {


                if (inputPath.isEmpty() || outputPath.isEmpty()) {
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
                if (files.isEmpty()) {
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
                        if (file.getName().contains("-color")) {
                            String imagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                            counter += 1;
                            saveImage(originalImage, imagePath, expectedType);
                            System.out.println("Color image save successfully: " + file.getName());
                        } else {
                            BufferedImage res = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                            Graphics g = res.getGraphics();
                            g.drawImage(originalImage, 0, 0, null);
                            g.dispose();


                            String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                            counter += 1;

                            saveImage(res, outImagePath, expectedType);

                            System.out.println("Image to grayscale successfully: " + file.getName());
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
