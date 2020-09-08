
public class Player {
	private int x, y;
	private final int r = 10;
	private boolean hasPuck = false;
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean hasPuck() {
		return hasPuck;
	}
	
	public void givePuck() {
		hasPuck = true;
	}
	
	public void noPuck() {
		hasPuck = false;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}
	
	public int getR() {
		return r;
	}
	
}
