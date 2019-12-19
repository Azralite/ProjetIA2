# set terminal pngcairo  background "#ffffff" enhanced font "arial,8" fontscale 1.0 size 540, 360 
set term x11 
# set output 'rgb_variable.4.png'
unset border
set angles degrees
set label 99 "" at 0.00000, 0.00000, 0.00000 left norotate back nopoint
set style increment default
set style data lines
set xzeroaxis linecolor rgb "red"  linewidth 2.000 dashtype solid
set yzeroaxis linecolor rgb "green"  linewidth 2.000 dashtype solid
set zzeroaxis linecolor rgb "blue"  linewidth 2.000 dashtype solid
set xyplane at 0
set xtics axis in scale 1,0.5 nomirror norotate  autojustify
set ytics axis in scale 1,0.5 nomirror norotate  autojustify
set ztics axis in scale 1,0.5 nomirror norotate  autojustify
set title "Nouvelle representation des pixels de l'image" 
set xlabel "Red" 
set xlabel  font "" textcolor rgb "red"  norotate
set xrange [ 0.00000 : 1.000 ] noreverse writeback
set x2range [ * : * ] noreverse writeback
set ylabel "Green" 
set ylabel  font "" textcolor rgb "green"  rotate
set yrange [ 0.00000 : 1.000 ] noreverse writeback
set y2range [ * : * ] noreverse writeback
set zlabel "Blue" 
set zlabel  font "" textcolor rgb "blue"  norotate
set zrange [ 0.00000 : 1.000 ] noreverse writeback
set cbrange [ * : * ] noreverse writeback
set rrange [ * : * ] noreverse writeback
set lmargin  5
set bmargin  2
set rmargin  5
rgb(r,g,b) = int(r*255)*65536 + int(g*255)*256 + int(b*255)
## Last datafile plotted: "rgb_variable.dat"
splot 'b.d' using 1:2:3:(0.5):(rgb($1,$2,$3)) with points pt 7 ps variable lc rgb variable     title"Ensemble des pixels de l'images"
