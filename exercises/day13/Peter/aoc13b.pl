use strict;
use List::Util qw(min);


my $totalscore = 0;

my @map = ();
my $lenX = 0;

while (<>) {
	chop();
    if ( $_ =~ /[\.#]+/ ) {
        push(@map, [ split(//, $_) ] );
        $lenX or ($lenX = length($_));
    } else {
        my $lenY = $#map+1;
        for my $xi (1 .. $lenX-1) {
#            print "$xi ".($xi + 1)."  ($xi, ".($lenX-$xi).") \n";
            my $okay = 2;
            YITER: for my $y (0 .. $lenY-1) {
                for my $xj (0 .. min($xi, $lenX-$xi)-1) {
#                    print " $map[$y][$xi-$xj-1] $map[$y][$xi+$xj] \n";
                    ($map[$y][$xi-$xj-1] eq $map[$y][$xi+$xj]) or ($okay-- > 0) or last YITER;
                }
            }
            ($okay == 1) and print "$xi ($okay)!\n";
            ($okay == 1) and $totalscore += $xi;
        }
        for my $yi (1 .. $lenY-1) {
#            print "$yi ".($yi + 1)."  ($yi, ".($lenY-$yi).") \n";
            my $okay = 2;
            XITER: for my $x (0 .. $lenX-1) {
                for my $yj (0 .. min($yi, $lenY-$yi)-1) {
#                    print " $map[$yi-$yj-1][$x] $map[$yi+$yj][$x] \n";
                    ($map[$yi-$yj-1][$x] eq $map[$yi+$yj][$x]) or ($okay-- > 0) or last XITER;
                }
            }
            ($okay == 1) and print "$yi ($okay)!\n";
            ($okay == 1) and $totalscore += $yi*100;
        }
        @map = ();
        $lenX = 0;
    }
}




print "\n== $totalscore\n";
