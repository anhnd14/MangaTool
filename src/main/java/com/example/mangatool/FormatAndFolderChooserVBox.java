package com.example.mangatool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public class FormatAndFolderChooserVBox extends VBox {



    private static final String FILENAME = Reusable.FILENAME;
    public TextField inputPathTextField;
    public TextField outputPathTextField;
    public TextField startIndexTextField;
    public ComboBox<String> fileFormatCombo;
    public ComboBox<String> nameFormatCombo;

    int smallTextFieldPrefWidth = Reusable.small_text_field_pref_width;
    int longTextFieldPrefWidth = Reusable.long_text_field_pref_width;

    String selectFolderText = Reusable.select_folder_button_text;
    String openFolderText = Reusable.open_folder_button_text;

    int defaultSpacing = Reusable.default_spacing;
    int defaultPadding = Reusable.default_padding;


    public FormatAndFolderChooserVBox() {
        ObservableList<String> formats = FXCollections.observableArrayList("jpg", "png", "bmp", "tiff", "webp");
        ObservableList<String> nameFormats = FXCollections.observableArrayList("1", "2", "3", "4", "5");

        this.fileFormatCombo = new ComboBox<>(formats);
        this.fileFormatCombo.getSelectionModel().selectFirst();

        this.nameFormatCombo = new ComboBox<>(nameFormats);
        this.nameFormatCombo.getSelectionModel().select(2);

        Text formatTitle = new Text(Reusable.file_format_text);
        Text nameFormatTitle = new Text(Reusable.name_format_text);
        Text startIndexTitle = new Text(Reusable.start_index_text);
        startIndexTextField = new TextField();
        startIndexTextField.setPrefWidth(smallTextFieldPrefWidth);
        startIndexTextField.setText("0");
        startIndexTextField.setTooltip(new Tooltip(Reusable.positive_number_tooltip));

        Text formatNotification = new Text();
        formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText()));

        fileFormatCombo.setOnAction(e -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText())));
        nameFormatCombo.setOnAction(e -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText())));
        startIndexTextField.setOnKeyTyped(e -> formatNotification.setText(setTextFormatNotification(nameFormatCombo.getValue(), fileFormatCombo.getValue(), startIndexTextField.getText())));

        Text inputText = new Text(Reusable.input_path_text);
        Text outputText = new Text(Reusable.output_path_text);

        inputPathTextField = new TextField();
        inputPathTextField.setPrefWidth(longTextFieldPrefWidth);

        outputPathTextField = new TextField();
        outputPathTextField.setPrefWidth(longTextFieldPrefWidth);

        Button inputPathSelectButton = new Button(selectFolderText);
        Button inputPathOpenButton = new Button(openFolderText);
        inputPathSelectButton.setOnAction(e -> {
            try {
                selectFolder(inputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        inputPathOpenButton.setOnAction(e -> {
            try {
                openFolder(inputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Button outputPathSelectButton = new Button(selectFolderText);
        Button outputPathOpenButton = new Button(openFolderText);
        outputPathSelectButton.setOnAction(e -> {
            try {
                selectFolder(outputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        outputPathOpenButton.setOnAction(e -> {
            try {
                openFolder(outputPathTextField);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        HBox hBoxChooseFormat = new HBox(defaultSpacing);
        hBoxChooseFormat.setSpacing(defaultSpacing);
        hBoxChooseFormat.setAlignment(Pos.CENTER);
        hBoxChooseFormat.setPadding(new Insets(defaultPadding));
        hBoxChooseFormat.getChildren().addAll(formatTitle, this.fileFormatCombo, nameFormatTitle, this.nameFormatCombo);

        HBox hBoxStartIndex = new HBox(defaultSpacing);
        hBoxStartIndex.setSpacing(defaultSpacing);
        hBoxStartIndex.setAlignment(Pos.CENTER);
        hBoxStartIndex.setPadding(new Insets(defaultPadding));
        hBoxStartIndex.getChildren().addAll(startIndexTitle, this.startIndexTextField);

        VBox vBoxChooseFormat = new VBox(defaultSpacing);
        vBoxChooseFormat.setSpacing(defaultSpacing);
        vBoxChooseFormat.setAlignment(Pos.CENTER);
        vBoxChooseFormat.setPadding(new Insets(defaultPadding));
        vBoxChooseFormat.getChildren().addAll(hBoxChooseFormat, hBoxStartIndex, formatNotification);


        HBox hBoxInput = new HBox(defaultSpacing);
        hBoxInput.setSpacing(defaultSpacing);
        hBoxInput.setAlignment(Pos.BASELINE_CENTER);
        hBoxInput.setPadding(new Insets(defaultPadding));
        hBoxInput.getChildren().addAll(inputText, this.inputPathTextField, inputPathSelectButton, inputPathOpenButton);

        HBox hBoxOutput = new HBox(defaultSpacing);
        hBoxOutput.setSpacing(defaultSpacing);
        hBoxOutput.setAlignment(Pos.BASELINE_CENTER);
        hBoxOutput.setPadding(new Insets(defaultPadding));
        hBoxOutput.getChildren().addAll(outputText, this.outputPathTextField, outputPathSelectButton, outputPathOpenButton);

        this.getChildren().addAll(vBoxChooseFormat, hBoxInput, hBoxOutput);
        this.setPadding(new Insets(defaultPadding));
        this.setAlignment(Pos.BASELINE_CENTER);

    }

    public void selectFolder(TextField textField) throws IOException {
        String lastOpenPath = "";
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(FILENAME));
        } catch (Exception exception) {
            properties.store(new FileOutputStream(FILENAME), null);
        }
        if (properties.containsKey("lastOpenPath")) {
            lastOpenPath = Reusable.loadData("lastOpenPath");
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        Path checkPath = Paths.get(lastOpenPath);
        boolean pathExist = Files.isDirectory(checkPath) && Files.exists(checkPath);

        if (!lastOpenPath.equals("") && pathExist) {
            directoryChooser.setInitialDirectory(new File(lastOpenPath));
        }

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            textField.setText(selectedDirectory.getAbsolutePath());
            Reusable.saveData("lastOpenPath", selectedDirectory.getAbsolutePath());
        } else {
            textField.setText("");
        }

    }

    public void openFolder(TextField textField) {
        String pathToOpen = textField.getText();
        if (pathToOpen.isEmpty()) {
            System.out.print("Folder not found");
            return;
        }

        try {
            Desktop.getDesktop().open(new File(pathToOpen));
        } catch (Exception exception) {
            System.out.print("Folder not found");
            exception.printStackTrace();
        }
    }

    public static String setTextFormatNotification(String nameFormat, String fileFormat, String startIndex) {
        int formatCounter = 0;
        if (Reusable.isPositiveInteger(startIndex)) {
            formatCounter = Integer.parseInt(startIndex);
        }
        String res;
        res = "Your file will be save as " + String.format("%0" + nameFormat + "d", formatCounter) + "." + fileFormat + ", " + String.format("%0" + nameFormat + "d", formatCounter + 1) + "." + fileFormat + ", etc.";

        return res;
    }


}
