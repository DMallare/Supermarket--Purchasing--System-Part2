import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * A class that serves as a factory to create new RabbitMQ Channel objects
 * for a specific RabbitMQ exchange and pub/sub configuration
 */
public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private static final String EXCHANGE_NAME = "purchase";
    private static final String EXCHANGE_TYPE = "fanout";
    private static final String HOST = System.getProperty("RABBITMQ_HOST");
    private static final String USERNAME = System.getProperty("RABBITMQ_USERNAME");
    private static final String PASSWORD = System.getProperty("RABBITMQ_PASSWORD");
    private static final boolean DURABLE = false;
    private static ChannelFactory channelFactory;
    private static ConnectionFactory factory = null;
    private static Connection connection = null;


    static {
        try {
            channelFactory = new ChannelFactory();
            factory = new ConnectionFactory();
            factory.setHost(HOST);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("Error instantiating ChannelFactory");
        }
    }


    public static ChannelFactory getChannelFactoryInstance() {
        return channelFactory;
    }


    @Override
    public Channel create() throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, DURABLE);
        return channel;
    }


    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

}