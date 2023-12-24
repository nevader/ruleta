package com.nevader.casino.services;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OCRProcessorService {

    private Tesseract tesseract;
    private static final Logger LOGGER = Logger.getLogger(OCRProcessorService.class.getName());

    public OCRProcessorService(String tessreactPath) {
        this.tesseract = new Tesseract();
        tesseract.setDatapath(tessreactPath);
    }

    public String doOCR(BufferedImage bufferedImage) {

        try {
            return tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            LOGGER.log(Level.SEVERE, "doOCR error", e);
            throw new RuntimeException("doOCR error", e);
        }

    }
 }
