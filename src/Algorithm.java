import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Timer;

/**
 * Created by xiongxicheng on 11/10/2017.
 */
public class Algorithm {
    Timer timer;
    public void run(Graph G, String algorithm, String output_file) throws IOException{

        PrintWriter output = new PrintWriter(output_file,"UTF-8");

        if(algorithm.equals("BnB")){
            //call branch and bound
            int count = bnb(G,output);
            System.out.println(count);

        }else if (algorithm.equals("Approx")){
            //call approximation algorithm
            approx();

        }else if(algorithm.equals("LS1")){
            //call local search 1

        }else if(algorithm.equals("LS2")){
            //call local search 2

        }

    }

    public int bnb(Graph G, PrintWriter output){
        int count = 0;
        for(int i=0;i<G.V.length;i++){
            Integer u = new Integer(i);
            List<Integer> adjList = G.V[i].adjacencyList;
            if(adjList.size()>0) count++;
            while(adjList.size()>0){
                Integer n = adjList.get(0);
                G.V[n-1].adjacencyList.remove(u);
                adjList.remove(0);
            }
        }
        return count;
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
