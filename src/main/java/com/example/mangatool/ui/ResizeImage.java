package com.example.mangatool.ui;

import com.example.mangatool.ui.component.*;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static com.example.mangatool.common.CommonFunction.*;
import static com.example.mangatool.common.TextConfig.*;

public class ResizeImage extends VBox {

    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;
    public SmallInputRow heightInput;

    public ResizeImage() {
        progressVBox = new ProgressVBox();
        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        heightInput = new SmallInputRow(top_image_height_text);

        progressVBox.runButton.setOnAction(_ -> resizeImages());

        this.getChildren().addAll(formatChooserVBox, inputSelector, outputSelector, heightInput, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }

    private void resizeImages() {

        String outputPath = this.outputSelector.getText();
        String expectedType = this.formatChooserVBox.getFileFormat();
        String expectedName = this.formatChooserVBox.getNameFormat();
        String expectedStartIndex = this.formatChooserVBox.getStartIndex();
        String height = this.heightInput.getText();
        List<File> files = this.inputSelector.fileList;

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {

                if (outputPath.isEmpty()) {
                    updateMessage("Please choose output path");
                    return null;
                }

                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }

                if (!isPositiveInteger(height)) {
                    updateMessage("Please choose valid new height");
                    return null;
                }

                int counter;
                counter = Integer.parseInt(expectedStartIndex);

                if (files.isEmpty()) {
                    updateMessage("No images had been chosen");
                    return null;
                }

                int newHeight = Integer.parseInt(height);

                for (int i = 0; i < files.size(); i++) {
                    updateProgress(i, files.size());
                    updateMessage(i + "/" + files.size());
                    File file = files.get(i);
                    try {
                        BufferedImage originalImage = ImageIO.read(file);

                        System.out.println("original width" + originalImage.getWidth() + "; original height" + originalImage.getHeight());

                        double ratio = (double) newHeight / originalImage.getHeight();
                        double newWidthDouble = ratio * originalImage.getWidth();
                        int newWidth = (int) newWidthDouble;

                        System.out.println("new width" + newWidth + "; new height" + newHeight);

                        BufferedImage resizeImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

                        Graphics2D g = resizeImage.createGraphics();
                        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                        g.dispose();

                        String imagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                        counter += 1;
                        saveImage(resizeImage, imagePath, expectedType);
                        System.out.println("Image resized successfully: " + file.getName());
                    } catch (Exception exception) {
                        System.err.println("Error processing image: " + file.getName());
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
