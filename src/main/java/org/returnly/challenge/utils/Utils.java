package org.returnly.challenge.utils;

public class Utils {

	public static void printTimeFromMilliseconds (long milliseconds){		
		long diffSeconds = milliseconds / 1000 % 60;
	    long diffMinutes = milliseconds / (60 * 1000) % 60;
	    long diffHours = milliseconds / (60 * 60 * 1000)%24;
	    int diffInDays = (int) (milliseconds) / (1000 * 60 * 60 * 24);	    
	    System.out.print("The minimal difference of time is: ");
	    System.out.println(diffInDays + " days, "+diffHours+" hours, "+ diffMinutes + " minutes, " + diffSeconds + " seconds.");
	}
	
}
