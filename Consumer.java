import java.io.*;
import java.util.concurrent.*;

public class Consumer implements Callable<Boolean> {

    int id;
    private String session;
    
    private final LinkedBlockingQueue<String> queue;

    public Consumer(LinkedBlockingQueue<String> queue, int id) throws Exception {
        this.id = id;
        this.queue = queue;
        createSession();
        System.out.println("Created Consumer - "+id);
    }

    private void createSession () throws Exception{
        this.session = "session" + id;
    }

    @Override
    public Boolean call() throws Exception {
        Boolean result = false;
        while (!queue.isEmpty()) {
            try {
                if (session == null)
                    createSession();
                String file = queue.take();
                processFile(file);
                if (file.contains("5")) {
                    throw new InterruptedException("interrupting in "+ file);
                } 
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
                throw new RuntimeException();
            }
        }
        return result;
    }

    private void processFile(String file) throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep((int)(Math.random() * (5 - 1 + 1) + 1));
        File f = new File("./sftp/"+file);
        f.createNewFile();
        System.out.println("Consumer" + id + " SENT " + file);
    }
}
