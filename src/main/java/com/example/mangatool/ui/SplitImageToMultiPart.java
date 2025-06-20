package com.example.mangatool.ui;


import com.example.mangatool.ui.component.FolderChooser;
import com.example.mangatool.ui.component.FormatChooserVBox;
import com.example.mangatool.ui.component.ProgressVBox;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static com.example.mangatool.common.TextConfig.*;
import static com.example.mangatool.common.CommonFunction.*;

public class SplitImageToMultiPart extends VBox {

    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;
    public FolderChooser outputSelector;
    public Image image;
    public ImageView imageView;
    public Pane pane;
    public double ratio;
    public List<Double> lineList;

    public SplitImageToMultiPart() {

        progressVBox = new ProgressVBox();
        progressVBox.runButton.setOnAction(_ -> splitImageMultiParts());

        formatChooserVBox = new FormatChooserVBox();
        outputSelector = new FolderChooser(output_path_text);

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
        resetLineButton.setOnAction(_ -> resetLine());

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
            line.setStrokeWidth(2);
            pane.getChildren().add(line);
            lineList.add(y);

        });


        HBox resetButtonHBox = new HBox();
        resetButtonHBox.setPadding(new Insets(default_padding));
        resetButtonHBox.setSpacing(default_spacing);
        resetButtonHBox.setAlignment(Pos.BASELINE_CENTER);
        resetButtonHBox.getChildren().addAll(resetAllButton, resetLineButton);


        this.getChildren().addAll(formatChooserVBox, openImageButton, outputSelector, resetButtonHBox, progressVBox, scrollPane);
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
                image = new Image(fileSelector("Image File", image_extensions));

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

    private void splitImageMultiParts() {

        String outputPath = this.outputSelector.getText();
        String expectedType = this.formatChooserVBox.getFileFormat();
        String expectedName = this.formatChooserVBox.getNameFormat();
        String expectedStartIndex = this.formatChooserVBox.getStartIndex();

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {

                if (outputPath.isEmpty()) {
                    updateMessage("Please choose output path");
                    return null;
                }
                if (expectedStartIndex.isEmpty()) {
                    updateMessage("Please choose start index");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
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

                int counter;
                counter = Integer.parseInt(expectedStartIndex);
                System.out.println(image.getUrl());
                BufferedImage inputImage = ImageIO.read(new File(image.getUrl()));
                Collections.sort(lineList);

                for (int i = 0; i <= lineList.size(); i++) {

                    updateProgress(i, lineList.size() + 1);
                    updateMessage(i + "/" + lineList.size() + 1);

                    int start;
                    int end;

                    if (i == 0) {
                        start = 0;
                    } else {
                        double x = lineList.get(i - 1)/ratio;
                        start = (int) x;
                    }

                    if (i == lineList.size()) {
                        end = (int) image.getHeight();
                    } else {
                        double x = lineList.get(i)/ratio;
                        end = (int)x;
                    }

                    BufferedImage resImage = inputImage.getSubimage(0, start, inputImage.getWidth(), end - start);
                    String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                    counter += 1;
                    saveImage(resImage, outImagePath, expectedType);

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
