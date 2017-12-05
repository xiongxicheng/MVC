import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by xiongxicheng on 11/10/2017.
 */
public class Algorithm {
    Timer timer;
    static PrintWriter output;
    static PrintWriter output_sol;
    int opt;
    long startTime;
    static ArrayList<Integer> result = new ArrayList<>();
    public void run(Graph G, PrintWriter output, PrintWriter output_sol) throws IOException{

        opt = G.V.length;
        this.output = output;
        this.output_sol = output_sol;
        //branch and bound
        startTime = System.nanoTime();
        ArrayList<Integer> res = new ArrayList<>();
        bnb(res, G,0, G.num_edges);
        //System.out.println(result.size());
        output_sol.print(result.get(0));
            for(int i=1;i<result.size();i++) {
                output_sol.print("," + result.get(i));
            }
    }
    public void bnb(ArrayList<Integer> res, Graph G, int index, int uncovered){

        //calculate currently coverred
        //System.out.println(index);
        if(opt<=res.size()) return;
        //System.out.println(uncovered);
        if(uncovered==0&&opt>res.size()){
            opt = res.size();
            long currentTime = System.nanoTime();
            //System.out.println((double) (currentTime-startTime)/1000000000+","+opt);
            output.println((double) (currentTime-startTime)/1000000000+","+opt);
            result = new ArrayList<>(res);
            return;
        }
        //approximate can be coverred by remaining vertices
        int max = 0;
        for(int i=index;i<G.V.length;i++){
            for(Integer v:G.V[i].adjacencyList){
                if(v>index){
                    max++;
                }else if(!res.contains(v)){
                    max +=2;
                }
            }
        }
        max /=2;
        //System.out.println(max);
        if(max<uncovered) return;
        if(res.size()<opt-1){
            for(int i=index;i<G.V.length;i++){
                Integer u = new Integer(i+1);
                res.add(u);
                List<Integer> temp = new LinkedList<>(G.V[u-1].adjacencyList);
                G.V[u-1].adjacencyList = new LinkedList<>();
                for(Integer v:temp){
                    G.V[v-1].adjacencyList.remove(u);
                    G.num_edges--;
                }
                bnb(res, G,i+1,G.num_edges);
                res.remove(res.size()-1);
                for(Integer v:temp){
                    G.V[u-1].adjacencyList = temp;
                    G.V[v-1].adjacencyList.add(u);
                    G.num_edges++;
                }
            }
        }
    }

    public int approx(ArrayList<ArrayList<Integer>> adjList, int num_edges, int num_vertices){

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
            //result.add(Integer.toString(vertexNum));
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



    public void ls(){
        
    }

    public Algorithm(int seconds){
        timer = new Timer();
        timer.schedule(new finishTask(),seconds*1000);
    }
}
