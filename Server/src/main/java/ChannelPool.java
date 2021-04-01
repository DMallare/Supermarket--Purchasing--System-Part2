import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * A pool of RabbitMQ Channels associated with a specific exchange
 * and pub/sub configuration
 */
public class ChannelPool extends GenericObjectPool<Channel> {
    private static final ChannelPool pool =
            new ChannelPool(ChannelFactory.getChannelFactoryInstance());

    /**
     * Creates a new ChannelPool
     *
     * @param factory - a factory for creating Pooled Channel
     *                  objects
     */
    private ChannelPool(PooledObjectFactory<Channel> factory) {
        super(factory);
        GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<>();
        config.setMaxIdle(100);
        config.setMaxTotal(200);
        this.setConfig(config);
    }

    public static ChannelPool getChannelPoolInstance() {
        return pool;
    }

}