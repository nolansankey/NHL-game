
public class Goalie {
	private int x, y;
	private boolean glove = false;
	private boolean blocker = false;
	private boolean fiveHole = true;
	private final int width = 50, height = 20;
	
	public Goalie(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setX(int newX) {
		if(glove&&blocker) x = newX;
		else if(glove) x = Math.max(275, Math.min(350, newX));
		else if(blocker) x = Math.max(450, Math.min(525, newX));
		else x = 400;
	}
	
	public void Glove() {
		glove = true;
		blocker = false;
		fiveHole = false;
	}
	
	public void Blocker() {
		blocker = true;
		glove = false;
		fiveHole = false;
	}
	
	public void FiveHole() {
		blocker = false;
		glove = false;
		fiveHole = true;
	}
	
	public void all() {
		glove = true;
		blocker = true;
		fiveHole = true;
	}
}
