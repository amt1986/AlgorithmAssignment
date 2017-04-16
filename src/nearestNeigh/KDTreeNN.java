package nearestNeigh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is implemented as part of RMIT Algorithm Assignment.
 * 
 * Students: Ali AlTuraish, Sultan AlRawahi
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{

	private Node root;
	private Node hospitalRoot;
	private Node restRoot;
	private Node eduRoot;
	
	
	/**
	 * This is an Inner Class used by the KDTREE
	 * The class is a simple Node model
	 * 
	 * @author Ali
	 *
	 */
	private class Node {
		private Point key;
		private Node parent, left, right;
		private boolean bXDim = true;
		/**
		 * This is the constructor for the Node
		 * @param key is the Point value of the node
		 * @param left is the left child node
		 * @param right is the right child node
		 * @param bXDim is a boolean for splitting with x or y axis
		 */
		public Node(Point key, Node left, Node right, boolean bXDim){
			this.key = key;
            this.left = left;
            this.right = right;
            this.bXDim = bXDim;
		}
	}
	
	/**
	 * This an Inner Class for comparing the Points
	 * It is used for sorting the points when building a new KD-tree
	 * @author Ali
	 *
	 */
	private class PointComparator implements Comparator<Point> {
		
		// this variable to select an axix to sort on 
		// 0 for X and 1 for Y
		private int compare_mode = 0;

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
	}
	

    
	/**
	 * This method will take the points as an argument 
	 * This is to start setting up the trees
	 * In our case 3 trees will be build for each category
	 */
    @Override
    public void buildIndex(List<Point> points) {


        Category hos = Point.parseCat("HOSPITAL");
        Category rest = Point.parseCat("RESTAURANT");
        Category edu = Point.parseCat("EDUCATION")	;	 
    	List<Point> hospitals = new ArrayList<Point>();
    	List<Point> restaurants = new ArrayList<Point>();
    	List<Point> education = new ArrayList<Point>();

    	// splitting the points based on category
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
    	
    	// building a tree for each category;
    	hospitalRoot = buildTree(hospitals,true);
    	restRoot = buildTree(restaurants, true);
    	eduRoot = buildTree(education,true);
        root = buildTree(points, true);
        
    }

    /**
     * This is a method to help buildIndex method to build the trees
     * @param points is the list of points to be converted to a tree
     * @param bXDim  this is the starting dimension for building the tree
     * @return the method will return the root node of the tree
     */
	private Node buildTree(List<Point> points, boolean bXDim) {
    	
    	if (points.size() == 0) return null;
    	// 0 for splitting with X and 1 for Y
    	int dim = 0;
    	if (bXDim) dim = 0;
    	if (!bXDim) dim = 1;
    	
    	// sorting the points
    	Collections.sort(points, new PointComparator(dim));
    	
    	int medianIndex = points.size() / 2;
    	
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
        	boolean rightflip;
        	if(bXDim == true) rightflip = false; else rightflip = true;
            rightChild = buildTree(rightPartition,rightflip); 
            rightChild.parent = currParent;
        }

        currParent.left = (leftChild); 
        currParent.right = (rightChild); 
     
        
        return currParent; 
       
  	}

    
    /**
     * This method is to search for k nearest neighbors.
     * The method takes the point to search upon and k the number of neighbors needed
     */
	@Override
    public List<Point> search(Point searchTerm, int k) {
    	
		// setting the tree root to start searching for the closest point
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

        // getting the closest point
        Node close = getClosestPoint(searchTerm, node, true);
        
        if (close != null) {

        	Set<Node> checked = new HashSet<Node>();

            // Traversing back to check for points with less distance
            node = close;
            while (node != null) {
                searchNode(searchTerm, node, k, results, checked);
                node = node.parent;
            }
        }
        
        // adding the neighbors to an array to return it
        List<Point> neighbors = new ArrayList<Point>();
        for (Node n : results)
            neighbors.add(n.key);
        
        return neighbors;
    }
    
	/**
	 * This method is used to get the Closest point to a searchTerm
	 * @param p is the search term point
	 * @param n is the root node to start searching
	 * @param xDim is the dimension to start checking upon
	 * @return this will return the node with the closest point
	 */
    public Node getClosestPoint(Point p, Node n, boolean xDim){
    	if (n == null) return null;
    	Node Closest = n;

    	if (p.equals(n.key)) return n;
    	if (xDim){
        	if (p.lat > n.key.lat) {
        		if (n.right == null) return n;
        		Closest = getClosestPoint(p, n.right, false);
        	}
        	if (p.lat < n.key.lat){
        		if (n.left == null) return n;
        		Closest = getClosestPoint(p, n.left, false);
        	}
    	}
    	else if(!xDim){
        	if (p.lon > n.key.lon ){
        		if (n.right == null) return n;
        		Closest = getClosestPoint(p,n.right, true);
        	}
        	if (p.lon < n.key.lon) {
        		if (n.left == null) return n;
        		Closest = getClosestPoint(p,n.left, true);
        	}
    	}


    
    	return Closest;
    	
    }
    
    /**
     * This method is used for traversing back the visited nodes after getting the closest point
     * 
     * This method is inspired by the author Justin Wetherell from http://programtalk.com/
     * 
     * 
     * @param value is the searchTerm point
     * @param node is the closest point node
     * @param K the the number of neighbors needed
     * @param results is the list k neighbors as nodes
     * @param examined the the list of checked nodes
     */
    private static final  void searchNode(Point value, Node node, int K, TreeSet<Node> results, Set<Node> examined) {
        examined.add(node);

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
            results.add(node);
        } else if (nodeDistance.equals(lastDistance)) {
            results.add(node);
        } else if (results.size() < K) {
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

            // Continue to the left branch
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

            // Continue to the right branch
            if (lineIntersectsCube)
                searchNode(value, greater, K, results, examined);
        }
    }
    
    
    
    
    /**
     * This method is used to add a new point to a KDtree
     */
    @Override
    public boolean addPoint(Point point) {
        // setting the correct root.
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
    	
    	// checking if the point is already exist
    	Node n = getTarget(addRoot,point,true);
    	if ( n != null ){
    		return false;
    	}
    	
    	// adding the new node to the tree
        addRoot = addNode(addRoot, point, true);
         
        return true;
    }
    
    /**
     * this method is used to help adding a new point to a KDtree
     * @param node is root node of the tree to add to
     * @param point is the point to be added
     * @param bXDim is the starting dimension check
     * @return 
     */
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

    
    /**
     * This method is to delete an existing point
     */
    @Override
    public boolean deletePoint(Point point) {
        // setting the correct root node.
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
    	
    	// getting the node to delete
    	Node toDelete = getTarget(deleteRoot,point,true);
    	
    	// return false if the point is not available
    	if(toDelete == null){
    		return false;
    	}

    	// deleting if the node is a leaf node
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
    	
    	// deleting if the node is not a leaf by creating a subtree
    	List<Point> children = new ArrayList<Point>();
    	children = getChildren(toDelete);

    	Node replace = buildTree(children,toDelete.bXDim);
    	

    	if (toDelete.key.equals(toDelete.parent.left.key)){
        	replace.parent = toDelete.parent;
    		toDelete.parent.left = replace;
    	}
    	else if(toDelete.key.equals(toDelete.parent.right.key)){
    		toDelete.parent.right = replace;
    		toDelete.parent.right = replace;
    	}
     	
        return true;
    }
    
    /**
     * this method is to get all children points of a target node
     * @param n the node to get the children for
     * @return a list of the points
     */
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
    
    /**
     * this method is used to get a certain node from the tree
     * @param root is the root node of the tree
     * @param point is the point to get 
     * @param bXDim  the dimension to start checking upon
     * @return the target node 
     */
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

    /**
     * This method is used to check if a point exist in the tree
     */
    @Override
    public boolean isPointIn(Point point) {
    	// selecting the tree to check
    	Node treeRoot = root;
    	if ( point.cat == Point.parseCat("HOSPITAL")){
    		treeRoot = hospitalRoot;
    	}
    	if ( point.cat == Point.parseCat("RESTAURANT")){
    		treeRoot = restRoot;
    	}
    	if ( point.cat == Point.parseCat("EDUCATION")){
    		treeRoot = eduRoot;
    	}
    	
    	Node n = getTarget(treeRoot, point, true);
    	
    	// if the point is not available return false
    	if ( n == null) {
    		return false;
    	}

        return true;
    }

    
    
    /**
     *  this is an inner class used to sort the nearest neighbors results
     *  the results will be sorted based on the distance
     * @author Ali
     *
     */
    protected static class DistanceComparator implements Comparator<Node> {

        private final Point point;

        public DistanceComparator(Point point) {
            this.point = point;
        }

        @Override
        public int compare(Node o1, Node o2) {
            Double d1 = point.distTo(o1.key);
            Double d2 = point.distTo(o2.key);
            if (d1.compareTo(d2) < 0)
                return -1;
            else if (d2.compareTo(d1) < 0)
                return 1;
          return 0;
        }
    }
    
    
}
