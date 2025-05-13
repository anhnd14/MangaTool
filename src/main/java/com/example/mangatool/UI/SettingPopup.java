package com.example.mangatool.UI;


import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Properties;

public class SettingPopup extends Stage {


    public TextField defaultTopCropTextField;
    public TextField defaultBottomCropTextField;
    public TextField defaultLeftCropTextField;
    public TextField defaultRightCropTextField;

    public String defaultTopCropValue;
    public String defaultBottomCropValue;
    public String defaultLeftCropValue;
    public String defaultRightCropValue;

    public TextField defaultTopImageHeightTextField;
    public TextField defaultTopImageOpacityTextField;
    public TextField defaultTopImageXCoordinateTextField;
    public TextField defaultTopImageYCoordinateTextField;

    public String defaultTopImageHeightValue;
    public String defaultTopImageOpacityValue;
    public String defaultTopImageXCoordinateValue;
    public String defaultTopImageYCoordinateValue;

    Properties properties = new Properties();



    public SettingPopup() throws Exception {
        this.initModality(Modality.APPLICATION_MODAL);

        Text defaultSettingTitle = new Text("Setting default values");

        Text defaultSettingCropTitle = new Text("Crop images:");
        Text defaultSettingOverlayTitle = new Text("Overlay images:");

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        Separator separator2 = new Separator(Orientation.HORIZONTAL);


        loadProperties(properties);

        defaultTopCropValue = loadData("defaultTopCrop", "420");
        defaultBottomCropValue = loadData("defaultBottomCrop", "420");
        defaultLeftCropValue = loadData("defaultLeftCrop", "0");
        defaultRightCropValue = loadData("defaultRightCrop", "0");

        defaultTopImageHeightValue = loadData("defaultImageHeight", "100");
        defaultTopImageOpacityValue = loadData("defaultImageOpacity", "0.1");
        defaultTopImageXCoordinateValue = loadData("defaultImageX", "10");
        defaultTopImageYCoordinateValue = loadData("defaultImageY", "10");


        Text defaultTopCropTitle = new Text(top_crop);
        Text defaultBottomCropTitle = new Text(bottom_crop);
        defaultTopCropTextField = new TextField(defaultTopCropValue);
        defaultTopCropTextField.setPrefWidth(small_text_field_pref_width);
        defaultBottomCropTextField = new TextField(defaultBottomCropValue);
        defaultBottomCropTextField.setPrefWidth(small_text_field_pref_width);

        Text defaultLeftCropTitle = new Text(left_crop);
        Text defaultRightCropTitle = new Text(right_crop);
        defaultLeftCropTextField = new TextField(defaultLeftCropValue);
        defaultLeftCropTextField.setPrefWidth(small_text_field_pref_width);
        defaultRightCropTextField = new TextField(defaultRightCropValue);
        defaultRightCropTextField.setPrefWidth(small_text_field_pref_width);


        Text defaultTopImageHeightTitle = new Text(top_image_height_text);
        Text defaultTopImageOpacityTitle = new Text(top_image_opacity_text);
        defaultTopImageHeightTextField = new TextField(defaultTopImageHeightValue);
        defaultTopImageHeightTextField.setPrefWidth(small_text_field_pref_width);
        defaultTopImageOpacityTextField = new TextField(defaultTopImageOpacityValue);
        defaultTopImageOpacityTextField.setPrefWidth(small_text_field_pref_width);


        Text defaultTopImageXCoordinateTitle = new Text(top_image_x_coordinate_text);
        Text defaultTopImageYCoordinateTitle = new Text(top_image_y_coordinate_text);
        defaultTopImageXCoordinateTextField = new TextField(defaultTopImageXCoordinateValue);
        defaultTopImageXCoordinateTextField.setPrefWidth(small_text_field_pref_width);
        defaultTopImageYCoordinateTextField = new TextField(defaultTopImageYCoordinateValue);
        defaultTopImageYCoordinateTextField.setPrefWidth(small_text_field_pref_width);


        HBox defaultTopAndBottomCropHBox = new HBox(default_spacing);
        defaultTopAndBottomCropHBox.setSpacing(default_spacing);
        defaultTopAndBottomCropHBox.setPadding(new Insets(default_padding));
        defaultTopAndBottomCropHBox.getChildren().addAll(defaultTopCropTitle, defaultTopCropTextField, defaultBottomCropTitle, defaultBottomCropTextField);
        defaultTopAndBottomCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultLeftAndRightCropHBox = new HBox(default_spacing);
        defaultLeftAndRightCropHBox.setSpacing(default_spacing);
        defaultLeftAndRightCropHBox.setPadding(new Insets(default_padding));
        defaultLeftAndRightCropHBox.getChildren().addAll(defaultLeftCropTitle, defaultLeftCropTextField, defaultRightCropTitle, defaultRightCropTextField);
        defaultLeftAndRightCropHBox.setAlignment(Pos.BASELINE_CENTER);


        HBox defaultTopImageHeightAndOpacityHBox = new HBox(default_spacing);
        defaultTopImageHeightAndOpacityHBox.setSpacing(default_spacing);
        defaultTopImageHeightAndOpacityHBox.setPadding(new Insets(default_padding));
        defaultTopImageHeightAndOpacityHBox.getChildren().addAll(defaultTopImageHeightTitle, defaultTopImageHeightTextField, defaultTopImageOpacityTitle, defaultTopImageOpacityTextField);
        defaultTopImageHeightAndOpacityHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultTopImageCoordinateHBox = new HBox(default_spacing);
        defaultTopImageCoordinateHBox.setSpacing(default_spacing);
        defaultTopImageCoordinateHBox.setPadding(new Insets(default_padding));
        defaultTopImageCoordinateHBox.getChildren().addAll(defaultTopImageXCoordinateTitle, defaultTopImageXCoordinateTextField, defaultTopImageYCoordinateTitle, defaultTopImageYCoordinateTextField);
        defaultTopImageCoordinateHBox.setAlignment(Pos.BASELINE_CENTER);


        Button saveButton = new Button("Save");
        Button closeButton = new Button("Close");

        closeButton.setOnAction(_ -> this.close());

        saveButton.setOnAction(_ -> {
            saveDefaultValues();
            System.out.println("Save Data");
            this.close();
        });

        HBox buttonHBox = new HBox(default_spacing);
        buttonHBox.setSpacing(default_spacing);
        buttonHBox.setPadding(new Insets(default_padding));
        buttonHBox.getChildren().addAll(saveButton, closeButton);
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);


        VBox mainVBox = new VBox(default_spacing);
        mainVBox.setSpacing(default_spacing);
        mainVBox.setPadding(new Insets(default_padding));
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(
                defaultSettingTitle,
                separator1,
                defaultSettingCropTitle,
                defaultTopAndBottomCropHBox,
                defaultLeftAndRightCropHBox,
                separator2,
                defaultSettingOverlayTitle,
                defaultTopImageHeightAndOpacityHBox,
                defaultTopImageCoordinateHBox,
                buttonHBox);


        Scene scene = new Scene(mainVBox);
        this.setScene(scene);

    }

    public void saveDefaultValues() {
        saveData("defaultImageHeight", defaultTopImageHeightTextField.getText());
        saveData("defaultImageOpacity", defaultTopImageOpacityTextField.getText());
        saveData("defaultImageX", defaultTopImageXCoordinateTextField.getText());
        saveData("defaultImageY", defaultTopImageYCoordinateTextField.getText());

        saveData("defaultTopCrop", defaultTopCropTextField.getText());
        saveData("defaultBottomCrop", defaultBottomCropTextField.getText());
        saveData("defaultLeftCrop", defaultLeftCropTextField.getText());
        saveData("defaultRightCrop", defaultRightCropTextField.getText());
    }


}
