import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HillClimbing {
    public static ArrayList<Integer> result = new ArrayList<>();
    public static int num_vertices = 0;
    public static int num_edges = 0;
    public static void main(String[] args)throws IOException{
        System.out.println("Hi");
        String baseFile = "C:\\Users\\Rohan\\Documents\\MVC";
        String dataPath = "\\Data\\";
        String fileName = "karate.graph";
        String file = baseFile + dataPath + fileName;
        BufferedReader inputFile = new BufferedReader(new FileReader(file));
        String firstLine = inputFile.readLine();
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
            str=inputFile.readLine();
            if(str.equals("")) {
                adjList.get(src).clear();
            }else {
                String[] neighbourList = str.split(" ");
                for(int j=0;j<neighbourList.length;j++) {
                    int dest = Integer.parseInt(neighbourList[j]);
                    adjList.get(src).add(dest);
                }
            }
        }
        inputFile.close();
        System.out.println(adjList);
        System.out.println(vertices);
        long startTime = System.currentTimeMillis();
        //Solve here
        int coverSize = LocalSearchHillClimbing(adjList, vertices, num_vertices, num_edges, startTime);
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
        String[] fileNameSplit = fileName.split("\\.");
        String outFile = baseFile + "\\" + fileNameSplit[0] + "_" + ((endTime - startTime) / 1000) + "_" + 0 + ".trace";
        System.out.println(outFile);
        output = new PrintWriter(outFile, "UTF-8");
        output.println(coverSize + "\n" + stringAns);
        output.close();
    }

    public static int LocalSearchHillClimbing(ArrayList<ArrayList<Integer>> adjList, Set<Integer> vertexList, int num_vertices, int num_edges, long startTime) {
        for (int i = 0; i < 10; i ++)
        System.out.println("I am entering local search hill climbing");
        System.out.println(adjList.get(2));
        long limitTime = 1000 * 10 * 1; //Time limit = 2 min now
        boolean betterSolution = false;
        HashSet<Integer> ansList = new HashSet<Integer>();
        HashSet<Integer> verticesCovered = new HashSet<Integer>();
        long seed = 0;
        Random rand = new Random(seed);
        long currTime = System.currentTimeMillis();
        int minSize = num_vertices + 1;
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
                }
                ansList.clear();
            }
            currTime = System.currentTimeMillis();
        }
        return minSize;
    }
}
