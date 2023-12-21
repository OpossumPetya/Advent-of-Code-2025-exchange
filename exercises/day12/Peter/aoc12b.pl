use strict;

use List::Util qw(sum);


sub completeRep {
    my ( $str, $numLst, $valRepRef, $doneStr ) = @_;
    my @num = split (/,/, $numLst );
#print "- $doneStr\n";
    if (sum(@num) > length($str)) {
        return;
    }

    if( $str =~ /^[\.\?]/) {
        my $inpStr = substr($str, 1);
        $inpStr =~ /^(\.*)(.+)/;
        completeRep($2, $numLst, $valRepRef, $doneStr.'.'.$1 );
    }
    if (substr($str.'.',0,$num[0]+1) =~ /^[#\?]+[\.\?]$/) {
        if ($#num) {
            my $t = substr($str,0,$num[0]);
            $t =~ tr/\?/#/;
            my $inpStr = substr($str, $num[0]+1);
            $inpStr =~ /^(\.*)(.+)/;
            completeRep($2, substr($numLst, index($numLst,',')+1), $valRepRef, $doneStr.$t.'.'.$1 );
        } elsif (! (substr($str,$num[0]) =~ /#/)) {
            my $t = substr($str,0,$num[0]);
            $t =~ tr/\?/#/;
            $t .= substr($str,$num[0]);
            $t =~ tr/\?/./;
            $doneStr .= $t;
print "= ".$doneStr."\n";
            (grep($doneStr eq $_, @$valRepRef)) or push(@$valRepRef, $doneStr);
        }
    }
}



my $totalscore = 0;
my @map = ();

while (<>) {
	chop();
    if ( $_ =~ /([\.\?#]+) ([,\d]+)/ ) {
        push(@map, { str => $1, numLst => $2 } );
    } 
}


foreach my $row ( @map ) {
    my @valRep = ();
print ": $$row{str} $$row{numLst}\n";
    my $numstr = $$row{numLst}.",".$$row{numLst}.",".$$row{numLst}.",".$$row{numLst}.",".$$row{numLst};
    my $inpStr = $$row{str}."?".$$row{str}."?".$$row{str}."?".$$row{str}."?".$$row{str};
print "! $inpStr $numstr\n";
    $inpStr =~ /^(\.*)(.+)/;
    completeRep($2, $numstr, \@valRep, $1);
    $totalscore += $#valRep+1;
print " ".($#valRep+1)."\n";
}


print "\n== $totalscore\n";
