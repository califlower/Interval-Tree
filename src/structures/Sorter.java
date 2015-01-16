package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	private Sorter() { }
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) 
	{
		if (lr=='r')
		{
			for (int i=0; i<intervals.size()-1; i++)	
			{
				if (intervals.get(i+1).rightEndPoint<intervals.get(i).rightEndPoint)
				{
					Interval temp=intervals.get(i+1);
					Interval temp2=intervals.get(i);
					
					intervals.set(i, temp);
					intervals.set(i+1, temp2);
					i=0;
					
				}
			}
			//System.out.println("Right Sort "+ intervals);

		}
		
		else if (lr=='l')
		{
			for (int i=0; i<intervals.size()-1; i++)	
			{
				if (intervals.get(i+1).leftEndPoint<intervals.get(i).leftEndPoint)
				{
					Interval temp=intervals.get(i+1);
					Interval temp2=intervals.get(i);
					
					intervals.set(i, temp);
					intervals.set(i+1, temp2);
					i=0;
					
				}
			}
			
			//System.out.println("Left Sort "+ intervals);
			return;
		}
		
		
		// COMPLETE THIS METHOD
	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) 
	{
		ArrayList<Integer> toReturn=new ArrayList<Integer>();
		
		for (int i=0;i<leftSortedIntervals.size();i++)
		{
			toReturn.add(leftSortedIntervals.get(i).leftEndPoint);
			toReturn.add(rightSortedIntervals.get(i).rightEndPoint);
		}
		
		for (int i=0; i<toReturn.size()-1; i++)	
		{
			if (toReturn.get(i+1)<toReturn.get(i))
			{
				int  temp=toReturn.get(i+1);
				int temp2=toReturn.get(i);
				
				toReturn.set(i, temp);
				toReturn.set(i+1, temp2);
				i=0;
				
			}
		}
		
		for (int i=0; i<toReturn.size()-1;i++)
		{
				if(toReturn.get(i)==toReturn.get(i+1))
				{
				    toReturn.remove(i+1);
				    i--;
				}
		}
		
		return toReturn;
	}
}
