/**
 * Created by xiongxicheng on 11/10/2017.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    Vertex[] V;
    int num_edges;
    public Graph(int v,int e){
        num_edges = e;
        V = new Vertex[v];
        for(int i=0;i<v;i++){
            V[i] = new Vertex();
        }
    }
}