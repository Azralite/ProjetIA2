set terminal pngcairo size 920, 920 
set output "steps2.png" 
set title 'Histogramme et Courbe'
set grid
set style data boxes
plot 'hist.d' title 'Histogramme', (0.5021803641494771/(sqrt(2*3.14*2.497118321045882)))*exp((-((x-2.9530952710070295)**2))/(2*2.497118321045882 ))+ (0.4978196358505229/(sqrt(2*3.14*0.041488479055229385)))*exp((-((x--2.006286962745238)**2))/(2*0.041488479055229385)) title 'f(x)' 
