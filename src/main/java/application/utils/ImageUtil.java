package application.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

/**
 * Created by lancec on 2014/8/24.
 */
public class ImageUtil {

    public static BufferedImage resize(BufferedImage sourceImage, int newWidth, boolean allowResizeBigger) throws IOException {

        Image resizedImage = null;
        int iWidth = sourceImage.getWidth(null);
        int iHeight = sourceImage.getHeight(null);

        //If not allow resize to bigger image, return the source image.
        if (!allowResizeBigger && iWidth <= newWidth) {
            return sourceImage;
        }
        if (iWidth > iHeight) {
            resizedImage = sourceImage.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
        } else {
            resizedImage = sourceImage.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
        }

        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(resizedImage).getImage();

        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();

        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor,
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        return bufferedImage;
    }

    public static BufferedImage resizeAndCutImage(BufferedImage sourceImage, Integer resizeWidth, int x, int y, int width, int height) throws IOException {
        Image cutImage = null;
        int iWidth = sourceImage.getWidth(null);
        int iHeight = sourceImage.getHeight(null);
        if (resizeWidth != null) {
            if (iWidth > iHeight) {
                cutImage = sourceImage.getScaledInstance(resizeWidth, (resizeWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
            } else {
                cutImage = sourceImage.getScaledInstance((resizeWidth * iWidth) / iHeight, resizeWidth, Image.SCALE_SMOOTH);
            }

        } else {
            cutImage = sourceImage;
        }
        if (iWidth < width || iHeight < height) {
            cutImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }

        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(cutImage).getImage();

        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();

        // Clear background and paint the image.
/*        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));*/
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        bufferedImage = bufferedImage.getSubimage(x, y, width, height);

        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor,
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        return bufferedImage;
    }
}
