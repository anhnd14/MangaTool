package com.example.mangatool.UI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.TextConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class OverlayImageVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;
    public FormatAndFolderChooserVBox formatAndFolderChooserVBox;
    public TextField topImagePathTextField;
    public TextField topImageHeightTextField;
    public TextField topImageOpacityTextField;
    public TextField topImageXCoordinateTextField;
    public TextField topImageYCoordinateTextField;
    public ComboBox<String> topImagePositionCombo;


    public OverlayImageVBox() {

        formatAndFolderChooserVBox = new FormatAndFolderChooserVBox();

        ObservableList<String> position = FXCollections.observableArrayList("Top Left", "Top Right", "Bottom Left", "Bottom Right");

        Text topImageTitle = new Text(top_image_text);
        topImagePathTextField = new TextField();
        topImagePathTextField.setPrefWidth(long_text_field_pref_width);

        Button selectTopImageButton = new Button(select_file_button_text);
        selectTopImageButton.setOnAction(e -> {
            try {
                fileSelector(topImagePathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        String topImageHeight = loadData("defaultImageHeight", "100");
        String topImageOpacity = loadData("defaultImageOpacity", "0.1");
        String topImageX = loadData("defaultImageX", "10");
        String topImageY = loadData("defaultImageY", "10");

        Text topImageHeightTitle = new Text(top_image_height_text);
        Text topImageOpacityTitle = new Text(top_image_opacity_text);
        topImageHeightTextField = new TextField(topImageHeight);
        topImageHeightTextField.setPrefWidth(small_text_field_pref_width);
        topImageHeightTextField.setTooltip(new Tooltip(positive_number_tooltip));
        topImageOpacityTextField = new TextField(topImageOpacity);
        topImageOpacityTextField.setPrefWidth(small_text_field_pref_width);
        topImageOpacityTextField.setTooltip(new Tooltip(valid_double_tooltip));

        Text topImagePositionTitle = new Text(top_image_position_text);
        topImagePositionCombo = new ComboBox<>(position);
        topImagePositionCombo.getSelectionModel().select(3);

        Text topImageXCoordinateTitle = new Text(top_image_x_coordinate_text);
        Text topImageYCoordinateTitle = new Text(top_image_y_coordinate_text);
        topImageXCoordinateTextField = new TextField(topImageX);
        topImageXCoordinateTextField.setPrefWidth(small_text_field_pref_width);
        topImageXCoordinateTextField.setTooltip(new Tooltip(positive_number_tooltip));
        topImageYCoordinateTextField = new TextField(topImageY);
        topImageYCoordinateTextField.setPrefWidth(small_text_field_pref_width);
        topImageYCoordinateTextField.setTooltip(new Tooltip(positive_number_tooltip));

        HBox topImagePathHBox = new HBox();
        topImagePathHBox.setSpacing(default_spacing);
        topImagePathHBox.setPadding(new Insets(default_padding));
        topImagePathHBox.setAlignment(Pos.BASELINE_CENTER);
        topImagePathHBox.getChildren().addAll(topImageTitle, topImagePathTextField, selectTopImageButton);

        HBox topImageHeightAndOpacityHBox = new HBox(default_spacing);
        topImageHeightAndOpacityHBox.setSpacing(default_spacing);
        topImageHeightAndOpacityHBox.setPadding(new Insets(default_padding));
        topImageHeightAndOpacityHBox.getChildren().addAll(topImageHeightTitle, topImageHeightTextField, topImageOpacityTitle, topImageOpacityTextField);
        topImageHeightAndOpacityHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox topImageCoordinateHBox = new HBox(default_spacing);
        topImageCoordinateHBox.setSpacing(default_spacing);
        topImageCoordinateHBox.setPadding(new Insets(default_padding));
        topImageCoordinateHBox.getChildren().addAll(topImagePositionTitle, topImagePositionCombo, topImageXCoordinateTitle, topImageXCoordinateTextField, topImageYCoordinateTitle, topImageYCoordinateTextField);
        topImageCoordinateHBox.setAlignment(Pos.BASELINE_CENTER);

        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");

        runButton.setOnAction(e -> {
            try {
                overlayImage(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.setSpacing(default_spacing);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(formatAndFolderChooserVBox, topImagePathHBox, topImageHeightAndOpacityHBox, topImageCoordinateHBox, runButton, progressBar, progressLabel);

    }

    public void overlayImage(OverlayImageVBox overlayImageVBox) {

        String imgPath = overlayImageVBox.topImagePathTextField.getText();
        String inputPath = overlayImageVBox.formatAndFolderChooserVBox.inputPathTextField.getText();
        String outputPath = overlayImageVBox.formatAndFolderChooserVBox.outputPathTextField.getText();
        String expectedType = overlayImageVBox.formatAndFolderChooserVBox.fileFormatCombo.getValue();
        String expectedName = overlayImageVBox.formatAndFolderChooserVBox.nameFormatCombo.getValue();
        String expectedStartIndex = overlayImageVBox.formatAndFolderChooserVBox.startIndexTextField.getText();
        String topImageHeight = overlayImageVBox.topImageHeightTextField.getText();
        String topImageOpacity = overlayImageVBox.topImageOpacityTextField.getText();
        String topImageX = overlayImageVBox.topImageXCoordinateTextField.getText();
        String topImageY = overlayImageVBox.topImageYCoordinateTextField.getText();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                float thisTopImageOpacity;
                int thisTopImageHeight;
                int thisTopImageX = 0;
                int thisTopImageY = 0;

                if (inputPath.equals("") || outputPath.equals("") || imgPath.equals("")) {
                    updateMessage("Please choose input path, output path and top image");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                if (topImageHeight.equals("") || topImageOpacity.equals("") || topImageX.equals("") || topImageY.equals("")) {
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

                File[] files = new File(inputPath).listFiles();
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

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }

}
