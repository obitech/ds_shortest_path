## Shortest path in a directed graph with edge weight and acceleration
This is a custom algorithm implemented in Java. In this form it only works on a specific graph (see below), but could be easily customized to work on other graphs as well.

### Data Model
The Graph is read in by arces.csv and nodes.csv, stored in the util package.

* Each vertex has an ID and a speed modifier
* Start has ID = 0, End has ID = 21
* Vertices are connected with directed edges
* Edges have distance
* Vertex with ID = 0 sets speed to 1
* Passing a vertex will modify current speed according to his modification
* Time to pass an edge = distance / current speed at vertex

### Algorithm
The algorithm calculates the shortest path from 0 to 21. It uses a breadth-first search approach to dynamically adjust time & speed for all connected vertices for the currently visited node, very similar to Bellman-Ford's SP algorithm.  It optimizes for time decrease or speed improvement from a vertex to its neighbours. Additionally there is a limit of how many times a vertex can be passed each iteration. 

At the beginning each vertex only gets passed once, the final time for the end vertex will then be compared against the previous final time (Infinity for the first iteration). If time improves, we restart the algorithm but now allow each vertex to be visited one more time. This gets repeated until there is no more measurable improvement in time.


 The optimal path for each vertex is stored in the HashMap 'pathMap' and are updated dynamically. The paths for each iteration are saved in the ArrayList 'results' and can be accessed via showResult()
 
#### Starting conditions
 * initialize endTime to Inf
 * all vertices initialized to: time = Inf, speed = 0
 * v0: time = 0, speed = 1
 *  nMax (max times each vertex can get visited in this iteration) = 1

#### Process 
1. vertex with id = 0 gets queued
2. Remove top vertex from queue
3. Check all connected vertices if either time or speed improves (speed can't be < 0) AND if number of times visited for destination would still be <= nMax:
    * adjust time & speed for destination
    * update path for destination vertex
    * add vertex to queue (except if it's 21 and/or already in queue)
 4. If queue has items left, go to 2)
 5. If final time for v21 < endTime:
    * nMax++ (vertices can be visited an additional time)
    * Set endTime to v21.time
    * Reset all vertices to starting conditions
    * Go back to 1)
    * **If false**: Time improvements from nMax - 1 to nMax are too small to keep track of -> Algorithm is finished
 6. The optimal path can be found via results.get(nMax - 1)

 ### Result
 The shortest path from 0 to 21 is: 
1) 0 -> 7 with speed 1.0 in 30.0 time. Arrival time: 30.0
2) 7 -> 13 with speed 4.0 in 12.5 time. Arrival time: 42.5
3) 13 -> 7 with speed 4000.0 in 0.0125 time. Arrival time: 42.5125
4) 7 -> 13 with speed 4003.0 in 0.012490632025980514 time. Arrival time: 42.52499063202598
5) 13 -> 7 with speed 4003000.0 in 1.2490632025980514E-5 time. Arrival time: 42.525003122658006
6) 7 -> 13 with speed 4003003.0 in 1.2490622665034226E-5 time. Arrival time: 42.52501561328067
7) 13 -> 7 with speed 4.003003E9 in 1.2490622665034225E-8 time. Arrival time: 42.525015625771296
8) 7 -> 13 with speed 4.003003003E9 in 1.2490622655673287E-8 time. Arrival time: 42.52501563826192
9) 13 -> 7 with speed 4.003003003E12 in 1.2490622655673286E-11 time. Arrival time: 42.52501563827441
10) 7 -> 13 with speed 4.003003003003E12 in 1.2490622655663925E-11 time. Arrival time: 42.5250156382869
11) 13 -> 7 with speed 4.003003003003E15 in 1.2490622655663926E-14 time. Arrival time: 42.525015638286916
12) 7 -> 23 with speed 4.003003003003003E15 in 1.2490622655663916E-14 time. Arrival time: 42.52501563828693
13) 23 -> 12 with speed 4.003003003002998E15 in 1.249062265566393E-15 time. Arrival time: 42.52501563828693
14) 12 -> 21 with speed 4.003003003003001E15 in 2.4981245311327843E-16 time. Arrival time: 42.52501563828693

Total time travelled from 0 to 21 is 42.52501563828693