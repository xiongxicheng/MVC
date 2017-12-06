import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LS2 {
	private static ArrayList<Integer> result = new ArrayList<>();
	private static int num_vertices = 0;
	private static int num_edges = 0;

	
	private static ArrayList<ArrayList<Integer>> ReadGraph(String inputFile) throws IOException{
		String file = inputFile+".graph";
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		String[] split = line.split(" ");
		num_vertices = Integer.parseInt(split[0]);
		//System.out.println("num_vertices: "+num_vertices);
		num_edges = Integer.parseInt(split[1]);
		//System.out.println("num_edges: "+num_edges);
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>(num_vertices);
		for(int i=0;i<num_vertices;i++) {
			adjList.add(i, new ArrayList<Integer>());
		}
		
	    String str = null;
	    for(int src=0;src<num_vertices;src++) {
	    	str=br.readLine(); 
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
	    br.close();
	    return adjList;
	}
	
	public static void simulated(String inputFile, int cutoff, int randseed) throws IOException
	{
		//Read the graph into a nested ArrayList
		ArrayList<ArrayList<Integer>> adjList = ReadGraph(inputFile);
		
		PrintWriter output1;
		PrintWriter output2;
		output1 = new PrintWriter(inputFile+"_LS2_"+cutoff+"_"+randseed+".sol", "UTF-8");
		output2 = new PrintWriter(inputFile+"_LS2_"+cutoff+"_"+randseed+".trace", "UTF-8");
		
		boolean betterSolution = false;
		HashSet<Integer> ansList = new HashSet<Integer>();
		HashSet<Integer> verticesCovered = new HashSet<Integer>();
				
		Random rand = new Random(randseed);
		
		int minSize = num_vertices ;
		int realMinSize = num_vertices;
		double deltaSize = 0.0;
		
		//Temperature start and cooling
		double temp = 10000.0;
		double cooling = 0.9999;
		double absTemp = 0.00001;
		long startTime = System.currentTimeMillis();
		long currTime = System.currentTimeMillis();
		
		while(temp>absTemp && currTime - startTime < cutoff * 1000) {
			if(!betterSolution) {
				//Make an initial solution
				//For every edge choose one of the vertices randomly and add it to the cover
				for(int i=0;i<adjList.size();i++) {
					List<Integer> currEdges = adjList.get(i);
					Collections.shuffle(currEdges, rand);
					for(int otherVertex : currEdges) {
						if(!ansList.contains(i+1)&&!ansList.contains(otherVertex)) {
							int currRand = rand.nextInt(2);
							if(currRand == 0) {
								ansList.add(i+1);
							}else {
								ansList.add(otherVertex);
							}
						}
					}
				}
				betterSolution = true;
			}else {
				//Prune the current solution one vertex at a time
				List<Integer> currSol = new ArrayList<Integer>(ansList);
				Collections.shuffle(currSol, rand);
				int currInd = 0;
				boolean canPrune = true;
				while(currInd < currSol.size()) {
					int candidateToRemove = currSol.get(currInd);
					ansList.remove(candidateToRemove);
					int adjInd = 1;
					while(canPrune && adjInd < adjList.size()) {
						for(int other: adjList.get(adjInd)) {
							if(!ansList.contains(adjInd)&&!ansList.contains(other)) {
								canPrune = false;
							}
						}
						adjInd++;
					}
					if(!canPrune) {
						ansList.add(candidateToRemove);
					}
					canPrune = true;
					currInd++;
				}
				betterSolution = false;

				verticesCovered.clear();
				deltaSize = ansList.size()-minSize;
				if(deltaSize<=0 || Math.exp(-Math.abs(deltaSize) / temp)>rand.nextDouble()) {
					result = new ArrayList<Integer>(ansList);
					minSize = ansList.size();
				}
				
				if(minSize<realMinSize) {
					realMinSize = minSize;
					currTime = System.currentTimeMillis();	
					output1.println((currTime-startTime)/1000.0+", "+realMinSize);
				}
				ansList.clear();
			}
			temp *= cooling;		
			currTime = System.currentTimeMillis();			
		}
		StringBuilder sb = new StringBuilder();
        for(int v=0;v<result.size();v++) {
        	sb.append(result.get(v)+" ");
        }
        
		output2.print(realMinSize + "\n" + sb);
		
		output1.close();
		output2.close();
		
	}

}