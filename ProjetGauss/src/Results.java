import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Results {

    // Charge une image et la transforme en double tableau. Chaque pixel une case avec 3 variables : HSB
    public static double[][] loadDatafromImage(String path) throws IOException {
        BufferedImage bui = ImageIO.read(new File(path));

        int width = bui.getWidth();
        int height = bui.getHeight();

        int[] im_pixels = bui.getRGB(0, 0, width, height, null, 0, width);
        Color[] tabColor = new Color[im_pixels.length];
        for(int i=0 ; i<im_pixels.length ; i++) {
            tabColor[i] = new Color(im_pixels[i]);
        }

        double[][] data = new double[height*width][3];
        for (int i=0; i<tabColor.length;i++){
            float[] hsb = Color.RGBtoHSB(tabColor[i].getRed(), tabColor[i].getGreen(), tabColor[i].getBlue(), null);
            data[i][0] = hsb[0];
            data[i][1] = hsb[1];
            data[i][2] = hsb[2];
        }
        return data;
    }


    // Fonction qui creer autant de fichier que de gaussienne
    // Pour chaque gaussienne, elle affiche les points qui lui sont attribuées et les autres en noir
    public static void separateImage(String image, double[][] centres, double[] ro, double[][] ecarts, String dir) throws IOException {
        BufferedImage bui = ImageIO.read(new File(image));
        for(int i = 0; i<centres.length; i++) {
            BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
            for(int j = 0; j<bui.getWidth(); j++){
                for(int k = 0; k<bui.getHeight(); k++){
                    Color im_pixels = new Color(bui.getRGB(j, k));
                    float[] hsb = Color.RGBtoHSB(im_pixels.getRed(), im_pixels.getGreen(), im_pixels.getBlue(), null);
                    double[] pixel = {hsb[0], hsb[1], hsb[2]};
                    if(MixGauss.belongTo(pixel, centres, ro, ecarts) == i)
                        bui_out.setRGB(j,k, im_pixels.getRGB());
                    else{
                        Color black = new Color(0, 0, 0);
                        bui_out.setRGB(j,k, black.getRGB());
                    }
                }
            }
            ImageIO.write(bui_out, "PNG", new File("Results/Separations/" + dir + "/" + i + ".png"));
        }

    }

    //Compresse une image avec nb_centres
    public static void compressImage(String image, int nb_centres, String filename) throws IOException {
        double[][] data = Results.loadDatafromImage(image);
        double[][] centres = MixGauss.createCentre(nb_centres, 3, 0, 1);
        double[][] ecarts = new double[centres.length][3];
        double[] ro = new double[centres.length];

        for (int i = 0; i< ro.length; i++){
            ro[i] = 1.0/ro.length;
            Arrays.fill(ecarts[i], Math.pow(0.3,2));
        }

        double eps = 0.01;
        double maj = 10;

        double [][] a = MixGauss.Assigner(data, centres, ro, ecarts);
        while(maj>eps){
            maj = MixGauss.Deplct(data, centres, ro, ecarts, a);
            a = MixGauss.Assigner(data, centres, ro, ecarts);
        }

        double[] score = new double[data.length];
        for (int i = 0; i < data.length; i++){
            score[i] = MixGauss.score(centres, ecarts, ro, data[i]);
        }
        System.out.println("Le score moyen est de : " + MixGauss.scoreMoy(score));

        BufferedImage bui = ImageIO.read(new File(image));
        BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);

        for(int j = 0; j<bui.getWidth(); j++) {
            for (int k = 0; k < bui.getHeight(); k++) {
                Color im_pixels = new Color(bui.getRGB(j, k));
                for(int i = 0; i<centres.length; i++){
                    float[] hsb = Color.RGBtoHSB(im_pixels.getRed(), im_pixels.getGreen(), im_pixels.getBlue(), null);
                    double[] pixel = {hsb[0], hsb[1], hsb[2]};
                    if(MixGauss.belongTo(pixel, centres, ro, ecarts) == i) {
                        int centre_color = Color.HSBtoRGB((float) centres[i][0], (float) centres[i][1], (float) centres[i][2]);
                        bui_out.setRGB(j, k, centre_color);
                        break;
                    }
                }
            }
        }
        ImageIO.write(bui_out, "PNG", new File(filename));
    }

}

