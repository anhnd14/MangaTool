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
        RenameVBox renameVBox = new RenameVBox();
        OverlayImageVBox overlayImageVBox = new OverlayImageVBox();

        primaryStage.setTitle("Manga Tool");

        TabPane tabPane = new TabPane();
        Tab splitTab = new Tab("ImageSplit", new Label("ImageSplit"));
        Tab renameTab = new Tab("Rename", new Label("Rename"));
        Tab overlayImageTab = new Tab("AddLogo", new Label("AddLogo"));
        tabPane.getTabs().addAll(splitTab, renameTab, overlayImageTab);

        splitTab.setContent(imageSplitVBox);
        renameTab.setContent(renameVBox);
        overlayImageTab.setContent(overlayImageVBox);


        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}