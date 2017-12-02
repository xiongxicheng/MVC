import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SimulateAnnealing {
	private static ArrayList<Integer> result = new ArrayList<>();
	private static int num_vertices = 0;
	private static int num_edges = 0;
	private static boolean finalresult;
	public static void main(String []args) throws FileNotFoundException, IOException {
		String file = "F:\\Homework\\CSE 6140\\Project\\Data\\email.graph";
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		String[] split = line.split(" ");
		num_vertices = Integer.parseInt(split[0]);
		System.out.println("num_vertices: "+num_vertices);
		num_edges = Integer.parseInt(split[1]);
		System.out.println("num_edges: "+num_edges);	
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>(num_vertices+1);
		Set<Integer> vertices = new HashSet<Integer>(num_vertices);
		for(int i=0;i<num_vertices;i++) {
			adjList.add(i, new ArrayList<Integer>());
			if(i>0) {
				vertices.add(i);
			}
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
	    long startTime = System.currentTimeMillis();
	    int coverSize = simulate(adjList, vertices, num_vertices, num_edges, startTime);
	    long endTime = System.currentTimeMillis();
	    System.out.println("total Time: "+ (endTime-startTime)+"finalResult: "+coverSize);
	    System.out.println("VC list: "+ result);
	    //inputFile.close();
    }
	
	private static int simulate(ArrayList<ArrayList<Integer>> adjList, Set<Integer> vertexList, int num_vertices, int num_edges, long startTime)
	{
		System.out.println("entering simulate");
		long limitTime = 1000*600*1; //Time limit = 2 min now
		boolean betterSolution = false;
		HashSet<Integer> ansList = new HashSet<Integer>();
		HashSet<Integer> verticesCovered = new HashSet<Integer>();
		
		long seed = 0;
		Random rand = new Random(seed);
		long currTime = System.currentTimeMillis();
		int minSize = num_vertices ;
		int realMinSize = num_vertices;
		double deltaSize = 0.0;
		
		//Temperature start and cooling
		double temp = 100000000000.0;
		double cooling = 0.99999999;
		double absTemp = 0.00000001;
		
		
		while(temp>absTemp && currTime - startTime < limitTime) {
			if(!betterSolution) {
				//Make an initial solution
				//For every edge choose one of the vertices randomly and add it to the cover
				for(int i=0;i<adjList.size();i++) {
					List<Integer> currEdges = adjList.get(i);
					Collections.shuffle(currEdges, rand);
					for(int otherVertex : currEdges) {
						if(!ansList.contains(i)&&!ansList.contains(otherVertex)) {
							int currRand = rand.nextInt(2);
							if(currRand == 0) {
								ansList.add(i);
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
				//System.out.println("we got a solution: "+ansList);
				verticesCovered.clear();
				deltaSize = ansList.size()-minSize;
				if(deltaSize<=0 || Math.exp(-Math.abs(deltaSize) / temp)>rand.nextDouble()) {
					result = new ArrayList<Integer>(ansList);
					minSize = ansList.size();
				}
				
				//Collections.sort(result);
				//currTime = System.currentTimeMillis();
				if(minSize<realMinSize) {
					realMinSize = minSize;
					currTime = System.currentTimeMillis();	
					System.out.println("time: "+ (currTime-startTime)/1000.0+"solution: "+realMinSize);
				}
				ansList.clear();
			}
			temp *= cooling;		
			currTime = System.currentTimeMillis();			
		}
		return realMinSize;	
		
	}
	/*
	private static ArrayList<String> InitialSol(ArrayList<ArrayList<Integer>> adjList) {
		//make a copy of graph
		ArrayList<ArrayList<Integer>> copyGraph = adjList;
		
        // 
		Random randomNum = new Random();
		randomNum.setSeed(4);
		
		
        int coversize = 0;
        int edgesUncovered = num_edges;
        
        while(edgesUncovered>0) {

        	//Pick random vertex from graph to add until get a vertex cover
        	int pickVertex = randomNum.nextInt(copyGraph.size() - 1);
        	while(result.contains(Integer.toString(pickVertex+1))) {
        	 pickVertex = randomNum.nextInt(copyGraph.size() - 1);
        	}
        	
        	int pickVdegree = copyGraph.get(pickVertex).size();
        		       	
        	coversize++;
        	edgesUncovered = edgesUncovered - pickVdegree;
        	result.add(Integer.toString(pickVertex+1));        	
        	ArrayList<Integer> incidentEdges = copyGraph.get(pickVertex);        	
        	
        	for(int i=0;i<incidentEdges.size();i++) {
				int neightbour = incidentEdges.get(i);		
				copyGraph.get(neightbour-1).remove(new Integer(pickVertex+1));
			}
			incidentEdges.clear();  
			copyGraph.get(pickVertex).clear();
        }
        System.out.println("RandomPickVertexCoverSize: "+ coversize);
        System.out.println("initial VC list: "+ result);
		return result;
	}
	
	private static ArrayList<String> simulate(ArrayList<ArrayList<Integer>> adjList, ArrayList<String> b, int cutoff, int randseed){
		
		//Measure running time
		long startTime = System.currentTimeMillis();
		long endTime;
		long time = 0;
		float elapsedFinal = 0;
		
		//Temperature start and cooling
		double temp = 10000000000000000000000.0;
		double cooling = 0.99999;
		double absTemp = 0.00000001;
		
		//initial solution
		ArrayList<String> preSol = b;
				
		//initial the cost as initial solution size
		int preCost = preSol.size();
		
		//Number of iterations
		int iteration = 0;
		
		//Init random generator
		Random randomNum = new Random();
		randomNum.setSeed(randseed);
		
		//Init nextSol and deltasize 
		ArrayList<String> nextSol = new ArrayList<String>();
		double deltaSize = 0.0;
		
		//Check if new solution is VC
		boolean[] check = new boolean[1];
		
		//static cost var passed in with vertex to measure cost
		int[] cost = new int[1];
		
		//Loop using simulated annealing
		while(temp > absTemp && elapsedFinal < cutoff) {
			//Get the next neighborhood solution
			//System.out.println("getNextSol adjList: "+adjList);
			nextSol = getNextSol(adjList, preSol, randomNum, check, cost);
			cost[0] = nextSol.size();
			deltaSize = cost[0] - preCost;
				
			//If new cost is smaller then take it or use Acceptance Function: Metropolis Condition
		    if((deltaSize<0)||(preCost>0 && Math.exp(-Math.abs(deltaSize) / temp)>randomNum.nextDouble())) {
				preSol = nextSol;
				preCost = cost[0];
				System.out.println("currsize: "+preCost);
			}
		
		    //Cool the temperature
		    temp = temp*cooling;
		    iteration++;
		    //Measure time
		    endTime = System.currentTimeMillis();
		    time = endTime - startTime;
		    elapsedFinal = time / 1000F;
		    //System.out.println("time: "+elapsedFinal+"temp: "+temp);

		    //System.out.println("iterations: "+ iteration+" elapsed Time: "+elapsedFinal+" currsize: "+preSol.size()+
						//" TEMP: "+temp);
		}				
		
		System.out.println("Total time:   "  + elapsedFinal + "  VC size : " + preSol.size());		
		// Return final VC
        return preSol;
    }
	
	private static ArrayList<String> getNextSol(ArrayList<ArrayList<Integer>> adjList, ArrayList<String> preSol, Random randomNum, boolean[] check, int[] cost){
		//finalresult = true;
		// Make a copy of the curr solution
		ArrayList<String> copy = new ArrayList<String>(preSol);
		ArrayList<String> newcopy = new ArrayList<String>();
        //System.out.println("VC list now: "+copy);
        
        // Pick random vertex from preSol to remove
        int index = randomNum.nextInt(copy.size()-1);
        String removeVertex = copy.get(index);
        //System.out.println("choose vertex from copy: "+ removeVertex);
        
        //make a copy of graph
      	ArrayList<ArrayList<Integer>> copyGraph = adjList;
        
        int edgesUncovered = num_edges;
                        	
        while(edgesUncovered>0 && newcopy.size()<num_vertices -2) {
    		//Pick random vertex from graph to add until get a vertex cover
        	int pickVertex = randomNum.nextInt(copyGraph.size() - 1);
        	//System.out.println("first pick vertex: "+ pickVertex);
        	while(newcopy.contains(Integer.toString(pickVertex+1))||(pickVertex+1) == Integer.parseInt(removeVertex)) {
           	 pickVertex = randomNum.nextInt(copyGraph.size() - 1);
           	}
           	//System.out.println("last pick vertex: "+ pickVertex);
           	
           	int pickVdegree = copyGraph.get(pickVertex).size();
           		                  	
           	edgesUncovered = edgesUncovered - pickVdegree;
           	newcopy.add(Integer.toString(pickVertex+1));
           	//System.out.println("newcopy: "+newcopy);
           	ArrayList<Integer> incidentEdges = copyGraph.get(pickVertex);        	
           	
           	for(int i=0;i<incidentEdges.size();i++) {
   				int neightbour = incidentEdges.get(i);		
   				copyGraph.get(neightbour-1).remove(new Integer(pickVertex+1));
   			}
   			incidentEdges.clear();  
   			copyGraph.get(pickVertex).clear();
    	}
        
        if(edgesUncovered != 0) {
        	newcopy = preSol;
        }
    		        
        cost[0] = newcopy.size();
        System.out.println("RandomPickVertexCoverSize: "+ cost[0]);
        //System.out.println("initial VC list: "+ copy);
        
        return copy;
	}
	*/
}
