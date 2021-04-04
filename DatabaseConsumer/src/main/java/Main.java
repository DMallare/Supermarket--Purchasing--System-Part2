import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    private static final int NUM_THREADS = 25;
    private static final boolean DURABLE = true;

    public static void main(String[] args) throws Exception {
        // set config properties
        FileInputStream configFile = new FileInputStream("config.properties");
        Properties props = new Properties(System.getProperties());
        props.load(configFile);
        System.setProperties(props);
        //System.getProperties().list(System.out);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(System.getProperty("RABBITMQ_HOST"));
        factory.setUsername(System.getProperty("RABBITMQ_USERNAME"));
        factory.setPassword(System.getProperty("RABBITMQ_PASSWORD"));

        final Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        final String queueName =
                channel.queueDeclare("purchase", DURABLE, false, false,  null).getQueue();

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i ++) {
            threads[i] = new Thread(new ConsumerRunnable(connection, queueName));
            threads[i].start();
        }

        for (int i = 0; i < NUM_THREADS; i ++) {
            threads[i].join();
        }
    }
}
