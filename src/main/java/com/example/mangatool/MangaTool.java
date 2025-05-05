package com.example.mangatool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

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
        JoinTwoImagesVBox joinTwoImagesVBox = new JoinTwoImagesVBox();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu settingMenu = new Menu("Setting");

        menuBar.getMenus().addAll(fileMenu, settingMenu);

        fileMenu.setOnShowing(e -> {
            System.out.println("Showing File Menu");
        });
        fileMenu.setOnHiding(e -> {
            System.out.println("Hiding File Menu");
        });

        settingMenu.setOnShowing(e -> {
            System.out.println("Showing File Menu");
        });
        settingMenu.setOnHiding(e -> {
            System.out.println("Hiding File Menu");
        });

        MenuItem closeMenuItem = new MenuItem("Close");
        MenuItem reloadMenuItem = new MenuItem("Reload");

        fileMenu.getItems().addAll(closeMenuItem, reloadMenuItem);

        MenuItem settingMenuItem = new MenuItem("Setting");
        settingMenu.getItems().add(settingMenuItem);


        VBox menuBarVBox = new VBox(menuBar);

        primaryStage.setTitle("Manga Tool");



        TabPane tabPane = new TabPane();
        Tab splitTab = new Tab("ImageSplit", new Label("ImageSplit"));
        Tab cropTab = new Tab("Crop Image", new Label("CropImage"));
        Tab renameTab = new Tab("Rename", new Label("Rename"));
        Tab toGrayScaleTab = new Tab("To Grayscale", new Label("ToGrayscale"));
        Tab cropAndGrayscaleTab = new Tab("Crop And Grayscale", new Label("CropAndGrayscale"));
        Tab overlayImageTab = new Tab("AddLogo", new Label("AddLogo"));
        Tab joinImagesTab = new Tab("Join Images", new Label("Join Images"));
        tabPane.getTabs().addAll(splitTab, cropTab, renameTab, toGrayScaleTab, cropAndGrayscaleTab, overlayImageTab, joinImagesTab);

        splitTab.setContent(imageSplitVBox);
        cropTab.setContent(cropByPixelVBox);
        renameTab.setContent(renameVBox);
        toGrayScaleTab.setContent(toGrayScaleVBox);
        cropAndGrayscaleTab.setContent(cropAndGrayscaleVBox);
        overlayImageTab.setContent(overlayImageVBox);
        joinImagesTab.setContent(joinTwoImagesVBox);

        VBox screen = new VBox();

        screen.getChildren().addAll(menuBarVBox, tabPane);

        AtomicReference<Scene> scene = new AtomicReference<>(new Scene(screen));
        primaryStage.setScene(scene.get());
        primaryStage.show();

        closeMenuItem.setOnAction(e -> {
            System.out.println("Close selected");
            primaryStage.close();
        });


        settingMenuItem.setOnAction(e -> {
            System.out.println("Setting Popup Opened");
            try {
                showSettingPopup(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        reloadMenuItem.setOnAction(_ -> {

            primaryStage.close();
            primaryStage.show();
        });
    }

    public void showSettingPopup (Stage stage) throws Exception {
        SettingPopup settingPopup = new SettingPopup();
        settingPopup.initOwner(stage);
        settingPopup.showAndWait();

    }


    public static void main(String[] args) {
        launch();
    }
}