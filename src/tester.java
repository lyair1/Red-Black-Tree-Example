import java.util.Arrays;
import java.util.Random;
public class tester {
	public static void main(String[] args){
		RBTree tree = new RBTree();
		System.out.println(tree.empty());
		
		int[] arr = new int[10000];
		for(int i=0; i<10000;i++){
			Random rand = new Random();
			int  n = rand.nextInt(99) + 1;
			//System.out.println("Putting "+n+":");
			tree.insert(n, "" + n);
			arr[i] = n;
		}

		while(tree.size() > 0){
			Random rand = new Random();
			int  n = rand.nextInt(99) + 1;
			//System.out.println("Deleting "+n+":");
			tree.delete(n);
			//tree.print();
		}
		
//		for(int n : arr){
//			System.out.println("Deleting "+n+":");
//			tree.delete(n);
//		}

		System.out.println(tree.empty());
		System.out.println(tree.size());
		System.out.println(tree.search(1));
		System.out.println(tree.search(2));
		System.out.println(tree.search(15));
		System.out.println(tree.search(199));
		
		System.out.println(tree.min());
		System.out.println(tree.max());
		
		int[] array = tree.keysToArray();
		System.out.println(Arrays.toString(array));
		String[] array2 = tree.valuesToArray();
		System.out.println(Arrays.toString(array2));
		//tree.print();
		
		//experiment:
		for(int i=1; i<11;i++){
			System.out.println(i);
			int insertCounter = 0;
			int[] array1 = new int[i*10000];
			while(tree.size() < i*10000){
				Random rand = new Random();
				int  n = rand.nextInt(i*100000) + 1;
				array1[tree.size()] = n;
				insertCounter += tree.insert(n, ""+n);
			}
			
			int deleteCounter=0;
			while(tree.size()>0){
				String x = tree.max();
				deleteCounter += tree.delete(Integer.parseInt(x));
			}
			
			System.out.println("Color Changes For Experiment: "+array1.length +" Delete: "+ deleteCounter +" Insert: "+ insertCounter);
		}
	}
}