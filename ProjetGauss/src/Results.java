import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Results {
    public static double[][] loadDatafromImage(String path) throws IOException {
        BufferedImage bui = ImageIO.read(new File(path));

        int width = bui.getWidth();
        int height = bui.getHeight();

        int[] im_pixels = bui.getRGB(0, 0, width, height, null, 0, width);
        Color[] tabColor= new Color[im_pixels.length];
        for(int i=0 ; i<im_pixels.length ; i++) {
            tabColor[i] = new Color(im_pixels[i]);
        }

        double[][] data = new double[height*width][3];
        for (int i=0; i<tabColor.length;i++){
            data[i][0] = tabColor[i].getRed()/255.0;
            data[i][1] = tabColor[i].getGreen()/255.0;
            data[i][2] = tabColor[i].getBlue()/255.0;
        }
        return data;
    }
}
