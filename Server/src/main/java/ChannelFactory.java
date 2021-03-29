import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private static ChannelFactory channelFactory = null;
    private static final String EXCHANGE_NAME = "purchase";
    private static final String EXCHANGE_TYPE = "fanout";
    private static final String HOST = System.getProperty("RABBITMQ_HOST");
    private final ConnectionFactory factory;
    private final Connection connection;

    public static ChannelFactory getChannelFactoryInstance() {
        if (channelFactory == null) {
            try {
                channelFactory = new ChannelFactory();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unable to get channel factory instance");
            }
        }
        return channelFactory;
    }

    private ChannelFactory() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        connection = factory.newConnection();
    }

    @Override
    public Channel create() throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
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