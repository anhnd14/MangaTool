package com.example.mangatool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MangaTool extends Application {
    @Override
    public void start(Stage primaryStage) {
        System.out.println("test");

        ImageSplitVBox imageSplitVBox = new ImageSplitVBox();
        CropByPixelVBox cropByPixelVBox = new CropByPixelVBox();
        RenameVBox renameVBox = new RenameVBox();
        ToGrayScaleVBox toGrayScaleVBox = new ToGrayScaleVBox();
        CropAndGrayscaleVBox cropAndGrayscaleVBox = new CropAndGrayscaleVBox();
        OverlayImageVBox overlayImageVBox = new OverlayImageVBox();

        primaryStage.setTitle("Manga Tool");

        TabPane tabPane = new TabPane();
        Tab splitTab = new Tab("ImageSplit", new Label("ImageSplit"));
        Tab cropTab = new Tab("Crop Image", new Label("CropImage"));
        Tab renameTab = new Tab("Rename", new Label("Rename"));
        Tab toGrayScaleTab = new Tab("To Grayscale", new Label("ToGrayscale"));
        Tab cropAndGrayscaleTab = new Tab("Crop And Grayscale", new Label("CropAndGrayscale"));
        Tab overlayImageTab = new Tab("AddLogo", new Label("AddLogo"));
        tabPane.getTabs().addAll(splitTab, cropTab, renameTab, toGrayScaleTab, cropAndGrayscaleTab, overlayImageTab);

        splitTab.setContent(imageSplitVBox);
        cropTab.setContent(cropByPixelVBox);
        renameTab.setContent(renameVBox);
        toGrayScaleTab.setContent(toGrayScaleVBox);
        cropAndGrayscaleTab.setContent(cropAndGrayscaleVBox);
        overlayImageTab.setContent(overlayImageVBox);


        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}