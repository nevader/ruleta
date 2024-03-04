package com.nevader.casino.services;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScreenshotService {

    private static final Logger LOGGER = Logger.getLogger(ScreenshotService.class.getName());

    private final Robot robot;
    private Rectangle rectangle;


    public ScreenshotService() {

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize Robot class", e);
            throw new RuntimeException("Failed to initialize Robot for Screenshot", e);
        }

        this.rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public BufferedImage takeScreenshot(Rectangle rectangle) {
        return robot.createScreenCapture(rectangle);
    }

    public BufferedImage takeScreenshot() {
        return robot.createScreenCapture(this.rectangle);
    }

    public Image bufferedImageToFx(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }


    public BufferedImage applyFilters (BufferedImage bufferedImage) {

        Mat image = bufferedImageToMat(bufferedImage);

        Mat resizedImage = new Mat();

        Size newSize = new Size(image.width() * 2, image.height() * 2);
        Imgproc.resize(image, resizedImage, newSize, 0, 0, Imgproc.INTER_CUBIC);

        Mat canny = applyCanny(resizedImage);

        Mat grayscale = convertGrayScale(canny);

        return matToBufferedImage(grayscale);
    }




    public String dominantColor(BufferedImage bufferedImage) {
        Mat image = bufferedImageToMat(bufferedImage);

        // Convert the image to the HSV color space
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

        int redCount = 0;
        int whiteCount = 0;

        // Define the color ranges
        Scalar lowerRed = new Scalar(0, 100, 100);  // Lower bound for red color in HSV
        Scalar upperRed = new Scalar(10, 255, 255);  // Upper bound for red color in HSV
        Scalar lowerWhite = new Scalar(0, 0, 200);  // Lower bound for white color in HSV
        Scalar upperWhite = new Scalar(180, 55, 255);  // Upper bound for white color in HSV

        // Iterate over each pixel in the image
        for (int y = 0; y < hsvImage.rows(); y++) {
            for (int x = 0; x < hsvImage.cols(); x++) {
                double[] pixel = hsvImage.get(y, x);

                // Check if the pixel is red
                if (pixel[0] >= lowerRed.val[0] && pixel[0] <= upperRed.val[0]
                        && pixel[1] >= lowerRed.val[1] && pixel[1] <= upperRed.val[1]
                        && pixel[2] >= lowerRed.val[2] && pixel[2] <= upperRed.val[2]) {
                    redCount++;
                }

                // Check if the pixel is white
                if (pixel[0] >= lowerWhite.val[0] && pixel[0] <= upperWhite.val[0]
                        && pixel[1] >= lowerWhite.val[1] && pixel[1] <= upperWhite.val[1]
                        && pixel[2] >= lowerWhite.val[2] && pixel[2] <= upperWhite.val[2]) {
                    whiteCount++;
                }
            }
        }

        // Determine the dominant color
        if (redCount > whiteCount) {
            return "red";
        } else if (whiteCount > redCount) {
            return "white";
        } else {
            return "equal";
        }
    }


    private Mat convertToBinary(Mat grayImage) {
        Mat binaryImage = new Mat();

        Imgproc.threshold(grayImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        return binaryImage;
    }

    private Mat reduceNoise(Mat binaryImage) {
        // Apply morphological opening (remove small objects from the background)
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)); // Adjust kernel size as needed
        Mat opening = new Mat();
        Imgproc.morphologyEx(binaryImage, opening, Imgproc.MORPH_OPEN, kernel);
/*
        // Apply morphological closing (fill small holes in the foreground)
        Mat closing = new Mat();
        Imgproc.morphologyEx(opening, closing, Imgproc.MORPH_CLOSE, kernel);

        // Optionally, apply a median filter
        Mat medianFiltered = new Mat();
        Imgproc.medianBlur(closing, medianFiltered, 3); // Adjust the kernel size as needed*/

        return opening;
    }


    private Mat adjustContrastAndBrightness(Mat image, double alpha, int beta) {
        Mat newImage = new Mat();
        image.convertTo(newImage, -1, alpha, beta);
        return newImage;
    }

    private Mat enhanceRedText(Mat image) {
        // Convert to HSV and enhance red color
        Mat hsv = new Mat();
        Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);
        Scalar lowerRed = new Scalar(0, 70, 50); // Adjust these values
        Scalar upperRed = new Scalar(10, 255, 255); // Adjust these values
        Mat redMask = new Mat();
        Core.inRange(hsv, lowerRed, upperRed, redMask);
        Mat redEnhanced = new Mat();
        hsv.copyTo(redEnhanced, redMask);

        // Convert back to BGR
        Imgproc.cvtColor(redEnhanced, redEnhanced, Imgproc.COLOR_HSV2BGR);

        return redEnhanced;
    }

    private Mat resizeImage(Mat image) {
        Mat resizedImage = new Mat();
        Size newSize = new Size(image.width() * 2, image.height() * 2);  // Double the size of the image
        Imgproc.resize(image, resizedImage, newSize);
        return resizedImage;
    }


    private Mat applyCanny (Mat image) {
        // Create an empty image with the same dimensions as the original
        Mat edges = new Mat(image.size(), image.type(), Scalar.all(0));

        // Apply Canny edge detector
        Mat detectedEdges = new Mat();
        Imgproc.Canny(image, detectedEdges, 100, 200); // Adjust the thresholds as needed

        // Draw the edges on the empty image
        image.copyTo(edges, detectedEdges);

        // Create an output Mat to store the final result
        Mat output = new Mat();
        Core.addWeighted(image, 0.8, edges, 0.2, 0.0, output); // Adjust weights as needed

        return output;
    }

    private Mat applyClahe (Mat image) {

        Mat lab = new Mat();
        Imgproc.cvtColor(image, lab, Imgproc.COLOR_BGR2Lab);
        ArrayList<Mat> lab_planes = new ArrayList<>(3);
        Core.split(lab, lab_planes);
        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(4);
        Mat clahe_output = new Mat();
        clahe.apply(lab_planes.get(0), clahe_output);
        lab_planes.set(0, clahe_output);
        Core.merge(lab_planes, lab);
        Imgproc.cvtColor(lab, image, Imgproc.COLOR_Lab2BGR);
        return image;
    }


    private Mat applyAdaptiveThresholding(Mat image) {
        Mat thresholdImage = new Mat();
        double thresholdValue = 130;  // Adjust this value based on the brightness of the objects
        Imgproc.threshold(image, thresholdImage, thresholdValue, 255, Imgproc.THRESH_BINARY);
        return thresholdImage;
    }

    public Mat applyGaussianBlur (Mat image) {
        Mat smoothed = new Mat();
        Imgproc.GaussianBlur(image, smoothed, new Size(3, 3), 0);
        return smoothed;
    }

    public Mat binarizeImage (Mat image) {
        Mat binarized = new Mat();
        Imgproc.adaptiveThreshold(image, binarized, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY, 11, 2);
        return binarized;
    }

    public Mat enhanceContrast (Mat image) {
        Mat contrastEnhanced = new Mat();
        Core.convertScaleAbs(image, contrastEnhanced, 1.5, 0);
        return contrastEnhanced;
    }

    public Mat sharpenImage (Mat image) {

        Mat sharpened = new Mat();
        Mat kernel = new Mat(3, 3, CvType.CV_32F) {
            {
                put(0, 0, -1); put(0, 1, -1); put(0, 2, -1);
                put(1, 0, -1); put(1, 1, 9);  put(1, 2, -1);
                put(2, 0, -1); put(2, 1, -1); put(2, 2, -1);
            }
        };
        Imgproc.filter2D(image, sharpened, image.depth(), kernel);

        return sharpened;
    }

    public Mat convertGrayScale (Mat image) {

        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat inverted = new Mat();
        Core.bitwise_not(grayImage, inverted);

        return inverted;
    }

    public Mat bufferedImageToMat(BufferedImage bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mat mat = Imgcodecs.imdecode(new MatOfByte(baos.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
        Mat rgbImage = new Mat();
        Imgproc.cvtColor(mat, rgbImage, Imgproc.COLOR_RGB2BGR);
        return mat;
    }

    public BufferedImage matToBufferedImage(Mat matrix) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte[] byteArray = mob.toArray();
        BufferedImage bi = null;

        try {
            bi = ImageIO.read(new ByteArrayInputStream(byteArray));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bi;
    }



}
