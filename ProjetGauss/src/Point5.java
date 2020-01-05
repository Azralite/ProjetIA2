import java.util.Random;

public class Point5 {

    public static Random rand = new Random();


    // Fonction qui escrit un script gnuplot qui affiche un histogramme et une fontion
    public static void ecritureScript(String fonction){
        String res = "set terminal pngcairo size 920, 920 \n" ;
        res+= "set output \"steps2.png\" \n";
        res+="set title 'Histogramme et Courbe'\n";
        res+="set grid\n";
        res+="set style data boxes\n";
        res+="plot 'hist.d' title 'Histogramme', "+ fonction + " title 'f(x)' \n";

        TasGaussien.ecrire("Results/Point5/script.gnu",res);
    }


    public static void main(String[] args) {

        // 2 Gaussiennes
        int K = 2;
        // 1 Dimension
        int D = 1;


        // On genere 500 points centré en -2 avec une variance de 0.2
        double[] data1 = TasGaussien.generationGauss(500, -2, 0.2);

        // On genere 500 points centré en 3 avec une variance de 1.5
        double[] data2 = TasGaussien.generationGauss(500, 3,1.5);

        double[][] data = new double[1000][D];

        for (int i = 0; i < 500;i++){
            data[i][0] = data1[i];
        }
        for (int i = 500; i < 1000; i++){
            data[i][0] = data2[i-500];
        }



        //On creer nos centres, nos ecarts et nos ro
        double[][] centre = MixGauss.createCentre(K, D, 0, 1);
        double[][] ecarts = new double[K][D];
        double[] ro = new double[K];

        // On assigne aleatoirement nos ecarts à 1 et ro à 1/K
        for (int i = 0; i < ecarts.length; i++){
            ro[i] = 1.0/K;
            for (int j= 0 ; j < ecarts[i].length; j++){
                ecarts[i][j] = 1;
            }
        }


        // On lance notre boucle jusqu'a ce que les centre ne se déplacent plus

        double eps=0.001;
        double maj = 10;
        double [][] a = MixGauss.Assigner(data, centre, ro, ecarts);
        while(maj>eps){
            maj = MixGauss.Deplct(data, centre, ro, ecarts, a);
            a = MixGauss.Assigner(data, centre, ro, ecarts);
        }


        double score[] = new double[data.length];
        for (int i = 0; i < data.length; i++){
            score[i] = MixGauss.score(centre, ecarts, ro, data[i]);
        }

        // On affiche le score moyen
        System.out.println("Le score moyen est de : " + MixGauss.scoreMoy(score));


        // Partie pour afficher la gaussienne et l'histogramme
        double[] tmp = new double[1000];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = data[i][0];
        }
        double[][] hist = TasGaussien.histogramme(-4,6,500, tmp);
        double max = 0;
        for (int i = 0; i < hist[1].length;i++){
            if (hist[1][i] > max)
                max = hist[1][i];

        }
        //Normalisation en divisant par la plus grande valeure
        for (int i = 0; i < hist[1].length;i++){
            hist[1][i] =  hist[1][i] / max;
        }

        // On enregistre l'histogramme dans un fichier .d
        TasGaussien.ecrHist(hist, "Results/Point5/hist.d");


        //On determine la fonction gaussienne en fonction des valiable
        String fct = "("+ ro[0] + "/(sqrt(2*3.14*"+ ecarts[0][0] + ")))*exp((-((x-"+  centre[0][0] + ")**2))/(2*"+ ecarts[0][0]+" ))+ ("+ ro[1] + "/(sqrt(2*3.14*"+ ecarts[1][0] + ")))*exp((-((x-"+  centre[1][0] + ")**2))/(2*"+ecarts[1][0]+"))";

        // On crée notre script pour tout afficher
        ecritureScript(fct);

    }
}