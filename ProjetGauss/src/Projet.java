import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.chrono.MinguoChronology;
import java.util.Arrays;

public class Projet{
    static BufferedImage bi;

//    public static void main(String[] args) throws IOException
//    {
//        String path = "./";
//        String imageMMS = path + "mms.png";
//
//        // Lecture de l'image ici
//        BufferedImage bui = ImageIO.read(new File(imageMMS));
//
//        int width = bui.getWidth();
//        int height = bui.getHeight();
//        System.out.println("Hauteur=" + width);
//        System.out.println("Largeur=" + height);
//
//        int pixel = bui.getRGB(0, 0);
//        //System.out.println("Pixel 0,0 = "+pixel);
//        Color c = new Color(pixel);
//        System.out.println("RGB = "+c.getRed()+" "+c.getGreen()+" "+c.getBlue());
//        // Calcul des trois composant de couleurs normalisé à 1
//        double[] pix = new double[3];
//        pix[0] = (double) c.getRed()/255.0;
//        pix[1] = (double) c.getGreen()/255.0;
//        pix[2] = (double) c.getBlue()/255.0;
//        System.out.println("RGB normalisé= "+pix[0]+" "+pix[1]+" "+pix[2]);
//
//        int[] im_pixels = bui.getRGB(0, 0, width, height, null, 0, width);
//
//        /** Creation du tableau **/
//        Color[] tabColor= new Color[im_pixels.length];
//        for(int i=0 ; i<im_pixels.length ; i++)
//            tabColor[i]=new Color(im_pixels[i]);
//
//        /** inversion des couleurs **/
//        for(int i=0 ; i<tabColor.length ; i++)
//            tabColor[i]=new Color(255-tabColor[i].getRed(),255-tabColor[i].getGreen(),255-tabColor[i].getBlue());
//
//        /** sauvegarde de l'image **/
//        BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
//        for(int i=0 ; i<height ; i++)
//        {
//            for(int j=0 ; j<width ; j++)
//                bui_out.setRGB(j,i,tabColor[i*width+j].getRGB());
//        }
//        ImageIO.write(bui_out, "PNG", new File(path+"test.png"));
//
//    }

    public static void ecrire(String nomFic, String texte) { //on va chercher le chemin et le nom du fichier et on me tout ca dans un String
        String adressedufichier = System.getProperty("user.dir") + "/"+ nomFic; //on met try si jamais il y a une exception
        try {
            FileWriter fw = new FileWriter(adressedufichier); // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
            BufferedWriter output = new BufferedWriter(fw);//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
            output.write(texte); //on peut utiliser plusieurs fois methode write
            output.flush(); //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
            output.close(); //et on le ferme
            System.out.println("fichier créé");
        }
        catch(IOException ioe){
            System.out.print("Erreur : ");
            ioe.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        String imageMMS = "mms.png";

        double[][] data = Results.loadDatafromImage(imageMMS);
        double[][] centres = new double[6][3];
        double[][] ecarts = new double[centres.length][3];
        double[] ro = new double[centres.length];

        for (int i = 0; i< ro.length; i++){
            ro[i] = 1.0/ro.length;
            Arrays.fill(ecarts[i], 0.3);
        }

        //On place les centres là où ça nous arrange
        //Vert
        centres[0][0] = 0.39; centres[0][1]= 0.80 ; centres[0][2]= 0.29;
        //Bleu...
        centres[1][0] = 0.09; centres[1][1]= 0.42 ; centres[1][2]= 0.78;
        //Rouge...
        centres[2][0] = 0.83; centres[2][1]= 0.4 ; centres[2][2]= 0.4;
        //Noir...
        centres[3][0] = 0.1; centres[3][1]= 0.1 ; centres[3][2]= 0.05;
        //Jaune..
        centres[4][0] = 0.89; centres[4][1]= 0.87 ; centres[4][2]= 0.1;
        //Orange
        centres[5][0] = 0.91; centres[5][1]= 0.35 ; centres[5][2]= 0.06;

        double eps=0.01;
        double maj = 10;

        double [][] a = MixGauss.Assigner(data, centres, ro, ecarts);
        while(maj>eps){
            maj = MixGauss.Deplct(data, centres, ro, ecarts, a);
            a = MixGauss.Assigner(data, centres, ro, ecarts);
        }

        Color[] centres2 = new Color[6];
        for (int i = 0; i< centres.length; i++){
                centres2[i] = new Color((int)(centres[i][0] * 255 ),(int)(centres[i][1] *255 ),(int)(centres[i][2]* 255));
                System.out.println(centres2[i].toString());
        }

        for (int i = 0; i < centres.length;i++){
            BufferedImage bui_out = new BufferedImage(1,1,BufferedImage.TYPE_3BYTE_BGR);
            bui_out.setRGB(0,0,centres2[i].getRGB());
            ImageIO.write(bui_out, "PNG", new File("couleur"+i+".png"));
        }

        System.out.println(Arrays.deepToString(centres));
        System.out.println(Arrays.deepToString(ecarts));
        System.out.println(Arrays.toString(ro));

        double[] test = {0.894, 0.827, 0.639};
        System.out.println("ici : " + MixGauss.belongTo(test, centres, ro, ecarts));

    }
}