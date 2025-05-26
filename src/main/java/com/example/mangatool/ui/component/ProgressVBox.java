package com.example.mangatool.ui.component;

import static com.example.mangatool.common.TextConfig.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class ProgressVBox extends VBox {

    public ProgressBar progressBar;
    public Label progressLabel;
    public Button runButton;

    public ProgressVBox() {

        this.runButton = new Button("Run");
        this.progressBar = new ProgressBar(0);
        this.progressLabel = new Label("");

        this.getChildren().addAll(runButton, progressBar, progressLabel);
        this.setSpacing(default_spacing);
        this.setPadding(new Insets(default_padding));
        this.setAlignment(Pos.TOP_CENTER);
    }
}
