package com.example.mangatool;

import com.example.mangatool.MinorUI.MenuBarVBox;
import com.example.mangatool.UI.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class MangaTool extends Application {
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Manga Tool");

        ImageSplitVBox imageSplitVBox = new ImageSplitVBox();
        CropByPixelVBox cropByPixelVBox = new CropByPixelVBox();
        RenameVBox renameVBox = new RenameVBox();
        ToGrayScaleVBox toGrayScaleVBox = new ToGrayScaleVBox();
        OverlayImageVBox overlayImageVBox = new OverlayImageVBox();
        JoinTwoImagesVBox joinTwoImagesVBox = new JoinTwoImagesVBox();
        ImageSplitMultilPartVBox imageSplitMultilPartVBox = new ImageSplitMultilPartVBox();
        PdfToImagesVBox pdfToImagesVBox = new PdfToImagesVBox();


        TabPane tabPane = new TabPane();
        Tab splitTab = new Tab("ImageSplit", new Label("ImageSplit"));
        Tab cropTab = new Tab("Crop Image", new Label("CropImage"));
        Tab renameFilesListTab = new Tab("Rename Files", new Label("Rename Files"));
        Tab toGrayScaleTab = new Tab("To Grayscale", new Label("ToGrayscale"));
        Tab overlayImageTab = new Tab("AddLogo", new Label("AddLogo"));
        Tab joinImagesTab = new Tab("Join Images", new Label("Join Images"));
        Tab imageSplitMultiPartTab = new Tab("Split Images", new Label("Split Images"));
        Tab pdfToImagesTab = new Tab("PDF to Images", new Label("PDF to Images"));


        splitTab.setClosable(false);
        cropTab.setClosable(false);
        renameFilesListTab.setClosable(false);
        toGrayScaleTab.setClosable(false);
        overlayImageTab.setClosable(false);
        joinImagesTab.setClosable(false);
        imageSplitMultiPartTab.setClosable(false);
        pdfToImagesTab.setClosable(false);

        tabPane.getTabs().addAll(splitTab, cropTab, renameFilesListTab, toGrayScaleTab, overlayImageTab, joinImagesTab, imageSplitMultiPartTab, pdfToImagesTab);

        splitTab.setContent(imageSplitVBox);
        cropTab.setContent(cropByPixelVBox);
        renameFilesListTab.setContent(renameVBox);
        toGrayScaleTab.setContent(toGrayScaleVBox);
        overlayImageTab.setContent(overlayImageVBox);
        joinImagesTab.setContent(joinTwoImagesVBox);
        imageSplitMultiPartTab.setContent(imageSplitMultilPartVBox);
        pdfToImagesTab.setContent(pdfToImagesVBox);

        VBox screen = new VBox();

        MenuBarVBox menuBarVBox = new MenuBarVBox();
        screen.getChildren().addAll(menuBarVBox, tabPane);

        AtomicReference<Scene> scene = new AtomicReference<>(new Scene(screen));
        primaryStage.setScene(scene.get());
        primaryStage.show();

        menuBarVBox.closeMenuItem.setOnAction(e -> {
            System.out.println("Close selected");
            primaryStage.close();
        });
        menuBarVBox.settingMenuItem.setOnAction(e -> {
            System.out.println("Setting Popup Opened");
            try {
                showSettingPopup(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        menuBarVBox.reloadMenuItem.setOnAction(_ -> {
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