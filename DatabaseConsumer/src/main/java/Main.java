import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    private static final int numThreads = 200;

    public static void main(String[] args) throws Exception {
        // set config properties
        FileInputStream configFile = new FileInputStream("config.properties");
        Properties props = new Properties(System.getProperties());
        props.load(configFile);
        System.setProperties(props);
        //System.getProperties().list(System.out);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(System.getProperty("MySQL_IP_ADDRESS"));

        final Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        final String queueName = channel.queueDeclare().getQueue();

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i ++) {
            threads[i] = new Thread(new ConsumerRunnable(connection, queueName));
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i ++) {
            threads[i].join();
        }
    }
}
