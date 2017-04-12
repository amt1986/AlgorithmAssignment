package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{

	private  List<Point> list = new ArrayList<Point>();
	
	
	
    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.
    	this.list = points;

    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
        List<Point> neighbors = new ArrayList<Point>();
        List<Point> tempList = new ArrayList(list);

        
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

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
    	if (isPointIn(point)){
    		return false;
    	}
    	return list.add(point);
    }

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
    	if (!isPointIn(point)){
    		return false;
    	}
    	return list.remove(point);
    }

    @Override
    public boolean isPointIn(Point point) {
        // To be implemented.
    	return list.contains(point);
    }

}
