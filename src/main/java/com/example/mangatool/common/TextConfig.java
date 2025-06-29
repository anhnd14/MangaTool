package com.example.mangatool.common;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public class TextConfig {

    //properties
    public static final String FILENAME = "app.properties";

    //text, title cho text field
    public static String file_format_text = "Choose File Format:";
    public static String name_format_text = "Choose Name Format:";
    public static String start_index_text = "Start Index:";
    public static String input_path_text = "Input Path:";
    public static String input_file_list_text = "Input Files:";
    public static String output_path_text = "Output Path:";
    public static String top_image_text = "Image Path:";
    public static String top_image_height_text = "Height:";
    public static String top_image_opacity_text = "Opacity:";
    public static String top_image_position_text = "Position:";
    public static String top_image_x_coordinate_text = "X:";
    public static String top_image_y_coordinate_text = "Y:";
    public static String top_crop = "Top:";
    public static String bottom_crop = "Bottom:";
    public static String left_crop = "Left:";
    public static String right_crop = "Right:";
    public static String choose_image_text = "Choose Image:";
    public static String choose_file_text = "Choose File:";
    public static String out_file_name_text = "Out File Name:";
    public static String join_direction_text = "Join Direction:";


    //tooltip
    public static String positive_number_tooltip = "Điền số nguyên dương";
    public static String valid_double_tooltip = "Điền số từ 0-1";

    //text button
    public static String select_folder_button_text = "Select Folder";
    public static String open_folder_button_text = "Open Folder";
    public static String select_file_button_text = "Select File";
    public static String open_file_button_text = "Open File";
    public static String select_files_button_text = "Select Files";
    public static String reset_all_button_text = "Reset All";
    public static String reset_line_button_text = "Reset Lines";


    //các giá trị default
    public static int long_text_field_pref_width = 300;
    public static int small_text_field_pref_width = 50;
    public static int default_spacing = 10;
    public static int default_padding = 10;
    public static int default_width = 500;


    //filter file
    public static String images_file_extension_text = "Image Files";
    public static String pdf_file_extension_text = "PDF File";
    public static List<String> image_extensions = Arrays.asList("*.jpg", "*.jpeg", "*.png", "*.bpm", "*.webp", "*.tiff");
    public static List<String> pdf_extensions = List.of("*.pdf");


    //dropdown box list
    public static ObservableList<String> images_format = FXCollections.observableArrayList("jpg", "jpeg", "png", "bmp", "tiff", "webp");
    public static ObservableList<String> name_counter_formats = FXCollections.observableArrayList("1", "2", "3", "4", "5");
    public static ObservableList<String> position = FXCollections.observableArrayList("Top Left", "Top Right", "Bottom Left", "Bottom Right");

}
