Description of the instances files for the p-median problem:
<n -- number of nodes> <p -- number of medians>
<node 1 coord x> <node 1 coord y>
...
<node n coord x> <node n coord y>

Obs:
- the nodes are randomly distributed in a grid of size [1000,1000].
- the distances between nodes should be calculated as their rounded Euclidian distances, i.e:
	distance(i,j) = floor(sqrt((xi-xj)^2+(yi-yj)^2)+0.5)
- any node can be a facility.

