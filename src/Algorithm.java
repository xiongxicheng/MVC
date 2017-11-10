import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiongxicheng on 11/10/2017.
 */
public class Algorithm {

    public static void run(String algorithm, String cutoff, String output_file) throws IOException{
        if(algorithm.equals("BnB")){
            //call branch and bound
            bnb();

        }else if (algorithm.equals("Approx")){
            //call approximation algorithm
            approx();

        }else if(algorithm.equals("LS1")){
            //call local search 1

        }else if(algorithm.equals("LS2")){
            //call local search 2

        }

        PrintWriter output = new PrintWriter(output_file,"UTF-8");

    }

    public static void bnb(){

    }

    public static void approx(){

    }

    public static void ls(){
        
    }
}
