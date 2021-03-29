import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseConsumerRunnable implements Runnable {
    private final Store purchaseStore;
    private static final String EXCHANGE_NAME = "purchase";
    private static final String EXCHANGE_TYPE = "fanout";
    private final Connection connection;
    private final String queueName;

    public PurchaseConsumerRunnable(Store store, Connection connection, String queueName)
            throws IOException {
        this.purchaseStore = store;
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
                boolean purchaseSuccessful = storePurchase(message);
                if (!purchaseSuccessful) {
                    System.err.println("Purchase was unable to be added to the store");
                }
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });

        } catch (IOException ex) {
            Logger.getLogger(PurchaseConsumerRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean storePurchase(String purchaseString) {
        Purchase newPurchase = new Gson().fromJson(purchaseString, Purchase.class);
        PurchaseItems purchaseItems = new Gson().fromJson(newPurchase.getPurchaseItems(), PurchaseItems.class);
        for (int i = 0; i < purchaseItems.getItems().size(); i++) {
            PurchaseItem item = purchaseItems.getItems().get(i);
            String itemId = item.getItemID();
            int quantity = item.getNumberOfItems();
            purchaseStore.addPurchaseItem(itemId, quantity);
        }
        return true;
    }

}

