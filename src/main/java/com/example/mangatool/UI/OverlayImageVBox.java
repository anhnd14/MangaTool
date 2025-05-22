package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class OverlayImageVBox extends VBox {

    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public ImagesListChooser inputSelector;
    public FolderChooser outputSelector;
    public FileChooser topImageSelector;
    public SmallTextFieldHBox topImageHeight;
    public SmallTextFieldHBox topImageOpacity;
    public SmallTextFieldHBox topImageXCoordinate;
    public SmallTextFieldHBox topImageYCoordinate;
    public ComboBox<String> topImagePositionCombo;


    public OverlayImageVBox() {

        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new ImagesListChooser(input_file_list_text);
        outputSelector = new FolderChooser(output_path_text);
        topImageSelector = new FileChooser(top_image_text, images_file_extension_text, image_extensions);

        String topImageHeightData = loadData("defaultImageHeight", "100");
        String topImageOpacityData = loadData("defaultImageOpacity", "0.1");
        String topImageXData = loadData("defaultImageX", "10");
        String topImageYData = loadData("defaultImageY", "10");

        topImageHeight = new SmallTextFieldHBox(top_image_height_text, topImageHeightData);
        topImageHeight.textField.setTooltip(new Tooltip(positive_number_tooltip));
        topImageOpacity = new SmallTextFieldHBox(top_image_opacity_text, topImageOpacityData);
        topImageOpacity.textField.setTooltip(new Tooltip(valid_double_tooltip));

        Text topImagePositionTitle = new Text(top_image_position_text);
        ObservableList<String> position = FXCollections.observableArrayList("Top Left", "Top Right", "Bottom Left", "Bottom Right");
        topImagePositionCombo = new ComboBox<>(position);
        topImagePositionCombo.getSelectionModel().select(3);

        topImageXCoordinate = new SmallTextFieldHBox(top_image_x_coordinate_text, topImageXData);
        topImageXCoordinate.textField.setTooltip(new Tooltip(positive_number_tooltip));
        topImageYCoordinate = new SmallTextFieldHBox(top_image_y_coordinate_text, topImageYData);
        topImageYCoordinate.textField.setTooltip(new Tooltip(positive_number_tooltip));

        HBox topImageHeightAndOpacityHBox = new HBox(default_spacing);
        topImageHeightAndOpacityHBox.setSpacing(default_spacing);
        topImageHeightAndOpacityHBox.setPadding(new Insets(default_padding));
        topImageHeightAndOpacityHBox.getChildren().addAll(topImageHeight, topImageOpacity);
        topImageHeightAndOpacityHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox topImageCoordinateHBox = new HBox(default_spacing);
        topImageCoordinateHBox.setSpacing(default_spacing);
        topImageCoordinateHBox.setPadding(new Insets(default_padding));
        topImageCoordinateHBox.getChildren().addAll(topImagePositionTitle, topImagePositionCombo, topImageXCoordinate, topImageYCoordinate);
        topImageCoordinateHBox.setAlignment(Pos.BASELINE_CENTER);


        progressVBox = new ProgressVBox();
        progressVBox.runButton.setOnAction(_ -> {
            try {
                overlayImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.setSpacing(default_spacing);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(formatChooserVBox, inputSelector, outputSelector, topImageSelector, topImageHeightAndOpacityHBox, topImageCoordinateHBox, progressVBox);
    }

    public void overlayImage(OverlayImageVBox overlayImageVBox) {

        String imgPath = overlayImageVBox.topImageSelector.textField.getText();
        String outputPath = overlayImageVBox.outputSelector.textField.getText();
        String expectedType = overlayImageVBox.formatChooserVBox.fileFormatCombo.getValue();
        String expectedName = overlayImageVBox.formatChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = overlayImageVBox.formatChooserVBox.startIndex.textField.getText();
        String topImageHeight = overlayImageVBox.topImageHeight.textField.getText();
        String topImageOpacity = overlayImageVBox.topImageOpacity.textField.getText();
        String topImageX = overlayImageVBox.topImageXCoordinate.textField.getText();
        String topImageY = overlayImageVBox.topImageYCoordinate.textField.getText();
        List<File> files = overlayImageVBox.inputSelector.fileList;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                float thisTopImageOpacity;
                int thisTopImageHeight;
                int thisTopImageX = 0;
                int thisTopImageY = 0;

                if (outputPath.isEmpty() || imgPath.isEmpty()) {
                    updateMessage("Please choose input path, output path and top image");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                if (topImageHeight.isEmpty() || topImageOpacity.isEmpty() || topImageX.isEmpty() || topImageY.isEmpty()) {
                    updateMessage("Please input top image data");
                    return null;
                }
                if (!isPositiveInteger(topImageHeight) || !isValidDouble(topImageOpacity) || !isPositiveInteger(topImageX) || !isPositiveInteger(topImageY)) {
                    updateMessage("Please input valid top image data");
                    return null;
                }

                thisTopImageHeight = Integer.parseInt(topImageHeight);
                thisTopImageOpacity = (float) Double.parseDouble(topImageOpacity);


                File imgFile = new File(imgPath);
                String fileName = imgFile.getName();
                int lastDotIndex = fileName.lastIndexOf('.');
                String extension = "";
                if (lastDotIndex > 0) {
                    extension = fileName.substring(lastDotIndex + 1);
                    extension = extension.toLowerCase(); // normalize to lower case
                }
                if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("bmp") && !extension.equals("tiff")) {
                    updateMessage("Please choose a jpg or png image file");
                    return null;
                }

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
                        BufferedImage topImage = ImageIO.read(imgFile);

                        BufferedImage combinedImg = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

                        Graphics2D g = combinedImg.createGraphics();
                        g.drawImage(originalImage, 0, 0, null);

                        double ratio = ((double) topImage.getWidth()) / topImage.getHeight();
                        int thisTopImageWidth = (int) (ratio * thisTopImageHeight);

                        //originalImageWidth-thisTopImageWidth

                        System.out.print(topImagePositionCombo.getSelectionModel());
                        String expectedPosition = topImagePositionCombo.getValue();
                        switch (expectedPosition){
                            case "Top Left": {
                                thisTopImageX = Integer.parseInt(topImageX);
                                thisTopImageY = Integer.parseInt(topImageY);
                                break;
                            }
                            case "Top Right": {
                                thisTopImageX = originalImage.getWidth() - thisTopImageWidth - Integer.parseInt(topImageX);
                                thisTopImageY = Integer.parseInt(topImageY);
                                break;
                            }
                            case "Bottom Left": {
                                thisTopImageX = Integer.parseInt(topImageX);
                                thisTopImageY = originalImage.getHeight() - thisTopImageHeight - Integer.parseInt(topImageY);
                                break;
                            }
                            case "Bottom Right": {
                                thisTopImageX = originalImage.getWidth() - thisTopImageWidth - Integer.parseInt(topImageX);
                                thisTopImageY = originalImage.getHeight() - thisTopImageHeight - Integer.parseInt(topImageY);
                                break;
                            }
                        }


                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, thisTopImageOpacity));
                        g.drawImage(topImage, thisTopImageX, thisTopImageY, thisTopImageWidth, thisTopImageHeight, null);
                        g.dispose();

                        String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                        counter += 1;
                        saveImage(combinedImg, outImagePath, "png");
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
