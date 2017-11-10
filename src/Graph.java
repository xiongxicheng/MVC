/**
 * Created by xiongxicheng on 11/10/2017.
 */

import java.util.LinkedList;
import java.util.List;

public class Graph {
    List<Vertex> V;
    public Graph(int num){
        V = new LinkedList<>();
        for(int i=0;i<num;i++){
            V.add(new Vertex());
        }
    }
}