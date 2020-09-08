import java.awt.*;
import java.util.*;

import javax.swing.SwingUtilities;

public class SimpleGoalieGUI extends DrawingGUI {
	private static final int width=800, height=950;
	private int period;
	private int min;
	private int seconds;
	private String homeTeam;
	private String awayTeam;
	private String userTeam;
	private String otherTeam;
	private boolean playerIsHome;
	private int homeScore;
	private int awayScore;
	private int homeShots;
	private int awayShots;
	
	private String[] teams = {"Montreal Canadiens", "Ottawa Senators", "Toronto Maple Leafs","Tampa Bay Lightning","Florida Panthers","Buffalo Sabres","Boston Briuns","Detroit Red Wings","New Jersey Devils","Carolina Hurricanes","Philidelphia Flyers","New York Islanders","New York Rangers","Columbus Blue Jackets","Pittsburgh Penguins","Washington Capitals","Colorado Avalanche","Dallas Stars","Winnipeg Jets","Nashville Predators","St. Louis Blues","Minnesota Wild","Chicago Blackhawks","Vancouver Canucks","Arizona Coyotes","Los Angeles Kings","Calgary Flames","San Jose Sharks","Edmonton Oilers","Anaheim Ducks"};
	private Color[] colors = {new Color(166,25,46),new Color(200,16,46),new Color(0,32,91),new Color(0,32,91),new Color(4,30,66),new Color(4,30,66),new Color(255,184,28),new Color(200,16,46),new Color(200,16,46),new Color(226,24,54),new Color(250,70,22),new Color(60,88,150),new Color(0,56,168),new Color(4,30,66),new Color(252,181,20),new Color(200,16,46),new Color(11,38,61),new Color(0,99,65),new Color(4,30,66),new Color(238,173,30),new Color(0,48,135),new Color(21,71,52),new Color(200,16,46),new Color(0,32,91),new Color(134,38,51),new Color(0,0,0),new Color(200,16,46),new Color(0,98,114),new Color(252,76,2),new Color(252,76,2)};
	private String[] arenas = {"Bell Centre","Canadian Tire Centre","Air Canada Centre","Amalie Arena","BB&T Center","KeyBank Center","TD Garden","Little Caesars Arena","Prudential Center","PNC Arena","Wells Fargo Center","Barclays Center","Madison Square Garden","Nationwide Arena","PPG Paints Arena","Capital One Arena","Pepsi Center","American Airlines Center","Bell MTS Place","Bridgestone Arena","Scottrade Center","Xcel Energy Center","United Center","Rogers Arena","Gila River Arena","Staples Center","Scotiabank Saddledome","SAP Center","Rogers Place","Honda Center"};
	
	private int timerDelay = 10;
	
	private String specialPrint = "";
	private String specialPrint2 = "";
	private String arena;
	private boolean overtime = false;
	private boolean gbOption = false;
	private boolean pause = true;
	
	private boolean quick = false;
	private boolean sim = false;
	
	private boolean gloveSave;
	
	private final Color BLACK = new Color(0,0,0);
	private final Color RED = new Color(255,0,0);
	private final Color BLUE = new Color(0,0,250);
	private final Color lGREY = new Color(181,181,181);
	private final Color dGREY = new Color(80,80,80);
	private Color homeColor = BLACK;
	private Color awayColor = BLACK;
	private Color specialColor1 = lGREY;
	private Color specialColor2 = lGREY;
	
	public SimpleGoalieGUI(String awayTeam, String homeTeam, boolean home) {
		super("Simple Goalie", width, height);
		period = 1;
		min = 20;
		seconds = 0;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		getDetails();
		playerIsHome = home;
		if(playerIsHome) {
			userTeam = homeTeam;
			otherTeam = awayTeam;
		}
		else {
			userTeam = awayTeam;
			otherTeam = homeTeam;
		}
		homeScore = 0;
		awayScore = 0;
		homeShots = 0;
		awayShots = 0;
		startTimer();
		repaint();
	}
	
	public void draw(Graphics g) {
		g.setColor(dGREY);
		g.fillRect(150, 0, 500, 400);
		g.setColor(lGREY);
		g.fillRect(150, 400, 500, 100);
		g.setColor(homeColor);
		int[] xpts = {80,150,150};
		int[] ypts = {0,0,500};
		g.fillPolygon(xpts,ypts,3);
		int[] xpts2 = {720,650,650};
		g.fillPolygon(xpts2,ypts,3);
		g.setColor(lGREY);
		g.fillRect(165, 50, 220, 75);
		g.fillRect(415, 50, 220, 75);
		g.setColor(specialColor1);
		g.fillRect(200, 150, 150, 150);
		g.setColor(lGREY);
		g.setColor(specialColor2);
		g.fillRect(450, 150, 150, 150);
		g.setColor(lGREY);
		g.fillRect(325, 325, 150, 50);
		g.fillRect(375, 250, 50, 50);
		g.setFont(new Font("Ariel",Font.PLAIN,16));
		g.setColor(awayColor);
		printSimpleString(g,awayTeam,275,83);
		g.setColor(homeColor);
		printSimpleString(g,homeTeam,525,83);
		g.setFont(new Font("Ariel",Font.PLAIN,32));
		printSimpleString(g,arena,400,450);
		g.setColor(BLACK);
		if(seconds<10) printSimpleString(g,""+min+":"+"0"+seconds,400,350);
		else printSimpleString(g,""+min+":"+seconds,400,350);
		printSimpleString(g,""+period,400,275);
		g.setFont(new Font("Ariel",Font.PLAIN,64));
		printSimpleString(g,""+awayScore,275,225);
		printSimpleString(g,""+homeScore,525,225);
		g.setFont(new Font("Ariel",Font.PLAIN,32));
		printSimpleString(g,specialPrint,400,650);
		printSimpleString(g,specialPrint2,400,725);
		if(gbOption) {
			g.drawLine(200, 825, 380, 825);
			g.drawLine(200, 925, 380, 925);
			g.drawLine(200, 825, 200, 925);
			g.drawLine(380, 825, 380, 925);
			printSimpleString(g,"Glove",290,875);
			g.drawLine(420, 825, 600, 825);
			g.drawLine(420, 925, 600, 925);
			g.drawLine(420, 825, 420, 925);
			g.drawLine(600, 825, 600, 925);
			printSimpleString(g,"Blocker",510,875);
		}	
	}
	
	private void printSimpleString(Graphics g, String s, int XPos, int YPos){
		int width = g.getFontMetrics().stringWidth(s);
		int height = g.getFontMetrics().getHeight();
        int stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int xStart = XPos - width/2;
        int yStart = YPos + height/4;
        g.drawString(s, xStart, yStart);
	}
	
	public void handleTimer() {
		timer.setDelay(timerDelay);
		if(pause) {
			specialPrint2 = "Click to play";
		}
		else if(gbOption) {
			specialPrint2 = "Glove or Blocker";
		}
		else {
			specialColor1=lGREY;
			specialColor2=lGREY;
			specialPrint = "";
			specialPrint2 = "";
			seconds--;
			if(quick) seconds = seconds - 4;
			if(sim) seconds = seconds - 9;
			if (seconds < 0) {
				min--;
				seconds = 59;
			}
			if (min < 0) {
				periodEnd();
			}
			checkScoringChance();
		}
		repaint();
	}
	
	public void handleKeyPress(char k) {
		if(k == 'f') timerDelay = timerDelay/2;
		if(k == 's') timerDelay = timerDelay*2;
		if(k == 'q') {
			quick = true;
			timerDelay = 1;
		}
		if(k == 'z') {
			sim = true;
			timerDelay = 1;
		}
	}
	
	public void handleMousePress(int x, int y) {
		if(pause) {
			pause = false;
		}
		if(gbOption) {
			if(x>200&&x<380&&y>825&&y<925) {
				if(gloveSave) save();
				else goalAgainst();
			}
			if(x>420&&x<600&&y>825&&y<925) {
				if(!gloveSave) save();
				else goalAgainst();
			}
		}
	}
	
	private void checkScoringChance() {
		int theNum = 250;
		if(quick) theNum = 50;
		if(sim) theNum = 25;
		if(((int)(Math.random()*theNum))==1) {
			int decider = ((int)(Math.random()*100));
			if(playerIsHome) {
				if (decider < 56) homeShots++;
				if (decider >= 56) awayShots++;
				if (decider < 28) userTeamScores();
				if (decider >=56) userPlay();
			}
			if(!playerIsHome) {
				if (decider < 56) homeShots++;
				if (decider >= 56) awayShots++;
				if (decider < 56) userPlay();
				if (decider >=78) userTeamScores();
			}
		}
	}
	
	private void userTeamScores() {
		specialPrint = userTeam + " score!";
		if(playerIsHome) {
			homeScore++;
			specialColor2 = homeColor;
		}
		else {
			awayScore++;
			specialColor1 = awayColor;
		}
		if(overtime) periodEnd();
		if(!sim) pause = true;
	}
	
	private void userPlay() {
		specialPrint = "Scoring chance for the "+otherTeam;
		if(((int)(Math.random()*2))==1) gloveSave = true;
		else gloveSave = false;
		if(!sim) gbOption = true;
		else {
			if(gloveSave) save();
			else goalAgainst();
		}
	}
	
	private void save() {
		specialPrint = "Save!";
		gbOption = false;
		if(!sim) pause = true;
	}
	
	private void goalAgainst() {
		specialPrint = otherTeam + " score!";
		if(playerIsHome) {
			awayScore++;
			specialColor1 = awayColor;
		}
		else {
			homeScore++;
			specialColor2 = homeColor;
		}
		if(overtime) periodEnd();
		gbOption = false;
		if(!sim) pause = true;
	}
	
	private void periodEnd() {
		if(period<3) {
			period++;
			min = 20;
			seconds = 0;
		}
		else {
			min = 0;
			seconds = 0;
			if(homeScore>awayScore) {
				specialPrint = homeTeam+ " win!";
				stopTimer();
			}
			else if(homeScore<awayScore) {
				specialPrint = awayTeam+ " win!";
				stopTimer();
			}
			else {
				period++;
				min = 20;
				seconds = 0;
				overtime = true;
			}
		}
		if(!sim) pause = true;
	}
	
	private void getDetails() {
		int homeIndex = -1;
		int awayIndex = -1;
		for (int i = 0 ; i<30 ; i++) {
			if(teams[i].equals(homeTeam)) homeIndex = i;
			if(teams[i].equals(awayTeam)) awayIndex = i;
		}
		homeColor = colors[homeIndex];
		awayColor = colors[awayIndex];
		arena = arenas[homeIndex];
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
		return homeShots;
	}

	public int getAwayShots() {
		return awayShots;
	}

	public boolean getOvertime() {
		return overtime;
	}
	
	public String getPeriodTime() {
		return "Game Final";
	}
}
