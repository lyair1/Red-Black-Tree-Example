import java.util.*;


public class RBTester {

	
	private static final int MAX_INTERATIONS = 5000;
    private static Random random = new Random();
    
	public static int[] sortInts(int[] arr)
	{
		int[] sortedArr = new int[arr.length];
		for (int j=0; j<arr.length; j++)
		{
			sortedArr[j] = arr[j];
		}
		Arrays.sort(sortedArr);
		return sortedArr;
	}
	
	public static boolean arraysIdentical(int[] arr1, int[] arr2)
	{
		if (arr1.length != arr2.length)
		{
			return false;
		}
		for (int j=0; j<arr1.length; j++)
		{
			if (arr1[j] != arr2[j])
			{
				return false;
			}
		}
		return true;
	}
	/*
	// Log base 2
	public static double log2(double a)
	{
		return Math.log(a)/Math.log(2);
	}
	
	// In a balanced tree, log_2(n)<=height<=2log_2(n+1) where n is the number of elements in the tree. 
	public static boolean isHeightOk(int n, int userHeight)
	{
		double upper = 2*log2(n+1);
		double lower = Math.floor(log2(n));
		
		return (userHeight >= lower) && (userHeight <= upper);
	}
	
	//Not needed
	// The average depth should be shallower than the max height.
	public static boolean isAvgDepthOk(int n, double userAvgDepth)
	{
		return userAvgDepth <= 2*log2(n+1);
	}
	*/
	public static void main(String[] args)
	{
		

		System.out.println("=========================================");
		System.out.println("TEST 1");
		System.out.println("=========================================");

		// initialize tests success array to false
		boolean[] success = new boolean[9];
		for (int i=0; i<success.length; i++)
		{
			success[i] = false;
		}
		
		// create array of values between 800-1800
		// like this - 800, 801, 802, 803, 804
		int[] valuesTemp = new int[1000];
		for (int j=0; j<valuesTemp.length; j++)
		{
			valuesTemp[j] = 800 + j;
		}
		
		// mix the values - create a new list of values taken
		// one from the start one from the end, alternately
		// i.e. values[0], values[-1], values[1], values[-2] ...
		int[] values = new int[1000];
		{
			int k = 0;
			for (int j=0; j< (values.length / 2); j++)
			{
				values[k] = valuesTemp[j];
				k++;
				values[k] = valuesTemp[valuesTemp.length-1-j];
				k++;
			}
		}
		
		// create custom array of values
		int[] values3 = new int[] {17,6,1,19,18,3,2,10,13,12,
				20,15,4,11,7,16,9,5,8,14,21};
		
		
		RBTreeTestable rbTree = null;
		MyTree myTree = null;
		int last_value_removed = -1;
		
		for (int i=0; i<(success.length); i++)
		{
			try
			{
				// Initialization
				if (i==0)
				{
					rbTree = new RBTreeTestable();
					success[i] = rbTree.empty() && rbTree.keysToArray().length == 0;
				}
				// Insert Sanity
				else if (i==1)
				{
					int n = 0;
					myTree = new MyTree();
					for (int j=0; j<values.length; j++)
					{
						rbTree.insert(values[j]);
						myTree.insert(values[j]);
					}
					for (int j=0; j<values.length; j++)
					{
						if (rbTree.search(values[j]) == null)
						{
							n++;
						}
					}
					success[i] = (n==0) && rbTree.keysToArray().length == values.length; 
				}
				// ToDoubleArray
				else if (i==2)
				{
					success[i] = arraysIdentical(sortInts(myTree.array()), 
									rbTree.keysToArray());
				}
				// Delete
				else if (i==3 || i==5)
				{
					int n=0;
					
					// delete values going from the middle outwards
					// i.e. 1000, 1002, 998, ..
					// do it in 2 chunks
					int chunk_size = values.length/4;
					for (int j=0; j<2; j++)
					{
						// delete a chunk of values
						int start = j*chunk_size;
						for (int k=start; k < (start+chunk_size); k++)
						{
							rbTree.delete(values[values.length-1-k]);
							myTree.delete(values[values.length-1-k]);
							last_value_removed = values[values.length-1-k];
						}
						// check correctness
						for (int k=0; k<values.length; k++)
						{
							boolean t = rbTree.search(values[k]) != null;
							if (t != myTree.contains(values[k]))
							{
								n++;
							}
						}
						if (!arraysIdentical(rbTree.keysToArray(),
								sortInts(myTree.array())))
						{
							n++;
						}
					}
					success[i] = (n==0);
				}
				// Re-Insert
				else if (i==4)
				{
					int n=0;
					// add values going from the middle outwards
					// i.e. 1000, 1002, 998, ..
					// do it in 2 chunks
					int chunk_size = values.length/4;
					for (int j=0; j<2; j++)
					{
						// insert a chunk of values
						int start = j*chunk_size;
						for (int k=start; k < (start+chunk_size); k++)
						{
							rbTree.insert(values[values.length-1-k]);
							myTree.insert(values[values.length-1-k]);
						}
						// check correctness
						for (int k=0; k<values.length; k++)
						{
							boolean t = rbTree.search(values[k]) != null;
							if (t != myTree.contains(values[k]))
							{
								n++;
							}
						}
						if (!arraysIdentical(rbTree.keysToArray(),
								sortInts(myTree.array())))
						{
							n++;
						}
					}
					success[i] = (n==0);
				}
				// Min Max
				else if (i==6)
				{
					int n = 0;
					for (int j=0; values[j]!=last_value_removed; j++)
					{
						rbTree.delete(values[j]);
						myTree.delete(values[j]);
						int[] arr = rbTree.keysToArray();
						if (values[j+1]==last_value_removed)
						{
							if (arr.length > 0)
							{
								n++;
							}
						}
						else
						{
							if (arr.length != myTree.size() ||
									arr[0] != Integer.valueOf(rbTree.min()) ||
									arr[0] != myTree.min() ||
									arr[arr.length-1] != Integer.valueOf(rbTree.max()) ||
									arr[arr.length-1] != Integer.valueOf(myTree.max()))
							{
								n++;
							}
						}
					}
					success[i] = (n == 0) && rbTree.empty();
				}
				// Insert random ordered ints
				else if (i==7)
				{
					int n=0;
					rbTree = new RBTreeTestable();
					myTree = new MyTree();
					for (int j=0; j<values3.length; j++)
					{
						rbTree.insert(values3[j]);
						myTree.insert(values3[j]);
						if (Integer.valueOf(rbTree.max()) != myTree.max() ||
								Integer.valueOf(rbTree.min()) != myTree.min())
						{
							n++;
						}
						for (int k=0; k<values3.length; k++)
						{
							boolean t = rbTree.search(values3[k]) != null;
							if (t != myTree.contains(values3[k]))
							{
								n++;
							}
						}
						if (!arraysIdentical(rbTree.keysToArray(),
								sortInts(myTree.array())))
						{
							n++;
						}
					}
					success[i] = (n == 0);
				}
				// Min/Max
				else if (i==8)
				{
					int n=0;
					for (int j=0; j<values3.length; j++)
					{
						rbTree.delete(values3[values3[j]-1]);
						myTree.delete(values3[values3[j]-1]);
						if (myTree.size() > 0)
						{
							if (Integer.parseInt(rbTree.max()) != myTree.max() ||	Integer.parseInt(rbTree.min()) != myTree.min())
							{
								n++;
							}
						}
						else
						{
							if (!rbTree.empty())
							{
								n++;
							}
						}
						for (int k=0; k<values3.length; k++)
						{
							boolean t = rbTree.search(values3[k]) != null;
							if (t != myTree.contains(values3[k]))
							{
								n++;
							}
						}
						if (!arraysIdentical(rbTree.keysToArray(),
								sortInts(myTree.array())))
						{
							n++;
						}
					}
					success[i] = (n == 0);
				}
				else if (i==9)
				{
					//Irrelevant
				}
				else if (i==10)
				{
				//Irrelevant	
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception Caught On Test" + i + " : "+e);
			}
			System.out.println("Success On Test " + i + " = " + success[i]);
		}

		int n = 0;
		for (boolean value : success)
		{
			if (value)
			{
				n++;
			}
		}
		//System.out.println("=========================================");
		System.out.println("Total : " + n + "/" + (success.length));		


		System.out.println("=========================================");
		System.out.println("TEST 2");
		System.out.println("=========================================");


		InAndOut();
		TestMixed();
		System.out.println("=========================================");

		System.out.println();
		System.out.println("DONE!");
		
	}
	
	

    private static void verifyTree(RBTreeTestable tree, ArrayList<Integer> array)
    {
    	tree.verifyProperties();
    	Collections.sort(array);
    	assert areEqual(array, tree.toArray());
    	if (tree.size() != 0)
    	{
	    	assert tree.min().equals(array.get(0).toString());
	    	assert tree.max().equals(array.get(array.size() - 1).toString());    		
    	}
    }
    
    private static void InAndOut()
    {
		RBTreeTestable tree = new RBTreeTestable();
		ArrayList<Integer> array = new ArrayList<Integer>();
	
		for (int i = 0; i < MAX_INTERATIONS; i++)
		{
			int key = random.nextInt(Integer.MAX_VALUE);
			tree.insert(key, String.valueOf(key));
			
			if (!array.contains(key))
			{
				array.add(key);
			}

			verifyTree(tree, array);
		}
		
		for (int j = 0; j < MAX_INTERATIONS; j++)
		{
			int i = random.nextInt(array.size());
			int key = array.get(i);
			
			tree.delete(key);
			array.remove(i);
			
			verifyTree(tree, array);
		}
		
		assert tree.size() == 0;
		
		System.out.println("InAndOut() passed");
    }

    private static void TestMixed()
    {	
		RBTreeTestable tree = new RBTreeTestable();
		ArrayList<Integer> array = new ArrayList<Integer>();
		
		for (int i = 0; i < MAX_INTERATIONS; i++)
		{
			int key = random.nextInt(Integer.MAX_VALUE);
			tree.insert(key, String.valueOf(key));
			
			if (!array.contains(key))
			{
				array.add(key);
			}

			verifyTree(tree, array);

			if (i < 2)
			{
				continue;
			}
			
		    int dcount = random.nextInt(tree.size() / 2);
		    for (int j = 0; j < dcount; j++)
		    {
		    	int element = random.nextInt(array.size());
		    	int dkey = array.get(element);
		    	
		    	tree.delete(dkey);
		    	array.remove(element);
		    	
		    	verifyTree(tree, array);
		    }
		}
		
		System.out.println("TestMixed() passed");
    }
    
    private static boolean areEqual(List<Integer> list1, int[] list2)
    {
		if (list1.size() != list2.length)
		{
		    return false;
		}

		for (int i = 0; i < list1.size(); i++)
		{
		    if (list1.get(i).intValue() != list2[i])
		    {
		    	return false;
		    }
		}
		return true;
    }

	
}


class MyTree
{
	MyTree()
	{
		list = new LinkedList<Integer>();
	}
	
	public int size()
	{
		return list.size();
	}
	public void insert(int v)
	{
		list.add((Integer)v);
	}
	
	public void delete(int v)
	{
		for (Integer v2:list)
		{
			if (v == (int)v2)
			{
				list.remove(v2);
				return;
			}
		}
	}
	public int min()
	{
		int m = -1;
		for (Integer v:list)
		{
			if (m==-1 || v<=m)
			{
				m = v;
			}
		}
		return m;
	}
	public int max()
	{
		int m = -1;
		for (Integer v:list)
		{
			if (m==-1 || v>=m)
			{
				m = v;
			}
		}
		return m;
	}
	public boolean contains(int v)
	{
		for (Integer v2:list)
		{
			if (v == (int)v2)
			{
				return true;
			}
		}
		return false;
	}
	
	public int[] array()
	{
		Object[] arr1 = new Integer[list.size()]; 
		int[] arr2 = new int[list.size()];
		arr1 = list.toArray();
		for (int j=0; j<arr1.length; j++)
		{
			arr2[j] = (Integer)(arr1[j]);
		}
		return arr2;
	}
	
	private LinkedList<Integer> list;
}




class RBTreeTestable extends RBTree {

	public int insert(int k) {
		return insert(k, String.valueOf(k));
	}
	
    public int[] toArray()
    {
		int[] treeArray = new int[this.size()];
		if (null != getRoot())
		{
			toArrayAux(getRoot(), 0, treeArray);			
		}
		return treeArray;
    }

    private int toArrayAux(RBNode node, int i, int[] treeArray)
    {
		if (node.isDummy())
		{
		    return i;
		}

		i = toArrayAux(node.getLeftChild(), i, treeArray);
	
		treeArray[i] = Integer.parseInt(node.getValue());
		i++;
	
		i = toArrayAux(node.getRightChild(), i, treeArray);
	
		return i;
    }    

    public void verifyProperties()
    {
    	if (size() > 0)
    	{

    		verifyProperty1(getRoot());
			verifyProperty2(getRoot());
			// Property 3 is implicit
			verifyProperty4(getRoot());
			verifyProperty5(getRoot());    		
    	}
    }

    /**
     * All nodes are either black or red
     * 
     * @param node
     */
    private static void verifyProperty1(RBNode node)
    {
    	node.isBlack();
		
		if (node.isDummy())
		{
		    return;
		}
		verifyProperty1(node.getLeftChild());
		verifyProperty1(node.getRightChild());
    }

    /**
     * Root is black
     * @param root
     */
    private static void verifyProperty2(RBNode root)
    {
    	assert root.isBlack();
    }

    /**
     * Red rule and, childs policy and dummies
     * 
     * @param node
     */
    private static void verifyProperty4(RBNode node)
    {
    	// Dummies can't be red, so if its a dummy - exception will be raised
    	if (!node.isBlack())
		{
    		assert node.getRightChild() != null;
    		assert node.getLeftChild() != null;

    		assert node.getRightChild().isBlack();
			assert node.getLeftChild().isBlack();;
			assert node.getParent().isBlack();;
		}
    	
		if (node.isDummy())
		{
			assert node.getLeftChild() == null;
			assert node.getRightChild() == null;
		    return;
		}

		verifyProperty4(node.getLeftChild());
		verifyProperty4(node.getRightChild());
    }

    private static void verifyProperty5(RBNode root)
    {
    	verifyProperty5Helper(root, 0, -1);
    }

    private static int verifyProperty5Helper(RBNode node, int blackCount, int pathBlackCount) {
		if (node.isBlack())
		{
		    blackCount++;
		}
	
		if (node.isDummy())
		{
		    if (pathBlackCount == -1)
		    {
		    	pathBlackCount = blackCount;
		    }
		    else 
		    {
		    	assert blackCount == pathBlackCount;
		    }
		    return pathBlackCount;
		}

		pathBlackCount = verifyProperty5Helper(node.getLeftChild(), blackCount, pathBlackCount);
		pathBlackCount = verifyProperty5Helper(node.getRightChild(), blackCount, pathBlackCount);
		return pathBlackCount;
    }
    
    public void displayTreeColors()
    {
  		Stack<RBNode> globalStack = new Stack<RBNode>();
  		globalStack.push(this.getRoot());	
  		int emptyLeaf = 32;
  		boolean isRowEmpty = false;
  		
  		System.out.println("****......................................................****");
  		
  		while(isRowEmpty==false)
  		{
   
  			Stack<RBNode> localStack = new Stack<RBNode>();
  			isRowEmpty = true;
  			for(int j=0; j<emptyLeaf; j++)
  				System.out.print(' ');
  			while(globalStack.isEmpty()==false)
  			{
  				RBNode temp = globalStack.pop();
  				if(temp != null && !temp.isDummy())
  				{
  					System.out.print(temp.isBlack() ? "B" : "R");
  					localStack.push(temp.getLeftChild());
  					localStack.push(temp.getRightChild());
  					if(!temp.getLeftChild().isDummy() || !temp.getRightChild().isDummy())
  						isRowEmpty = false;
  				}
  				else
  				{
  					System.out.print("--");
  					localStack.push(null);
  					localStack.push(null);
  				}
  				for(int j=0; j<emptyLeaf*2-2; j++)
  					System.out.print(' ');
  			}
  			System.out.println();
  			emptyLeaf /= 2;
  			while(localStack.isEmpty()==false)
  				globalStack.push( localStack.pop() );
  		}
  		
  		System.out.println("****......................................................****");

    }
    
    public void displayTree()
    {
  		Stack<RBNode> globalStack = new Stack<RBNode>();
  		globalStack.push(this.getRoot());	
  		int emptyLeaf = 32;
  		boolean isRowEmpty = false;
  		
  		System.out.println("****......................................................****");
  		
  		while(isRowEmpty==false)
  		{
   
  			Stack<RBNode> localStack = new Stack<RBNode>();
  			isRowEmpty = true;
  			for(int j=0; j<emptyLeaf; j++)
  				System.out.print(' ');
  			while(globalStack.isEmpty()==false)
  			{
  				RBNode temp = globalStack.pop();
  				if(temp != null && !temp.isDummy())
  				{
  					System.out.print(temp.getValue());
  					localStack.push(temp.getLeftChild());
  					localStack.push(temp.getRightChild());
  					if(!temp.getLeftChild().isDummy() || !temp.getRightChild().isDummy())
  						isRowEmpty = false;
  				}
  				else
  				{
  					System.out.print("--");
  					localStack.push(null);
  					localStack.push(null);
  				}
  				for(int j=0; j<emptyLeaf*2-2; j++)
  					System.out.print(' ');
  			}
  			System.out.println();
  			emptyLeaf /= 2;
  			while(localStack.isEmpty()==false)
  				globalStack.push( localStack.pop() );
  		}
  		
  		System.out.println("****......................................................****");
  	} 
}