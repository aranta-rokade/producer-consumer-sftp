import java.util.*;
import java.util.concurrent.*;

public class Producer {

    public static void main(String[] args) throws Exception {

        int fileSize = Integer.parseInt(args[0]);
        int poolSize = Integer.parseInt(args[1]);

        List<String> files = new ArrayList<String>(); 

        for (int i = 0; i < fileSize; i++) {
            files.add("test"+i+".tar");
        }

        ConsumerManager consumerMngr = new ConsumerManager(files, poolSize);
        
    }
}
