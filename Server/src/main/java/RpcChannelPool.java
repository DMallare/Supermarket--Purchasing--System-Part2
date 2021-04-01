import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RpcChannelPool extends GenericObjectPool<Channel> {
    private static final RpcChannelPool rpcPool =
            new RpcChannelPool(RpcChannelFactory.getRpcChannelFactoryInstance());

    /**
     * Creates a new RpcChannelPool
     *
     * @param factory - a factory for creating RpcPooled Channel
     *                  objects
     */
    private RpcChannelPool(PooledObjectFactory<Channel> factory) {
        super(factory);
        GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<>();
        config.setMaxIdle(1);
        config.setMaxTotal(3);
        this.setConfig(config);
    }

    public static RpcChannelPool getChannelPoolInstance() {
        return rpcPool;
    }

}
