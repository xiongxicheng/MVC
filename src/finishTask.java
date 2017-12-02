import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiongxicheng on 11/13/2017.
 */
public class finishTask extends TimerTask {
    public void run(){
        System.out.println("Time's up");
        Algorithm.output.close();
        System.out.println(Algorithm.result.size());
        for(int i:Algorithm.result){
            System.out.print(i+" ");
        }
        System.exit(0);
    }
}
