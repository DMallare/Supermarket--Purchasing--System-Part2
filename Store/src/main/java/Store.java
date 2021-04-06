import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private static Store store = new Store();
    private final Map<Integer,Map<String, Integer>> storePurchases;
    private final Map<String,Map<Integer, Integer>> itemPurchases;


    /**
     * Given the storeID, itemID, and quantity purchased, adds this information to
     * the map keyed by store ID
     * @param storeId - The ID of the store
     * @param itemId - The ID of the item that was purchased
     * @param quantityPurchased - The amount of the item that was purchased
     */
    public void addItemToStorePurchases(int storeId, String itemId, Integer quantityPurchased) {
        // System.out.println("Adding item id " + itemId + " to store id " + storeId + " purchases.");
        Map<String, Integer> storeItemsPurchased =
                storePurchases.getOrDefault(storeId, new HashMap<>());

        int currentTotal = storeItemsPurchased.getOrDefault(itemId, 0);
        storeItemsPurchased.put(itemId, quantityPurchased + currentTotal);
        storePurchases.put(storeId, storeItemsPurchased);
        // System.out.println("Added? " + storePurchases);
    }


    /**
     * Given the storeID, itemID, and quantity purchased, adds this information to
     * the map keyed by item ID
     * @param storeId - The ID of the store
     * @param itemId - The ID of the item that was purchased
     * @param quantityPurchased - The amount of the item that was purchased
     */
    public void addStoreToItemPurchases(int storeId, String itemId, Integer quantityPurchased) {
        Map<Integer, Integer> storesForItem =
                itemPurchases.getOrDefault(itemId, new HashMap<>());

        int currentTotal = storesForItem.getOrDefault(storeId, 0);
        storesForItem.put(storeId, quantityPurchased + currentTotal);
        itemPurchases.put(itemId, storesForItem);
    }


    /**
     * Constructs a new instance of Store by initializing concurrent hash maps
     * keyed on Store ID and Item ID
     */
    private Store() {
        storePurchases = new ConcurrentHashMap<>();
        itemPurchases = new ConcurrentHashMap<>();
    }


    /**
     * Returns the instance of the Store
     * @return the instance of the Store
     */
    public static Store getStoreInstance() {
        return store;
    }


    /**
     * Returns the top N items purchased at the given store
     * @param n - the number of top items to return
     * @param storeId - the store ID
     * @return the top N items purchased at the given store
     */
    public StoreQueryResponse getTopNItemsForStore(int n, int storeId) {
        // System.out.println("Store is= " + storeId);
        ItemCountPair itemCount;
        // System.out.println("The store map :" + storePurchases);
        PriorityQueue<ItemCountPair> itemCounts = new PriorityQueue<>();
        StoreQueryResponse results = new StoreQueryResponse();
        List<ItemItemCount> resultItems = new ArrayList<>();
        results.setItems(resultItems);

        // if there have not been any purchases made at the given store
        if (!storePurchases.containsKey(storeId)) {
            System.out.println("Store ID not found");
            return results;
        }

        // get items bought from the given store
        Map<String, Integer> itemsBoughtAtStore = storePurchases.get(storeId);

        // add items to the min heap, heap has size <= n
        for (String itemId : itemsBoughtAtStore.keySet()) {
            itemCount = new ItemCountPair(itemId, itemsBoughtAtStore.get(itemId));
            itemCounts.add(itemCount);

            // remove the minimum element if heap length is > n
            if (itemCounts.size() > n) {
                itemCounts.poll();
            }
        }

        while (!itemCounts.isEmpty()) {
            itemCount = itemCounts.poll();
            int itemId = Integer.parseInt(itemCount.getFirst());
            int count = itemCount.getSecond();
            ItemItemCount item = new ItemItemCount(itemId, count);
            results.getItems().add(item);
        }
        return results;
    }

    /**
     * Returns the top N stores that sold the given item
     * @param n - the number of stores to return
     * @param itemId - the item ID
     * @return the top N stores that sold the given item
     */
    public ItemQueryResponse getTopNStoresForItem(int n, Integer itemId) {
        StoreCountPair storeCount;
        PriorityQueue<StoreCountPair> storeCounts = new PriorityQueue<>();
        ItemQueryResponse results = new ItemQueryResponse();
        List<StoreItemCount> resultStores = new ArrayList<>();
        results.setStores(resultStores);

        // if there have not been any purchases made at the given store
        if (!itemPurchases.containsKey(itemId.toString())) {
            return results;
        }

        // get items bought from the given store
        Map<Integer, Integer> storesThatSoldItem = itemPurchases.get(itemId.toString());

        // add items to the min heap, heap has size <= n
        for (Integer storeId : storesThatSoldItem.keySet()) {
            storeCount = new StoreCountPair(storeId, storesThatSoldItem.get(storeId));
            storeCounts.add(storeCount);

            // remove the minimum element if heap length is > n
            if (storeCounts.size() > n) {
                storeCounts.poll();
            }
        }

        while (!storeCounts.isEmpty()) {
            storeCount = storeCounts.poll();
            StoreItemCount item = new StoreItemCount(storeCount.getFirst(), storeCount.getSecond());
            results.getStores().add(item);
        }
        return results;
    }

    public Map<Integer, Map<String, Integer>> getStorePurchases() {
        return storePurchases;
    }

    public Map<String, Map<Integer, Integer>> getItemPurchases() {
        return itemPurchases;
    }
}
