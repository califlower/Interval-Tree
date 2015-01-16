package structures;

import java.util.*;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) 
	{
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);
		//System.out.println(sortedEndPoints.toString());
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) 
	{
		
	Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
	
	for (int i=0; i<endPoints.size();i++)
	{
			IntervalTreeNode T = new IntervalTreeNode(i,i,i);
			T.leftIntervals=new ArrayList<Interval>();
			T.rightIntervals=new ArrayList<Interval>();
			Q.enqueue(T);
	}
		
		int S = Q.size;
		
		
		while (S > 0)
		{
			if (S == 1)
			{
				return Q.dequeue();
			}
			else 
			{
				int temps = S;
				while (temps > 1)
				{
					IntervalTreeNode T1 = Q.dequeue();
					IntervalTreeNode T2 = Q.dequeue();
					float v1 = T1.maxSplitValue;
					float v2 = T2.minSplitValue;
					float x = (v1+v2)/(2);
					IntervalTreeNode N = new IntervalTreeNode(x, T1.minSplitValue, T2.maxSplitValue);
					N.leftIntervals = new ArrayList<Interval>(); //Doesnt work without this
					N.rightIntervals = new ArrayList<Interval>();
					N.leftChild = T1;
					N.rightChild = T2;
					Q.enqueue(N);
					temps = temps-2;
				}
				if (temps == 1)
				{
					IntervalTreeNode temp = Q.dequeue();
					Q.enqueue(temp);
				}
				S = Q.size;
			}
		}
		
		return Q.dequeue();
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) 
	{
		//Searches recursively for each sorted interval
		for (int i=0; i<leftSortedIntervals.size();i++)
		{
			treeSearch(root, leftSortedIntervals.get(i), 'L');
		}
		
		for (int i=0; i<rightSortedIntervals.size();i++)
		{
			treeSearch(root, leftSortedIntervals.get(i), 'R'); 	
		}
		
	}
	
	//Recursive helper, very annoying to do otherwise
	private void treeSearch(IntervalTreeNode root, Interval iteratedInterval, char side)
	
	{
		if (iteratedInterval.contains(root.splitValue))
		{
			if (side=='L')
			{
				root.leftIntervals.add(iteratedInterval);
			}
			if (side=='R')
			{
				root.rightIntervals.add(iteratedInterval);
			}
			return;
			
		}
		
		if (root.splitValue<iteratedInterval.leftEndPoint)
		{
			treeSearch(root.rightChild, iteratedInterval, 'L');
			
		}
		else if (root.splitValue>iteratedInterval.rightEndPoint)
		{
			treeSearch(root.leftChild, iteratedInterval, 'R');
		}
	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	
	
	
	
	
	public ArrayList<Interval> findIntersectingIntervals(Interval q) 
	{
	
		return findIntersectingIntervalsHelper(root, q);
	}
	
	//Another helper. Made helper based on the inputs in the Sakai thingy
	private ArrayList<Interval> findIntersectingIntervalsHelper(IntervalTreeNode T, Interval Q)
	{
		
		ArrayList<Interval> ResultList=new ArrayList<Interval>();
		//If it reaches a null node, return empty list. Goes up recursively
		if (!(T==null))
		{
			
			Float SplitVal=T.splitValue;
			ArrayList<Interval> Llist=T.leftIntervals;
			ArrayList<Interval> Rlist=T.rightIntervals;
			IntervalTreeNode Lsub=T.leftChild;
			IntervalTreeNode Rsub=T.rightChild;
			
			if (Q.contains(SplitVal))
			{
				ResultList=Llist;
				ResultList.addAll(findIntersectingIntervalsHelper(Lsub, Q));
				ResultList.addAll(findIntersectingIntervalsHelper(Rsub, Q));
			}
			else if (SplitVal<Q.leftEndPoint)
			{
				int i=Rlist.size()-1;
				while (i>=0 && (Rlist.get(i).intersects(Q)))
				{
					ResultList.add(Rlist.get(i));
					i = i-1; 
				}
				ResultList.addAll(findIntersectingIntervalsHelper(Rsub, Q));
			}
			else if (SplitVal > Q.rightEndPoint)
			{	
				int i = 0;
				while ((i < Llist.size()) && (Llist.get(i).intersects(Q)))
				{
					ResultList.add(Llist.get(i));						
					i = i+1;
				}
				ResultList.addAll(findIntersectingIntervalsHelper(Lsub, Q));
			}
		}
		return ResultList;
		
	}
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
}

