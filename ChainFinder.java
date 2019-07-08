

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ChainFinder {
  public static void main(String[] args) {
    Graph movieGraph = new Graph();
    if (args[0] == "chain") {
      movieGraph.breadthFirstSearch(args[1], args[2]);

    }
  }




}
