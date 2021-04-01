import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumerRunnable implements Runnable {
    private static final String EXCHANGE_NAME = "purchase";
    private static final String EXCHANGE_TYPE = "fanout";
    private final Connection connection;
    private final PurchaseDao purchaseDao;
    private final String queueName;

    public ConsumerRunnable(Connection connection, String queueName) throws IOException {
        purchaseDao = new PurchaseDao();
        this.connection = connection;
        this.queueName = queueName;
    }

    @Override
    public void run() {
        try {
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                boolean purchaseSuccessful = makePurchase(message);
                if (!purchaseSuccessful) {
                    System.err.println("Purchase was unable to be added to the database");
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });

        } catch (Exception ex) {
            Logger.getLogger(ConsumerRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean makePurchase(String purchaseString) {
        Purchase newPurchase = new Gson().fromJson(purchaseString, Purchase.class);
        return purchaseDao.createPurchase(newPurchase);
    }

}
