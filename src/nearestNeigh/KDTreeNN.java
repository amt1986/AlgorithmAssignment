package nearestNeigh;

import java.util.ArrayList;
import java.util.Collections;
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
    	this.list = points;
    	for ( int i = 0; i< list.size();i++){
            root = put(root, list.get(i), list.get(i), false);
            
         //   Collections.sort(list, c);
    	}
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        Point nearestPoint = root.key;
        List<Point> neighbors = new ArrayList<Point>();
        neighbors.add(nearestPoint);
        
    	
    	
        return neighbors;
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
         root = put(root, point, point, false);

    	
        return true;
    }
    
    
    
    private Node put(Node node, Point key, Point val, boolean isHorizontal) {
        if (node == null) {
            size++;
            return new Node(key, val, null, null, isHorizontal);
        }
        
        if (node.key.equals(key)) {
            node.val = val;
        } else if ((!node.isHorizontal && node.key.lon >= key.lon || (node.isHorizontal && node.key.lat >= key.lat))) {
            node.left = put(node.left, key, val, !isHorizontal);
        } else {
            node.right = put(node.right, key, val, !isHorizontal);
        }
        
        return node;
    }
    

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
        return false;
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
