package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public final class MyTextGraphicsConverter implements TextGraphicsConverter {
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private double maxRatio = 0;
    private TextColorSchema schema = new MyTextColorSchema();

    private void checkBadImageSize(double imgRatio) throws BadImageSizeException {
        if (maxRatio != 0 && (imgRatio - maxRatio) > 0.0001) {
            throw new BadImageSizeException(Math.round(imgRatio * 100) / 100.0, maxRatio);
        }
    }

    private double getValidImgRatio(int imgWidth, int imgHeight) {
        if (maxWidth != Integer.MAX_VALUE || maxHeight != Integer.MAX_VALUE) {
            int maxImgSize = Integer.max(imgWidth, imgHeight);
            int minSize = Integer.min(maxWidth, maxHeight);
            return 1.0 * maxImgSize / minSize;
        } else {
            return 1.0;
        }
    }

    private void writeImage(BufferedImage bwImg) throws IOException {
        ImageIO.write(bwImg, "png", new File("out.png"));
    }

    private char[][] convertToSymbols(BufferedImage bwImg) {
        char[][] chars = new char[bwImg.getHeight()][bwImg.getWidth()];
        WritableRaster bwRaster = bwImg.getRaster();
        for (int h = 0; h < chars.length; h++) {
            for (int w = 0; w < chars[h].length; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                chars[h][w] = schema.convert(color);
            }
        }
        return chars;
    }

    private String buildString(char[][] chars) {
        StringBuilder hStringBuilder = new StringBuilder(chars.length);
        for (char[] hChars : chars) {
            StringBuilder wStringBuilder = new StringBuilder(hChars.length * 2);
            for (char wChar : hChars) {
                wStringBuilder.append(wChar);
                wStringBuilder.append(wChar);
            }
            hStringBuilder.append(wStringBuilder);
            hStringBuilder.append(System.lineSeparator());
        }
        return hStringBuilder.toString();
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        double imgRatio = 1.0 * imgWidth / imgHeight;

        checkBadImageSize(imgRatio);

        imgRatio = getValidImgRatio(imgWidth, imgHeight);
        imgWidth = (int) (imgWidth / imgRatio);
        imgHeight = (int) (imgHeight / imgRatio);

        Image scaledImage = img.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        return buildString(convertToSymbols(bwImg));
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
