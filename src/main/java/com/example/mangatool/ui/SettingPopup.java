package com.example.mangatool.ui;


import static com.example.mangatool.common.CommonFunction.*;
import static com.example.mangatool.common.TextConfig.*;
import static com.example.mangatool.common.AppProperties.*;


import com.example.mangatool.ui.component.SmallInputRow;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Properties;

public class SettingPopup extends Stage {

    public SmallInputRow defaultTopCropTextField;
    public SmallInputRow defaultBottomCropTextField;
    public SmallInputRow defaultLeftCropTextField;
    public SmallInputRow defaultRightCropTextField;

    public String defaultTopCropValue;
    public String defaultBottomCropValue;
    public String defaultLeftCropValue;
    public String defaultRightCropValue;

    public SmallInputRow defaultTopImageHeightTextField;
    public SmallInputRow defaultTopImageOpacityTextField;
    public SmallInputRow defaultTopImageXCoordinateTextField;
    public SmallInputRow defaultTopImageYCoordinateTextField;

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

        defaultTopCropValue = loadData(DEFAULT_TOP_CROP, "420");
        defaultBottomCropValue = loadData(DEFAULT_BOTTOM_CROP, "420");
        defaultLeftCropValue = loadData(DEFAULT_LEFT_CROP, "0");
        defaultRightCropValue = loadData(DEFAULT_RIGHT_CROP, "0");

        defaultTopImageHeightValue = loadData(DEFAULT_OVERLAY_IMAGE_HEIGHT, "100");
        defaultTopImageOpacityValue = loadData(DEFAULT_OVERLAY_IMAGE_OPACITY, "0.1");
        defaultTopImageXCoordinateValue = loadData(DEFAULT_OVERLAY_IMAGE_X, "10");
        defaultTopImageYCoordinateValue = loadData(DEFAULT_OVERLAY_IMAGE_Y, "10");

        defaultTopCropTextField = new SmallInputRow(top_crop, defaultTopCropValue);
        defaultBottomCropTextField = new SmallInputRow(bottom_crop, defaultBottomCropValue);
        defaultLeftCropTextField = new SmallInputRow(left_crop, defaultLeftCropValue);
        defaultRightCropTextField = new SmallInputRow(right_crop, defaultRightCropValue);

        defaultTopImageHeightTextField = new SmallInputRow(top_image_height_text, defaultTopImageHeightValue);
        defaultTopImageOpacityTextField = new SmallInputRow(top_image_opacity_text, defaultTopImageOpacityValue);
        defaultTopImageXCoordinateTextField = new SmallInputRow(top_image_x_coordinate_text, defaultTopImageXCoordinateValue);
        defaultTopImageYCoordinateTextField = new SmallInputRow(top_image_y_coordinate_text, defaultTopImageYCoordinateValue);

        HBox defaultTopAndBottomCropHBox = new HBox(default_spacing);
        defaultTopAndBottomCropHBox.setPadding(new Insets(default_padding));
        defaultTopAndBottomCropHBox.getChildren().addAll(defaultTopCropTextField, defaultBottomCropTextField);
        defaultTopAndBottomCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultLeftAndRightCropHBox = new HBox(default_spacing);
        defaultLeftAndRightCropHBox.setPadding(new Insets(default_padding));
        defaultLeftAndRightCropHBox.getChildren().addAll(defaultLeftCropTextField, defaultRightCropTextField);
        defaultLeftAndRightCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultTopImageHeightAndOpacityHBox = new HBox(default_spacing);
        defaultTopImageHeightAndOpacityHBox.setPadding(new Insets(default_padding));
        defaultTopImageHeightAndOpacityHBox.getChildren().addAll(defaultTopImageHeightTextField, defaultTopImageOpacityTextField);
        defaultTopImageHeightAndOpacityHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultTopImageCoordinateHBox = new HBox(default_spacing);
        defaultTopImageCoordinateHBox.setPadding(new Insets(default_padding));
        defaultTopImageCoordinateHBox.getChildren().addAll(defaultTopImageXCoordinateTextField, defaultTopImageYCoordinateTextField);
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
        saveData(DEFAULT_OVERLAY_IMAGE_HEIGHT, defaultTopImageHeightTextField.getText());
        saveData(DEFAULT_OVERLAY_IMAGE_OPACITY, defaultTopImageOpacityTextField.getText());
        saveData(DEFAULT_OVERLAY_IMAGE_X, defaultTopImageXCoordinateTextField.getText());
        saveData(DEFAULT_OVERLAY_IMAGE_Y, defaultTopImageYCoordinateTextField.getText());

        saveData(DEFAULT_TOP_CROP, defaultTopCropTextField.getText());
        saveData(DEFAULT_BOTTOM_CROP, defaultBottomCropTextField.getText());
        saveData(DEFAULT_LEFT_CROP, defaultLeftCropTextField.getText());
        saveData(DEFAULT_RIGHT_CROP, defaultRightCropTextField.getText());
    }


}
