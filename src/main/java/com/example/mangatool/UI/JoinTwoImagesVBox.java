package com.example.mangatool.UI;


import static com.example.mangatool.AppFunction.*;

import static com.example.mangatool.TextConfig.*;

import com.example.mangatool.MinorUI.FileChooser;
import com.example.mangatool.MinorUI.ProgressVBox;
import com.example.mangatool.MinorUI.SmallTextFieldHBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class JoinTwoImagesVBox extends VBox {


    public ProgressVBox progressVBox;
    public ComboBox<String> fileFormatCombo;
    public FileChooser firstImageSelector;
    public FileChooser secondImageSelector;
    public SmallTextFieldHBox outFileName;
    public ComboBox<String> joinDirection;



    public JoinTwoImagesVBox() {

        ObservableList<String> formats = FXCollections.observableArrayList("jpg", "png", "bmp", "tiff", "webp");

        fileFormatCombo = new ComboBox<>(formats);
        fileFormatCombo.getSelectionModel().selectFirst();
        Text formatTitle = new Text(file_format_text);

        HBox hBoxChooseFormat = new HBox(default_spacing);
        hBoxChooseFormat.setSpacing(default_spacing);
        hBoxChooseFormat.setAlignment(Pos.CENTER);
        hBoxChooseFormat.setPadding(new Insets(default_padding));
        hBoxChooseFormat.getChildren().addAll(formatTitle, this.fileFormatCombo);


        firstImageSelector = new FileChooser(choose_image_text, images_file_extension_text, image_extensions);
        secondImageSelector = new FileChooser(choose_image_text, images_file_extension_text, image_extensions);
        outFileName = new SmallTextFieldHBox(out_file_name_text);

        Text joinDirectionTitle = new Text(join_direction_text);
        ObservableList<String> directions = FXCollections.observableArrayList("Horizontal", "Vertical");
        joinDirection = new ComboBox<>(directions);
        joinDirection.getSelectionModel().select(0);

        HBox outFileNameAndDirectionHBox = new HBox();
        outFileNameAndDirectionHBox.setSpacing(default_spacing);
        outFileNameAndDirectionHBox.setPadding(new Insets(default_padding));
        outFileNameAndDirectionHBox.setAlignment(Pos.BASELINE_CENTER);
        outFileNameAndDirectionHBox.getChildren().addAll(outFileName, joinDirectionTitle, joinDirection);


        Button openFolderButton = openFolderButton();


        progressVBox = new ProgressVBox();


        progressVBox.runButton.setOnAction(_ -> {
            try {
                joinImages(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.getChildren().addAll(hBoxChooseFormat, firstImageSelector, secondImageSelector,outFileNameAndDirectionHBox, openFolderButton, progressVBox);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }

    private Button openFolderButton() {
        Button openFolderButton = new Button(open_folder_button_text);
        openFolderButton.setOnAction(_ -> {
            try {
                File file = new File(firstImageSelector.textField.getText());
                String outputPath = firstImageSelector.textField.getText().substring(0, firstImageSelector.textField.getText().length() - file.getName().length() - 1);
                openFolder(outputPath);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return openFolderButton;
    }


    public void joinImages(JoinTwoImagesVBox joinTwoImagesVBox) {
        String firstImagePath = joinTwoImagesVBox.firstImageSelector.textField.getText();
        String secondImagePath = joinTwoImagesVBox.secondImageSelector.textField.getText();
        String extension = joinTwoImagesVBox.fileFormatCombo.getValue();


        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {


                if (firstImagePath.isEmpty() || secondImagePath.isEmpty()) {
                    updateMessage("Please choose images path");
                    return null;
                }

                File firstImageFile = new File(firstImagePath);
                File secondImageFile = new File(secondImagePath);

                if (!firstImageFile.exists() || !secondImageFile.exists()) {
                    updateMessage("File(s) do not exist");
                    return null;
                }

                String outImagePath = getOutImagePath(firstImageFile);

                try {
                    BufferedImage resImage;
                    if (joinDirection.getSelectionModel().isSelected(0)) {
                        resImage = joinHorizontal(firstImageFile, secondImageFile);
                    } else {
                        resImage = joinVertical(firstImageFile, secondImageFile);
                    }

                    saveImage(resImage, outImagePath, extension);
                    System.out.println("Image save successfully");

                } catch (Exception e) {
                    throw new Exception(e);
                }

                updateProgress(100, 100);
                updateMessage("Processing complete.");
                System.out.println("Processing complete.");

                return null;
            }
            private String getOutImagePath(File firstImageFile) {
                String fileName = firstImageFile.getName();
                int lastDotIndex = fileName.lastIndexOf('.');

                int fileNameLength = fileName.length();
                String outputPath = firstImagePath.substring(0, firstImagePath.length() - fileNameLength - 1);
                String name;
                if (outFileName.textField.getText().isEmpty()) {
                    name = fileName.substring(0, lastDotIndex) + "-joined";
                } else {
                    name = outFileName.textField.getText();
                }
                return outputPath + File.separator + name + "." + extension;
            }

            private BufferedImage joinHorizontal (File firstImageFile, File secondImageFile) throws Exception {
                BufferedImage firstImg = ImageIO.read(firstImageFile);
                BufferedImage secondImg = ImageIO.read(secondImageFile);

                System.out.println(firstImg.getType());

                int largerHeight = Math.max(firstImg.getHeight(), secondImg.getHeight());

                BufferedImage resImage = new BufferedImage(firstImg.getWidth() + secondImg.getWidth(), largerHeight, firstImg.getType());

                Graphics2D g = resImage.createGraphics();
                g.drawImage(firstImg, 0, 0, null);
                g.drawImage(secondImg, firstImg.getWidth(), 0, null);
                g.dispose();

                return resImage;
            }

            private BufferedImage joinVertical (File firstImageFile, File secondImageFile) throws Exception {
                BufferedImage firstImg = ImageIO.read(firstImageFile);
                BufferedImage secondImg = ImageIO.read(secondImageFile);
                int largerWidth = Math.max(firstImg.getWidth(), secondImg.getWidth());
                BufferedImage resImage = new BufferedImage(largerWidth, firstImg.getHeight() + secondImg.getHeight(), firstImg.getType());

                Graphics2D g = resImage.createGraphics();
                g.drawImage(firstImg, 0, 0, null);
                g.drawImage(secondImg, 0, firstImg.getHeight(), null);
                g.dispose();

                return resImage;
            }
        };

        progressVBox.progressBar.progressProperty().bind(task.progressProperty());
        progressVBox.progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }


}
