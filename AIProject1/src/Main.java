
public class Main {
	
	public static String solve(String grid, String strategy, boolean visualize) {
		EndGame endgame = new EndGame(grid);
		
		SearchTreeNode node = endgame.generalSearch(strategy);
		String result = "";
		
		if(node != null){
			int pathcost = node.pathCost;
			result += "snap";
			while(node.parent != null){
				result = node.operator+"("+node.pathCost+")" +", "+ result;
				node = node.parent;
			}
			result +=";"+pathcost+";"+endgame.nodesExpanded;
		}
		
		return result;
		
	}
	
	public static void main(String[]args){
		String grid= "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3";
		//String grid="2,2;1,0;0,1;1,1;0,0";
		String s = solve(grid,"UC",false);
		System.out.println(s);
	}
}
