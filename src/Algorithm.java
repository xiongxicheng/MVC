import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by xiongxicheng on 11/10/2017.
 */
public class Algorithm {
    Timer timer;
    PrintWriter output;
    int opt;
    long startTime;
    public void run(Graph G, String algorithm, String output_file) throws IOException{

        opt = G.V.length;
        output = new PrintWriter(output_file,"UTF-8");

        if(algorithm.equals("BnB")){
            //branch and bound
            startTime = System.nanoTime();
            bnb(G,0,G.num_edges,0,G.V.length);

        }else if (algorithm.equals("Approx")){
            //call approximation algorithm
            approx();

        }else if(algorithm.equals("LS1")){
            //call local search 1

        }else if(algorithm.equals("LS2")){
            //call local search 2

        }

    }
    public void bnb(Graph G, int k, int uncovered, int count, int lb){
        if(uncovered==0){
            //output.println(count);
            if(count<opt){
                opt = count;
                long currentTime = System.nanoTime();
                System.out.println((double) (currentTime-startTime)/1000000000+","+opt);
            }
        }
        if(k>=G.V.length) return;
        if(count>opt) return;
//        int count1=count;
//        Graph g = new Graph(G.V.length,G.num_edges);
//        for(int k=0;k<g.V.length;k++){
//            g.V[k].adjacencyList = new ArrayList<>(G.V[k].adjacencyList);
//        }
//        Integer u = new Integer(i+1);
//        List<Integer> adjList = g.V[i].adjacencyList;
//        if(adjList.size()>0) count1++;
//        while(adjList.size()>0){
//            Integer n = adjList.get(0);
//            g.V[n-1].adjacencyList.remove(u);
//            adjList.remove(0);
//            g.num_edges--;
//        }
//        bnb(G,i+1,uncovered,count1,lb);//not include vertex i
//        bnb(g,i+1,g.num_edges,count,i);
        for(int i=k;i<G.V.length;i++){
            Integer u = new Integer(i+1);
            List<Integer> adjList = G.V[i].adjacencyList;
            if(adjList.size()>0) count++;
            for(Integer n:adjList){
                G.V[n-1].adjacencyList.remove(u);
                G.num_edges--;
            }
            bnb(G,i+1,G.num_edges,count,i);
            if(adjList.size()>0) count--;
            for(Integer n:adjList){
                G.V[n-1].adjacencyList.add(u);
                G.num_edges++;
            }
        }
    }

    public void approx(){

    }

    public void ls(){
        
    }

    public Algorithm(int seconds){
        timer = new Timer();
        timer.schedule(new finishTask(),seconds*1000);
    }
}
