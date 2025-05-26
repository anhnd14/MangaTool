package com.example.mangatool.ui;

import static com.example.mangatool.common.TextConfig.*;
import static com.example.mangatool.common.CommonFunction.*;

import com.example.mangatool.ui.component.FileChooser;
import com.example.mangatool.ui.component.FolderChooser;
import com.example.mangatool.ui.component.FormatChooserVBox;
import com.example.mangatool.ui.component.ProgressVBox;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfToImage extends VBox {
    public FileChooser inputSelector;
    public FolderChooser outputSelector;
    public ProgressVBox progressVBox;
    public FormatChooserVBox formatChooserVBox;

    public PdfToImage() {
        progressVBox = new ProgressVBox();
        formatChooserVBox = new FormatChooserVBox();
        inputSelector = new FileChooser(choose_file_text, pdf_file_extension_text, pdf_extensions);
        outputSelector = new FolderChooser(output_path_text);
        progressVBox.runButton.setOnAction(_ -> {
            try {
                formatPdfToImages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.getChildren().addAll(formatChooserVBox, inputSelector, outputSelector, progressVBox);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(800, 600);
        this.setSpacing(default_spacing);
        this.setPadding(new Insets(default_padding));


    }

    private void formatPdfToImages() {
        String inputFilePath = this.inputSelector.getText();
        String outputPath = this.outputSelector.getText();
        String expectedType = this.formatChooserVBox.getFileFormat();
        String expectedName = this.formatChooserVBox.getNameFormat();
        String expectedStartIndex = this.formatChooserVBox.getStartIndex();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                updateMessage("Please wait");

                if (inputFilePath.isEmpty() || outputPath.isEmpty()) {
                    updateMessage("Please choose input file, output path");
                    return null;
                }
                if (!isPositiveInteger(expectedStartIndex)) {
                    updateMessage("Please choose expected start index");
                    return null;
                }

                File pdfFile = new File(inputFilePath);

                String fileName = pdfFile.getName();
                int lastDotIndex = fileName.lastIndexOf('.');
                String extension = "";
                if (lastDotIndex > 0) {
                    extension = fileName.substring(lastDotIndex + 1);
                    extension = extension.toLowerCase(); // normalize to lower case
                }
                if (!extension.equals("pdf")) {
                    updateMessage("Please choose a pdf file");
                    return null;
                }

                PDDocument pdDocument = null;
                try {
                    pdDocument = PDDocument.load(pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pdDocument == null) {
                    updateMessage("Có lỗi chuyển đổi file, kiểm tra lại file pdf");
                    return null;
                }

                int counter = Integer.parseInt(expectedStartIndex);
                int pagesNumber = pdDocument.getNumberOfPages();
                PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);

                for (int i = 0; i < pagesNumber; i++) {
                    updateProgress(i, pagesNumber);
                    updateMessage(i + "/" + pagesNumber);

                    try {
                        BufferedImage resImage = pdfRenderer.renderImage(i);

                        String outImagePath = outputPath + File.separator + String.format("%0" + expectedName + "d", counter) + "." + expectedType;
                        counter += 1;
                        saveImage(resImage, outImagePath, expectedType);
                        System.out.println("Image save successfully, index: " + i);

                    } catch (IOException e) {
                        System.err.println("Error processing image. Index: " + i);
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
