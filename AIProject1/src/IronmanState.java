import java.awt.Point;
import java.util.ArrayList;


public class IronmanState extends State {
	ArrayList <Point> positions; // ironman, (-1,-1), warriorPositions, (-1,-1), stonesPositions
	int damage; //cumulative or just the new damage?
	
	public IronmanState(ArrayList<Point> positions, int damage) {
		
		super();
		this.positions = positions;
		this.damage = damage;
		this.pathCost = this.damage;
		
	}
	 
	public String toString()
	{
		String value = "";
		for(int i=0; i<positions.size();i++){
			//if(positions.get(i).x == -1)
				//value += ";";
			value += positions.get(i).x + "," + positions.get(i).y +",";
		}
		
		
		return value;
	}

}
