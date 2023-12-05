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
input = input_example
all_maps = input.split("\n\n")
seeds = [int(i) for i in all_maps[0].split(":")[1].split()]
seed_ranges = [(seeds[i], seeds[i] + seeds[i+1] - 1) for i in range(0, len(seeds), 2)]
maps = []

for m in all_maps[1:]:
    mm = []
    for l in m.splitlines()[1:]:
        mm.append((int(i) for i in l.split()))
    maps.append(mm)

def map_overlaps(seed_ranges, src_start, src_end, dest_start):
    res = []
    unmapped_seeds = []
    diff = dest_start - src_start
    for start, end in seed_ranges:
        if (start > src_end) or (src_start > end): 
            unmapped_seeds.append((start, end))
            continue
        else:
            if src_start < start and end < src_end:
                res.append((start + diff, end + diff))
                continue
            overlap_start, overlap_end = max(start, src_start), min(end, src_end)
            res.append((overlap_start + diff, overlap_end + diff))
            if overlap_start > start:
                unmapped_seeds.append((start, overlap_start - 1))
            if overlap_end < end:
                unmapped_seeds.append((overlap_end + 1, end))
    return unmapped_seeds, res

def compute_ranges(seeds, map):
    res = []
    unmapped_seeds = seeds
    for dest, src, r in map:
        unmapped_seeds, res_ = map_overlaps(unmapped_seeds, src, src + r - 1, dest)
        res.extend(res_)
    res.extend(unmapped_seeds)
    return res

for map in maps:
    seed_ranges = compute_ranges(seed_ranges, map)

print(min(seed_ranges)[0])