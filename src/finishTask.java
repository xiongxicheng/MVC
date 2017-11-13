import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiongxicheng on 11/13/2017.
 */
public class finishTask extends TimerTask {
    public void run(){
        System.out.println("Time's up");
        System.exit(0);
    }
}
