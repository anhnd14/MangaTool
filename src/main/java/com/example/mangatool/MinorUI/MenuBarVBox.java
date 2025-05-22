package com.example.mangatool.MinorUI;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class MenuBarVBox extends VBox {

    public MenuItem closeMenuItem;
    public MenuItem reloadMenuItem;
    public MenuItem settingMenuItem;

    public MenuBarVBox() {
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

        closeMenuItem = new MenuItem("Close");
        reloadMenuItem = new MenuItem("Reload");

        fileMenu.getItems().addAll(closeMenuItem, reloadMenuItem);

        settingMenuItem = new MenuItem("Setting");
        settingMenu.getItems().add(settingMenuItem);

        this.getChildren().add(menuBar);

    }
}
