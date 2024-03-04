package com.nevader.casino.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OCRProcessorService {

    private ITesseract tesseract;
    private static final Logger LOGGER = Logger.getLogger(OCRProcessorService.class.getName());

    public OCRProcessorService() {
        this.tesseract = new Tesseract();
        tesseract.setLanguage("eng");
    }

    public void setTesseract(String tessreactPath) {
        tesseract.setDatapath(tessreactPath);
    }

    public List<Word> findNumbers (BufferedImage bufferedImage) {
        List<Word> words = tesseract.getWords(bufferedImage, 3);
        System.out.println(words.size() + "size");
        return words;
    }



    public String doOCR(BufferedImage bufferedImage) {

        try {
            return tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            LOGGER.log(Level.SEVERE, "doOCR error", e);
            throw new RuntimeException("doOCR error", e);
        }

    }



































/*    public BufferedImage convertImage(BufferedImage bufferedImage) {

        Mat src = bufferedImageToMat(bufferedImage);

        Mat gray = new Mat();
        Mat binary = new Mat();

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_CLOSE, kernel);
        return matToBufferedImage(binary);

    }

    private BufferedImage matToBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] buffer = new byte[bufferSize];
        matrix.get(0, 0, buffer); // get all the pixels
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    private Mat bufferedImageToMat(BufferedImage bi) {
        BufferedImage convertedImage;
        if (bi.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            convertedImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            convertedImage.getGraphics().drawImage(bi, 0, 0, null);
        } else {
            convertedImage = bi;
        }

        Mat mat = new Mat(convertedImage.getHeight(), convertedImage.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) convertedImage.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    public BufferedImage enchanceImage (BufferedImage bufferedImage) {

        int newWidth = bufferedImage.getWidth() * 4;  // Double the width
        int newHeight = bufferedImage.getHeight() * 4;  // Double the height
        Image tmp = bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public BufferedImage convertImageRed(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        int lowerRedBound = 53;
        int upperRedBound = 136;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(bufferedImage.getRGB(x, y));

                if (color.getRed() >= lowerRedBound && color.getRed() <= upperRedBound) {
                    outputImage.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    outputImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return outputImage;
    }

    public BufferedImage processImageForSpecificColorRange(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Color lowerBound = new Color(108, 21, 15);
        Color upperBound = new Color(145, 26, 18);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(bufferedImage.getRGB(x, y), true);

                if (isWithinRange(color, lowerBound, upperBound)) {
                    outputImage.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    outputImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        return outputImage;
    }

    private boolean isWithinRange(Color color, Color lowerBound, Color upperBound) {
        return color.getRed() >= lowerBound.getRed() && color.getRed() <= upperBound.getRed() &&
                color.getGreen() >= lowerBound.getGreen() && color.getGreen() <= upperBound.getGreen() &&
                color.getBlue() >= lowerBound.getBlue() && color.getBlue() <= upperBound.getBlue();
    }*/


}
