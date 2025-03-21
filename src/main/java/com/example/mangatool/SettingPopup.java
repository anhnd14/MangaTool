package com.example.mangatool;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

import javafx.scene.control.Tooltip;
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

    int defaultSpacing = Reusable.default_spacing;
    int defaultPadding = Reusable.default_padding;

    int smallTextFieldPrefWidth = Reusable.small_text_field_pref_width;

    public SettingPopup() throws Exception {
        this.initModality(Modality.APPLICATION_MODAL);

        Text defaultSettingTitle = new Text("Setting default values");

        Text defaultSettingCropTitle = new Text("Crop images:");
        Text defaultSettingOverlayTitle = new Text("Overlay images:");

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        Separator separator2 = new Separator(Orientation.HORIZONTAL);


        Reusable.loadProperties(properties);

        defaultTopCropValue = Reusable.loadData("defaultTopCrop", "420");
        defaultBottomCropValue = Reusable.loadData("defaultBottomCrop", "420");
        defaultLeftCropValue = Reusable.loadData("defaultLeftCrop", "0");
        defaultRightCropValue = Reusable.loadData("defaultRightCrop", "0");

        defaultTopImageHeightValue = Reusable.loadData("defaultImageHeight", "100");
        defaultTopImageOpacityValue = Reusable.loadData("defaultImageOpacity", "0.1");
        defaultTopImageXCoordinateValue = Reusable.loadData("defaultImageX", "10");
        defaultTopImageYCoordinateValue = Reusable.loadData("defaultImageY", "10");


        Text defaultTopCropTitle = new Text(Reusable.top_crop);
        Text defaultBottomCropTitle = new Text(Reusable.bottom_crop);
        defaultTopCropTextField = new TextField(defaultTopCropValue);
        defaultTopCropTextField.setPrefWidth(smallTextFieldPrefWidth);
        defaultBottomCropTextField = new TextField(defaultBottomCropValue);
        defaultBottomCropTextField.setPrefWidth(smallTextFieldPrefWidth);

        Text defaultLeftCropTitle = new Text(Reusable.left_crop);
        Text defaultRightCropTitle = new Text(Reusable.right_crop);
        defaultLeftCropTextField = new TextField(defaultLeftCropValue);
        defaultLeftCropTextField.setPrefWidth(smallTextFieldPrefWidth);
        defaultRightCropTextField = new TextField(defaultRightCropValue);
        defaultRightCropTextField.setPrefWidth(smallTextFieldPrefWidth);


        Text defaultTopImageHeightTitle = new Text(Reusable.top_image_height_text);
        Text defaultTopImageOpacityTitle = new Text(Reusable.top_image_opacity_text);
        defaultTopImageHeightTextField = new TextField(defaultTopImageHeightValue);
        defaultTopImageHeightTextField.setPrefWidth(smallTextFieldPrefWidth);
        defaultTopImageOpacityTextField = new TextField(defaultTopImageOpacityValue);
        defaultTopImageOpacityTextField.setPrefWidth(smallTextFieldPrefWidth);


        Text defaultTopImageXCoordinateTitle = new Text(Reusable.top_image_x_coordinate_text);
        Text defaultTopImageYCoordinateTitle = new Text(Reusable.top_image_y_coordinate_text);
        defaultTopImageXCoordinateTextField = new TextField(defaultTopImageXCoordinateValue);
        defaultTopImageXCoordinateTextField.setPrefWidth(smallTextFieldPrefWidth);
        defaultTopImageYCoordinateTextField = new TextField(defaultTopImageYCoordinateValue);
        defaultTopImageYCoordinateTextField.setPrefWidth(smallTextFieldPrefWidth);


        HBox defaultTopAndBottomCropHBox = new HBox(defaultSpacing);
        defaultTopAndBottomCropHBox.setSpacing(defaultSpacing);
        defaultTopAndBottomCropHBox.setPadding(new Insets(defaultPadding));
        defaultTopAndBottomCropHBox.getChildren().addAll(defaultTopCropTitle, defaultTopCropTextField, defaultBottomCropTitle, defaultBottomCropTextField);
        defaultTopAndBottomCropHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultLeftAndRightCropHBox = new HBox(defaultSpacing);
        defaultLeftAndRightCropHBox.setSpacing(defaultSpacing);
        defaultLeftAndRightCropHBox.setPadding(new Insets(defaultPadding));
        defaultLeftAndRightCropHBox.getChildren().addAll(defaultLeftCropTitle, defaultLeftCropTextField, defaultRightCropTitle, defaultRightCropTextField);
        defaultLeftAndRightCropHBox.setAlignment(Pos.BASELINE_CENTER);


        HBox defaultTopImageHeightAndOpacityHBox = new HBox(defaultSpacing);
        defaultTopImageHeightAndOpacityHBox.setSpacing(defaultSpacing);
        defaultTopImageHeightAndOpacityHBox.setPadding(new Insets(defaultPadding));
        defaultTopImageHeightAndOpacityHBox.getChildren().addAll(defaultTopImageHeightTitle, defaultTopImageHeightTextField, defaultTopImageOpacityTitle, defaultTopImageOpacityTextField);
        defaultTopImageHeightAndOpacityHBox.setAlignment(Pos.BASELINE_CENTER);

        HBox defaultTopImageCoordinateHBox = new HBox(defaultSpacing);
        defaultTopImageCoordinateHBox.setSpacing(defaultSpacing);
        defaultTopImageCoordinateHBox.setPadding(new Insets(defaultPadding));
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

        HBox buttonHBox = new HBox(defaultSpacing);
        buttonHBox.setSpacing(defaultSpacing);
        buttonHBox.setPadding(new Insets(defaultPadding));
        buttonHBox.getChildren().addAll(saveButton, closeButton);
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);


        VBox mainVBox = new VBox(defaultSpacing);
        mainVBox.setSpacing(defaultSpacing);
        mainVBox.setPadding(new Insets(defaultPadding));
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
        Reusable.saveData("defaultImageHeight", defaultTopImageHeightTextField.getText());
        Reusable.saveData("defaultImageOpacity", defaultTopImageOpacityTextField.getText());
        Reusable.saveData("defaultImageX", defaultTopImageXCoordinateTextField.getText());
        Reusable.saveData("defaultImageY", defaultTopImageYCoordinateTextField.getText());

        Reusable.saveData("defaultTopCrop", defaultTopCropTextField.getText());
        Reusable.saveData("defaultBottomCrop", defaultBottomCropTextField.getText());
        Reusable.saveData("defaultLeftCrop", defaultLeftCropTextField.getText());
        Reusable.saveData("defaultRightCrop", defaultRightCropTextField.getText());
    }


}
