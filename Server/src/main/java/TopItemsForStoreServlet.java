import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@WebServlet(name = "TopItemForStoreServlet", value = "/items/store")
public class TopItemsForStoreServlet extends HttpServlet {
    private static final int NUM_TOP_ITEMS = 10;
    private static final String REQUEST_QUEUE_NAME = "rpc";
    private final BlockingQueue<String> responses = new ArrayBlockingQueue<>(1);
    private Channel channel;

    @Override
    public void init() throws ServletException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(System.getProperty("RABBITMQ_HOST"));
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        String urlPath = request.getPathInfo();

        // check we have a URL
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("missing parameters");
            return;
        }

        // validate URL query params
        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Invalid Store ID");
            return;
        }

        // put request on queue
        try {
            final String correlationId = UUID.randomUUID().toString();
            // TODO: Should this be in the init method and name constant for thread?
            String replyQueueName = channel.queueDeclare().getQueue();

            // build the message properties
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlationId)
                    .replyTo(replyQueueName)
                    .contentType("application/json")
                    .build();

            // build message object
            StoreQuery query = new StoreQuery();
            query.setId(Integer.parseInt(urlParts[1]));
            query.setN(NUM_TOP_ITEMS);
            query.setType("Store");
            String message = new Gson().toJson(query);

            // enqueue message
            channel.basicPublish("", REQUEST_QUEUE_NAME, props, message.getBytes("UTF-8"));

            // wait for response
            String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                    responses.offer(new String(delivery.getBody(), "UTF-8"));
                }
            }, consumerTag -> {
            });

            // grab the response and return the result
            String result = responses.take();
            channel.basicCancel(ctag);
            response.setStatus((HttpServletResponse.SC_OK));
            response.getWriter().write(result);

        } catch (Exception e) {
            response.setStatus((HttpServletResponse.SC_NOT_FOUND));
            response.getWriter().write("Request was unable to be processed.");
        }

    }

    private boolean verifyStoreId(String storeIdString) {
        try {
            int storeId = Integer.parseInt(storeIdString);
            if (storeId < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isUrlValid(String[] urlParts) {
        // urlPath = "/items/store/store_id
        // urlParts = [ , store_id]
        return verifyStoreId(urlParts[1]);
    }

    @Override
    public void destroy() {
        RpcChannelPool.getChannelPoolInstance().returnObject(channel);
    }

}
