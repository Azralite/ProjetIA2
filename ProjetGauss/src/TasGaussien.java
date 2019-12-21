import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class TasGaussien {


    public static Random rand = new Random();

    public static double[][] histogramme(double xmin, double xmax, int NbCases, double[] ech) {
        double[][] Histo = new double[2][NbCases];
        // TODO: Calcule de la taille d'une case
        double largeur = xmax-xmin;
        double tailleCase = largeur/NbCases;
        int cases;
        for (int i = 0; i < NbCases; i++){
            Histo[0][i]= (xmin + i * (tailleCase));
        }
        for(int i=0; i<ech.length; i++) {
            // TODO: pour chaque valeur: trouver a quelle case elle appartient et incrementer de un l'histogramme
            cases = (int)(ech[i]/tailleCase) - (int)(xmin/tailleCase);
            if (cases >= 0 && cases < NbCases)
                Histo[1][cases]++;

        }
        return Histo;
    }

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

    public static void ecritScript(double xmin, double xmax){
        String res = "set terminal pngcairo size 920, 920 \n" ;
        res+= "set output \"steps2.png\" \n";
        res+="set title 'Histogra'\n";
        res+="set grid\n";
        res+="set style data boxes\n";

        res+="plot 'hist.d'\n";

        ecrire("script.gnu",res);
    }

    public static void ecrHist(double[][] tab, String name){
        String res ="";
        for (int i = 0; i < tab[0].length; i++){
            res += tab[0][i] + " " + tab[1][i] + "\n" ;
        }
        ecrire(name, res);
    }

    public static double[] generation(int n){
        double[] res = new double[n];
        for (int i = 0 ; i < n ; i++){
            res[i] = rand.nextDouble();
        }
        return res;
    }

    public static double[] generationGauss(int n, double centre, double variance){
        double[] res = new double[n];
        for (int i = 0 ; i < n ; i++){
            res[i] = rand.nextGaussian() * variance + centre;
        }
        return res;
    }

    public static double[][] generationGaussDim(int n, double[] centre, double variance){
        double[][] res = new double[n][centre.length];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < centre.length;j++){
                res[i][j] = rand.nextGaussian() * variance + centre[j];
            }
        }
        return res;
    }

    public static double moyenne(double[] tab){
        double res = 0;
        for (int i = 0; i < tab.length; i++){
            res += tab[i];
        }
        return res/tab.length;
    }

    public static double ecartType(double[] tab){
        double res = 0;
        double moy = moyenne(tab);
        for (int i = 0; i < tab.length; i++){
            res += Math.pow((tab[i]-moy),2);
        }
        return Math.sqrt((1/tab.length)*res);
    }

    public static void main(String[] args) {
        int taille = 1000;
        double[][] hist = histogramme(-5,5,20,generationGauss(taille, 0, 1));
        System.out.println(Arrays.deepToString(hist));
        ecrHist(hist, "hist.d");
        ecritScript(-2,2);
    }
}
