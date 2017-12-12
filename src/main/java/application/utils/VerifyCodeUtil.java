package application.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by lancec on 2014/6/19.
 */
public class VerifyCodeUtil {
    private static final String NUMBER = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int HEIGHT=30;
    private static final int WIDTH=70;

    public static String drawVerifyCode(OutputStream os) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        String passNumber = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(NUMBER.charAt(random.nextInt(NUMBER.length())));
            passNumber += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 15 * i + 8, 24);
        }
        g.dispose();
        ImageIO.write(image, "JPEG", os);
        return passNumber;
    }

    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
