import java.util.ArrayList;
import java.util.Hashtable;


public abstract class SearchProblem {
	State initialState; //should it be general walla 3adi?
	String [] operators;
	int nodesExpanded = 0;
	int currlevel = 0;
	Hashtable<String, String> states = new Hashtable<String, String>();
	
	public abstract boolean goalTest(SearchTreeNode node);
	
	public abstract State transitionFunction(State state, String operator);
	
	public ArrayList<SearchTreeNode> expand(SearchTreeNode node){
		ArrayList<SearchTreeNode> nodes = new ArrayList<SearchTreeNode>();
		nodesExpanded +=1;
		for(int i =0; i<operators.length ; i++)
		{
			State state = transitionFunction(node.state, operators[i]);
			if(state !=null){
				SearchTreeNode nodeTemp = new SearchTreeNode(state,node,operators[i],node.depth+1,state.pathCost);
				nodes.add(nodeTemp);
			}
		}
		return nodes;
	}
	public ArrayList<SearchTreeNode> qingFunction(String strategy, ArrayList<SearchTreeNode> oldNodes, ArrayList<SearchTreeNode> newNodes){
		switch(strategy){
		case "BF":{
			 oldNodes.addAll(newNodes);
			 return oldNodes;
		}
		case "DF":{
			 newNodes.addAll(oldNodes);
			 return newNodes;
		}
		case "ID":{
			
			if(!newNodes.isEmpty() && newNodes.get(0).depth <= currlevel){
				newNodes.addAll(oldNodes);
				return newNodes;
			}
			else if(oldNodes.isEmpty()){
				currlevel +=1;
				states= new Hashtable<String,String>();
				oldNodes.add(new SearchTreeNode(this.initialState,null,null,0,0)); // new queue with the root node only
				return oldNodes;
			}
			return oldNodes;
			
		}
		
		case "UC":{
			oldNodes.addAll(newNodes);
			return sort(oldNodes);
		}
		
		 default:
			return oldNodes;
			
		}
	}

	public ArrayList<SearchTreeNode> sort(ArrayList<SearchTreeNode> nodes)  
    { 
        int n = nodes.size(); 
        for (int i = 1; i < n; ++i) { 
            int key = nodes.get(i).pathCost; 
            int j = i - 1; 
  
            /* Move elements of arr[0..i-1], that are 
               greater than key, to one position ahead 
               of their current position */
            while (j >= 0 && nodes.get(j).pathCost > key) { 
            	nodes.get(j+1).pathCost = nodes.get(j).pathCost; 
                j = j - 1; 
            } 
            nodes.get(j+1).pathCost= key; 
        }
        return nodes;
    }
	
	public SearchTreeNode generalSearch(String strategy){
		ArrayList<SearchTreeNode> nodes = new ArrayList<SearchTreeNode>();
		nodes.add(new SearchTreeNode(this.initialState,null,null,0,0));
		while(!nodes.isEmpty()){
			SearchTreeNode node = nodes.remove(0);
			if(this.goalTest(node)){
				return node;
			}
			nodes = qingFunction(strategy, nodes, expand(node));
		}	
		return null;
	}
}
