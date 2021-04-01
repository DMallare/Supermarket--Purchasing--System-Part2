import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RpcChannelFactory extends BasePooledObjectFactory<Channel> {
    private static RpcChannelFactory rpcChannelFactory = null;
    private static final String HOST = System.getProperty("RABBITMQ_HOST");
    private final ConnectionFactory factory;
    private final Connection connection;

    public static RpcChannelFactory getRpcChannelFactoryInstance() {
        if (rpcChannelFactory == null) {
            try {
                rpcChannelFactory = new RpcChannelFactory();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unable to get Rpc channel factory instance");
            }
        }
        return rpcChannelFactory;
    }

    private RpcChannelFactory() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(HOST);
        connection = factory.newConnection();
    }

    @Override
    public Channel create() throws IOException {
        Channel channel = connection.createChannel();
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
