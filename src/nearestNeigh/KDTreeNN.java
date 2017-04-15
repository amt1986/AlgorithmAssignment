package nearestNeigh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{

	
	private  List<Point> list = new ArrayList<Point>();

	
	private Node root;
	private int size;
	
	private class Node {
		private Point key;
		private Point val;
		private Node left, right;
		private boolean isHorizontal = false;
		
		public Node(Point key, Point val, Node left, Node right, boolean isHorizontal){
			this.key = key;
			this.val = val;
            this.left = left;
            this.right = right;
            this.isHorizontal = isHorizontal;
		}
	}
	
	private class PointComparator implements Comparator<Point> {

		public static final int COMPARE_BY_X = 0;
		public static final int COMPARE_BY_Y = 1;

		private int compare_mode = COMPARE_BY_X;

		public PointComparator() {
		}

		public PointComparator(int compare_mode) {
		    this.compare_mode = compare_mode;
		}

		@Override
		public int compare(Point o1, Point o2) {
            if(compare_mode == 0){
    			if (o1.lat < o2.lat)
                    return -1;
                if (o1.lat > o2.lat)
                    return 1;
                return 0;
            }
            if(compare_mode == 1){
    			if (o1.lon < o2.lon)
                    return -1;
                if (o1.lon > o2.lon)
                    return 1;
                return 0;
            }
            return 0;

		}

//		@Override
//		public int compare(Point p1, Point p2) {
//		    switch (compare_mode) {
//		    case COMPARE_BY_Y:
//		        return p1.lat.compareTo(p2.lat);
//		    default:
//		        return p1.getInputRecipeName().compareTo(p2.getInputRecipeName());
//		    }
//		}
	}
	
    // construct an empty set of points
	public KDTreeNN() {
		root = null;
		size = 0;
	}
	
	 // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points in the set
    public int size() {
       return size;
    }
    

    
    
    
    
    
    
    
    
    
    
    
    
	
    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.
    	//this.list = points;
    	//for ( int i = 0; i< list.size();i++){
         //   root = put(root, list.get(i), list.get(i), true);
            
         //   Collections.sort(list, c);
    	//}
    	
        root = buildTree(points, true); 

    	printNode(root);
		System.out.println("Right Node");

    	printNode(root.right);
		System.out.println("Left Node");

    	printNode(root.left);
		System.out.println("Next level right Node");

    	printNode(root.right.right);
    	printNode(root.right.left);

		System.out.println("next level Left Node");
    	printNode(root.left.right);
    	printNode(root.left.left);
    	
    }

    private void printNode(Node root2) {
    	if(root2 != null){
    		System.out.println(root2.key.id + "   " + root2.key.lat + "   "  + root2.key.lon);
    		if (root2.right != null ) {
        		System.out.println("to the right    " + root2.right.key.id + "   " + root2.right.key.lat + "   "  + root2.right.key.lon);

    		}
    		if (root2.left != null) {
        		System.out.println("to the left    " + root2.left.key.id + "   " + root2.left.key.lat + "   "  + root2.left.key.lon);
    		}
    	
    	
    	
    	}
    	
	}

	private Node buildTree(List<Point> points, boolean bXDim) {
		// TODO Auto-generated method stub
    	
      //  List<Point> sortedPoints = sort(points, bXDim); 
    	if (points.size() == 0) return null;

    	
    	
    	int dim = 0;
    	if (bXDim) dim = 0;
    	if (!bXDim) dim = 1;
    	
    	
    	Collections.sort(points, new PointComparator(dim));
    	
    	int medianIndex = points.size() / 2;
    	System.out.println("the median is :  "+ medianIndex);
    	
    	Node currParent = new Node(points.get(medianIndex),null, null, null , true);
    	Node leftChild = null;
    	Node rightChild = null;
    	
    	int leftIndex = medianIndex-1;
    	int rightIndex = medianIndex+1;
    	
    	
        // Check if there is a left partition (indexing starts at 0).  If so, recursively partition it
        if (medianIndex > 0) {
        	
        	List<Point> leftPartition = new ArrayList(points.subList(0, medianIndex));
        	boolean leftflip ;
        	if(bXDim == true) leftflip = false; else leftflip = true;
            leftChild = buildTree(leftPartition, leftflip); 

        }
        // check if there is a right partition 
        if (medianIndex < points.size()-1) {
        	List<Point> rightPartition = new ArrayList(points.subList(rightIndex, points.size()));
        	System.out.println(rightPartition.toString() + " with size : " + rightPartition.size());
        	boolean rightflip;
        	if(bXDim == true) rightflip = false; else rightflip = true;
            rightChild = buildTree(rightPartition,rightflip); 

        }
//    	if (points.size() == 1) {
//    		currParent =  new Node(points.get(0), null, null , null ,true);
//    	}
        
        currParent.left = (leftChild); 
        currParent.right = (rightChild); 
     
        return currParent; 
        
        
    	
  	}

    
    
    
    
	@Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
    	
    //	System.out.println(root.key.id);
    	//System.out.println(root.right.key.id);
    	//System.out.println(root.left.key.id);

    	
        
        Node close = getClosestPoint(searchTerm, root, true);
     //   System.out.println("Result  " + close.key.id);
        Point nearestPoint = close.key;

        
        
        List<Point> neighbors = new ArrayList<Point>();
        neighbors.add(nearestPoint);
        
   // 	System.out.println("Size is : " + size);
    	System.out.println(neighbors.toString());
        return neighbors;
    }
    
    public Node getClosestPoint(Point p, Node n, boolean xDim){
    	if (n == null) return null;
    	Node Closest = n;

    	//if (p.equals(n.key)) return n;
    	if (xDim){
        	if (p.lat > n.key.lat) {
        		if (n.right == null) return n;
            	System.out.println("going right from x " + n.key.id );
        		Closest = getClosestPoint(p, n.right, false);
        	}
        	if (p.lat < n.key.lat){
        		if (n.left == null) return n;
            	System.out.println("going left from x " + n.key.id);

        		Closest = getClosestPoint(p, n.left, false);
        	}
    	}
    	else if(!xDim){
        	if (p.lon > n.key.lon ){
        		if (n.right == null) return n;
            	System.out.println("going right from y " + n.key.id);
        		Closest = getClosestPoint(p,n.right, true);
        	}
        	if (p.lon < n.key.lon) {
        		if (n.left == null) return n;
            	System.out.println("going left from x " + n.key.id);
        		Closest = getClosestPoint(p,n.left, true);
        	}
    	}


    
    	return Closest;
    	
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
         root = addNode(root, point, true);
        
        System.out.println("Printing Tree after add"); 
     	printNode(root);
 		System.out.println("Right Node");

     	printNode(root.right);
 		System.out.println("Left Node");

     	printNode(root.left);
 		System.out.println("Next level right Node");

     	printNode(root.right.right);
     	printNode(root.right.left);

 		System.out.println("next level Left Node");
     	printNode(root.left.right);
     	printNode(root.left.left);
    	
        return true;
    }
    
    
    private Node addNode(Node node, Point point, boolean bXDim){
    	
    	if (node == null) {
    		return new Node(point, null, null , null , true);
    	}
    	if (bXDim){
    		if (point.lat > node.key.lat) {
    			node.right = addNode(node.right, point, false);
    		}
    		else if(point.lat < node.key.lat){
    			node.left = addNode(node.left, point, false);
    		}
    	}
    	if (!bXDim){
    		if (point.lon > node.key.lon) {
    			node.right = addNode(node.right, point, true);
    		}
    		else if(point.lon < node.key.lon) {
    			node.left = addNode(node.left, point, true);
    		}
    	}
    	
    	return node;
    	
    }
//    private Node put(Node node, Point key, Point val, boolean isHorizontal) {
//        if (node == null) {
//            size++;
//            return new Node(key, val, null, null, isHorizontal);
//        }
//        
//        if (node.key.equals(key)) {
//            node.val = val;
//        } else if ((!node.isHorizontal && node.key.lon >= key.lon || (node.isHorizontal && node.key.lat >= key.lat))) {
//            node.left = put(node.left, key, val, !isHorizontal);
//        } else {
//            node.right = put(node.right, key, val, !isHorizontal);
//        }
//        
//        return node;
//    }
    

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
    	Node toDelete = getTarget(root,point,true);

    	List<Point> children = new ArrayList<Point>();
    	children = getChildren(toDelete);

    	Node replace = buildTree(children,false);
    	System.out.println(replace.right.key.toString());
    	System.out.println(toDelete.right.key.toString());

    	toDelete = replace;
    	System.out.println(toDelete.right.key.toString());
    	
        System.out.println("Printing Tree after delete"); 
     	printNode(root);
 		System.out.println("Right Node");

     	printNode(root.right);
 		System.out.println("Left Node");

     	printNode(root.left);
 		System.out.println("Next level right Node");

     	printNode(root.right.right);
     	printNode(root.right.left);

 		System.out.println("next level Left Node");
     	printNode(root.left.right);
     	printNode(root.left.left);
     	
     	
        return true;
       
    }
    
    public List<Point> getChildren(Node n){
    	List<Point> children = new ArrayList<Point>();
    	if (n.right != null){
    		children.add(n.right.key);
    		children.addAll(getChildren(n.right));
    	}
    	if (n.left != null){
    		children.add(n.left.key);
    		children.addAll(getChildren(n.left));
    	}
    	
    	return children;
    }
    
    public Node getTarget(Node root, Point point, boolean bXDim){
    	Node target = null;
    	if (root.key.equals(point)){
    		return root;
    	}
    	if (bXDim){
    		if (point.lat > root.key.lat) {
    			target = getTarget(root.right, point, false);
    		}
    		else if(point.lat < root.key.lat){
    			target = getTarget(root.left, point, false);
    		}
    	}
    	if (!bXDim){
    		if (point.lon > root.key.lon) {
    			target = getTarget(root.right, point, true);
    		}
    		else if(point.lon < root.key.lon) {
    			target = getTarget(root.left, point, true);
    		}
    	}
    	
    	
    	return target;
    }

    @Override
    public boolean isPointIn(Point point) {

        Node node = root;
        while (node != null) {
            if (node.key.lat == point.lat && node.key.lon == point.lon) {
                return true;
            } else if ((!node.isHorizontal && node.key.lat >= point.lat) || (node.isHorizontal && node.key.lon >= point.lon)) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    
    
}
