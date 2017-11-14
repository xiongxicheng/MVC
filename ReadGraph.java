package readgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ReadGraph {
	public static ArrayList<String> result = new ArrayList<>();
	public static int num_vertices = 0;
	public static int num_edges = 0;
	public static void main(String[] args)throws IOException{
		String file = "F:\\Homework\\CSE 6140\\Project\\Data\\football.graph";
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		String[] split = line.split(" ");
		num_vertices = Integer.parseInt(split[0]);
		System.out.println("num_vertices: "+num_vertices);
		num_edges = Integer.parseInt(split[1]);
		System.out.println("num_edges: "+num_edges);	
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
        long start = System.currentTimeMillis();
        int f = GreedyAlgo(adjList);
        long end = System.currentTimeMillis();
        long time = (end - start);
        System.out.println("coversize:"+f+" "+ " vertex: "+result);
        System.out.println("time: "+time);
        StringBuilder sb = new StringBuilder();
        for(int v=0;v<result.size();v++) {
        	sb.append(result.get(v)+" ");
        }
        //PrintWriter output;
        //output = new PrintWriter("F:\\Homework\\CSE 6140\\Project\\Data\\netscience_Approx.sol", "UTF-8");
        //output.println(f+"\n"+sb);
        br.close();
        //output.close();
	}
	private static int GreedyAlgo(ArrayList<ArrayList<Integer>> adjList) {
		// TODO Auto-generated method stub
        int coversize = 0;
        int edgesUncovered = num_edges;
        while(edgesUncovered>0) {
        	int maxDegree = 0;        	
        	int vertexNum = 0;
        	for(int i=0;i<num_vertices;i++) {
        		int de = adjList.get(i).size();
        		if(de>maxDegree) {
        			vertexNum = i+1;
        			maxDegree = de;
        		}
        	}
        	coversize++;
        	edgesUncovered = edgesUncovered - maxDegree;
        	result.add(Integer.toString(vertexNum));
        	ArrayList<Integer> incidentEdges = adjList.get(vertexNum-1);
        	for(int i=0;i<incidentEdges.size();i++) {
				int neightbour = incidentEdges.get(i);		
				adjList.get(neightbour-1).remove(new Integer(vertexNum));
			}
			incidentEdges.clear();  
			adjList.get(vertexNum-1).clear();
        }
		return coversize;
	}

}
