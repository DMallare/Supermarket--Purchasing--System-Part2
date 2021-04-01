import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryConsumerRunnable implements Runnable {
    private static final String QUERY_QUEUE_NAME = "rpc";
    private final Store store;
    private final Connection connection;

    public QueryConsumerRunnable(Store store, Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    @Override
    public void run() {
        try (Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUERY_QUEUE_NAME, false, false, false, null);
            channel.queuePurge(QUERY_QUEUE_NAME);
            channel.basicQos(1);
            Object monitor = new Object();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .contentType("application/json")
                        .build();

                String response = "";
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    Query query = new Gson().fromJson(message, Query.class);

                    if (query.getType().equals("Store")) {
                        System.out.println("Got request for the top 10 items!");
                        StoreQueryResponse results =
                                store.getTopNItemsForStore(query.getN(), query.getId());
                        System.out.println("Results are..." + results);
                        response = new Gson().toJson(results);
                    } else if (query.getType().equals("Item")) {
                        System.out.println("Got request for the top 5 stores!");
                        ItemQueryResponse results =
                                store.getTopNStoresForItem(query.getN(), query.getId());
                        System.out.println("Look at store: " + query.getId());
                        System.out.println("Purchases by Store:" + store.getStorePurchases());
                        response = new Gson().toJson(results);
                        System.out.println("Results are..." + response);
                    } else {
                        response = "Query not supported";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(),
                            replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    // RabbitMQ consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(QUERY_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
            }));

            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException | TimeoutException e) {
            Logger.getLogger(QueryConsumerRunnable.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
