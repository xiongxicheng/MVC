import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiongxicheng on 11/13/2017.
 */
public class finishTask extends TimerTask {
    public void run(){
        //System.out.println("Time's up");
        Algorithm.output.close();
        Algorithm.output_sol.println(Algorithm.result.size());
        Algorithm.output_sol.print(Algorithm.result.get(0));
        for(int i=1;i<Algorithm.result.size();i++){
            Algorithm.output_sol.print(","+Algorithm.result.get(i));
        }
        Algorithm.output_sol.close();
        System.exit(0);
    }
}
