package com.example.mangatool;

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
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

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


    int defaultSpacing = Reusable.default_spacing;
    int defaultPadding = Reusable.default_padding;

    int smallTextFieldPrefWidth = Reusable.small_text_field_pref_width;
    int longTextFieldPrefWidth = Reusable.long_text_field_pref_width;

    public OverlayImageVBox() {

        formatAndFolderChooserVBox = new FormatAndFolderChooserVBox();

        ObservableList<String> position = FXCollections.observableArrayList("Top Left", "Top Right", "Bottom Left", "Bottom Right");

        Text topImageTitle = new Text(Reusable.top_image_text);
        topImagePathTextField = new TextField();
        topImagePathTextField.setPrefWidth(longTextFieldPrefWidth);

        Button selectTopImageButton = new Button(Reusable.select_file_button_text);
        selectTopImageButton.setOnAction(e -> {
            try {
                fileSelector(topImagePathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        Text topImageHeightTitle = new Text(Reusable.top_image_height_text);
        Text topImageOpacityTitle = new Text(Reusable.top_image_opacity_text);
        topImageHeightTextField = new TextField("100");
        topImageHeightTextField.setPrefWidth(smallTextFieldPrefWidth);
        topImageHeightTextField.setTooltip(new Tooltip(Reusable.positive_number_tooltip));
        topImageOpacityTextField = new TextField("0.5");
        topImageOpacityTextField.setPrefWidth(smallTextFieldPrefWidth);
        topImageOpacityTextField.setTooltip(new Tooltip(Reusable.valid_double_tooltip));

        Text topImagePositionTitle = new Text(Reusable.top_image_position_text);
        this.topImagePositionCombo = new ComboBox<>(position);
        this.topImagePositionCombo.getSelectionModel().select(3);

        Text topImageXCoordinateTitle = new Text(Reusable.top_image_x_coordinate_text);
        Text topImageYCoordinateTitle = new Text(Reusable.top_image_y_coordinate_text);
        topImageXCoordinateTextField = new TextField("0");
        topImageXCoordinateTextField.setPrefWidth(smallTextFieldPrefWidth);
        topImageXCoordinateTextField.setTooltip(new Tooltip(Reusable.positive_number_tooltip));
        topImageYCoordinateTextField = new TextField("0");
        topImageYCoordinateTextField.setPrefWidth(smallTextFieldPrefWidth);
        topImageYCoordinateTextField.setTooltip(new Tooltip(Reusable.positive_number_tooltip));

        HBox topImagePathHBox = new HBox();
        topImagePathHBox.setSpacing(defaultSpacing);
        topImagePathHBox.setPadding(new Insets(defaultPadding));
        topImagePathHBox.setAlignment(Pos.BASELINE_CENTER);
        topImagePathHBox.getChildren().addAll(topImageTitle, topImagePathTextField, selectTopImageButton);

        HBox topImageHeightAndOpacityHBox = new HBox(defaultSpacing);
        topImageHeightAndOpacityHBox.setSpacing(defaultSpacing);
        topImageHeightAndOpacityHBox.setPadding(new Insets(defaultPadding));
        topImageHeightAndOpacityHBox.getChildren().addAll(topImageHeightTitle, topImageHeightTextField, topImageOpacityTitle, topImageOpacityTextField);
        topImageHeightAndOpacityHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox topImageCoordinateHBox = new HBox(defaultSpacing);
        topImageCoordinateHBox.setSpacing(defaultSpacing);
        topImageCoordinateHBox.setPadding(new Insets(defaultPadding));
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

        this.setSpacing(defaultSpacing);
        this.setPadding(new Insets(defaultPadding));
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
                if (!Reusable.isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }
                if (topImageHeight.equals("") || topImageOpacity.equals("") || topImageX.equals("") || topImageY.equals("")) {
                    updateMessage("Please input top image data");
                    return null;
                }
                if (!Reusable.isPositiveInteger(topImageHeight) || !Reusable.isValidDouble(topImageOpacity) || !Reusable.isPositiveInteger(topImageX) || !Reusable.isPositiveInteger(topImageY)) {
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
                        Reusable.saveImage(combinedImg, outImagePath, "png");
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

    public void fileSelector(TextField textField) throws Exception {

        String lastOpenFile = "";

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(Reusable.FILENAME));
        } catch (Exception e) {
            prop.store(new FileOutputStream(Reusable.FILENAME), null);
        }
        if (prop.containsKey("lastOpenFile")) {
            lastOpenFile = Reusable.loadData("lastOpenFile");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        Path checkFilePath = Paths.get(lastOpenFile);
        boolean fileExist = Files.isRegularFile(checkFilePath) && Files.exists(checkFilePath);

        if (!lastOpenFile.equals("") && fileExist) {
            fileChooser.setInitialDirectory(new File(new File(lastOpenFile).getParent()));
        }

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            textField.setText(selectedFile.getAbsolutePath());
            Reusable.saveData("lastOpenFile", selectedFile.getAbsolutePath());
            System.out.print(selectedFile.getAbsolutePath());
        } else {
            textField.setText("");
        }

    }
}
