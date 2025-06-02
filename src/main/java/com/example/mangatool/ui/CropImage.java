package com.example.mangatool.ui;

import static com.example.mangatool.common.CommonFunction.*;
import static com.example.mangatool.common.TextConfig.*;
import static com.example.mangatool.common.AppProperties.*;

import com.example.mangatool.ui.component.*;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CropImage extends VBox {


    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;
    public SmallInputRow topCrop;
    public SmallInputRow bottomCrop;
    public SmallInputRow leftCrop;
    public SmallInputRow rightCrop;


    public CropImage() {

        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        progressVBox = new ProgressVBox();
        progressVBox.runButton.setOnAction(_ -> {
            try {
                cropImage();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        String topCropData = loadData(DEFAULT_TOP_CROP, "420");
        String bottomCropData = loadData(DEFAULT_BOTTOM_CROP, "420");
        String leftCropData = loadData(DEFAULT_LEFT_CROP, "0");
        String rightCropData = loadData(DEFAULT_RIGHT_CROP, "0");

        topCrop = new SmallInputRow(top_crop, topCropData);
        topCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));
        bottomCrop = new SmallInputRow(bottom_crop, bottomCropData);
        bottomCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));

        leftCrop = new SmallInputRow(left_crop, leftCropData);
        leftCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));
        rightCrop = new SmallInputRow(right_crop, rightCropData);
        rightCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));

        HBox topAndBottomCropHBox = new HBox();
        topAndBottomCropHBox.setSpacing(default_spacing);
        topAndBottomCropHBox.setPadding(new Insets(default_padding));
        topAndBottomCropHBox.getChildren().addAll(topCrop, bottomCrop);
        topAndBottomCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox leftAndRightCropHBox = new HBox();
        leftAndRightCropHBox.setSpacing(default_spacing);
        leftAndRightCropHBox.setPadding(new Insets(default_padding));
        leftAndRightCropHBox.getChildren().addAll(leftCrop, rightCrop);
        leftAndRightCropHBox.setAlignment(Pos.BASELINE_CENTER);

        this.getChildren().addAll(formatChooserVBox, inputSelector, outputSelector, topAndBottomCropHBox, leftAndRightCropHBox, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    private void cropImage() {

        String outputPath = this.outputSelector.getText();
        String expectedType = this.formatChooserVBox.getFileFormat();
        String expectedName = this.formatChooserVBox.getNameFormat();
        String expectedStartIndex = this.formatChooserVBox.getStartIndex();
        String topCrop = this.topCrop.getText();
        String bottomCrop = this.bottomCrop.getText();
        String leftCrop = this.leftCrop.getText();
        String rightCrop = this.rightCrop.getText();
        List<File> files = this.inputSelector.fileList;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                int top = Integer.parseInt(topCrop);
                int bottom = Integer.parseInt(bottomCrop);
                int left = Integer.parseInt(leftCrop);
                int right = Integer.parseInt(rightCrop);

                if (outputPath.isEmpty()) {
                    updateMessage("Please choose input path, output path");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                if (topCrop.isEmpty() || bottomCrop.isEmpty() || leftCrop.isEmpty() || rightCrop.isEmpty()) {
                    updateMessage("Please input crop data");
                    return null;
                }
                if (!isPositiveInteger(topCrop) || !isPositiveInteger(bottomCrop) || !isPositiveInteger(leftCrop) || !isPositiveInteger(rightCrop)) {
                    updateMessage("Please input valid crop data");
                    return null;
                }

                int counter;
                counter = Integer.parseInt(expectedStartIndex);

                if (files.isEmpty()) {
                    updateMessage("No files had been chosen");
                    return null;
                }

                for (int i = 0; i < files.size(); i++) {
                    updateProgress(i, files.size());
                    updateMessage(i + "/" + files.size());
                    File file = files.get(i);

                    try {
                        BufferedImage originalImage = ImageIO.read(file);

                        if ((top + bottom) > originalImage.getHeight() || (left + right) > originalImage.getWidth()) {
                            updateMessage("Crop size is larger than image size");
                            Thread.sleep(100);
                            continue;
                        }

                        BufferedImage resImage;
                        resImage = originalImage.getSubimage(left, top, originalImage.getWidth() - left - right, originalImage.getHeight() - top - bottom);

                        String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                        counter += 1;
                        saveImage(resImage, outImagePath, expectedType);
                        System.out.println("Full image save successfully: " + file.getName());

                    } catch (Exception e) {
                        System.err.println("Error processing image: " + file.getName());
                        e.printStackTrace();
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
