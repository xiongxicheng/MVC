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
       if (args.length < 4) {
           System.err.println("Unexpected number of command line arguments");
           System.exit(1);
       }
       CliArgs cliArgs = new CliArgs(args);
       String input_file = cliArgs.switchValue("-inst");
       String algorithm = cliArgs.switchValue("-alg");
       int randseed = cliArgs.switchLongValue("-seed").intValue();
       int cutoff = cliArgs.switchLongValue("-time").intValue();
        // String input_file = "karate.graph";
        // String cut = "6";
        // int randseed = 1;
        // String algorithm = "LS1";
        if(algorithm.equals("BnB")){
        	String output_file_sol = input_file.split("\\.")[0] + "_" + algorithm + "_" + cutoff + "_" + seed +".sol";
       		String output_file_trace = input_file.split("\\.")[0] + "_" + algorithm + "_" + cutoff + "_" + seed + ".trace";
            PrintWriter output_trace = new PrintWriter(output_file_trace,"UTF-8");
            PrintWriter output_sol = new PrintWriter(output_file_sol,"UTF-8");
            Graph G = parse(input_file);
            Algorithm algo= new Algorithm(cutoff);
            algo.run(G,output_trace,output_sol);
            output_trace.close();
            output_sol.close();
            System.exit(0);
        }else if(algorithm.equals("Approx")){
            Approx.GreedyAlgo(input_file.split("\\.")[0]);
        }else if(algorithm.equals("LS1")){
            HillClimbing.LocalSearch1(input_file,cutoff,randseed);
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
