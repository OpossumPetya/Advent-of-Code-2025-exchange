from typing import List
import re

def input_as_string(filename:str) -> str:
    """returns the content of the input file as a string"""
    with open(filename) as f:
        return f.read().rstrip("\n")

def input_as_lines(filename:str) -> List[str]:
    """Return a list where each line in the input file is an element of the list"""
    return input_as_string(filename).split("\n")


def line2node(line):
    match = re.search("([A-Z0-9]+) = \\(([A-Z0-9]+), ([A-Z0-9]+)\\)",line)
    (pp,pl,pr) = match.groups()
    return (pp,pl,pr)


def solve2(filename):
    lines = input_as_lines(filename)
    instructions = lines[0]
    nodelist = list(map(line2node,lines[2:]))
    nodemap = {}
    for node in nodelist:
        nodemap[node[0]] = node

    ghosts = list( map( lambda node: (0,node[0]),filter(lambda node:node[0][-1]=='A',nodelist)) )
    print(ghosts)

    def findnextz2(ghost):
        d=0
        while True:
            ipos = ghost[0] % len(instructions)
            nextpos = nodemap[ghost[1]][1] if instructions[ipos] == 'L' else nodemap[ghost[1]][2]
            d = d+1
            if nextpos[-1]== 'Z':
                return (d,nextpos)
            ghost = (ghost[0]+1,nextpos)
    
    cache = {}
    def findnextz(ghost):
        ipos = ghost[0] % len(instructions)
        jpos = (ipos,ghost[1])
        if jpos in cache:
            jump = cache[jpos]
            return (ghost[0]+jump[0],jump[1])
        jump = findnextz2(ghost)
        cache[jpos] = jump
        return (ghost[0]+jump[0],jump[1])

    minstep=0
    maxstep=1
    while minstep<maxstep:
        minstep=maxstep
        for i in range(len(ghosts)):
            while ghosts[i][0]<maxstep:
                ghosts[i]=findnextz(ghosts[i])
            maxstep = ghosts[i][0]

    print(maxstep)
    print("cache: " + str(cache))
    '''
    (0, 'GQA'): (22411, 'TKZ'), 
    (0, 'TKZ'): (22411, 'TKZ'), 
    (0, 'AAA'): (18727, 'ZZZ'), 
    (0, 'ZZZ'): (18727, 'ZZZ'), 
    (0, 'XCA'): (24253, 'LLZ'), 
    (0, 'LLZ'): (24253, 'LLZ'), 
    (0, 'HBA'): (14429, 'JLZ'), 
    (0, 'JLZ'): (14429, 'JLZ'), 
    (0, 'GVA'): (16271, 'KJZ'), 
    (0, 'KJZ'): (16271, 'KJZ'), 
    (0, 'NVA'): (20569, 'HVZ'), 
    (0, 'HVZ'): (20569, 'HVZ'), 
    '''


from datetime import datetime 
start_time = datetime.now() 
print("---")
solve2("sample.txt")
print("---")
solve2("sample2.txt")
print("---")
solve2("input.txt")
print("---")
time_elapsed = datetime.now() - start_time 
print('Time elapsed (hh:mm:ss.ms) {}'.format(time_elapsed))

'''
---
[(0, 'GQA'), (0, 'AAA'), (0, 'XCA'), (0, 'HBA'), (0, 'GVA'), (0, 'NVA')]
18024643846273
cache: {(0, 'GQA'): (22411, 'TKZ'), (0, 'AAA'): (18727, 'ZZZ'), (0, 'ZZZ'): (18727, 'ZZZ'), (0, 'XCA'): (24253, 'LLZ'), (0, 'LLZ'): (24253, 'LLZ'), (0, 'HBA'): (14429, 'JLZ'), (0, 'JLZ'): (14429, 'JLZ'), (0, 'GVA'): (16271, 'KJZ'), (0, 'KJZ'): (16271, 'KJZ'), (0, 'NVA'): (20569, 'HVZ'), (0, 'HVZ'): (20569, 'HVZ'), (0, 'TKZ'): (22411, 'TKZ')}
---
Time elapsed (hh:mm:ss.ms) 0:30:42.186078
'''