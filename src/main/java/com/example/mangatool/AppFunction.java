package com.example.mangatool;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.example.mangatool.TextConfig.*;

public class AppFunction {



    //Check điều kiện
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


    //load, lưu properties
    public static void loadProperties(Properties properties) throws Exception {

        try {
            properties.load(new FileInputStream(FILENAME));
        } catch (Exception e) {
            properties.store(new FileOutputStream(FILENAME), null);
        }
    }
    public static String loadData(String key) {
        try {
            Properties properties = new Properties();
            loadProperties(properties);
            return properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String loadData(String key, String replace) {
        try {
            Properties properties = new Properties();
            loadProperties(properties);
            if (properties.containsKey(key)) {
                return  properties.getProperty(key);
            } else {
                return replace;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void saveData(String key, String value) {
        try {
            Properties properties = new Properties();
            loadProperties(properties);
            properties.setProperty(key, value);
            properties.store(new FileOutputStream(FILENAME), null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //chọn file, folder
    public static void fileSelector(TextField textField) throws Exception {

        String lastOpenFile = "";

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(FILENAME));
        } catch (Exception e) {
            prop.store(new FileOutputStream(FILENAME), null);
        }
        if (prop.containsKey("lastOpenFile")) {
            lastOpenFile = loadData("lastOpenFile");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        assert lastOpenFile != null;
        Path checkFilePath = Paths.get(lastOpenFile);
        boolean fileExist = Files.isRegularFile(checkFilePath) && Files.exists(checkFilePath);

        if (!lastOpenFile.isEmpty() && fileExist) {
            fileChooser.setInitialDirectory(new File(new File(lastOpenFile).getParent()));
        }

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            textField.setText(selectedFile.getAbsolutePath());
            saveData("lastOpenFile", selectedFile.getAbsolutePath());
            System.out.print(selectedFile.getAbsolutePath());
        } else {
            textField.setText("");
        }

    }
    public static String fileSelector() throws Exception {

        String lastOpenFile = "";

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(FILENAME));
        } catch (Exception e) {
            prop.store(new FileOutputStream(FILENAME), null);
        }
        if (prop.containsKey("lastOpenFile")) {
            lastOpenFile = loadData("lastOpenFile");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        assert lastOpenFile != null;
        Path checkFilePath = Paths.get(lastOpenFile);
        boolean fileExist = Files.isRegularFile(checkFilePath) && Files.exists(checkFilePath);

        if (!lastOpenFile.isEmpty() && fileExist) {
            fileChooser.setInitialDirectory(new File(new File(lastOpenFile).getParent()));
        }

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            saveData("lastOpenFile", selectedFile.getAbsolutePath());
            System.out.println(selectedFile.getAbsolutePath());
        }

        return  selectedFile.getAbsolutePath();
    }
    public static List<File> filesSelector(TextField textField) throws Exception {
        String lastOpenPath = "";
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(FILENAME));
        } catch (Exception exception) {
            properties.store(new FileOutputStream(FILENAME), null);
        }
        if (properties.containsKey("lastOpenPath")) {
            lastOpenPath = loadData("lastOpenPath");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");

        Path checkPath = Paths.get(lastOpenPath);
        boolean pathExist = Files.isDirectory(checkPath) && Files.exists(checkPath);

        if (!lastOpenPath.isEmpty() && pathExist) {
            fileChooser.setInitialDirectory(new File(lastOpenPath));
        }

        List<File> fileList;

        fileList = fileChooser.showOpenMultipleDialog(null);

        List<String> fileListName = new ArrayList<>();

        for (File file : fileList) {
            fileListName.add(file.getName());
        }

        textField.setText(fileListName.toString());

        return fileList;
    }
    public static void selectFolder(TextField textField) throws IOException {
        String lastOpenPath = "";
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(FILENAME));
        } catch (Exception exception) {
            properties.store(new FileOutputStream(FILENAME), null);
        }
        if (properties.containsKey("lastOpenPath")) {
            lastOpenPath = loadData("lastOpenPath");
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
            saveData("lastOpenPath", selectedDirectory.getAbsolutePath());
        } else {
            textField.setText("");
        }

    }
    public static void openFolder(TextField textField) {
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
    public static void openFolder(String pathToOpen) throws IOException {
        if (pathToOpen.isEmpty()) {
            System.out.println("Folder not found");
        }
        try {
            Desktop.getDesktop().open(new File(pathToOpen));
        } catch (Exception exception) {
            System.out.println("Folder not found");
            throw exception;
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

    public static List<File> filterFiles(List<File> files) {
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
