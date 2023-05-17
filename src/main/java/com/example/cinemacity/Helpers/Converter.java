package com.example.cinemacity.Helpers;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;

public class Converter {

    public static byte[] convertImageToByteArray(Image image, String formatName) throws IOException {
        if (image == null){
            throw new NullPointerException();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, formatName, baos);
        return baos.toByteArray();
    }

    public static Image convertArrayOfByteToImage(byte[] buffer) {
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
        return new Image(bis);
    }
}
