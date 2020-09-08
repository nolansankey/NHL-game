
public class SimGame {
	private String homeTeam;
	private String awayTeam;
	private int homeScore;
	private int awayScore;
	private boolean overtime = false;
	
	public SimGame(String awayTeam, String homeTeam) {
		this.awayTeam = awayTeam;
		this.homeTeam = homeTeam;
		int Score1 = (int)(Math.random()*3)+(int)(Math.random()*4);
		int Score2 = (int)(Math.random()*3)+(int)(Math.random()*4);
		if(Score1==Score2) {
			Score1++;
			overtime = true;
		}
		if(Math.random()<0.6) {
			if(Score1>Score2) {
				homeScore = Score1;
				awayScore = Score2;
			}
			else {
				homeScore = Score2;
				awayScore = Score1;
			}
		}
		else {
			if(Score1>Score2) {
				homeScore = Score2;
				awayScore = Score1;
			}
			else {
				homeScore = Score1;
				awayScore = Score2;
			}
		}
	}
	
	public String getHomeTeam() {
		return homeTeam;
	}
	
	public String getAwayTeam() {
		return awayTeam;
	}
	
	public int getHomeScore() {
		return homeScore;
	}
	
	public int getAwayScore() {
		return awayScore;
	}
	
	public int getHomeShots() {
		return (int)(Math.random()*8)+(int)(Math.random()*8)+10;
	}
	
	public int getAwayShots() {
		return (int)(Math.random()*8)+(int)(Math.random()*8)+10;
	}
	
	public boolean getOvertime() {
		return overtime;
	}
}
