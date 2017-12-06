# MVC
Minimum Vertex Cover

Our code has already been compiled and tested in Ubuntu (64-bit).

# How to Run?
Change to the source file directory, in the command line, input the following:

javac RunExperiments,java (if you still need to compile)

java RunExperiments -inst <filename> -alg <algorithm> -time <cutoff time in seconds> -seed <random seed>
  
e.g., java RunExperiments -inst karate.graph -alg LS1 -time 600 -seed 11

The input graph file should be in the same directory as the source code, and the output file wil be generated in the same directory as well.

# Structure
<RunExperiments> - for running the program.
  
<Algorithm> - for branch and bound algorithm
  
<Approx> - for approximation algorithm
  
<finishTask> - for terminating branch and bound
  
<Graph> - graph data structure used in branch and bound
  
<HillClimbing> - local search algorithm with randomized restart hill climbing
  
<LS2> - local search algorithm with simulated annealing

<Vertex> - vertex data structure used in branch and bound

# Sources:
Other than the sources mentioned in the paper, command line parsing class is from https://github.com/jjenkov/cli-args

