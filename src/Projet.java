import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Projet{


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


    public static double score(double[][] centres,double[][] ecarts, double[] ro, double[] data ){

        double res = 0;
        double tmp = 1d;
        for (int i = 0; i < centres.length; i++){
            for (int j = 0; j < data.length; j++){
                tmp = tmp * (1./Math.sqrt(2*Math.PI*ecarts[i][j]))*Math.exp(-1*(Math.pow(data[j]-centres[i][j],2)/(2*ecarts[i][j])));
            }
            res = res + (ro[i] * tmp);
            tmp = 1;
        }
        return Math.log(res);
    }


    public static double scoreMoy(double[] score){
        double res = 0;
        for (int i = 0; i < score.length; i++){
            res += score[i];
        }
        return res/score.length;
    }


    public static void main(String[] args) throws IOException {
        Random rand = new Random();


        String path = "./";
        String imageMMS = path + "mms.png";

        //Nombre de Gaussiennes
        int K = 20  ;
        //Dimension
        int D = 3;

        // On charge nos données en HSB
        double[][] data = Results.loadDatafromImage(imageMMS);

        double[][] centres = new double[K][D];
        double[][] ecarts = new double[K][D];
        double[] ro = new double[K];

        //On initialise nos tableau de sigma et de ro
        for (int i = 0; i< K; i++){
            ro[i] = 1.0/K;
            for (int j= 0; j < D; j++){
                ecarts[i][j] = 1;
            }
        }

        // Partie pour assigner les centres de facons aleatoire
        for (int i = 0; i < K; i++){
            for (int j = 0; j < D; j++){
                centres[i][j] = rand.nextDouble();
            }
        }


        double eps=0.01;
        double maj = 10;

        double [][] a = MixGauss.Assigner(data, centres, ro, ecarts);
        while(maj>eps){
            maj = MixGauss.Deplct(data, centres, ro, ecarts, a);
            a = MixGauss.Assigner(data, centres, ro, ecarts);
        }


        double[] score = new double[data.length];
        for (int i = 0; i < data.length; i++){
            score[i] = score(centres, ecarts, ro, data[i]);
        }
        System.out.println("Le score moyen est de : " + scoreMoy(score));

        // On enregsitre nos differentes images dans un fichier
        Results.separateImage(imageMMS, centres, ro, ecarts);
    }
}