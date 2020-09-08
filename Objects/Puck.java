import java.awt.Point;

public class Puck {
	private double x, y;
	private double dx, dy;
	private final int r = 5;
	private boolean passing = true;
	private boolean shooting = false;
	private final int longPassingSteps = 25;
	private final int shortPassingSteps = 15;
	private int shootingSteps;
	private Point moveTo;
	
	public Puck() {
		x = 400;
		y = 475;
	}
	
	public Puck(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getR() {
		return r;
	}
	
	public void passing() {
		passing = true;
		shooting = false;
	}
	
	public void shooting() {
		shooting = true;
		passing = false;
	}
	
	public void flipDX() {
		dx = -dx;
	}
	
	public void flipDY() {
		dy = -dy;
	}

	public void setMoveTo(int newX, int newY, boolean home) {
		moveTo = new Point(newX, newY);
		if(x!=newX||y!=newY) {
			dx = newX-x;
			dy = newY-y;
			if(passing) {
				if(dx+dy<=400) {
					dx = dx/shortPassingSteps;
					dy = dy/shortPassingSteps;
				}
				else {
					dx = dx/longPassingSteps;
					dy = dy/longPassingSteps;
				}
			}
			else {
				shootingSteps = ((int)Math.random()*5)+12+(int)((dx+dy)/100);
				int theSpot;
				if(home) theSpot = 55;
				else theSpot = 895;
				dy = theSpot-y;
				dx = dx/shootingSteps;
				dy = dy/shootingSteps;
			}
		}
	}
	
	public void move() {
		if(x != moveTo.getX() || y != moveTo.getY()) {
			x = x + dx;
			y = y + dy;
		}
		if(x<=5) x = 5;
		if(x>=795) x = 795;
		if(y<=30) {
			y = 15;
			x= 400;
		}
		if(y>=920) {
			y = 935;
			x = 400;
		}
	}
}
