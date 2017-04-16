package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is implemented as part of RMIT Algorithm assignment.
 * 
 * Students : Ali AlTuraish and Sultan AlRawahi
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{

	private  List<Point> list = new ArrayList<Point>();
	
	
	/**
	 * This method to store the points in an arraylist
	 */
    @Override
    public void buildIndex(List<Point> points) {
    	this.list = points;

    }

    /**
     * this method is used to search for the nearest neighbors
     */
    @Override
    public List<Point> search(Point searchTerm, int k) {

    	List<Point> neighbors = new ArrayList<Point>();
        List<Point> tempList = new ArrayList(list);

        // brute force checking
    	for (int i = 0; i<k; i++){
    		
            Point nearestNeighbor = null;
            double nearestDistance = Double.MAX_VALUE;

            for (Point neighbor : tempList) {
            	if ( searchTerm.cat != neighbor.cat) continue;
                double distance = searchTerm.distTo(neighbor);
                if (distance < nearestDistance) {
                    nearestNeighbor = neighbor;
                    nearestDistance = distance;
                }
    	}
            neighbors.add(nearestNeighbor);
            tempList.remove(nearestNeighbor);

    	
}
          
        return neighbors;
    }

    /**
     * this is to add a new point to the structure
     */
    @Override
    public boolean addPoint(Point point) {
    	if (isPointIn(point)){
    		return false;
    	}
    	return list.add(point);
    }

    /**
     * this is to delete a point from the structure
     */
    @Override
    public boolean deletePoint(Point point) {
    	if (!isPointIn(point)){
    		return false;
    	}
    	return list.remove(point);
    }

    /**
     * this is to check that a point exists.
     */
    @Override
    public boolean isPointIn(Point point) {
    	return list.contains(point);
    }

}
