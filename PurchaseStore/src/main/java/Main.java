import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {
    private static final int numThreads = 10;

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

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
