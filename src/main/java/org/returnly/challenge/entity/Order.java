package org.returnly.challenge.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Comparable<Order>{

	private String app_id;
	private Date created_at;
	private Customer customer;
	private List<Item> line_items;
	private Double total_price;
	
	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Item> getLine_items() {
		return line_items;
	}

	public void setLine_items(List<Item> line_items) {
		this.line_items = line_items;
	}

	public Double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Double total_price) {
		this.total_price = total_price;
	}

	@Override
	public int compareTo(Order o) {		
		return this.created_at.compareTo(o.created_at);
	}
}
