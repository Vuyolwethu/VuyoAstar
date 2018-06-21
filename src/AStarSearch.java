import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
  This class contain the methods to find the best path
*/
public class AStarSearch {


  /**
    A simple priority list, also called a priority queue.
    Objects in the list are ordered by their priority,
    determined by the object's Comparable interface.
    The highest priority item is first in the list.
  */
  public static class PriorityList extends LinkedList {

    public void add(Comparable object) {
      for (int i=0; i<size(); i++) {
        if (object.compareTo(get(i)) <= 0) {
          add(i, object);
          return;
        }
      }
      addLast(object);
    }
  }


  /**
    Construct the path, not including the start node.
  */
  protected List constructPath(AStarNode node) {
    LinkedList path = new LinkedList();
    while (node.pathParent != null) {
      path.addFirst(node);
      node = node.pathParent;
    }
    return path;
  }


  /**
    Find the path from the start node to the end node. A list
    of AStarNodes is returned, or null if the path is not
    found. 
  */
  public List findPath(AStarNode startNode, AStarNode goalNode) {

    PriorityList openList = new PriorityList();
    LinkedList closedList = new LinkedList();

    startNode.costFromStart = 0;
    startNode.estimatedCostToGoal =
    startNode.getEstimatedCost(goalNode);
    startNode.pathParent = null;
    openList.add(startNode);

    while (!openList.isEmpty()) {
      AStarNode node = (AStarNode)openList.removeFirst();
      if (node == goalNode) {
        // construct the path from start to goal
        return constructPath(goalNode);
      }

      List neighbors = node.getNeighbors();
      for (int i=0; i<neighbors.size(); i++) {
        AStarNode neighborNode =
          (AStarNode)neighbors.get(i);
        boolean isOpen = openList.contains(neighborNode);
        boolean isClosed =
          closedList.contains(neighborNode);
        float costFromStart = node.costFromStart +
          node.getCost(neighborNode);

        // check if the neighbor node has not been
        // traversed or if a shorter path to this
        // neighbor node is found.
        if ((!isOpen && !isClosed) ||
          costFromStart < neighborNode.costFromStart)
        {
          neighborNode.pathParent = node;
          neighborNode.costFromStart = costFromStart;
          neighborNode.estimatedCostToGoal =
            neighborNode.getEstimatedCost(goalNode);
          if (isClosed) {
            closedList.remove(neighborNode);
          }
          if (!isOpen) {
            openList.add(neighborNode);
          }
        }
      }
      closedList.add(node);
    }

    // no path found
    return null;
  }
  public static void main(String[] args) throws IOException{ 
	  /** Reads the map from a text file residing on the following path*/
	  String path ="C:\\Users\\a232832\\eclipse-workspace\\VuyoAStar\\map.txt"; 
	  File file = new File(path);
	  
	  String grid[][] = new String[20][20]; // Initializes the grid 
	  int i=0,j=0;
			  
	  BufferedReader br = new BufferedReader(new FileReader(file));
	  Map<String, String> originalMap = new HashMap<String, String>();
	  String st = "";
	  String line = br.readLine();
	  
	  /** Read line by line from the buffer*/
	  while ((line = br.readLine())  != null) {
		st += br.readLine();
		j=0;
		String delim = " ";
		String tokens[] = line.split(delim);
		for(String a : tokens) {
			 grid[i][j] = a;
			 j++;
		}
		i++;
	  }
      for(i=0;i<20;i++)
      {
          for( j=0;j<20;j++)
          {
              System.out.print("  "+grid[i][j]);
          }
      }
	  br.close();
	  //System.out.println("The original map is "+ st +"");
  }
}