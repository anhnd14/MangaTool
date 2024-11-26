package com.example.mangatool;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Reusable {

//    static FileReader jsonFile;
//
//    static {
//        try {
//            jsonFile = new FileReader("resources.json");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    static JSONObject jsonObject = new JSONObject(jsonFile);
//    private static final String FILENAME = jsonObject.getString("FILENAME");


    static final String FILENAME = "app.properties";
    static String file_format_text = "Choose File Format:";
    static String name_format_text = "Choose Name Format:";
    static String start_index_text = "Start Index:";
    static String input_path_text = "Input Path:";
    static String output_path_text = "Output Path:";
    static String top_image_text = "Image Path:";
    static String top_image_height_text = "Height";
    static String top_image_opacity_text = "Opacity:";
    static String top_image_position_text = "Position:";
    static String top_image_x_coordinate_text = "X:";
    static String top_image_y_coordinate_text = "Y:";
    static String top_crop = "Top:";
    static String bottom_crop = "Bottom:";
    static String left_crop = "Left:";
    static String right_crop = "Right:";

    static int small_text_field_pref_width = 50;
    static int long_text_field_pref_width = 300;

    static String positive_number_tooltip = "Điền số nguyên dương";
    static String valid_double_tooltip = "Điền số từ 0-1";

    static String select_folder_button_text = "Select Folder";
    static String open_folder_button_text = "Open Folder";
    static String select_file_button_text = "Select File";

    static int default_spacing = 10;
    static int default_padding = 10;


    public static boolean isPositiveInteger(String string) {
        try {
            Integer.parseInt(string);
            int i = Integer.parseInt(string);
            return i >= 0;
        } catch (Exception e) {
            System.out.print("Not an positive integer number");
            return false;
        }
    }

    public static boolean isValidDouble(String string) {
        try {
            Double.parseDouble(string);
            double d = Double.parseDouble(string);
            return !(d < 0) && !(d > 1);
        } catch (Exception e) {
            System.out.print("Not an valid double number");
            return false;
        }
    }

    public static String loadData(String key) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(FILENAME));
            return properties.getProperty(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void saveData(String key, String value) {
        try {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(FILENAME));
            } catch (Exception exception) {
                properties.store(new FileOutputStream(FILENAME), null);
            }
            properties.setProperty(key, value);
            properties.store(new FileOutputStream(FILENAME), null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //Lọc các file ảnh
    public static List<File> filterFiles(File[] files) {
        List<File> fileList = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                int lastDotIndex = fileName.lastIndexOf('.');
                String extension = "";
                if (lastDotIndex > 0) {
                    extension = fileName.substring(lastDotIndex + 1);
                    extension = extension.toLowerCase(); // normalize to lower case
                }
                if (extension.equals("jpg") || extension.equals("png") || extension.equals("bmp") || extension.equals("tiff") || extension.equals("webp")) {
                    fileList.add(file);
                }
            }
        }

        return fileList;
    }

    //Lưu ảnh vào outputPath
    public static void saveImage(BufferedImage image, String outputPath, String formatName) {
        try {
            File output = new File(outputPath);
            ImageIO.write(image, formatName, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
