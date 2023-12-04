input = """
2qlljdqcbeight
eight47srvbfive
slconeightfoureight557m38
xvqeightwosixnine61eightsn2tdczfhx
"""

import re

data = ["one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]
data_map = {k: i+1 for i, k in enumerate(data)}

for i in range(1,10):
    data_map[f"{i}"] = i

pattern = f"("+"|".join(data)+"|\d)"
pattern_reverse = f"("+"|".join(data)[::-1]+"|\d)"

sum = 0
for l in input.split():
    v1 = re.search(pattern, l).group(0)
    v2 = re.search(pattern_reverse, l[::-1]).group(0)[::-1]
    sum += data_map[v1]*10 + data_map[v2]
print(sum)