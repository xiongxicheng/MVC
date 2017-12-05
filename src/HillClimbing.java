import java.io.*;
import java.util.*;

public class HillClimbing {
    public static ArrayList<Integer> result = new ArrayList<>();
    public static int num_vertices = 0;
    public static int num_edges = 0;

    public static void main(String[] args)throws IOException{
        System.out.println("Hi");
        String baseFile = "C:\\Users\\Rohan\\Documents\\MVC";
        String dataPath = "\\Data\\";
        String fileName = "netscience.graph";
        String file = baseFile + dataPath + fileName;
        LocalSearch1(file, 300, 0);
        LocalSearch1(file, 300, 11);
        LocalSearch1(file, 300, 13);
        LocalSearch1(file, 300, 17);
        LocalSearch1(file, 300, 23);
        LocalSearch1(file, 300, 27);
        LocalSearch1(file, 300, 31);
        LocalSearch1(file, 300, 37);
        LocalSearch1(file, 300, 41);
        LocalSearch1(file, 300, 47);
    }

    public static void LocalSearch1(String fileName, int cutoffTime, int seed) {
        BufferedReader inputFile = null;
        try {
            inputFile = new BufferedReader(new FileReader(fileName));
        } catch(FileNotFoundException e) {
            System.err.println("Input file not found in current directory");
            System.exit(1);
        }
        String firstLine = "";
        try {
            firstLine = inputFile.readLine();
        } catch(IOException e) {
            System.err.println("Error reading the first line of the specified file");
            System.exit(1);
        }
        String[] split = firstLine.split(" ");
        num_vertices = Integer.parseInt(split[0]);
        System.out.println("num_vertices: "+ num_vertices);
        num_edges = Integer.parseInt(split[1]);
        System.out.println("num_edges: "+ num_edges);
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>(num_vertices + 1);
        Set<Integer> vertices = new HashSet<Integer>(num_vertices);
        for(int i=0; i<=num_vertices; i++) {
            adjList.add(i, new ArrayList<Integer>());
            if (i > 0) {
                vertices.add(i);
            }
        }

        String str = null;
        for(int src=1;src<=num_vertices;src++) {
            try {
                str = inputFile.readLine();
            } catch(IOException e) {
                System.err.println("Can't read next line");
                System.exit(1);
            }
            if(str.equals("")) {
                adjList.get(src).clear();
            } else {
                String[] neighbourList = str.split(" ");
                for(int j=0;j<neighbourList.length;j++) {
                    int dest = Integer.parseInt(neighbourList[j]);
                    adjList.get(src).add(dest);
                }
            }
        }
        try {
            inputFile.close();
        } catch (IOException e) {
            System.err.println("Error closing input file");
            System.exit(1);
        }
        System.out.println(adjList);
        System.out.println(vertices);
        long startTime = System.currentTimeMillis();
        //Solve here
        String[] fileNameSplit = fileName.split("\\.");
        String traceFile = fileNameSplit[0] + "_LS1_" + cutoffTime + "_" + seed + ".trace";
        int coverSize = LocalSearchHillClimbing(adjList, vertices, num_vertices, num_edges, startTime, cutoffTime, seed, traceFile);
        long endTime = System.currentTimeMillis();
        long runTime = (endTime - startTime);
        Collections.sort(result);
        System.out.println("coversize: "+ coverSize + " " + " vertices: " + result);
        System.out.println("time: "+ runTime);
        StringBuilder stringAns = new StringBuilder();
        for(int v=0; v<result.size(); v++) {
            stringAns.append(result.get(v));
            if (v != result.size() - 1) stringAns.append(",");
        }
        PrintWriter output;
        String outFile = fileNameSplit[0] + "_LS1_" + cutoffTime + "_" + seed + ".sol";
        System.out.println(outFile);
        output = null;
        try {
            output = new PrintWriter(outFile, "UTF-8");
        } catch (IOException e) {
            System.err.println("Error opening output solution file");
            System.exit(1);
        }
        output.println(coverSize + "\n" + stringAns);
        output.close();
    }

    public static int LocalSearchHillClimbing(ArrayList<ArrayList<Integer>> adjList, Set<Integer> vertexList,
                                              int num_vertices, int num_edges, long startTime, int cutoff_time,
                                              int seed, String traceFile) {
        System.out.println("I am entering local search hill climbing");
        System.out.println(adjList.get(2));
        long limitTime = 1000 * cutoff_time; //Convert time limit to milliseconds
        boolean betterSolution = false;
        HashSet<Integer> ansList = new HashSet<Integer>();
        HashSet<Integer> verticesCovered = new HashSet<Integer>();
        Random rand = new Random(seed);
        PrintWriter trace = null;
        try {
            trace = new PrintWriter(traceFile, "UTF-8");
        } catch (IOException e) {
            System.err.println("Error opening trace solution file");
            System.exit(1);
        }
        int minSize = num_vertices + 1;
        long currTime = System.currentTimeMillis();
        while (currTime - startTime < limitTime) {
            if (!betterSolution) {
                // Make an initial solution first
                // Basic idea: For every edge choose one of the vertices randomly and add it to the cover
                // This will create a valid solution but it may not be optimal. This is 2-approx to start.
                for (int i = 1; i < adjList.size(); i++) {
                    List<Integer> currEdges = adjList.get(i);
                    Collections.shuffle(currEdges, rand);
                    for (int otherVertex : currEdges) {
                        if (!ansList.contains(i) && !ansList.contains(otherVertex)) {
                            int currRand = rand.nextInt(2);
                            if (currRand == 0) {
                                ansList.add(i);
                            } else {
                                ansList.add(otherVertex);
                            }
                        }
                    }
                }
                betterSolution = true;
            } else {
                // Prune the current solution one vertex at a time
                List<Integer> currSol = new ArrayList<Integer>(ansList);
                Collections.shuffle(currSol, rand);
                int currInd = 0;
                boolean canPrune = true;
                while (currInd < currSol.size()) {
                    int candidateToRemove = currSol.get(currInd);
                    ansList.remove(candidateToRemove);
                    int adjInd = 1;
                    while (canPrune && adjInd < adjList.size()) {
                        for (int other : adjList.get(adjInd)) {
                            if (!ansList.contains(adjInd) && !ansList.contains(other)) {
                                canPrune = false;
                            }
                        }
                        adjInd++;
                    }
                    if (!canPrune) {
                        ansList.add(candidateToRemove);
                    }
                    canPrune = true;
                    currInd++;
                }
                betterSolution = false;
                System.out.println("We got a solution: " + ansList);
                verticesCovered.clear();
                if (ansList.size() < minSize) {
                    result = new ArrayList<Integer>(ansList);
                    minSize = ansList.size();
                    long timeNow = System.currentTimeMillis();
                    double timeDiff = (timeNow - startTime) / 1000.0;
                    trace.println(timeDiff + "," + minSize);
                }
                ansList.clear();
            }
            currTime = System.currentTimeMillis();
        }
        trace.close();
        return minSize;
    }
}
