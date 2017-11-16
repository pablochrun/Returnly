package org.returnly.challenge.application;

import org.returnly.challenge.entity.Item;
import org.returnly.challenge.service.OrderService;
import org.returnly.challenge.utils.Utils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	OrderService orderService = new OrderService();
    	
        System.out.println("TASK 1:\nTotal number of orders: "+orderService.getTotalNumberOfOrders());
        System.out.println("TASK 2:\nNumber of unique customers: "+orderService.getNumberUniqueCustomers());

        Item[] items = orderService.getMostLeastFrequentlyOrderedItem();
        System.out.println("TASK 3:\nThe most frequently ordered item: "+ items[0].getTitle());
        System.out.println("The least frequently ordered item: "+ items[1].getTitle());
        
        System.out.println("TASK 4:\nMedian order value: "+orderService.getMedianOrderValue());
        
        long minimalIntervalTime = orderService.getShortestIntervalBetweenOrders();
        System.out.print("TASK 5:\n");
        Utils.printTimeFromMilliseconds(minimalIntervalTime);
    }
}
