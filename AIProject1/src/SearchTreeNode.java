
public class SearchTreeNode {
	State state; //should it be general walla 3adi?
	SearchTreeNode parent;
	String operator;
	int depth;
	int pathCost;
	
	public SearchTreeNode(State state, SearchTreeNode parent, String operator,
			int depth, int pathCost) {
		
		super();
		this.state = state;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = pathCost;
	}
}
