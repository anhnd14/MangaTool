package com.example.mangatool;

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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CropByPixelVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;
    public FormatAndFolderChooserVBox formatAndFolderChooserVBox;
    public TextField topCropTextField;
    public TextField bottomCropTextField;
    public TextField leftCropTextField;
    public TextField rightCropTextField;


    int defaultSpacing = Reusable.default_spacing;
    int defaultPadding = Reusable.default_padding;
    int smallTextFieldPrefWidth = Reusable.small_text_field_pref_width;


    public CropByPixelVBox() {

        formatAndFolderChooserVBox = new FormatAndFolderChooserVBox();

        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");

        runButton.setOnAction(_ -> {
            try {
//                splitImage(this);
                //thêm phương thức crop
                cropImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Text topCropTitle = new Text(Reusable.top_crop);
        Text bottomCropTitle = new Text(Reusable.bottom_crop);
        topCropTextField = new TextField("420");
        topCropTextField.setPrefWidth(smallTextFieldPrefWidth);
        topCropTextField.setTooltip(new Tooltip(Reusable.positive_number_tooltip));
        bottomCropTextField = new TextField("420");
        bottomCropTextField.setPrefWidth(smallTextFieldPrefWidth);
        bottomCropTextField.setTooltip(new Tooltip(Reusable.valid_double_tooltip));

        Text leftCropTitle = new Text(Reusable.left_crop);
        Text rightCropTitle = new Text(Reusable.right_crop);
        leftCropTextField = new TextField("0");
        leftCropTextField.setPrefWidth(smallTextFieldPrefWidth);
        leftCropTextField.setTooltip(new Tooltip(Reusable.positive_number_tooltip));
        rightCropTextField = new TextField("0");
        rightCropTextField.setPrefWidth(smallTextFieldPrefWidth);
        rightCropTextField.setTooltip(new Tooltip(Reusable.valid_double_tooltip));

        HBox topAndBottomCropHBox = new HBox(defaultSpacing);
        topAndBottomCropHBox.setSpacing(defaultSpacing);
        topAndBottomCropHBox.setPadding(new Insets(defaultPadding));
        topAndBottomCropHBox.getChildren().addAll(topCropTitle, topCropTextField, bottomCropTitle, bottomCropTextField);
        topAndBottomCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox leftAndRightCropHBox = new HBox(defaultSpacing);
        leftAndRightCropHBox.setSpacing(defaultSpacing);
        leftAndRightCropHBox.setPadding(new Insets(defaultPadding));
        leftAndRightCropHBox.getChildren().addAll(leftCropTitle, leftCropTextField, rightCropTitle, rightCropTextField);
        leftAndRightCropHBox.setAlignment(Pos.BASELINE_CENTER);

        this.getChildren().addAll(formatAndFolderChooserVBox, topAndBottomCropHBox, leftAndRightCropHBox, runButton, progressBar, progressLabel);
        this.setSpacing(defaultSpacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(defaultPadding));
        this.setAlignment(Pos.TOP_CENTER);
    }


    public void cropImage(CropByPixelVBox cropByPixelVBox) {

        String inputPath = cropByPixelVBox.formatAndFolderChooserVBox.inputPathTextField.getText();
        String outputPath = cropByPixelVBox.formatAndFolderChooserVBox.outputPathTextField.getText();
        String expectedType = cropByPixelVBox.formatAndFolderChooserVBox.fileFormatCombo.getValue();
        String expectedName = cropByPixelVBox.formatAndFolderChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = cropByPixelVBox.formatAndFolderChooserVBox.startIndexTextField.getText();
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

                if (inputPath.equals("") || outputPath.equals("")) {
                    updateMessage("Please choose input path, output path");
                    return null;
                }
                if (!Reusable.isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                if (topCrop.equals("") || bottomCrop.equals("") || leftCrop.equals("") || rightCrop.equals("")) {
                    updateMessage("Please input crop data");
                    return null;
                }
                if (!Reusable.isPositiveInteger(topCrop) || !Reusable.isPositiveInteger(bottomCrop) || !Reusable.isPositiveInteger(leftCrop) || !Reusable.isPositiveInteger(rightCrop)) {
                    updateMessage("Please input valid crop data");
                    return null;
                }


                File[] files = new File(inputPath).listFiles();
                int counter;
                counter = Integer.parseInt(expectedStartIndex);
                List<File> fileList = Reusable.filterFiles(files);

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
                        Reusable.saveImage(resImage, outImagePath, expectedType);
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

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }

}
