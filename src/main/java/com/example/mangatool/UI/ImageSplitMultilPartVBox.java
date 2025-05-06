package com.example.mangatool.UI;


import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static com.example.mangatool.TextConfig.*;
import static com.example.mangatool.AppFunction.*;

public class ImageSplitMultilPartVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;

    public TextField outputPathTextField;

    public Image image;
    public ImageView imageView;

    public Pane pane;

    public double ratio;

    public List<Double> lineList;

    public ImageSplitMultilPartVBox() {
        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");

        lineList = new ArrayList<>();



        Button resetAllButton;
        Button resetLineButton;
        resetAllButton = new Button(reset_all_button_text);
        resetAllButton.setOnAction(_ -> {
            imageView.setImage(null);
            pane.getChildren().clear();
            lineList.clear();
        });
        resetLineButton = new Button(reset_line_button_text);
        resetLineButton.setOnAction(_ -> {
            resetLine();
        });


        imageView = new ImageView();
        imageView.setPreserveRatio(true);

        pane = new Pane();
        pane.getChildren().add(imageView);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(default_padding));
        scrollPane.setContent(pane);

        Button openImageButton = setActionOpenImage();

        pane.setOnMouseClicked(e -> {

            Bounds imageBounds = imageView.getBoundsInParent();

            double y = e.getY();

            Line line = new Line(imageBounds.getMinX(), y, imageBounds.getMaxX(), y);
            line.setStroke(Color.rgb(255, 0, 0, 0.8));
            line.setStrokeWidth(5);
            pane.getChildren().add(line);
            lineList.add(y);

        });


        Text outputText = new Text(output_path_text);
        outputPathTextField = new TextField();
        outputPathTextField.setPrefWidth(long_text_field_pref_width);
        Button outputPathSelectButton = new Button(select_folder_button_text);
        Button outputPathOpenButton = new Button(open_folder_button_text);
        outputPathSelectButton.setOnAction(e -> {
            try {
                selectFolder(outputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        outputPathOpenButton.setOnAction(e -> {
            try {
                openFolder(outputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        HBox runAndResetButtonHBox = new HBox();
        runAndResetButtonHBox.setPadding(new Insets(default_padding));
        runAndResetButtonHBox.setSpacing(default_spacing);
        runAndResetButtonHBox.setAlignment(Pos.BASELINE_CENTER);
        runAndResetButtonHBox.getChildren().addAll(runButton, resetAllButton, resetLineButton);
        runButton.setOnAction(_ -> {
            try {
                splitImageMultiParts(this);
            } catch (Exception e) {
                throw e;
            }
        });

        HBox hBoxOutput = new HBox(default_spacing);
        hBoxOutput.setSpacing(default_spacing);
        hBoxOutput.setAlignment(Pos.BASELINE_CENTER);
        hBoxOutput.setPadding(new Insets(default_padding));
        hBoxOutput.getChildren().addAll(outputText, outputPathTextField, outputPathSelectButton, outputPathOpenButton);


        this.getChildren().addAll(openImageButton, hBoxOutput, runAndResetButtonHBox, progressBar, progressLabel, scrollPane);
        this.setSpacing(default_spacing);
        this.setPrefSize(800, 800);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);

    }

    private void resetLine() {
        pane.getChildren().clear();
        lineList.clear();
        pane.getChildren().add(imageView);
    }

    private Button setActionOpenImage() {
        Button openImageButton = new Button(select_file_button_text);
        openImageButton.setOnAction(_ -> {

            try {
                image = new Image(fileSelector());

                pane.setMaxWidth(default_width);
                pane.setMinHeight(image.getHeight() * ratio);

                ratio = default_width / image.getWidth();
                imageView.setImage(image);
                imageView.setFitWidth(default_width);

                resetLine();


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return openImageButton;
    }

    public void splitImageMultiParts(ImageSplitMultilPartVBox imageSplitMultilPartVBox) {

        String outputPath = imageSplitMultilPartVBox.outputPathTextField.getText();


        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {


                if (outputPath.equals("")) {
                    updateMessage("Please choose output path");
                    return null;
                }
                if (!Files.isDirectory(Paths.get(outputPath))) {
                    updateMessage("Please choose valid output path");
                    return null;
                }
                if (lineList.isEmpty()) {
                    updateMessage("Please draw line to cut the image");
                    return null;
                }
                if (imageView.getImage() == null) {
                    updateMessage("Please choose image");
                    return null;
                }

                int counter = lineList.size();

                System.out.println(image.getUrl());

                BufferedImage inputImage = ImageIO.read(new File(image.getUrl()));



                for (int i = 0; i <= counter; i++) {

                    int start;
                    int end;

                    if (i == 0) {
                        start = 0;
                    } else {
                        double x = lineList.get(i - 1)/ratio;
                        start = (int) x;
                    }

                    if (i == counter) {
                        end = (int) image.getHeight();
                    } else {
                        double x = lineList.get(i)/ratio;
                        end = (int)x;
                    }


                    BufferedImage resImage = new BufferedImage((int) image.getWidth(), end - start, BufferedImage.TYPE_INT_ARGB);

                    resImage = inputImage.getSubimage(0, start, inputImage.getWidth(), end - start);


                    String outImagePath = outputPath + File.separator + String.format("%0" + 3 + "d", i) + "." + "png";

                    saveImage(resImage, outImagePath, "png");


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
