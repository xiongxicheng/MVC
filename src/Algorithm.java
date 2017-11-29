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
    ArrayList<Integer> res;
    public void run(Graph G, String algorithm, String output_file) throws IOException{

        opt = G.V.length;
        output = new PrintWriter(output_file,"UTF-8");

        if(algorithm.equals("BnB")){
            //branch and bound
            startTime = System.nanoTime();
            res = new ArrayList<>();
            bnb(res, G,0);

        }else if (algorithm.equals("Approx")){
            //call approximation algorithm


        }else if(algorithm.equals("LS1")){
            //call local search 1

        }else if(algorithm.equals("LS2")){
            //call local search 2

        }

    }
    public void bnb(ArrayList<Integer> res, Graph G, int index){
//        if(count<opt&uncovered==0){
//            //output.println(count);
//            opt = count;
//            long currentTime = System.nanoTime();
//            System.out.println((double) (currentTime-startTime)/1000000000+","+opt);
//        }
//        if(k>=G.V.length) return;
//        if(count>opt)

        //calculate currently coverred
        //System.out.println(index);
        if(opt<res.size()) return;
        int coverred = 0;
        for(Integer u:res){
            for(Integer v:G.V[u-1].adjacencyList){
                if(res.contains(v)){
                    coverred++;
                }else{
                    coverred +=2;
                }
            }
        }
        coverred /=2;
        //System.out.println(coverred);
        if(coverred==G.num_edges){
            if(opt>res.size()){
                opt = res.size();
                long currentTime = System.nanoTime();
                System.out.println((double) (currentTime-startTime)/1000000000+","+opt);
            }
        }else{
            //calculate max can be coverred
            int max = 0;
            for(int i=index;i<G.V.length;i++){
                for(Integer v:G.V[i].adjacencyList){
                    if(v>index){
                        max++;
                    }else if(res.contains(v)){
                        continue;
                    }else {
                        max +=2;
                    }
                }
            }
            max /=2;
            //System.out.println(max);
            if(max<G.num_edges-coverred) return;
        }
        for(int i=index;i<G.V.length;i++){
            Integer u = new Integer(i+1);
            res.add(u);
            bnb(res, G,i+1);
            res.remove(res.size()-1);
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
