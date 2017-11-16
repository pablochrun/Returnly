package org.returnly.challenge.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.returnly.challenge.entity.Customer;
import org.returnly.challenge.entity.GetOrders;
import org.returnly.challenge.entity.Order;
import org.returnly.challenge.utils.Constants;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseService implements IService{
	
	public BaseService(){
	}
	
	public String getStringFromConnection(String method, String path){
		URL url;
		HttpURLConnection conn = null;
		String strObtained = null;
		try
		{
			url = new URL(Constants.URL_100PURE + path +"?limit="+Constants.MAX_LIMIT);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("X-Shopify-Access-Token", Constants.ACCESS_TOKEN);
			
			conn.connect();
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			strObtained = br.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(conn != null) conn.disconnect();
		}		
		return strObtained;
	}
	
	public String getStringFromConnectionLimitSize(String method, String path, int limit, int page){
		URL url;
		HttpURLConnection conn = null;
		String strObtained = null;
		try
		{
			url = new URL(Constants.URL_100PURE + path +"?limit="+limit+"&page="+page);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("X-Shopify-Access-Token", Constants.ACCESS_TOKEN);
			
			conn.connect();
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			strObtained = br.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(conn != null) conn.disconnect();
		}
		
		return strObtained;
	}
	
	@Override
	public JsonNode getJSONNodeFromConnection(String method, String path) {	
		
		JsonNode currentNode = null;
		
		try {			
			String output = getStringFromConnection(method, path);
			ObjectMapper mapper = new ObjectMapper();
		    JsonFactory factory = mapper.getJsonFactory();
		    JsonParser parser = factory.createJsonParser(output);
		    currentNode = mapper.readTree(parser);
	    
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return currentNode;
	}
	
	@Override
	public List<Order> getListOrderFromConnection(String method, String path, int limit, int page) {	
		GetOrders results = null;
		ObjectMapper mapper = new ObjectMapper();			
		String output = getStringFromConnectionLimitSize(method, path, limit, page);			
		results = mapJsonStringToOrderList(results, output, mapper);		
		return results != null ? results.getOrders() : new ArrayList<Order>();
	}

	@SuppressWarnings("rawtypes")
	public static <K, V extends Comparable> Map<K, V> sortByValues(Map<K, V> tempMap, boolean ascOrder) {
		TreeMap<K, V> map = new TreeMap<>(buildComparator(tempMap, true));
		map.putAll(tempMap);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V extends Comparable> Comparator<? super K> buildComparator(final Map<K, V> tempMap, boolean asc) {
		if(asc){
			return (o1, o2) -> tempMap.get(o2).compareTo(tempMap.get(o1));
		}
		else{
			return (o1, o2) -> tempMap.get(o1).compareTo(tempMap.get(o2));

		}
	}
	
	public static Map<Customer, List<Order>> sortOrdersByCreationDate(Map<Customer, List<Order>> customerHash, boolean ascOrder) {
		TreeMap<Customer, List<Order>> map = new TreeMap<>(buildComparatorOrdersByCreationDate(customerHash, true));
		map.putAll(customerHash);
		return map;
	}

	public static Comparator<Customer> buildComparatorOrdersByCreationDate(
			final Map<Customer, List<Order>> tempMap, boolean asc) {
		
		if(asc){
			return (o1, o2) -> ((Order)tempMap.get(o2)).getCreated_at().compareTo(((Order)tempMap.get(o1)).getCreated_at());
		}
		else{
			return (o1, o2) -> ((Order)tempMap.get(o1)).getCreated_at().compareTo(((Order)tempMap.get(o2)).getCreated_at());
		}
	}
	
	Double getRoundedDouble(Double value){
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	private GetOrders mapJsonStringToOrderList(GetOrders results, String output, ObjectMapper mapper) {
		try {
			results = mapper.readValue(output, GetOrders.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
}
