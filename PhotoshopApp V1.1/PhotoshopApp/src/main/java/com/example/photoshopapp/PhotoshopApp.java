package com.example.photoshopapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PhotoshopApp {

    @FXML
    private BarChart<?, ?> barChartImage;

    @FXML
    private Canvas canvasImage;

    private WritableImage originalImage;

    private WritableImage filteredImage;

    @FXML
    void blueValue(MouseEvent event) {

    }
// Masih error bagian coordinateX dan coordinateY
    @FXML
    void coordinateX(MouseEvent event) {
        double x = event.getX();
        System.out.println("X Coordinate: " + x);
    }

    @FXML
    void coordinateY(MouseEvent event) {
        double y = event.getY();
        System.out.println("Y Coordinate: " + y);
    }

    @FXML
    void cropImage(ActionEvent event) {

    }

    @FXML
    void exitApp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Close the application
                Platform.exit();
            }
        });
    }

    @FXML
    void greenValue(MouseEvent event) {

    }

    @FXML
    void imageBrightening(ActionEvent event) {
        try {
            Image image = canvasImage.snapshot(null, null);

            WritableImage brightenedImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = brightenedImage.getPixelWriter();

            double brightnessFactor = 1.2; // You can adjust this factor as needed

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);

                    double red = Math.min(1, color.getRed() * brightnessFactor);
                    double green = Math.min(1, color.getGreen() * brightnessFactor);
                    double blue = Math.min(1, color.getBlue() * brightnessFactor);

                    pixelWriter.setColor(x, y, Color.color(red, green, blue));
                }
            }

            canvasImage.setWidth(brightenedImage.getWidth());
            canvasImage.setHeight(brightenedImage.getHeight());

            GraphicsContext graphicsContext = canvasImage.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            graphicsContext.drawImage(brightenedImage, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to apply image brightening.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void imageSharpening(ActionEvent event) {
        try {
            Image image = canvasImage.snapshot(null, null);

            WritableImage sharpenedImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = sharpenedImage.getPixelWriter();

            // Define the sharpening kernel
            double[][] kernel = {
                    {0, -1, 0},
                    {-1, 5, -1},
                    {0, -1, 0}
            };

            int kernelSize = kernel.length;
            int kernelRadius = kernelSize / 2;

            for (int y = kernelRadius; y < image.getHeight() - kernelRadius; y++) {
                for (int x = kernelRadius; x < image.getWidth() - kernelRadius; x++) {
                    double red = 0, green = 0, blue = 0;

                    // Apply the convolution kernel
                    for (int ky = 0; ky < kernelSize; ky++) {
                        for (int kx = 0; kx < kernelSize; kx++) {
                            Color color = pixelReader.getColor(x + kx - kernelRadius, y + ky - kernelRadius);
                            red += color.getRed() * kernel[ky][kx];
                            green += color.getGreen() * kernel[ky][kx];
                            blue += color.getBlue() * kernel[ky][kx];
                        }
                    }

                    // Clamp the values to [0, 1]
                    red = Math.min(1, Math.max(0, red));
                    green = Math.min(1, Math.max(0, green));
                    blue = Math.min(1, Math.max(0, blue));

                    pixelWriter.setColor(x, y, Color.color(red, green, blue));
                }
            }

            canvasImage.setWidth(sharpenedImage.getWidth());
            canvasImage.setHeight(sharpenedImage.getHeight());

            GraphicsContext graphicsContext = canvasImage.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            graphicsContext.drawImage(sharpenedImage, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to apply image sharpening.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void importImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image fxImage = new Image(selectedFile.toURI().toString());

                // Set the canvas size to match the image size
                canvasImage.setWidth(fxImage.getWidth());
                canvasImage.setHeight(fxImage.getHeight());

                // Draw the image on the canvas
                GraphicsContext graphicsContext = canvasImage.getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                graphicsContext.drawImage(fxImage, 0, 0);

                // Set absolute position for the canvas
                double canvasX = fxImage.getWidth() + 50; // Adjust the X-coordinate as needed
                double canvasY = 100; // Adjust the Y-coordinate as needed
                canvasImage.setLayoutX(canvasX);
                canvasImage.setLayoutY(canvasY);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open the image file.", Alert.AlertType.ERROR);
            }
        }
    }




    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void redValue(MouseEvent event) {

    }

    @FXML
    void resizeImage(ActionEvent event) {

    }

    @FXML
    void rotateImage(ActionEvent event) {
        try {
            Image image = canvasImage.snapshot(null, null);

            // Create a writable image to hold the rotated image
            WritableImage rotatedImage = new WritableImage((int) image.getHeight(), (int) image.getWidth());
            PixelWriter pixelWriter = rotatedImage.getPixelWriter();

            // Define rotation angle (e.g., 90 degrees clockwise)
            double angle = 90;

            // Set up a transformation for rotation
            Rotate rotation = new Rotate(angle);
            rotation.setPivotX(image.getWidth() / 2);
            rotation.setPivotY(image.getHeight() / 2);

            // Apply the rotation to the image
            GraphicsContext graphicsContext = canvasImage.getGraphicsContext2D();
            graphicsContext.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(),
                    rotation.getTx(), rotation.getTy());

            // Draw the rotated image onto the writable image
            graphicsContext.clearRect(0, 0, rotatedImage.getWidth(), rotatedImage.getHeight());
            graphicsContext.drawImage(image, 0, 0);

            // Reset the transformation
            graphicsContext.setTransform(1, 0, 0, 1, 0, 0);

            // Draw the rotated image onto the canvas in an absolute position
            double rotatedWidth = rotatedImage.getWidth();
            double rotatedHeight = rotatedImage.getHeight();
            double canvasWidth = Math.max(rotatedWidth, canvasImage.getWidth());
            double canvasHeight = Math.max(rotatedHeight, canvasImage.getHeight());

            double translateX = (canvasWidth - rotatedWidth) / 2;
            double translateY = (canvasHeight - rotatedHeight) / 2;

            canvasImage.setTranslateX(translateX);
            canvasImage.setTranslateY(translateY);

            canvasImage.setWidth(canvasWidth);
            canvasImage.setHeight(canvasHeight);

            graphicsContext.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            graphicsContext.drawImage(rotatedImage, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to rotate the image.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    void saveImage(ActionEvent event) {

    }

    @FXML
    void toBW(ActionEvent event) {
        try {
            Image image = canvasImage.snapshot(null, null);

            WritableImage bwImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = bwImage.getPixelWriter();

            double threshold = 0.5;

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    double grayValue = 0.21 * color.getRed() + 0.71 * color.getGreen() + 0.07 * color.getBlue();
                    Color bwColor = (grayValue > threshold) ? Color.WHITE : Color.BLACK;
                    pixelWriter.setColor(x, y, bwColor);
                }
            }

            canvasImage.setWidth(bwImage.getWidth());
            canvasImage.setHeight(bwImage.getHeight());

            GraphicsContext graphicsContext = canvasImage.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            graphicsContext.drawImage(bwImage, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to convert the image to black and white.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void toGray(ActionEvent event) {
        try {
            Image image = canvasImage.snapshot(null, null);

            WritableImage grayImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = grayImage.getPixelWriter();

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    double grayValue = 0.21 * color.getRed() + 0.71 * color.getGreen() + 0.07 * color.getBlue();
                    pixelWriter.setColor(x, y, Color.color(grayValue, grayValue, grayValue));
                }
            }

            canvasImage.setWidth(grayImage.getWidth());
            canvasImage.setHeight(grayImage.getHeight());

            GraphicsContext graphicsContext = canvasImage.getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            graphicsContext.drawImage(grayImage, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to convert the image to grayscale.", Alert.AlertType.ERROR);
        }
    }





}
