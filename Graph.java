/** a CLASS defining a Graph of information that can provide
 * Breadth First Search (BFS) when loading from a file formatted
 * such as "cast-mpaa.txt". The number of edges in the Graph can
 * be retrieved with edges().
 *
 * @author Rie Kurita
 * 　
 */

public class Graph {

	private HashMap<String, HashSet<GraphNode>> ActorsMap;
	private Scanner theScanner = null;
	private int edges;

	public Graph( String InputFile ){
		ActorsMap = new HashMap<String, HashSet<GraphNode>>();
		edges = 0;
		loadDataFromFile( InputFile );
	}

		public class GraphNode {
			private String name;
			private int birthYear;
			private int deathYear;
			private boolean visited;
			private GraphNode previous;

			public GraphNode (String actorName) {
				name = actorName;
				visited = false;
				previous = null;
			}

			public String getName() {
				return name;
			}

			public int getBirthYear() {
				return birthYear;
			}

			public int getDeathYear() {
				return deathYear;
			}

			public GraphNode getPrevious() {
				return previous;
			}

			public void setPrevious(GraphNode previousNode) {
				previous = previousNode;
			}

			public void setVisited(boolean a) {
				visited = a;
			}

			public boolean visited() {
				return visited;
			}

			// public String toString() {
			// 	return name;
			// }

		}

		/** loads data to the graph from the specified file.
		 * @param actorFile
		 */
	 public static HashSet<GraphNode> loadActorList(File actorFile) {
		 HashSet<GraphNode> actorList = new HashSet<GraphNode>();

		 Scanner scanner = null;
		 try {
				 scanner = new Scanner(actorFile);
		 } catch (FileNotFoundException e) {
				 System.err.println(e);
				 System.exit(1);
		 }

		 int numberOfLines = 0;
		 while (scanner.hasNextLine()) {
				 String currentLine = scanner.nextLine();
				 String[] pieces = currentLine.split(",");

				 String actorID = pieces[0];

				 String actorName = pieces[1];

				 String yearOfBirth = pieces[2];
				 int birthInt = Integer.parseInt(yearOfBirth);

				 String yearOfDeath = pieces[3];
				 int deathInt = Integer.parseInt(yearOfDeath);

				 actorList.add(new GraphNode(actorID, actorName, yearOfBirth, yearOfDeath));
				 }
		 return actorList;
	 }

	 File actorFile = new File("actors.csv");
	 HashSet<GraphNode> actorList = loadActorList(actorFile);

	public void loadDataFromFile(String inputFile){
		ArrayList<String> allLines = new ArrayList<String>();
		try {
			theScanner = new Scanner(new FileInputStream(InputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(theScanner.hasNextLine()){
			allLines.add(theScanner.nextLine());
		}

		for( String aString : allLines ){
			String[] data = aString.split(",");
			GraphNode theMovie = new GraphNode( data[0] );
			for( int i = 1; i < data.length; i++ ){
				HashSet<GraphNode> actorValue = new HashSet<GraphNode>();
				actorValue.add( theMovie );
				if( ! ActorsMap.containsKey( data[i] )){
					ActorsMap.put( data[i], actorValue );
					edges += ( data.length - 1 );
				}
				else{
					HashSet<GraphNode> orig = ActorsMap.get( data[i]);
					ActorsMap.remove( data[i] );
					orig.add( theMovie );
					ActorsMap.put( data[i], orig );
				}
				edges += (( data.length * ( data.length - 1 ) ));
			}
			HashSet<GraphNode> movieValue = new HashSet<GraphNode>();
			for( int i = 1; i < data.length; i++ ){
				GraphNode anActor = new GraphNode( data[i] );
				movieValue.add( anActor );
			}
			ActorsMap.put( data[0], movieValue );
		}
	}


	/** returns the number of edges in the graph.
	 * @return int
	 */
	public int edges(){
		return edges;
	}

	/** returns true is v is a vertex in the Graph
	 * @return boolean
	 */
	public boolean exists(String actor) {
		return ActorsMap.containsKey(actor);
	}

	/** returns true if an edge exists between actor1 and actor2
	 * @param actor1, actor2
	 * @return boolean
	 */
	public boolean exists(String actor1, String actor2) {
		for (GraphNode aNode : ActorsMap.get(actor)) {
			for (GraphNode bNode : ActorsMap.get(aNode.getName())) {
				if (bNode.getName().equalsIgnoreCase(actor2) ){
					return true;
				}
			}
		}
		return false;
	}

	/** returns the path (if one exists) between actor1 and actor2
	 * @param actor1, actor2
	 * @print the sentense
	 */
	public void breadthFirstSearch(String actor1, String actor2){
		ArrayList<String> result = new ArrayList<String>();
		Queue<GraphNode> toDo = new LinkedList<GraphNode>();
		if(!exists(actor1)) {
			System.out.println("That actor1 does not exist.");
			// ArrayList<String> nullResult = new ArrayList<String>();
			// nullResult.add( "That actor1 does not exist." );
			// return nullResult;
		}
		else if(!exists(actor2)) {
			System.out.println("That actor2 does not exist.");
			// ArrayList<String> nullResult = new ArrayList<String>();
			// nullResult.add( "That actor2ination doesn't exist." );
			// return nullResult;
		}

		Queue<GraphNode> connectedActors = new LinkedList<GraphNode>();
		GraphNode actor1Node = new GraphNode(actor1);
		for (GraphNode aNode : ActorsMap.get(actor1)) {
			aNode.setPrevious(actor1Node);
			for (GraphNode bNode : ActorsMap.get(aNode.getName())) {
				if (!bNode.getName().equals(actor1)) {
					bNode.setPrevious(aNode);
					connectedActors.add(bNode);
				}
			}
			aNode.setVisited(true);
			toDo.addAll(connectedActors);
			connectedActors.clear();
		}

		while (!toDo.isEmpty()){
			GraphNode currentActor = toDo.remove();
			if (currentActor.getName().equalsIgnoreCase(actor2)){
				GraphNode previous = currentActor.getPrevious();
				result.add(currentActor.getName());
				while( !previous.getName().equals( actor1 )){
					result.add( previous.getName() );
					previous = previous.getPrevious();
				}
				result.add(actor1);
				// ArrayList<String> finalResult = new ArrayList<String>();
				for (int i = 0; i < result.size(); i++) {
					if (!(( i%2 ) == 0) ){
						System.out.print(" was in "+ result.get(i) + " with " );
					}
					else{
						System.out.println(+ result.get(i));
					}
				}
			}

			if (!currentActor.visited()) {
				ArrayList<GraphNode> b = new ArrayList<GraphNode>();
				for (GraphNode aNode : ActorsMap.get( currentActor.getName())) {
					if (aNode.visited() == false) {
						b.clear();
						aNode.setPrevious(currentActor);
						for (GraphNode bNode : ActorsMap.get(aNode.getName())){
							if ((bNode.visited() == false)) {
								bNode.setPrevious(aNode);
								b.add(bNode);
							}
						}
						aNode.setVisited(true);
						toDo.addAll(b);
					}
				}
				currentActor.setVisited(true);
			}
		}
		// ArrayList<String> nullResult = new ArrayList<String>();
		// nullResult.add( "There was no connection found." );
		// return nullResult;

	}

	// /** creates a file, BaconNumbers.txt, that lists the "bacon numbers", or number of edges,
	//  * between "Bacon, Kevin" and all other actor names in the Graph.
	//  * Then prints the average number of edges.
	//  */
	// public void baconNumbers(){
	// 	Queue<ArrayList<String>> baconNumbers = new LinkedList<ArrayList<String>>();
	// 	Queue<GraphNode> toDo = new LinkedList<GraphNode>();
	// 	String actor1 = "Bacon, Kevin";
	// 	Queue<GraphNode> a = new LinkedList<GraphNode>();
	// 	GraphNode actor1PreNode = new GraphNode( actor1 );
	// 	ArrayList<String> numberX = new ArrayList<String>();
	// 	HashMap<String, Boolean> visitations = new HashMap<String, Boolean>();
	//
	// 	for( GraphNode aNode : ActorsMap.get( actor1 )){
	// 		aNode.setPrevious( actor1PreNode );
	// 		for( GraphNode someNode : ActorsMap.get( aNode.getValue() ) ){
	// 			numberX.add( someNode.getValue() );
	// 			if( !someNode.getValue().equals( actor1 )){
	// 				a.add( someNode );
	// 			}
	// 		}
	// 		aNode.setVisited( true );
	// 		visitations.put( aNode.getValue(), true);
	// 		toDo.addAll( a );
	// 		a.clear();
	// 	}
	// 	ArrayList<String> numberOne = new ArrayList<String>();
	// 	numberOne.addAll( numberX );
	// 	baconNumbers.add( numberOne );
	// 	numberX.clear();
	// 	GraphNode bReak = new GraphNode( "BREAK" );
	// 	toDo.add( bReak );
	// 	while( ! toDo.isEmpty() ){
	// 		// System.out.println( toDo );
	// 		GraphNode currentActor = toDo.remove();
	// 		if( currentActor.getValue().equals( "BREAK" ) ){
	// 			if(! toDo.isEmpty() ){
	// 				ArrayList<String> copy = new ArrayList<String>();
	// 				copy.addAll( numberX );
	// 				baconNumbers.add( copy );
	// 				numberX.clear();
	// 				toDo.add( bReak );
	//
	// 			}
	// 			if( toDo.isEmpty() ){
	// 				ArrayList<String> copy = new ArrayList<String>();
	// 				copy.addAll( numberX );
	// 				baconNumbers.add( copy );
	// 				break;
	// 			}
	// 		}
	// 		else if(( ! currentActor.visited() ) && (! currentActor.getValue().equals("Bacon, Kevin"))){
	// 			ArrayList<GraphNode> b = new ArrayList<GraphNode>();
	// 			for( GraphNode aNode : ActorsMap.get( currentActor.getValue() ) ){
	// 				if( aNode.visited() == false ){
	// 					b.clear();
	// 					aNode.setPrevious( currentActor );
	// 					for( GraphNode bNode : ActorsMap.get( aNode.getValue() ) ){
	// 						if( ( bNode.visited() == false )){
	// 							b.add( bNode );
	// 							numberX.add( bNode.getValue() );
	// 						}
	// 					}
	// 					aNode.setVisited( true );
	// 					toDo.addAll( b );
	// 				}
	// 			}
	// 			currentActor.setVisited( true );
	// 		}
	// 	}
	// 	ArrayList<String> noConnections = new ArrayList<String>();
	// 	for( HashSet<GraphNode> aSet : ActorsMap.values() ){
	// 		for( GraphNode aNode : aSet ){
	// 			if( ( aNode.visited() == false ) && ( ! listContains( aNode.getValue(), baconNumbers ) && ( ! aNode.getValue().matches(".*[0-9].*") )) ){
	// 				noConnections.add( aNode.getValue() );
	// 			}
	// 		}
	// 	}
	// 	baconNumbers.add( noConnections );
	//
	// 	PrintWriter writer = null;
	// 	try {
	// 		writer = new PrintWriter( new FileOutputStream( "BaconNumbers.txt"));
	// 	} catch (FileNotFoundException e) {
	// 		e.printStackTrace();
	// 	}
	//
	// 	int limit = baconNumbers.size();
	// 	ArrayList<String> current = baconNumbers.remove();
	// 	Queue<ArrayList<String>> finalNumbers = new LinkedList<ArrayList<String>>();
	// 	ArrayList<String> kevinBacon = new ArrayList<String>();
	// 	kevinBacon.add( "Bacon, Kevin");
	// 	finalNumbers.add( kevinBacon );
	// 	finalNumbers.add( current );
	// 	for( int i = 2; i < limit; i++ ){
	// 		ArrayList<String> next = baconNumbers.remove();
	// 		Set<String> hs = new HashSet<String>();
	// 		hs.addAll( current );
	// 		ArrayList<String> done = new ArrayList<String>();
	// 		done.addAll( hs );
	// 		next.removeAll( done );
	// 		current = next;
	//
	// 		finalNumbers.add( done );
	//
	// 	}
	//
	// 	int count = 0;
	// 	int sum = 0;
	// 	int totalNum = 0;
	// 	for( ArrayList<String> anotherList : finalNumbers ){
	// 		if( anotherList.size() > 0 ){
	// 			writer.println( "||||||| Bacon Number :: " + count + " |||||||" );
	// 			Collections.sort( anotherList );
	// 			sum += ( anotherList.size() * count );
	// 			totalNum += anotherList.size();
	// 			for( String theString : anotherList ){
	// 				writer.println( theString );
	// 			}
	// 			count++;
	// 			writer.println("\n");
	// 		}
	// 	}
	// 	writer.println( "||||||| Actors with no connections to Kevin Bacon |||||||" );
	// 	Collections.sort( noConnections );
	// 	for( String aString : noConnections ){
	// 		writer.println( aString );
	// 	}
	//
	//
	// 	System.out.println("BaconNumbers.txt  was generated");
	//
	// 	totalNum += noConnections.size();
	// 	int average = ( sum / totalNum );
	//
	// 	writer.println( "\n||||||||||\nThe average Bacon Number is " + average + "\n||||||||||" );
	// 	writer.flush();
	// 	writer.close();
	//
	// }

	// /** returns true is the specified list contains the specified String.
	//  * @param aString, aList
	//  * @return boolean
	//  */
	// public boolean listContains( String aString, Queue<ArrayList<String>> aList ){
	// 	for( ArrayList<String> someList : aList ){
	// 		if( someList.contains( aString )){
	// 			return true;
	// 		}
	// 	}
	// 	return false;
	// }

	/** returns the HashSet<GraphNode> (value) of the specified String (key).
	 * @param actor
	 * @return HashSet<GraphNode>
	 */
	public HashSet<GraphNode> get(String actor) {
		return ActorsMap.get(actor);
	}
}
