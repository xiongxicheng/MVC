/**
 * Created by xiongxicheng on 11/10/2017.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
//        String output_file_sol = input_file + "_" + algorithm + "_" + cutoff + "_" + seed +".sol";
//        String output_file_trace = input_file + "_" + algorithm + "_" + cutoff + "_" + seed + ".trace";
//        Graph G = parse(input_file);
        String input_file = "karate.graph";
        String cut = "600";
        int cutoff = Integer.valueOf(cut);
        //int randseed = Integer.valueOf(seed);
        int randseed = 1;
        String algorithm = "LS2";
        if(algorithm.equals("BnB")){
            PrintWriter output_trace = new PrintWriter("netscience_BnB_600.trace","UTF-8");
            PrintWriter output_sol = new PrintWriter("netscience_BnB_600.sol","UTF-8");
            Graph G = parse(input_file);
            Algorithm algo= new Algorithm(600);
            algo.run(G,output_trace,output_sol);
            output_trace.close();
            output_sol.close();
            System.exit(0);
        }else if(algorithm.equals("Approx")){
            Approx.GreedyAlgo(input_file.split("\\.")[0]);
        }else if(algorithm.equals("LS1")){

        }else if(algorithm.equals("LS2")){
            LS2.simulated(input_file.split("\\.")[0],cutoff,randseed);
        }else {
            System.out.println("invalid algorithm name");
            System.exit(1);
        }
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
            if(!line.equals("")) {
                split = line.split(" ");
                for (int i = 0; i < split.length; i++) {
                    G.V[j].adjacencyList.add(Integer.parseInt(split[i]));
                }
            }
            j++;
        }
        br.close();
        return G;
    }
}
