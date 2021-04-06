import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "PurchaseServlet", value = "/PurchaseServlet")
public class PurchaseServlet extends HttpServlet {
    private static final String EXCHANGE_NAME = "purchase";
    private static final int NON_PERSISTENT= 1;
    private static final int PERSISTENT = 2;
    final private int STOREID_INDEX = 1;
    final private int CUSTOMER_INDEX = 2;
    final private int CUSTOMERID_INDEX = 3;
    final private int DATE_INDEX = 4;
    final private int DATE_VAL_INDEX = 5;
    private ChannelPool channelPool;
    private Channel channel;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
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
            response.getWriter().write("Invalid parameters");
            return;
        }

        // validate request body
        String purchaseItemsStr = parseRequestBody(request.getReader());
        try {
            PurchaseItems purchaseItems =
                    new Gson().fromJson(purchaseItemsStr, PurchaseItems.class);
            if (!validatePurchase(purchaseItems)) {
                response.setStatus((HttpServletResponse.SC_BAD_REQUEST));
                response.getWriter().write("Invalid purchaseItems");
            } else {
                // request body is properly formatted, create Purchase
                Purchase newPurchase = createNewPurchase(urlParts, purchaseItemsStr);

                // publish purchase to the RabbitMQ exchange
                channelPool = ChannelPool.getChannelPoolInstance();
                channel = channelPool.borrowObject();
                enqueuePurchase(newPurchase);

                channelPool.returnObject(channel);
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            response.setStatus((HttpServletResponse.SC_BAD_REQUEST));
            response.getWriter().write("Invalid purchase");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.setStatus((HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
            response.getWriter().write("Purchase did not get enqueued.");
        }
    }


    private Purchase createNewPurchase(String[] urlParts, String purchaseItemsStr) {
        int storeId = Integer.parseInt(urlParts[STOREID_INDEX]);
        int customerId = Integer.parseInt(urlParts[CUSTOMERID_INDEX]);

        // create a new Purchase instance
        Purchase newPurchase = new Purchase();
        newPurchase.setStoreID(storeId);
        newPurchase.setCustomerID(customerId);
        newPurchase.setDate(urlParts[DATE_VAL_INDEX]);
        newPurchase.setPurchaseItems(purchaseItemsStr);
        return newPurchase;
    }


    /**
     * Serializes the given Purchase object and publishes it to the exchange
     * @param purchase - the given Purchase object to publish to the exchange
     * @throws IOException
     */
    private void enqueuePurchase(Purchase purchase) throws IOException {
        System.out.println("Sending to RMQ");
        BasicProperties messageProperties = new BasicProperties.Builder().deliveryMode(NON_PERSISTENT).build();
        String message = new Gson().toJson(purchase);
        channel.basicPublish(EXCHANGE_NAME, "", messageProperties, message.getBytes("UTF-8"));
    }


    /**
     * Parses the body of the request
     * @param requestBodyReader
     * @return the String contents of the request body
     * @throws IOException
     */
    private String parseRequestBody(BufferedReader requestBodyReader) throws IOException {
        // read in request body
        StringBuilder requestBodyBuilder = new StringBuilder();
        String line;
        while ( (line = requestBodyReader.readLine()) != null) {
            requestBodyBuilder.append(line);
        }
        return requestBodyBuilder.toString();
    }


    /**
     * Validates the purchase
     * @param purchaseItems - the items specified in the request body
     * @return
     */
    private boolean validatePurchase(PurchaseItems purchaseItems) {
        for (PurchaseItem item : purchaseItems.getItems()) {
            if (item.getItemID() == null || item.getItemID().isEmpty()
                    || item.getNumberOfItems() < 0) {
                return false;
            }
        }
        return true;
    }


    private boolean verifyDate(String dateString) {
        // verify date
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }


    private boolean verifyCustomerId(String customerIdString) {
        try {
            int customerId = Integer.parseInt(customerIdString);
            if (customerId < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
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
        // urlPath  = "/purchase/store_id/customer/customer_id/date/YYYYMMDD"
        // urlParts = [ , 22, customer, 11, date, 202101011
        if (!urlParts[CUSTOMER_INDEX].equals("customer")
                || !urlParts[DATE_INDEX].equals("date")) {
            return false;
        }

        return verifyDate(urlParts[DATE_VAL_INDEX])
                && verifyCustomerId(urlParts[CUSTOMERID_INDEX])
                && verifyStoreId(urlParts[STOREID_INDEX]);
    }
}
