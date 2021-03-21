import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private static final ChannelFactory channelFactory = new ChannelFactory();
    private static final String EXCHANGE_NAME = "purchase";
    private static final String EXCHANGE_TYPE = "fanout";
    private final ConnectionFactory factory;

    public static ChannelFactory getChannelFactoryInstance() {
        return channelFactory;
    }

    private ChannelFactory() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    @Override
    public Channel create() {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            return channel;

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.err.println("Unable to create channel");
            return null;
        }
    }

    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

}