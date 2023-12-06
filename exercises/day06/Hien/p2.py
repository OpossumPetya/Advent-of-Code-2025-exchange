input="""Time:        48     98     90     83
Distance:   390   1103   1112   1360"""
time, distance = [i.split(":")[1] for i in input.splitlines()]
time = [int(time.replace(' ','')) ]
distance = [int(distance.replace(' ','')) ]

# we need to solve the inequality x * (p-x) > q
# the solution is actually all x that is between x1 and x2 :)
import math
res = 1
ep = 1e-9
for i in range(len(time)):
    p, q = time[i], distance[i]
    delta = math.sqrt(p*p/4 - q)
    x1, x2 = p/2.0 + delta, p/2.0 - delta
    res*= (math.floor(x1-ep)-math.ceil(x2+ep)+1)

print(res)