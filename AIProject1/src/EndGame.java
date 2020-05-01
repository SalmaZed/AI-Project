import java.awt.Point;
import java.util.*;


public class EndGame extends SearchProblem{
	int rows;
	int columns;
	Point thanos;
	
	//Hashtable<String, String> states = new Hashtable<String, String>();
	
	public EndGame (String grid) {
		super();
		
		String[] g1= grid.split(";");
		String[] g2= g1[0].split(",");
		
		rows= Integer.parseInt(g2[0]);
		columns= Integer.parseInt(g2[1]);

		String[] g3= g1[1].split(",");
		Point ironman= new Point(Integer.parseInt(g3[0]),Integer.parseInt(g3[1]));

		String [] g4= g1[2].split(",");
		thanos = new Point(Integer.parseInt(g4[0]),Integer.parseInt(g4[1]));

		String[] g5= g1[3].split(",");
		ArrayList<Point> stones= new ArrayList<Point>();
		for( int i=0; i< g5.length;i=i+2){
			stones.add( new Point(Integer.parseInt(g5[i]),Integer.parseInt(g5[i+1])));
		}

		String[] g6= g1[4].split(",");
		ArrayList<Point> warriors= new ArrayList<Point>();
		for( int i=0; i< g6.length;i=i+2){
			warriors.add( new Point(Integer.parseInt(g6[i]),Integer.parseInt(g6[i+1])));
		}
		
		ArrayList<Point> positions= new ArrayList<Point>();
		positions.add(ironman);
		positions.add(new Point(-1,-1));
		positions.addAll(warriors);
		positions.add(new Point(-1,-1));
		positions.addAll(stones);

		initialState = new IronmanState(positions,0);
		operators = new String[]{"collect","down","left","right","up","kill"};

		//SearchTreeNode root = new SearchTreeNode(initialState,null,null,0,0); 

	}

	public boolean warriorCollision (Point ironman, ArrayList <Point>warriorPositions){
		for(int i=0; i<warriorPositions.size(); i++)
		{
			if(ironman.x == warriorPositions.get(i).x && ironman.y == warriorPositions.get(i).y)
				return true;
		}
		return false;
	}

	public boolean thanosCollision (Point ironman){
		if(ironman.x == thanos.x && ironman.y == thanos.y)
			return true;
		return false;
	}

	public int damage(Point ironman, ArrayList <Point>warriorPositions, int currDamage){
		int newDamage = currDamage;
		
		//warrior in any adjacent cell
		for(int i=0; i<warriorPositions.size(); i++)
		{
			if(ironman.x-1 == warriorPositions.get(i).x && ironman.y == warriorPositions.get(i).y) //adjacent warrior down
				newDamage++;
			if(ironman.x+1 == warriorPositions.get(i).x && ironman.y == warriorPositions.get(i).y) //adjacent warrior up
				newDamage++;
			if(ironman.x == warriorPositions.get(i).x && ironman.y-1 == warriorPositions.get(i).y) //adjacent warrior left
				newDamage++;
			if(ironman.x == warriorPositions.get(i).x && ironman.y+1 == warriorPositions.get(i).y) //adjacent warrior right
				newDamage++;
		}
		
		//Thanos in any adjacent cell
		if(ironman.x-1 == thanos.x && ironman.y == thanos.y) //adjacent thanos down
			newDamage+=5;
		if(ironman.x+1 == thanos.x && ironman.y == thanos.y) //adjacent thanos up
			newDamage+=5;
		if(ironman.x == thanos.x && ironman.y-1 == thanos.y) //adjacent thanos left
			newDamage+=5;
		if(ironman.x ==thanos.x && ironman.y+1 == thanos.y) //adjacent thanos right
			newDamage+=5;
		
		return newDamage;
	}

	public State transitionFunction(State s1, String operator){
		
//		if(states.containsKey(s1.toString())){
//			return null;
//		}
		
		Point ironman = ((IronmanState)((IronmanState)s1)).positions.get(0);
		int index1 = ((IronmanState)((IronmanState)s1)).positions.lastIndexOf(new Point(-1,-1));
		
		ArrayList<Point> warriorPositions = new ArrayList<Point>(((IronmanState)s1).positions.subList(2, index1));
		ArrayList<Point> stonesPositions = new ArrayList<Point>(((IronmanState)s1).positions.subList(index1 +1, ((IronmanState)s1).positions.size()));

		switch(operator){
		case "up":{ 
			Point ironman2 = new Point(ironman.x -1, ironman.y);
			if(ironman2.x>=0 && !warriorCollision(ironman2,warriorPositions) && !thanosCollision(ironman2)){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)((IronmanState)s1)).damage));
				if(states.containsKey(newState.toString())){
					return null;
				}
				
				states.put(newState.toString(), "");
				return newState;

			}
			else if(thanosCollision(ironman2) && ((IronmanState)s1).damage<100 && stonesPositions.isEmpty()){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)+5);						
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;

			}
			else 
				return null;
	}
		case "down": {
			Point ironman2 = new Point(ironman.x +1,ironman.y);
			if(ironman2.x<rows && !warriorCollision(ironman2,warriorPositions) && !thanosCollision(ironman2)){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				//System.out.println("Down damage: " + ((IronmanState)s1).damage);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)); 
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
				
			}
			else if(thanosCollision(ironman2) && ((IronmanState)s1).damage<100 && stonesPositions.isEmpty()){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)+5);						//
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
			}
			else 
				return null;
			}

		case "left": {
			Point ironman2 = new Point(ironman.x,ironman.y -1);
			if(ironman2.y>=0 && !warriorCollision(ironman2,warriorPositions) && !thanosCollision(ironman2)){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)); 
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
			}
			else if(thanosCollision(ironman2) && ((IronmanState)s1).damage<100 && stonesPositions.isEmpty()){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)+5);						
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
			}
			else 
				return null;
			}
		
		case "right": {
			Point ironman2 = new Point(ironman.x,ironman.y +1);
			if(ironman2.y<columns && !warriorCollision(ironman2,warriorPositions) && !thanosCollision(ironman2)){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)); 
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
				//return new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)); 
			}
			else if(thanosCollision(ironman2) && ((IronmanState)s1).damage<100 && stonesPositions.isEmpty()){
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman2);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)+5);						//
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
				//return new IronmanState(newPositions, damage(ironman2,warriorPositions,((IronmanState)s1).damage)+5);						//

			}
			else 
				return null;
			}
		
		case "collect": {
			if(stonesPositions.contains(ironman))
			{
				ArrayList<Point> tmp2 = new ArrayList<Point>();
				tmp2 = (ArrayList<Point>) stonesPositions.clone();
				tmp2.remove(ironman);
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(warriorPositions);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(tmp2);
				State newState = new IronmanState(newPositions, damage(ironman,warriorPositions,((IronmanState)s1).damage)+3);
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
				//return new IronmanState(newPositions, damage(ironman,warriorPositions,((IronmanState)s1).damage)+3);
			}
			else
				return null;
			
		}
		
		case "kill": {
			//warrior in any adjacent cell
			ArrayList <Point> tmp = new ArrayList <Point>();
			tmp = (ArrayList<Point>) warriorPositions.clone();
			int damage2 = ((IronmanState)s1).damage;
			
			for(int i=0; i<warriorPositions.size(); i++)
			{
				if(ironman.x-1 == warriorPositions.get(i).x && ironman.y == warriorPositions.get(i).y) //adjacent warrior down
				{
					damage2 +=2;
					tmp.remove(warriorPositions.get(i));
				}
				if(ironman.x+1 == warriorPositions.get(i).x && ironman.y == warriorPositions.get(i).y) //adjacent warrior up
				{
					damage2 +=2;
					tmp.remove(warriorPositions.get(i));
				}
				if(ironman.x == warriorPositions.get(i).x && ironman.y-1 == warriorPositions.get(i).y) //adjacent warrior left
				{
					damage2 +=2;
					tmp.remove(warriorPositions.get(i));
				}				
				if(ironman.x == warriorPositions.get(i).x && ironman.y+1 == warriorPositions.get(i).y) //adjacent warrior right
				{
					damage2 +=2;
					tmp.remove(warriorPositions.get(i));
				}			
			}
			if(((IronmanState)s1).damage == damage2)
				return null;
			else{
				ArrayList<Point> newPositions = new ArrayList<Point>();
				newPositions.add(ironman);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(tmp);
				newPositions.add(new Point(-1,-1));
				newPositions.addAll(stonesPositions);
				State newState = new IronmanState(newPositions,damage2);
				if(states.containsKey(newState.toString())){
					return null;
				}
				states.put(newState.toString(), "");
				return newState;
				//return new IronmanState(newPositions,damage2);
			}
		}
		/*case "snap": {
			if(thanosCollision(ironman) && ((IronmanState)s1).damage<100 && stonesPositions.isEmpty())
				return new IronmanState(((IronmanState)s1).positions, ((IronmanState)s1).damage);
			else
				return null;
		}*/
		default: return null;
		}
	}


	public static void main(String[] args){
		String grid= "5,5;1,2;3,1;0,2,1,1,2,1,2,2,4,0,4,1;0,3,3,0,3,2,3,4,4,3";
		EndGame e1= new EndGame(grid);
		//SearchTreeNode root = new SearchTreeNode(initialState,null,null,0,0);
		State ir = e1.transitionFunction((IronmanState) e1.initialState, "up");
		State ir1 = e1.transitionFunction((IronmanState) ir, "collect");
		State ir2 = e1.transitionFunction((IronmanState) ir1, "left");
		State ir3 = e1.transitionFunction((IronmanState) ir2, "left");
		State ir4 = e1.transitionFunction((IronmanState) ir3, "right");
		System.out.println(e1.states);
		System.out.println(ir4);
//		State ir4 = e1.transitionFunction((IronmanState) ir3, "collect");
//		State ir5 = e1.transitionFunction((IronmanState) ir4, "down");
//		State ir6 = e1.transitionFunction((IronmanState) ir5, "collect");
//		State ir7 = e1.transitionFunction((IronmanState) ir6, "right");
//		State ir8 = e1.transitionFunction((IronmanState) ir7, "collect");
//		State ir9 = e1.transitionFunction((IronmanState) ir8, "kill");
//		State ir10 = e1.transitionFunction((IronmanState) ir9, "down");
//		State ir11 = e1.transitionFunction((IronmanState) ir10, "down");
//		State ir12 = e1.transitionFunction((IronmanState) ir11, "left");
//		State ir13 = e1.transitionFunction((IronmanState) ir12, "collect");
//		State ir14 = e1.transitionFunction((IronmanState) ir13, "left");
//		State ir15 = e1.transitionFunction((IronmanState) ir14, "collect");
//		State ir16 = e1.transitionFunction((IronmanState) ir15, "right");
//		State ir17 = e1.transitionFunction((IronmanState) ir16, "up");
		//State ir18 = e1.transitionFunction((IronmanState) ir17, "snap");

//		System.out.println(((IronmanState)ir17).positions);
//		System.out.println("pathCost:"+((IronmanState)ir17).pathCost);
//		


		
	}

	@Override
	public boolean goalTest(SearchTreeNode node) {
		IronmanState s1 = (IronmanState)node.state;
		int index1 = s1.positions.lastIndexOf(new Point(-1,-1));
		Point ironman = s1.positions.get(0);
		ArrayList<Point> stonesPositions = new ArrayList<Point>(s1.positions.subList(index1 +1, s1.positions.size()));

		if(thanosCollision(ironman) && (s1.damage<100 && stonesPositions.isEmpty()))
			return true;
		else
			return false;
	}
}
