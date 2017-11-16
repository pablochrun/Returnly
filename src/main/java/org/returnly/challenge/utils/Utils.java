package org.returnly.challenge.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Auxiliar class for non-business and commons methods 
 * 
 * @author pablo
 *
 */
public class Utils {

	/**
	 * Print the result of obtain the shortest interval 
	 * between any two consecutive orders placed by the same customer
	 * 
	 * @param milliseconds
	 */
	public static void printTimeFromMilliseconds (long milliseconds){		
		long diffSeconds = milliseconds / 1000 % 60;
	    long diffMinutes = milliseconds / (60 * 1000) % 60;
	    long diffHours = milliseconds / (60 * 60 * 1000)%24;
	    int diffInDays = (int) (milliseconds) / (1000 * 60 * 60 * 24);	    
	    System.out.print("The minimal difference of time is: ");
	    System.out.println(diffInDays + " days, "+diffHours+" hours, "+ diffMinutes + " minutes, " + diffSeconds + " seconds.");
	}
	
	/**
	 * Get a rounded Double value.
	 * 
	 * @param value
	 * @return
	 */
	public static Double getRoundedDouble(Double value){
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}
