package com.example.mangatool.ui.component;

import static com.example.mangatool.common.CommonFunction.*;
import static com.example.mangatool.common.TextConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FormatChooserVBox extends VBox {

    public SmallInputRow startIndex;
    public ComboBox<String> fileFormatCombo;
    public ComboBox<String> nameFormatCombo;

    public FormatChooserVBox() {


        this.fileFormatCombo = new ComboBox<>(images_format);
        this.fileFormatCombo.getSelectionModel().selectFirst();

        this.nameFormatCombo = new ComboBox<>(name_counter_formats);
        this.nameFormatCombo.getSelectionModel().select(2);

        Text formatTitle = new Text(file_format_text);
        Text nameFormatTitle = new Text(name_format_text);
        Text startIndexTitle = new Text(start_index_text);
        startIndex = new SmallInputRow(start_index_text, "0");
        startIndex.textField.setTooltip(new Tooltip(positive_number_tooltip));

        Text formatNotification = new Text();
        formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndex.textField.getText()));

        fileFormatCombo.setOnAction(_ -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndex.textField.getText())));
        nameFormatCombo.setOnAction(_ -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndex.textField.getText())));
        startIndex.textField.setOnKeyTyped(_ -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndex.textField.getText())));


        HBox hBoxChooseFormat = new HBox(default_spacing);
        hBoxChooseFormat.setSpacing(default_spacing);
        hBoxChooseFormat.setAlignment(Pos.CENTER);
        hBoxChooseFormat.setPadding(new Insets(default_padding));
        hBoxChooseFormat.getChildren().addAll(formatTitle, this.fileFormatCombo, nameFormatTitle, this.nameFormatCombo);

        HBox hBoxStartIndex = new HBox(default_spacing);
        hBoxStartIndex.setSpacing(default_spacing);
        hBoxStartIndex.setAlignment(Pos.CENTER);
        hBoxStartIndex.setPadding(new Insets(default_padding));
        hBoxStartIndex.getChildren().addAll(startIndexTitle, this.startIndex.textField);

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

    public String getNameFormat() {
        return this.nameFormatCombo.getValue();
    }

    public String getFileFormat() {
        return this.fileFormatCombo.getValue();
    }

    public String getStartIndex() {
        return this.startIndex.getText();
    }

}
