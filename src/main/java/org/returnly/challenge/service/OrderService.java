package org.returnly.challenge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.returnly.challenge.entity.Customer;
import org.returnly.challenge.entity.Item;
import org.returnly.challenge.entity.Order;
import org.returnly.challenge.utils.Constants;
import org.returnly.challenge.utils.Utils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class designed for specific order service operations.
 * 
 * @author pablo
 *
 */
public class OrderService extends BaseService{

	private int totalNumberOrders = 0;
	
	public OrderService(){
		super();		
	}
	
	/**
	 * Specific method to retrieve the total number of orders.
	 * 
	 * @return
	 */
	public int getTotalNumberOfOrders(){		
		JsonNode jsonNode = getJSONNodeFromConnection("GET", "/admin/orders/count.json");
		JsonNode countNode = jsonNode.get("count");		
		totalNumberOrders = countNode.asInt();
		return totalNumberOrders;
	}
	
	/**
	 * Specific method to obtain the number of unique customers from all orders.
	 * 
	 * @return
	 */
	public int getNumberUniqueCustomers(){
		
		HashSet<String> customerIds = new HashSet<String>();
		
		if(totalNumberOrders == 0){
			totalNumberOrders = Constants.MAX_LIMIT;
		}
		
		if(totalNumberOrders > Constants.MAX_LIMIT){				
			int quotient = totalNumberOrders/Constants.MAX_LIMIT;
			if(totalNumberOrders%Constants.MAX_LIMIT >0) {
				quotient++;
			}
			
			int page = 1;
			do{
				addCustomerIds(customerIds,page);
				page++;
			}
			while(page<=quotient);
		}
		else{
			addCustomerIds(customerIds,1);
		}
	
		return customerIds.size();
	}
	
	/**
	 * Specific method to retrieve the items more and least frequently ordered in all order list.
	 * 
	 * @return
	 */
	public Item[] getMostLeastFrequentlyOrderedItem(){
		
		List<Order> orders = getAllOrders(this.totalNumberOrders);		
		List<Item> itemsList = new ArrayList<Item>();
		for(Order order : orders){
			itemsList.addAll(order.getLine_items());
		}
		
		Map<Item, Integer> itemsMap = getItemMapGroupedByFrequency(orders);		
		itemsMap = sortByValues(itemsMap, true);		
		return getItemsArrayFromMap(itemsMap);
	}

	/**
	 * Get average of amount from all orders.
	 * 
	 * @return
	 */
	public Double getMedianOrderValue() {
		
		List<Order> orders = getAllOrders(this.totalNumberOrders);	
		Double totalAmountAllOrders = 0.0d;
		int numberOrders = 0;
		for(Order order : orders){
			totalAmountAllOrders += order.getTotal_price();
			numberOrders++;
		}
		
	    return Utils.getRoundedDouble(totalAmountAllOrders/numberOrders);
	}
	
	/**
	 * Specific method to obtain the shortest interval of time between two
	 * consecutives orders for the same customer.
	 * 
	 * @return
	 */
	public Long getShortestIntervalBetweenOrders() {
		List<Order> orders = getAllOrders(this.totalNumberOrders);		
		Map<Customer, List<Order>> customerHash = getCustomerOrderMap(orders);
		
		long minimalGlobalDifferenceOfTime = 0;
		for (Map.Entry<Customer, List<Order>> entry : customerHash.entrySet())
		{
			Collections.sort(entry.getValue());
			//System.out.println("number of values: "+ entry.getValue().size());
			if(entry.getValue().size() > 1){
				long minTimeCustomerOrders = getMinimalTimeDifferenceBetweenOrders(entry.getValue());
				if(minimalGlobalDifferenceOfTime == 0 || minTimeCustomerOrders < minimalGlobalDifferenceOfTime) {
					minimalGlobalDifferenceOfTime = minTimeCustomerOrders;					
				}
			}
		}	
		return minimalGlobalDifferenceOfTime;
	}

	/**
	 * Discriminates the minimal time between orders for the same customer
	 * 
	 * @param orders
	 * @return
	 */
	private long getMinimalTimeDifferenceBetweenOrders(List<Order> orders){
		long minTime = 0;		
		long prevTime = 0
				;
		for(Order order : orders){
			if(prevTime == 0){
				prevTime = order.getCreated_at().getTime();
			}
			else{
				long thisTime = order.getCreated_at().getTime();
				long difference = thisTime - prevTime;
				if(difference < minTime || minTime == 0){
					minTime = difference;					
				}
			}
		}
		return minTime;
	}	
	
	/**
	 * Obtain the customer map with the order list for each @Customer
	 * 
	 * @param orders
	 * @return
	 */
	private Map<Customer, List<Order>> getCustomerOrderMap(List<Order> orders){
		Map<Customer, List<Order>> customerHash = new Hashtable<Customer, List<Order>>();

		for(Order order : orders){
			Customer customer = order.getCustomer();
			if(customerHash.containsKey(customer)){
				List<Order> customerOrders = customerHash.get(customer);
				customerOrders.add(order);
				customerHash.put(customer, customerOrders);				
			}
			else{
				List<Order> customerOrders = new ArrayList<Order>();
				customerOrders.add(order);
				customerHash.put(customer, customerOrders);			
			}
		}
		
		return customerHash;		
	}
	
	/**
	 * Group each item depending on its frequence.
	 * 
	 * @param orders
	 * @return
	 */
	private Map<Item, Integer> getItemMapGroupedByFrequency(List<Order> orders) {
		Map<Item, Integer> maps = new HashMap<Item, Integer>();
		for(Order order : orders){
			List<Item> itemsList2 = order.getLine_items();			
			for(Item item : itemsList2){
				if(maps.containsKey(item)){
					maps.put(item, maps.get(item) + 1);
				}
				else{
					maps.put(item, 1);
				}
			}
		}
		return maps;
	}
	
	/**
	 * Retrieve a list with all orders.
	 * 
	 * @param totalNumberOrders
	 * @return
	 */
	private List<Order> getAllOrders(int totalNumberOrders){
		List<Order> orders = new LinkedList<Order>();

		if(totalNumberOrders == 0){
			totalNumberOrders = Constants.MAX_LIMIT;
		}
		
		if(totalNumberOrders > Constants.MAX_LIMIT){				
			int quotient = totalNumberOrders/Constants.MAX_LIMIT;
			if(totalNumberOrders%Constants.MAX_LIMIT >0) {
				quotient++;
			}
			
			int page = 1;
			do{
				orders.addAll(getListOrderFromConnection("GET", "/admin/orders.json",Constants.MAX_LIMIT, page));
				page++;
			}
			while(page<=quotient);
		}
		else{
			orders.addAll(getListOrderFromConnection("GET", "/admin/orders.json",Constants.MAX_LIMIT, 1));
		}
		
		return orders;
	}	
	
	/**
	 * Build a LinkedList from the values of item map and return the most and the least
	 * item ordered.
	 * item[0]: The most frequently ordered.
	 * item[1]: The least frequently ordered.
	 * 
	 * @param maps
	 * @return
	 */
	private Item[] getItemsArrayFromMap(Map<Item, Integer> maps) {
		
		LinkedList<Item> orderedList = new LinkedList<Item>(maps.keySet());
		Item[] itemsArray = new Item[2];
		itemsArray[0] = orderedList.getFirst();
		itemsArray[1] = orderedList.getLast();
		
		return itemsArray;
	}

	/**
	 * Retrieve a list of customers from all orders.
	 * 
	 * @param customerIds
	 * @param page
	 */
	private void addCustomerIds(HashSet<String> customerIds, int page){
		List<Order> orders = getListOrderFromConnection("GET", "/admin/orders.json",Constants.MAX_LIMIT, page);	
		for (Order order : orders){
			Customer customer = order.getCustomer();
			customerIds.add(customer.getId());
		}
	}
}
