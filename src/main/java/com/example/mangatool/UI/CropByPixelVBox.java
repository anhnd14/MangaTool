package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.FoldersChooserVBox;
import com.example.mangatool.MinorUI.FormatChooserVBox;
import com.example.mangatool.MinorUI.ProgressVBox;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CropByPixelVBox extends VBox {


    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public FoldersChooserVBox foldersChooserVBox;
    public TextField topCropTextField;
    public TextField bottomCropTextField;
    public TextField leftCropTextField;
    public TextField rightCropTextField;


    public CropByPixelVBox() {

        formatChooserVBox = new FormatChooserVBox();
        foldersChooserVBox = new FoldersChooserVBox();
        progressVBox = new ProgressVBox();


        progressVBox.runButton.setOnAction(_ -> {
            try {
                cropImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        String topCrop = loadData("defaultTopCrop", "420");
        String bottomCrop = loadData("defaultBottomCrop", "420");
        String leftCrop = loadData("defaultLeftCrop", "0");
        String rightCrop = loadData("defaultRightCrop", "0");

        Text topCropTitle = new Text(top_crop);
        Text bottomCropTitle = new Text(bottom_crop);
        topCropTextField = new TextField(topCrop);
        topCropTextField.setPrefWidth(small_text_field_pref_width);
        topCropTextField.setTooltip(new Tooltip(positive_number_tooltip));
        bottomCropTextField = new TextField(bottomCrop);
        bottomCropTextField.setPrefWidth(small_text_field_pref_width);
        bottomCropTextField.setTooltip(new Tooltip(valid_double_tooltip));

        Text leftCropTitle = new Text(left_crop);
        Text rightCropTitle = new Text(right_crop);
        leftCropTextField = new TextField(leftCrop);
        leftCropTextField.setPrefWidth(small_text_field_pref_width);
        leftCropTextField.setTooltip(new Tooltip(positive_number_tooltip));
        rightCropTextField = new TextField(rightCrop);
        rightCropTextField.setPrefWidth(small_text_field_pref_width);
        rightCropTextField.setTooltip(new Tooltip(valid_double_tooltip));

        HBox topAndBottomCropHBox = new HBox(default_spacing);
        topAndBottomCropHBox.setSpacing(default_spacing);
        topAndBottomCropHBox.setPadding(new Insets(default_padding));
        topAndBottomCropHBox.getChildren().addAll(topCropTitle, topCropTextField, bottomCropTitle, bottomCropTextField);
        topAndBottomCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox leftAndRightCropHBox = new HBox(default_spacing);
        leftAndRightCropHBox.setSpacing(default_spacing);
        leftAndRightCropHBox.setPadding(new Insets(default_padding));
        leftAndRightCropHBox.getChildren().addAll(leftCropTitle, leftCropTextField, rightCropTitle, rightCropTextField);
        leftAndRightCropHBox.setAlignment(Pos.BASELINE_CENTER);

        this.getChildren().addAll(formatChooserVBox, foldersChooserVBox, topAndBottomCropHBox, leftAndRightCropHBox, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }

    public void cropImage(CropByPixelVBox cropByPixelVBox) {

        String inputPath = cropByPixelVBox.foldersChooserVBox.inputSelector.textField.getText();
        String outputPath = cropByPixelVBox.foldersChooserVBox.outputSelector.textField.getText();
        String expectedType = cropByPixelVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = cropByPixelVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = cropByPixelVBox.formatChooserVBox.startIndexTextField.getText();
        String topCrop = cropByPixelVBox.topCropTextField.getText();
        String bottomCrop = cropByPixelVBox.bottomCropTextField.getText();
        String leftCrop = cropByPixelVBox.leftCropTextField.getText();
        String rightCrop = cropByPixelVBox.rightCropTextField.getText();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                int top = Integer.parseInt(topCrop);
                int bottom = Integer.parseInt(bottomCrop);
                int left = Integer.parseInt(leftCrop);
                int right = Integer.parseInt(rightCrop);

                if (inputPath.isEmpty() || outputPath.isEmpty()) {
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


                File[] files = new File(inputPath).listFiles();

                if (files == null) {
                    updateMessage("Found no file in the input folder");
                    return null;
                }

                int counter;
                counter = Integer.parseInt(expectedStartIndex);
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
