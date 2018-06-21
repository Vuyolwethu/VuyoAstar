import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
  This class contain the methods to find the best path
*/
public class AStarSearch {


  /**
    This function implements a priority queue.
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
        /**construct the path from start to goal*/
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

        /**check if the neighbor node has not been
         traversed or if a shorter path to this
         neighbor node is found.*/
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

    /** Returns a null when there is not path found to the goal state*/
    return null;
  }
  
  /** A method that prints the grid for visibility*/
  public static void printRow(char[][]grid) {
      for (int i = 0; i < grid[0].length; i++) {
          for (int j = 0; j < grid.length; j++) {
              System.out.print(grid[j][i] + " ");
          }
          System.out.print("\n");
      }
      System.out.println();
  } 
  
  /** Main function for the A* search algorithm*/
  public static void main(String[] args) throws IOException{ 
	  /** Reads the map from a text file residing on the following path*/
	  String path ="C:\\Users\\a232832\\eclipse-workspace\\VuyoAStar\\map.txt"; 
      
      File f = new File(""+path);     
      Scanner in = new Scanner(f);
      int WIDTH = 20;
      int HEIGHT = 10;
      char[][] grid = new char[WIDTH][HEIGHT];
      try {
    	  /** Creates the input stream*/
          BufferedReader newBuf = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
          String line ="";
          char startNode;
          char endNode;
          
          /** Sanitizes the input text*/
          newBuf.mark(1);
          if (newBuf.read() != 0xFEFF) {
        	  newBuf.reset();
          }
          
    	  System.out.println("\nThe original map is:");
          /**add elements to the grid array */
          for(int y = 0;y < HEIGHT;y++){
        	  Scanner tempLine = new Scanner(newBuf.readLine());
              for(int x = 0; x < WIDTH;x++){
            	  char c = tempLine.next().charAt(0);
            	  //System.out.println("x: "+x+" y:"+y+" "+c+"");
                  grid[x][y]= c;
                  
                  if(c == "S".charAt(0)) {  /** Sets the start node from the map*/
                	  startNode = "S".charAt(0);
                  }
                  if(c == "E".charAt(0)) {  /** Sets the destination node on the map*/
                	  endNode = "E".charAt(0);
                  }
              }
          }
          printRow(grid);
          newBuf.close(); /** Closes the read stream object*/
          System.out.println("\nThe best path from S to E is:");
          //AStarSearch.findPath(startNode, endNode);
      }catch(IOException e) { /** Handles the error during the file read.*/
          e.printStackTrace();
      }

  }
}