package org.returnly.challenge.entity;

import java.util.List;

/**
 * Wrapper class for a list of orders.
 * 
 * @author pablo
 *
 */
public class GetOrders {
	private List<Order> orders;

    public List<Order> getOrders(){
        return this.orders;
    }
    public void setOrders(List<Order> orders){
        this.orders = orders;
    }
}
