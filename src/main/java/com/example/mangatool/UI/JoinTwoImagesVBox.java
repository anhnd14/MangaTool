package com.example.mangatool.UI;


import static com.example.mangatool.AppFunction.*;

import static com.example.mangatool.TextConfig.*;

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


public class JoinTwoImagesVBox extends VBox {


    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;


    public TextField firstImageTextField;
    public TextField secondImageTextField;

    public TextField outFileNameTextField;

    public ComboBox<String> joinDirection;



    public JoinTwoImagesVBox() {

        Text firstImageTitle = new Text(choose_image_text);
        firstImageTextField = new TextField();
        firstImageTextField.setPrefWidth(long_text_field_pref_width);
        Button selectFirstImageButton = new Button(select_file_button_text);
        selectFirstImageButton.setOnAction(_ -> {
            try {
                fileSelector(firstImageTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Text secondImageTitle = new Text(choose_image_text);
        secondImageTextField = new TextField();
        secondImageTextField.setPrefWidth(long_text_field_pref_width);
        Button selectSecondImageButton = new Button(select_file_button_text);
        selectSecondImageButton.setOnAction(e -> {
            try {
                fileSelector(secondImageTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Text outFileNameTitle = new Text(out_file_name_text);
        outFileNameTextField = new TextField();
        outFileNameTextField.setPrefWidth(small_text_field_pref_width);

        Text joinDirectionTitle = new Text(join_direction_text);
        ObservableList<String> directions = FXCollections.observableArrayList("Horizontal", "Vertical");
        joinDirection = new ComboBox<>(directions);
        joinDirection.getSelectionModel().select(0);

        HBox outFileNameAndDirectionHBox = new HBox();
        outFileNameAndDirectionHBox.setSpacing(default_spacing);
        outFileNameAndDirectionHBox.setPadding(new Insets(default_padding));
        outFileNameAndDirectionHBox.setAlignment(Pos.BASELINE_CENTER);
        outFileNameAndDirectionHBox.getChildren().addAll(outFileNameTitle, outFileNameTextField, joinDirectionTitle, joinDirection);


        HBox firstImagePathHBox = new HBox();
        firstImagePathHBox.setSpacing(default_spacing);
        firstImagePathHBox.setPadding(new Insets(default_padding));
        firstImagePathHBox.setAlignment(Pos.BASELINE_CENTER);
        firstImagePathHBox.getChildren().addAll(firstImageTitle, firstImageTextField, selectFirstImageButton);

        HBox secondImagePathHBox = new HBox();
        secondImagePathHBox.setSpacing(default_spacing);
        secondImagePathHBox.setPadding(new Insets(default_padding));
        secondImagePathHBox.setAlignment(Pos.BASELINE_CENTER);
        secondImagePathHBox.getChildren().addAll(secondImageTitle, secondImageTextField, selectSecondImageButton);

        Button openFolderButton = new Button(open_folder_button_text);
        openFolderButton.setOnAction(e -> {
            try {
                File file = new File(firstImageTextField.getText());
                String outputPath = firstImageTextField.getText().substring(0, firstImageTextField.getText().length() - file.getName().length() - 1);
                openFolder(outputPath);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");


        runButton.setOnAction(_ -> {
            try {
                System.out.print("join image button pressed");
                joinImages(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.getChildren().addAll(firstImagePathHBox, secondImagePathHBox,outFileNameAndDirectionHBox, openFolderButton, runButton, progressBar, progressLabel);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }

    public void openFolder(String pathToOpen) {
        if (pathToOpen.isEmpty()) {
            System.out.print("Folder not found");
        }
        try {
            Desktop.getDesktop().open(new File(pathToOpen));
        } catch (Exception exception) {
            System.out.print("Folder not found");
            exception.printStackTrace();
        }
    }


    public void joinImages(JoinTwoImagesVBox joinTwoImagesVBox) {
        String firstImagePath = joinTwoImagesVBox.firstImageTextField.getText();
        String secondImagePath = joinTwoImagesVBox.secondImageTextField.getText();


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

                    saveImage(resImage, outImagePath, "png");
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
                String extension = "";
                if (lastDotIndex > 0) {
                    extension = fileName.substring(lastDotIndex + 1);
                    extension = extension.toLowerCase(); // normalize to lower case
                }
                if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("bmp") && !extension.equals("tiff")) {
                    extension = "png";
                }

                int fileNameLength = fileName.length();
                String outputPath = firstImagePath.substring(0, firstImagePath.length() - fileNameLength - 1);
                String outFileName;
                if (outFileNameTextField.getText().isEmpty()) {
                    outFileName = fileName.substring(0, lastDotIndex) + "-joined";
                } else {
                    outFileName = outFileNameTextField.getText();
                }

                return outputPath + File.separator + outFileName + "." + extension;
            }

            private BufferedImage joinHorizontal (File firstImageFile, File secondImageFile) throws Exception {
                BufferedImage firstImg = ImageIO.read(firstImageFile);
                BufferedImage secondImg = ImageIO.read(secondImageFile);

                int largerHeight = Math.max(firstImg.getHeight(), secondImg.getHeight());

                BufferedImage resImage = new BufferedImage(firstImg.getWidth() + secondImg.getWidth(), largerHeight, BufferedImage.TYPE_INT_ARGB);

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
                BufferedImage resImage = new BufferedImage(largerWidth, firstImg.getHeight() + secondImg.getHeight(), BufferedImage.TYPE_INT_ARGB);

                Graphics2D g = resImage.createGraphics();
                g.drawImage(firstImg, 0, 0, null);
                g.drawImage(secondImg, 0, firstImg.getHeight(), null);
                g.dispose();

                return resImage;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();

    }


}
