import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {
    private static final int NUM_PURCHASE_THREADS = 10;

    public static void main(String[] args) throws Exception {
        Store store = Store.getStoreInstance();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // create queue for purchase consumers to pull from
        final Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        final String purchaseQueueName = channel.queueDeclare().getQueue();

        Thread[] purchaseThreads = new Thread[NUM_PURCHASE_THREADS];
        for (int i = 0; i < NUM_PURCHASE_THREADS; i ++) {
            purchaseThreads[i] =
                    new Thread(new PurchaseConsumerRunnable(store, connection, purchaseQueueName));
            purchaseThreads[i].start();
        }

        Thread queryRequestThread =
                new Thread (new QueryConsumerRunnable(store, connection));
        queryRequestThread.start();

        for (int i = 0; i < NUM_PURCHASE_THREADS; i ++) {
            purchaseThreads[i].join();
        }

        queryRequestThread.join();
    }

}
