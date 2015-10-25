/*
 * Mehgan Cook and Vlad Smirnov
 * credit to: Christoper Marriott for graph and node code.
 */
import java.util.*;

public class Maze {

	private int myDepth;
	private int myWidth;
	private boolean myDebug;
	private Graph myGraph;
	private Random rand;
	private int[][] result;

	public Maze(int width, int depth, boolean debug) {
		myDepth = depth;
		myWidth = width;
		myDebug = debug;
		myGraph = new Graph();
		rand = new Random();
		result = new int[myDepth][myWidth];
		createNodes();
		createEdges();
	}
	
	public void display() {
		DFS();
		solveMaze();
	}

	private void createNodes() {
		for (int i = 0; i < myWidth * myDepth; i++) {
			myGraph.addNode(i);
		}
	}

	private void createEdges() {
		for (int k = 0; k < myWidth * myDepth; k += myWidth) {
			for(int i = 0; i < myWidth - 1; i++) {
				myGraph.addEdge(i + k, i + k + 1);
			}
		}
		for (int k = 0; k < myWidth * myDepth; k += myDepth) {
			for(int i = 0; i < myDepth; i++) {
				myGraph.addEdge(i + k, i + k + myWidth);
			}
		}
	}

	public void printMaze() {
		if (myDebug) {
			for (int a = 0; a < myDepth; a ++) {
				int i = 0;
				while (i < myWidth) {
					if(result[a][i] % 10 == 2) {
						System.out.print("*   *");
					} else {
						System.out.print("*****");
					}
					i++;
				}
				System.out.println();
				i = 0;
				while (i < myWidth) {
					int nodeID = a * myWidth + i;
					Node current = myGraph.nodeList.get(nodeID);
					char v = ' ';
					if (current.visited) {
						v = 'V';
					}
					if (result[a][i] / 10 % 10 == 2) {
						System.out.print(" " + " " + v  + " ");
					} else {
						System.out.print("*" + " " + v + " ");
					}
					if (result [a][i] / 1000 == 2) {
						System.out.print(" ");
					} else {
						System.out.print("*");
					}
					i++;
				}
				System.out.println();
				i = 0;
				while (i < myWidth) {
					if(result[a][i] / 100 % 10 == 2) {
						System.out.print("*   *");
					} else {
						System.out.print("*****");
					}
					i++;
				}
				System.out.println();

			}
			System.out.println();
		}
	}

	public void solveMaze() {
		Node end = myGraph.nodeList.get((myDepth * myWidth - 1));
		Node start = myGraph.nodeList.get(0);
		Set<Integer> set = new HashSet<Integer>();

		while (end.parent.label != start.label) {
			set.add(end.label);
			end = end.parent;
			set.add(end.label);
		}
		set.add(0);			
		for (int a = 0; a < myDepth; a ++) {
			int i = 0;
			while (i < myWidth) {
				if(result[a][i] % 10 == 2) {
					System.out.print("*   *");
				} else {
					System.out.print("*****");
				}
				i++;
			}
			System.out.println();
			i = 0;
			while (i < myWidth) {
				int nodeID = a * myWidth + i;
				if (result[a][i] / 10 % 10 == 2) {
					if (set.contains(nodeID)) {
						System.out.print("  " + "+ ");
					} else {
						System.out.print("    ");
					}
				} else {
					if (set.contains(nodeID)) {
						System.out.print("* + ");
					} else {
						System.out.print("*   ");
					}
				}
				if (result [a][i] / 1000 == 2) {
					System.out.print(" ");
				} else {
					System.out.print("*");
				}
				i++;
			}
			System.out.println();
			i = 0;
			while (i < myWidth) {
				if(result[a][i] / 100 % 10 == 2) {
					System.out.print("*   *");
				} else {
					System.out.print("*****");
				}
				i++;
			}
			System.out.println();

		}
		System.out.println();
	}

	public void encodeMaze() {
		for (int a = 0; a < myDepth; a++) {
			for (int b = 0; b < myWidth; b++) {
				result[a][b] = 1111;
			}
		}
		for (int c = 0; c < myDepth; c++) {
			for (int d = 0; d < myWidth; d++) {
				int nodeID = c * myWidth + d;
				Node current = myGraph.nodeList.get(nodeID);
				if (current.parent != null) {
					if (current.getVal() - current.parent.getVal() == -1) {
						result[c][d] += 1000;
						result[c][d + 1] += 10;
					}
					if (current.getVal() - current.parent.getVal() == 1) {
						result[c][d] += 10;
						result[c][d - 1] += 1000;
					}
					if (current.getVal() - current.parent.getVal() == -myWidth) {
						result[c][d] += 100;
						result[c + 1][d] += 1;
					}
					if (current.getVal() - current.parent.getVal() == myWidth) {
						result[c][d] += 1;
						result[c - 1][d] += 100;
					}
				}
			}
		}
		result[0][0] += 1;
		result[myDepth - 1][myWidth - 1] += 100;
	}

	private void DFS() {
		Stack<Node> CellStack = new Stack<Node>();
		Node current = myGraph.nodeList.get(0);
		current.visited = true;
		int visitCount = 1;
		if (myDebug) {
			encodeMaze();
			printMaze();
		}
		while (visitCount < myWidth * myDepth) {
			List<Node> notVisited = new ArrayList<Node>();
			for (Node neighbor : current.adjacencyList) {
				if (!neighbor.visited) {
					notVisited.add(neighbor);
				}
			}
			if (!notVisited.isEmpty()) {
				int random = rand.nextInt(notVisited.size());
				Node temp = notVisited.get(random);
				temp.visited = true;
				visitCount++;
				temp.parent = current;
				CellStack.push(temp);
				current = temp;
			} else {
				current = CellStack.pop();
			}
			if (myDebug) {
				encodeMaze();
				printMaze();
			}
		}
		if (!myDebug) {
			encodeMaze();
			printMaze();
		}
	}

	public class Node{
		List<Node> adjacencyList;
		Integer label; 
		boolean visited;
		Node parent;
		String wall;

		public Node(Integer lbl){
			label = lbl;
			adjacencyList = new ArrayList<Node>();
			visited = false;
			parent = null;
			wall = "*";
		}

		public void removeEdge(Node remove) {
			for (int i = 0; i < adjacencyList.size(); i++) {
				if (adjacencyList.get(i).label.equals(remove.label)) {
					adjacencyList.remove(adjacencyList.get(i));
				}
			}
		}

		public void setVisited(boolean result) {
			visited = result;
		}

		public int getVal() {
			return label;
		}

	}

	public class Graph {

		Map<Integer, Node> nodeList;
		boolean directed;

		public Graph(){
			directed = false;
			nodeList = new HashMap<Integer, Node>();
		}

		public void addNode(Integer label){
			nodeList.put(label, new Node(label));
		}

		public void addEdge(Integer source, Integer dest){
			Node src = nodeList.get(source);
			Node dst = nodeList.get(dest);
			if(src != null && dst != null){
				src.adjacencyList.add(dst);
				if(!directed && src != dst) dst.adjacencyList.add(src);
			}	
		}
	}

}