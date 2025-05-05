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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.mangatool.Reusable.saveImage;

public class JoinTwoImagesVBox extends VBox {


    private static final String FILENAME = Reusable.FILENAME;

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;


    public TextField firstImageTextField;
    public TextField secondImageTextField;

    int smallTextFieldPrefWidth = Reusable.small_text_field_pref_width;
    int longTextFieldPrefWidth = Reusable.long_text_field_pref_width;


    int defaultSpacing = Reusable.default_spacing;
    int defaultPadding = Reusable.default_padding;


    public JoinTwoImagesVBox() {

        Text firstImageTitle = new Text(Reusable.choose_image_text);
        firstImageTextField = new TextField();
        firstImageTextField.setPrefWidth(longTextFieldPrefWidth);
        Button selectFirstImageButton = new Button(Reusable.select_file_button_text);
        selectFirstImageButton.setOnAction(e -> {
            try {
                Reusable.fileSelector(firstImageTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Text secondImageTitle = new Text(Reusable.choose_image_text);
        secondImageTextField = new TextField();
        secondImageTextField.setPrefWidth(longTextFieldPrefWidth);
        Button selectSecondImageButton = new Button(Reusable.select_file_button_text);
        selectSecondImageButton.setOnAction(e -> {
            try {
                Reusable.fileSelector(secondImageTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        HBox firstImagePathHBox = new HBox();
        firstImagePathHBox.setSpacing(defaultSpacing);
        firstImagePathHBox.setPadding(new Insets(defaultPadding));
        firstImagePathHBox.setAlignment(Pos.BASELINE_CENTER);
        firstImagePathHBox.getChildren().addAll(firstImageTitle, firstImageTextField, selectFirstImageButton);

        HBox secondImagePathHBox = new HBox();
        secondImagePathHBox.setSpacing(defaultSpacing);
        secondImagePathHBox.setPadding(new Insets(defaultPadding));
        secondImagePathHBox.setAlignment(Pos.BASELINE_CENTER);
        secondImagePathHBox.getChildren().addAll(secondImageTitle, secondImageTextField, selectSecondImageButton);

        Button openFolderButton = new Button(Reusable.open_folder_button_text);
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

        this.getChildren().addAll(firstImagePathHBox, secondImagePathHBox, openFolderButton, runButton, progressBar, progressLabel);
        this.setSpacing(defaultSpacing);
        this.setPrefSize(800, 600);
        this.setPadding(new Insets(defaultPadding));
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


                if (firstImagePath.equals("") || secondImagePath.equals("")) {
                    updateMessage("Please choose images path");
                    return null;
                }

                File firstImageFile = new File(firstImagePath);
                File secondImageFile = new File(secondImagePath);

                if (!firstImageFile.exists() || !secondImageFile.exists()) {
                    updateMessage("File(s) do not exist");
                    return null;
                }

                try {
                    BufferedImage firstImg = ImageIO.read(firstImageFile);
                    BufferedImage secondImg = ImageIO.read(secondImageFile);
                    BufferedImage resImage = new BufferedImage(firstImg.getWidth() + secondImg.getWidth(), firstImg.getHeight(), BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g = resImage.createGraphics();
                    g.drawImage(firstImg, 0, 0, null);
                    g.drawImage(secondImg, firstImg.getWidth(), 0, null);
                    g.dispose();

                    String fileName = firstImageFile.getName();
                    int lastDotIndex = fileName.lastIndexOf('.');
                    String extension = "";
                    if (lastDotIndex > 0) {
                        extension = fileName.substring(lastDotIndex + 1);
                        extension = extension.toLowerCase(); // normalize to lower case
                    }
                    if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("bmp") && !extension.equals("tiff")) {
                        extension = "png";
                        return null;
                    }

                    int fileNameLength = fileName.length();
                    String outputPath = firstImagePath.substring(0, firstImagePath.length() - fileNameLength - 1);

                    String outImagePath = outputPath + File.separator + fileName.substring(0, lastDotIndex) + "-joined" + "." + extension;
                    Reusable.saveImage(resImage, outImagePath, "png");
                    System.out.println("Image save successfully");


                } catch (Exception e) {
                    throw new Exception(e);
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
