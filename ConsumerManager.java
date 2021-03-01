import java.util.*;
import java.util.concurrent.*;

public class ConsumerManager {

    public ConsumerManager(List<String> files, int poolSize) throws Exception {

        int QUEUE_SIZE = files.size();
        int POOL_SIZE = poolSize;

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();        
        ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < QUEUE_SIZE; i++) {
            queue.offer(files.get(i));
        }

        List<Consumer> consumers = new ArrayList<Consumer>();
        for (int i = 0; i < POOL_SIZE; i++) {
            consumers.add(new Consumer(queue, i));
        }

        List<Future<Boolean>> futures = executorService.invokeAll(consumers);

        for (Future<Boolean> future : futures) {
            try {
                if (future.get()) {
                }
            } catch (Exception e) {
                System.out.println("Stop tasks");
                for (Future<Boolean> f : futures) {
                    f.cancel(true);
                }
                throw e;
            } finally {
                System.out.println("Shutdown Now");
                executorService.shutdown();
            }
        }

        shutdownAndAwaitTermination(executorService);

    }

    public void shutdownAndAwaitTermination (ExecutorService executorService) {
        System.out.println("Shutting down");
        executorService.shutdown();
        try {
          if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                System.err.println("Pool did not terminate");
          }
        } catch (InterruptedException ie) {
          executorService.shutdownNow();
          Thread.currentThread().interrupt();
        }
    }
}
