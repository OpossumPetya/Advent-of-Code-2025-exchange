input = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
0dddd1
"""

import re
sum = 0
for l in input.split():
    num = re.sub(r'\D', '', l)
    sum += int(num[0]+num[-1])
print(sum)