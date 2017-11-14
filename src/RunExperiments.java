/**
 * Created by xiongxicheng on 11/10/2017.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;



public class RunExperiments {
    public static void main(String[] args)throws IOException{
//        if (args.length < 4) {
//            System.err.println("Unexpected number of command line arguments");
//            System.exit(1);
//        }
//        String input_file = args[0];
//        String algorithm = args[1];
//        String cutoff = args[2]; // in seconds
//        String seed = args[3];
//        String output_file = input_file + "_" + algorithm + "_" + cutoff + "_" + seed +".sol";
//
//        Graph G = parse(input_file);
        Graph G = parse("jazz.graph");
        String cutoff = "600";

        //Algorithm.run(G,algorithm,cutoff,output_file);
        Algorithm algorithm = new Algorithm(600);
        algorithm.run(G,"BnB","testout.txt");
        System.exit(0);
    }

    public static Graph parse(String file) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        String[] split = line.split(" ");
        int num_vertices = Integer.parseInt(split[0]);
        int num_edges = Integer.parseInt(split[1]);
        Graph G = new Graph(num_vertices,num_edges);
        //to be implemented
        int j=0;
        while ((line = br.readLine()) != null) {
            split = line.split(" ");
            for (int i = 0; i < split.length; i++) {
                G.V[j].adjacencyList.add(Integer.parseInt(split[i]));
            }
            j++;
        }
        return G;
    }
}
