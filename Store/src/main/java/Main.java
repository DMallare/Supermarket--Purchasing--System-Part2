import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    private static final int NUM_PURCHASE_THREADS = 25;
    private static final boolean DURABLE = false;

    public static void main(String[] args) throws Exception {
        // set config properties
        FileInputStream configFile = new FileInputStream("config.properties");
        Properties props = new Properties(System.getProperties());
        props.load(configFile);
        System.setProperties(props);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(System.getProperty("RABBITMQ_HOST"));
        factory.setUsername(System.getProperty("RABBITMQ_USERNAME"));
        factory.setPassword(System.getProperty("RABBITMQ_PASSWORD"));

        // create queue for purchase consumers to pull from
        final Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        final String queueName =
                channel.queueDeclare("purchaseQueue", DURABLE, false, false,  null).getQueue();

        Thread[] purchaseThreads = new Thread[NUM_PURCHASE_THREADS];
        for (int i = 0; i < NUM_PURCHASE_THREADS; i ++) {
            purchaseThreads[i] =
                    new Thread(new PurchaseConsumerRunnable(connection, queueName));
            purchaseThreads[i].start();
        }

        Thread queryRequestThread =
                new Thread (new QueryConsumerRunnable(connection));
        queryRequestThread.start();

        for (int i = 0; i < NUM_PURCHASE_THREADS; i ++) {
            purchaseThreads[i].join();
        }

        queryRequestThread.join();
    }

}
