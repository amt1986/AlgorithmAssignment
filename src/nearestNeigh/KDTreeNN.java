package nearestNeigh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{

	
	private  List<Point> list = new ArrayList<Point>();

	
	private Node root;
	private Node hospitalRoot;
	private Node restRoot;
	private Node eduRoot;
	
	private int size;
	
	private class Node {
		private Point key;
		private Node parent, left, right;
		private boolean bXDim = true;
		
		public Node(Point key, Node left, Node right, boolean bXDim){
			this.key = key;
            this.left = left;
            this.right = right;
            this.bXDim = bXDim;
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
    	

    	
        Category hos = Point.parseCat("HOSPITAL");
        Category rest = Point.parseCat("RESTAURANT");
        Category edu = Point.parseCat("EDUCATION")	;	 
    	List<Point> hospitals = new ArrayList<Point>();
    	List<Point> restaurants = new ArrayList<Point>();
    	List<Point> education = new ArrayList<Point>();

    	
    	for (Point p: points ){
    		if (p.cat == hos){
    			hospitals.add(p);
    		}
    		if (p.cat == rest){
    			restaurants.add(p);
    		}
    		if (p.cat == edu){
    			education.add(p);
    		}
    	}
    	
    	hospitalRoot = buildTree(hospitals,true);
    	restRoot = buildTree(restaurants, true);
    	eduRoot = buildTree(education,true);
        root = buildTree(points, true); 

        
        
       // Point //
        
    //    S hospital -36.40 145.0 2
        
      //  Point{id=id1167, cat=HOSPITAL, lat=-36.4355438275, lon=145.032232726}


    //    Point{id=id1000, cat=HOSPITAL, lat=-36.5421357563, lon=144.931418298}
   //     Point{id=id1137, cat=HOSPITAL, lat=-36.5078246006, lon=145.204107411}
       
        Point A = new Point("id1167", hos, -36.40, 145.0);
        Point B = new Point("id1000", hos, -36.5421357563, 144.931418298);
        Point C = new Point("id1137", hos, -36.5078246006, 145.204107411);

        System.out.println("the distance is");
        System.out.println(A.distTo(B));
        System.out.println(A.distTo(C));

        
        
        
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
    		if (root2.parent != null){
    		//	System.out.println( "  and my parent is : " + root2.parent.key.id);
    		}
    		if (root2.right != null ) {
        		System.out.println("to the right    " + root2.right.key.id + "   " + root2.right.key.lat + "   "  + root2.right.key.lon + " and my parent is : " + root2.right.parent.key.id );

    		}
    		if (root2.left != null) {
        		System.out.println("to the left    " + root2.left.key.id + "   " + root2.left.key.lat + "   "  + root2.left.key.lon  + " and my parent is : " + root2.left.parent.key.id);
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
    	
    	Node currParent = new Node(points.get(medianIndex), null, null , bXDim);
    	Node leftChild = null;
    	Node rightChild = null;
    	currParent.parent = null;
    	
    	int leftIndex = medianIndex-1;
    	int rightIndex = medianIndex+1;
    	
    	
        // Check if there is a left partition (indexing starts at 0).  If so, recursively partition it
        if (medianIndex > 0) {
        	
        	List<Point> leftPartition = new ArrayList(points.subList(0, medianIndex));
        	boolean leftflip ;
        	if(bXDim == true) leftflip = false; else leftflip = true;
            leftChild = buildTree(leftPartition, leftflip); 
            leftChild.parent = currParent;

        }
        // check if there is a right partition 
        if (medianIndex < points.size()-1) {
        	List<Point> rightPartition = new ArrayList(points.subList(rightIndex, points.size()));
        	System.out.println(rightPartition.toString() + " with size : " + rightPartition.size());
        	boolean rightflip;
        	if(bXDim == true) rightflip = false; else rightflip = true;
            rightChild = buildTree(rightPartition,rightflip); 
            rightChild.parent = currParent;


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

        Node node = root;

    	if ( searchTerm.cat == Point.parseCat("HOSPITAL")){
    		node = hospitalRoot;
    	}
    	if ( searchTerm.cat == Point.parseCat("RESTAURANT")){
    		node = restRoot;
    	}
    	if ( searchTerm.cat == Point.parseCat("EDUCATION")){
    		node = eduRoot;
    	}
        TreeSet<Node> results = new TreeSet<Node>(new DistanceComparator(searchTerm));

        
        Node close = getClosestPoint(searchTerm, node, true);
     //   System.out.println("Result  " + close.key.id);
        Point nearestPoint = close.key;
        System.out.println("The closest point is : ");
        System.out.println(nearestPoint.toString());
        
        if (close != null) {
            // Used to not re-examine nodes
            Set<Node> checked = new HashSet<Node>();

            // Go up the tree, looking for better solutions
            node = close;
            while (node != null) {
                // Search node
//                if(results.size() > 0){
//                	for (Node result : results){
//                		if (result.key.cat != searchTerm.cat){
//                			results.remove(result);
//                		}
//                		
//                	}
////                	Node lastNode = results.last();
////                	if (lastNode.key.cat != searchTerm.cat){
////                		results.remove(lastNode);
////                		results.
////                	}
//                }
                searchNode(searchTerm, node, k, results, checked);
                node = node.parent;
            }
        }


        
        
        List<Point> neighbors = new ArrayList<Point>();
        
        // Load up the collection of the results
       // Collection<T> collection = new ArrayList<T>(K);
        for (Node kdNode : results)
            neighbors.add(kdNode.key);
        
   // 	System.out.println("Size is : " + size);
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
    
    
    private static final  void searchNode(Point value, Node node, int K, TreeSet<Node> results, Set<Node> examined) {
        examined.add(node);

//        if(results.size() > 0){
//        	if (results.last().key.cat != value.cat){
//        		results.remove(results.last());
//        	}
//        }
        
        // Search node
        Node lastNode = null;
        Double lastDistance = Double.MAX_VALUE;
        if (results.size() > 0) {
            lastNode = results.last();
            lastDistance = lastNode.key.distTo(value);
        }
        Double nodeDistance = node.key.distTo(value);
        if (nodeDistance.compareTo(lastDistance) < 0) {
            if (results.size() == K && lastNode != null){
                results.remove(lastNode);
            }
//            if (node.key.cat == value.cat){
//                results.add(node);
//            }
            results.add(node);
        } else if (nodeDistance.equals(lastDistance)) {
//            if (node.key.cat == value.cat){
//                results.add(node);
//            }
            results.add(node);
        } else if (results.size() < K) {
//            if (node.key.cat == value.cat){
//                results.add(node);
//            }
            results.add(node);
        }
        if (results.size() > 0){
            lastNode = results.last();
            lastDistance = lastNode.key.distTo(value);

        }

        boolean xAxis = node.bXDim;
        Node lesser = node.left;
        Node greater = node.right;

        // Search children branches, if axis aligned distance is less than
        // current distance
        if (lesser != null && !examined.contains(lesser)) {
            examined.add(lesser);

            double nodePoint = Double.MIN_VALUE;
            double valuePlusDistance = Double.MIN_VALUE;
            if (xAxis) {
                nodePoint = node.key.lat;
                valuePlusDistance = value.lat - lastDistance;
            } else if (!xAxis) {
                nodePoint = node.key.lon;
                valuePlusDistance = value.lon - lastDistance;
            } 
            boolean lineIntersectsCube = ((valuePlusDistance <= nodePoint) ? true : false);

            // Continue down lesser branch
            if (lineIntersectsCube)
                searchNode(value, lesser, K, results, examined);
        }
        if (greater != null && !examined.contains(greater)) {
            examined.add(greater);

            double nodePoint = Double.MIN_VALUE;
            double valuePlusDistance = Double.MIN_VALUE;
            if (xAxis) {
                nodePoint = node.key.lat;
                valuePlusDistance = value.lat + lastDistance;
            } else if (!xAxis) {
                nodePoint = node.key.lon;
                valuePlusDistance = value.lon + lastDistance;
            } 
            boolean lineIntersectsCube = ((valuePlusDistance >= nodePoint) ? true : false);

            // Continue down greater branch
            if (lineIntersectsCube)
                searchNode(value, greater, K, results, examined);
        }
    }
    
    
    
    

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
    	Node addRoot = root;
    	if ( point.cat == Point.parseCat("HOSPITAL")){
    		addRoot = hospitalRoot;
    	}
    	if ( point.cat == Point.parseCat("RESTAURANT")){
    		addRoot = restRoot;
    	}
    	if ( point.cat == Point.parseCat("EDUCATION")){
    		addRoot = eduRoot;
    	}
    	
    	Node n = getTarget(addRoot,point,true);
    	if ( n != null ){
    		return false;
    	}
    	
    	
         root = addNode(addRoot, point, true);
        
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
    		return new Node(point, null , null , bXDim);
    	}
    	if (bXDim){
    		if (point.lat > node.key.lat) {
    			node.right = addNode(node.right, point, false);
    			node.right.parent = node;
    		}
    		else if(point.lat < node.key.lat){
    			node.left = addNode(node.left, point, false);
    			node.left.parent = node;
    		}
    	}
    	if (!bXDim){
    		if (point.lon > node.key.lon) {
    			node.right = addNode(node.right, point, true);
    			node.right.parent = node;
    		}
    		else if(point.lon < node.key.lon) {
    			node.left = addNode(node.left, point, true);
    			node.left.parent = node;
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
    	Node deleteRoot = root;
    	if ( point.cat == Point.parseCat("HOSPITAL")){
    		deleteRoot = hospitalRoot;
    	}
    	if ( point.cat == Point.parseCat("RESTAURANT")){
    		deleteRoot = restRoot;
    	}
    	if ( point.cat == Point.parseCat("EDUCATION")){
    		deleteRoot = eduRoot;
    	}
    	
    	System.out.println("Delete Method Starting");
    	Node toDelete = getTarget(deleteRoot,point,true);
    	
    	
    	if(toDelete == null){
    		System.out.println("Point Not Available");
    		return false;
    	}

    	if (toDelete.right == null && toDelete.left == null){
    		if (toDelete.parent.left != null){
        		if (toDelete.key.equals(toDelete.parent.left.key)){
        			toDelete.parent.left = null;
        			return true;
        		}
    		}

    		else if (toDelete.key.equals(toDelete.parent.right.key)){
    			toDelete.parent.right = null;
    			return true;
    		}
    	}
    	
    	List<Point> children = new ArrayList<Point>();
    	children = getChildren(toDelete);

    	System.out.println(children.toString());
    	Node replace = buildTree(children,toDelete.bXDim);
    	
    	System.out.println("Finished building new tree");

    	if (toDelete.key.equals(toDelete.parent.left.key)){
        	replace.parent = toDelete.parent;
    		toDelete.parent.left = replace;
    	}
    	else if(toDelete.key.equals(toDelete.parent.right.key)){
    		toDelete.parent.right = replace;
    		toDelete.parent.right = replace;
    	}
    	
    	
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
    	
    	if(root == null) return null;
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

    	Node n = getTarget(root, point, true);
    	
    	if ( n == null) {
    		return false;
    	}
//        Node node = root;
//        while (node != null) {
//            if (node.key.lat == point.lat && node.key.lon == point.lon) {
//                return true;
//            } else if ((!node.isHorizontal && node.key.lat >= point.lat) || (node.isHorizontal && node.key.lon >= point.lon)) {
//                node = node.left;
//            } else {
//                node = node.right;
//            }
//        }
        return true;
    }

    
    
    
    protected static class DistanceComparator implements Comparator<Node> {

        private final Point point;

        public DistanceComparator(Point point) {
            this.point = point;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(Node o1, Node o2) {
            Double d1 = point.distTo(o1.key);
            Double d2 = point.distTo(o2.key);
            if (d1.compareTo(d2) < 0)
                return -1;
            else if (d2.compareTo(d1) < 0)
                return 1;
          //  return o1.id.compareTo(o2.id);
          return 0;
        }
    }
    
    
}
