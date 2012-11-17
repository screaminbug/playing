
public class Coordinate {
	private int x;
	private int y;
	private Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public static Coordinate instance(int x, int y) {
		return new Coordinate(x, y);
	}
	
	
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
}
