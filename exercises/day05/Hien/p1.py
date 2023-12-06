input_example = """seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4"""
input=input_example
all_maps = input.split("\n\n")
seeds = [int(i) for i in all_maps[0].split(":")[1].split()]

for m in all_maps[1:]:
    new_seeds = []
    for s in seeds:
        mapped = False
        for l in m.splitlines()[1:]:
            mm = [int(i) for i in l.split()]
            if mm[1] <= s <mm[1]+mm[2]:
                new_seeds.append(s+mm[0]-mm[1])
                mapped = True
                break
        if not mapped: new_seeds.append(s)
    seeds = new_seeds
    
print(min(seeds))