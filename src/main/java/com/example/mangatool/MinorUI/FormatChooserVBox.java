package com.example.mangatool.MinorUI;

import static com.example.mangatool.AppFunction.*;
import static com.example.mangatool.TextConfig.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FormatChooserVBox extends VBox {

    public TextField startIndexTextField;
    public ComboBox<String> fileFormatCombo;
    public ComboBox<String> nameFormatCombo;

    public FormatChooserVBox() {
        ObservableList<String> formats = FXCollections.observableArrayList("jpg", "png", "bmp", "tiff", "webp");
        ObservableList<String> nameFormats = FXCollections.observableArrayList("1", "2", "3", "4", "5");

        this.fileFormatCombo = new ComboBox<>(formats);
        this.fileFormatCombo.getSelectionModel().selectFirst();

        this.nameFormatCombo = new ComboBox<>(nameFormats);
        this.nameFormatCombo.getSelectionModel().select(2);

        Text formatTitle = new Text(file_format_text);
        Text nameFormatTitle = new Text(name_format_text);
        Text startIndexTitle = new Text(start_index_text);
        startIndexTextField = new TextField();
        startIndexTextField.setPrefWidth(small_text_field_pref_width);
        startIndexTextField.setText("0");
        startIndexTextField.setTooltip(new Tooltip(positive_number_tooltip));

        Text formatNotification = new Text();
        formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText()));

        fileFormatCombo.setOnAction(e -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText())));
        nameFormatCombo.setOnAction(e -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText())));
        startIndexTextField.setOnKeyTyped(e -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText())));


        HBox hBoxChooseFormat = new HBox(default_spacing);
        hBoxChooseFormat.setSpacing(default_spacing);
        hBoxChooseFormat.setAlignment(Pos.CENTER);
        hBoxChooseFormat.setPadding(new Insets(default_padding));
        hBoxChooseFormat.getChildren().addAll(formatTitle, this.fileFormatCombo, nameFormatTitle, this.nameFormatCombo);

        HBox hBoxStartIndex = new HBox(default_spacing);
        hBoxStartIndex.setSpacing(default_spacing);
        hBoxStartIndex.setAlignment(Pos.CENTER);
        hBoxStartIndex.setPadding(new Insets(default_padding));
        hBoxStartIndex.getChildren().addAll(startIndexTitle, this.startIndexTextField);

        this.getChildren().addAll(hBoxChooseFormat, hBoxStartIndex, formatNotification);
        this.setSpacing(default_spacing);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(default_padding));

    }

    public static String setTextFormatNotification(String nameFormat, String fileFormat, String startIndex) {
        int formatCounter = 0;
        if (isPositiveInteger(startIndex)) {
            formatCounter = Integer.parseInt(startIndex);
        }
        String res;
        res = "Your file will be save as " + String.format("%0" + nameFormat + "d", formatCounter) + "." + fileFormat + ", " + String.format("%0" + nameFormat + "d", formatCounter + 1) + "." + fileFormat + ", etc.";

        return res;
    }

}
