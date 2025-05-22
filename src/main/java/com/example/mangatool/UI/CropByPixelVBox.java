package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.*;
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

public class CropByPixelVBox extends VBox {


    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;
    public SmallTextFieldHBox topCrop;
    public SmallTextFieldHBox bottomCrop;
    public SmallTextFieldHBox leftCrop;
    public SmallTextFieldHBox rightCrop;


    public CropByPixelVBox() {

        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        progressVBox = new ProgressVBox();
        progressVBox.runButton.setOnAction(_ -> {
            try {
                cropImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        String topCropData = loadData("defaultTopCrop", "420");
        String bottomCropData = loadData("defaultBottomCrop", "420");
        String leftCropData = loadData("defaultLeftCrop", "0");
        String rightCropData = loadData("defaultRightCrop", "0");

        topCrop = new SmallTextFieldHBox(top_crop, topCropData);
        topCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));
        bottomCrop = new SmallTextFieldHBox(bottom_crop, bottomCropData);
        bottomCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));

        leftCrop = new SmallTextFieldHBox(left_crop, leftCropData);
        leftCrop.textField.setTooltip(new Tooltip(positive_number_tooltip));
        rightCrop = new SmallTextFieldHBox(right_crop, rightCropData);
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

//    public void cropImage(CropByPixelVBox cropByPixelVBox) {
//
//        String inputPath = cropByPixelVBox.foldersChooserVBox.inputSelector.textField.getText();
//        String outputPath = cropByPixelVBox.foldersChooserVBox.outputSelector.textField.getText();
//        String expectedType = cropByPixelVBox.formatChooserVBox.fileFormatCombo.getValue();
//        String expectedName = cropByPixelVBox.formatChooserVBox.nameFormatCombo.getValue();
//        String expectedStartIndex = cropByPixelVBox.formatChooserVBox.startIndex.textField.getText();
//        String topCrop = cropByPixelVBox.topCrop.textField.getText();
//        String bottomCrop = cropByPixelVBox.bottomCrop.textField.getText();
//        String leftCrop = cropByPixelVBox.leftCrop.textField.getText();
//        String rightCrop = cropByPixelVBox.rightCrop.textField.getText();
//
//        Task<Void> task = new Task<>() {
//            @Override
//            protected Void call() {
//
//                int top = Integer.parseInt(topCrop);
//                int bottom = Integer.parseInt(bottomCrop);
//                int left = Integer.parseInt(leftCrop);
//                int right = Integer.parseInt(rightCrop);
//
//                if (inputPath.isEmpty() || outputPath.isEmpty()) {
//                    updateMessage("Please choose input path, output path");
//                    return null;
//                }
//                if (!isPositiveInteger(expectedStartIndex)) {
//                    updateMessage("Please choose expected start index");
//                    return null;
//                }
//                if (topCrop.isEmpty() || bottomCrop.isEmpty() || leftCrop.isEmpty() || rightCrop.isEmpty()) {
//                    updateMessage("Please input crop data");
//                    return null;
//                }
//                if (!isPositiveInteger(topCrop) || !isPositiveInteger(bottomCrop) || !isPositiveInteger(leftCrop) || !isPositiveInteger(rightCrop)) {
//                    updateMessage("Please input valid crop data");
//                    return null;
//                }
//
//
//                File[] files = new File(inputPath).listFiles();
//
//                if (files == null) {
//                    updateMessage("Found no file in the input folder");
//                    return null;
//                }
//
//                int counter;
//                counter = Integer.parseInt(expectedStartIndex);
//                List<File> fileList = filterFiles(files);
//
//                if (fileList.isEmpty()) {
//                    updateMessage("Found no file in the input folder");
//                    return null;
//                }
//
//                for (int i = 0; i < fileList.size(); i++) {
//                    updateProgress(i, fileList.size());
//                    updateMessage(i + "/" + fileList.size());
//                    File file = fileList.get(i);
//                    try {
//                        BufferedImage originalImage = ImageIO.read(file);
//                        BufferedImage resImage;
//
//                        resImage = originalImage.getSubimage(left, top, originalImage.getWidth() - left - right, originalImage.getHeight() - top - bottom);
//
//
//                        String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
//                        counter += 1;
//                        saveImage(resImage, outImagePath, expectedType);
//                        System.out.println("Full image save successfully: " + file.getName());
//
//                    } catch (IOException e) {
//                        System.err.println("Error processing image: " + file.getName());
//                        e.printStackTrace();
//                    }
//                }
//                updateProgress(100, 100);
//                updateMessage("Processing complete.");
//                System.out.println("Processing complete.");
//
//                return null;
//            }
//
//        };
//
//        progressVBox.progressBar.progressProperty().bind(task.progressProperty());
//        progressVBox.progressLabel.textProperty().bind(task.messageProperty());
//
//        new Thread(task).start();
//
//    }

    public void cropImage(CropByPixelVBox cropByPixelVBox) {

        String outputPath = cropByPixelVBox.outputSelector.textField.getText();
        String expectedType = cropByPixelVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = cropByPixelVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = cropByPixelVBox.formatChooserVBox.startIndex.textField.getText();
        String topCrop = cropByPixelVBox.topCrop.textField.getText();
        String bottomCrop = cropByPixelVBox.bottomCrop.textField.getText();
        String leftCrop = cropByPixelVBox.leftCrop.textField.getText();
        String rightCrop = cropByPixelVBox.rightCrop.textField.getText();
        List<File> files = cropByPixelVBox.inputSelector.fileList;

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

                List<File> fileList = filterFiles(files);

                if (fileList.isEmpty()) {
                    updateMessage("Found no file in the input folder");
                    return null;
                }

                for (int i = 0; i < fileList.size(); i++) {
                    updateProgress(i, fileList.size());
                    updateMessage(i + "/" + fileList.size());
                    File file = fileList.get(i);
                    try {
                        BufferedImage originalImage = ImageIO.read(file);
                        BufferedImage resImage;
                        resImage = originalImage.getSubimage(left, top, originalImage.getWidth() - left - right, originalImage.getHeight() - top - bottom);

                        String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                        counter += 1;
                        saveImage(resImage, outImagePath, expectedType);
                        System.out.println("Full image save successfully: " + file.getName());

                    } catch (IOException e) {
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
