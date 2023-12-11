
my $totalscore = 0;

my %dirMap = (  F => { u => r, l => d},
                7 => { u => l, r => d},
                J => { d => l, r => u},
                L => { d => r, l => u},
                '-' => { l => l, r => r},
                '|' => { u => u, d => d} );

my %dirVal = (  u => { dx => 0, dy => -1 },
                d => { dx => 0, dy => 1 },
                l => { dx => -1, dy => 0 },
                r => { dx => 1, dy => 0 } );

my @map = ();

my $x, $y;

while (<>) {
	chop();
    if ( $_ =~ /(\S+)/ ) {
        push(@map, [ split(//, $_) ] );
        if (index($_, 'S')>=0) {
            $x = index($_, 'S');
            $y = $#map;
        }
    } 
}

print "$x,$y $map[$y][$x]\n";

my @pos;

foreach $d ( split(//,"udlr") ) {
    if (my $c = $map[$y+$dirVal{$d}{dy}][$x+$dirVal{$d}{dx}]) {
        if (my $n = $dirMap{$c}{$d}) {
            push(@pos, { y => $y+$dirVal{$d}{dy}, x => $x+$dirVal{$d}{dx}, d => $n } );
print "$c -> $n\n";            
        }
        print "$c\n";
    }
}

($#pos == 1) or exit;

while ( !($pos[0]{x} == $pos[1]{x} and $pos[0]{y} == $pos[1]{y}) ) {
    $totalscore++;
    print "$totalscore: ";
    foreach my $p ( 0 .. 1) {
        my $c = $map[$pos[$p]{y}][$pos[$p]{x}];
        my $cn = $map[$pos[$p]{y} + $dirVal{$pos[$p]{d}}{dy}][$pos[$p]{x} + $dirVal{$pos[$p]{d}}{dx}];
        my $dn = $dirMap{$cn}{$pos[$p]{d}};
        print "$c -> $cn ($pos[$p]{d} -> $dn) ";
        $pos[$p] = {x => $pos[$p]{x} + $dirVal{$pos[$p]{d}}{dx}, y => $pos[$p]{y} + $dirVal{$pos[$p]{d}}{dy}, d => $dn }; 
    }
    print "\n";
}
$totalscore++;


print "\n== $totalscore\n";
