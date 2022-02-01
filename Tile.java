public class Tile {

	private int value;

	public Tile(int value){

		this.value = value;

	}
	
	public int getValue(){
	    return value;
	}
	
	public void addTile(Tile t) {
		this.value += t.getValue();		
	}
	
	public static int compareTile(Tile t1, Tile t2) {
		if (t1.getValue() == t2.getValue()) {
			return 0;
		}
		else if (t1.getValue() < t2.getValue()) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	public Tile copy() {
		return new Tile(value);
	}

}