import java.awt.*;
import java.util.*;

import javax.swing.SwingUtilities;

public class GoalieModeGUI extends DrawingGUI {
	private static final int width=800, height=950;
	private ArrayList<Player> awayPlayers;
	private ArrayList<Player> homePlayers;
	private Goalie goalie;
	private Goalie awayGoalie;
	private Puck puck;
	private Point moveTo;
	private boolean playing;
	private boolean started;
	private boolean puckDrop;
	private int passingTo = -1;
	private int hasPuck = -1;
	private boolean shooting = false;
	private boolean rebound = false;
	private boolean rest;
	private boolean goal = false;
	private boolean homeHasPuck;
	private int restLeft = 7;
	private int movingBackTimes = 20;
	private int period;
	private int min;
	private int seconds;
	private String homeTeam;
	private String awayTeam;
	private boolean playerIsHome;
	private int homeScore;
	private int awayScore;
	private int homeShots;
	private int awayShots;
	private boolean awaySave;
	private boolean homeSave;
	private double savePercent = 0.8;
	private double homeFOWinPercent = 0.5;
	private double homeShotProbability = 0.3;
	private double awayShotProbability = 0.3;
	
	private final Color BLACK = new Color(0,0,0);
	private final Color RED = new Color(255,0,0);
	private final Color BLUE = new Color(0,0,250);
	
	private boolean sim = false;
	private boolean endSim = false;
	private boolean homeWins = false;
	private boolean awayWins = false;
	private boolean overtime = false;
	private boolean gameOver = false;
	
	
	public GoalieModeGUI(String awayTeam, String homeTeam, boolean home) {
		super("Goalie Mode", width, height);
		awayPlayers = new ArrayList<Player>();
		homePlayers = new ArrayList<Player>();
		addPlayers();
		goalie = new Goalie(400,910);
		awayGoalie = new Goalie(400,40);
		puck = new Puck();
		playing = true;
		started = false;
		puckDrop = false;
		rest = true;
		period = 1;
		min = 20;
		seconds = 0;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		playerIsHome = home;
		if(playerIsHome) homeFOWinPercent = 0.6;
		else homeFOWinPercent = 0.4;
		homeScore = 0;
		awayScore = 0;
		homeShots = 0;
		awayShots = 0;
		startTimer();
		repaint();
	}
	
	public void addPlayers() {
		awayPlayers.add(new Player(200,550));
		awayPlayers.add(new Player(600,550));
		awayPlayers.add(new Player(400,675));
		awayPlayers.add(new Player(150,750));
		awayPlayers.add(new Player(650,750));
		homePlayers.add(new Player(200,400));
		homePlayers.add(new Player(600,400));
		homePlayers.add(new Player(400,275));
		homePlayers.add(new Player(150,200));
		homePlayers.add(new Player(650,200));
	}
	
	public void draw(Graphics g) {
		g.fillRect(0, 900, 250, 50);
		g.fillRect(550, 900, 250, 50);
		g.fillRect(0, 0, 250, 50);
		g.fillRect(550, 0, 250, 50);
		g.setFont(new Font("Ariel",Font.PLAIN,12));
		printSimpleString(g,"g",300,930);
		printSimpleString(g,"f",400,930);
		printSimpleString(g,"b",500,930);
		g.setFont(new Font("Ariel",Font.PLAIN,28));
		printSimpleString(g,"Period "+period,75,440);
		printSimpleString(g,getMin()+":"+getSec(),75,510);
		if(playerIsHome) {
			printSimpleString(g,awayTeam,625,510);
			printSimpleString(g,homeTeam,625,440);
			printSimpleString(g,"Home",760,315);
		}
		else {
			printSimpleString(g,homeTeam,700,510);
			printSimpleString(g,awayTeam,700,440);
			printSimpleString(g,"Home",760,635);
		}
		g.setFont(new Font("Ariel",Font.BOLD,72));
		printSimpleString(g,""+homeScore,760,395);
		printSimpleString(g,""+awayScore,760,555);
		g.setFont(new Font("Ariel",Font.PLAIN,24));
		printSimpleString(g,""+homeShots,760,353);
		printSimpleString(g,""+awayShots,760,595);
		g.setColor(RED);
		g.fillRect(240,900,10,50);
		g.fillRect(550,900,10,50);
		g.fillRect(240,940,320,10);
		g.fillRect(240,0,10,50);
		g.fillRect(550,0,10,50);
		g.fillRect(240,0,320,10);
		g.fillRect(0, 460, 800, 30);
		g.setColor(BLUE);
		g.fillOval(385, 460, 30, 30);
		for(Player p : awayPlayers) {
			if(p.hasPuck()) {
				g.setColor(BLACK);
				g.fillOval((int)(p.getX()-(p.getR()*1.5)),(int)(p.getY()-(p.getR()*1.5)), p.getR()*3, p.getR()*3);
			}
			g.setColor(RED);
			g.fillOval(p.getX()-p.getR(), p.getY()-p.getR(), p.getR()*2, p.getR()*2);
		}
		if(awayGoalie!=null) g.fillRect((int)(awayGoalie.getX()-(awayGoalie.getWidth()/2)), (int)(awayGoalie.getY()-(awayGoalie.getHeight()/2)), awayGoalie.getWidth(), awayGoalie.getHeight());
		for(Player p : homePlayers) {
			if(p.hasPuck()) {
				g.setColor(BLACK);
				g.fillOval((int)(p.getX()-(p.getR()*1.5)),(int)(p.getY()-(p.getR()*1.5)), p.getR()*3, p.getR()*3);
			}
			g.setColor(BLUE);
			g.fillOval(p.getX()-p.getR(), p.getY()-p.getR(), p.getR()*2, p.getR()*2);
		}
		if(goalie!=null) g.fillRect((int)(goalie.getX()-(goalie.getWidth()/2)), (int)(goalie.getY()-(goalie.getHeight()/2)), goalie.getWidth(), goalie.getHeight());
		g.setColor(BLACK);
		g.fillOval((int)(puck.getX()-puck.getR()), (int)(puck.getY()-puck.getR()), puck.getR()*2, puck.getR()*2);
		if(rest&&!goal) {
			g.setFont(new Font("TimesRoman",Font.BOLD,28));
			g.drawString("Play begins in "+restLeft, 300, 725);
		}
		if(goal) {
			g.setFont(new Font("TimesRoman",Font.BOLD,36));
			g.drawString("GOAL!!!", 335, 725);
		}
		if(homeWins) {
			g.setFont(new Font("TimesRoman",Font.BOLD,36));
			g.drawString("Home Wins!!!", 325, 725);
		}
		if(awayWins) {
			g.setFont(new Font("TimesRoman",Font.BOLD,36));
			g.drawString("Away Wins!!!", 325, 725);
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
	
	public void handleKeyPress(char k) {
		if(k == 'g') goalie.Glove();
		if(k == 'b') goalie.Blocker();
		if(k == 'f') goalie.FiveHole();
		if(k == 'p') pausePlay();
		if(k == 'z') {
			if(rest&&goalie!=null) {
				goalie = null;
				homeFOWinPercent += 0.3;
				homeShotProbability += 0.2;
			}
		}
		if(k == 'a') {
			if(rest&&goalie==null) {
				goalie = new Goalie(400,910);
				homeFOWinPercent -= 0.3;
				homeShotProbability -= 0.2;
			}
		}
		if(k == 's') {
			if(!sim) {
				sim=true;
				timer.setDelay(1);
			}
			else {
				endSim = true;
			}
		}
		repaint();
	}
	
	public void pausePlay() {
		if(playing) {
			timer.stop();
			playing = false;
		}
		else {
			timer.start();
			playing = true;
		}
	}
	
	public void handleMouseMotion(int x, int y) {
		if(playing&&goalie!=null&&!sim) goalie.setX(x);
		repaint();
	}
	
	public void handleTimer() {
		if(rest) {	
			if(endSim) {
				sim = false;
				endSim = false;
				if(goalie!=null) goalie.FiveHole();
			}
			if(goal) {
				if(!sim) timer.setDelay(1000);
				else timer.setDelay(1);
				restLeft--;
				if(restLeft == 0) {
					goal = false;
					restLeft = 5;
				}
			}
			else {
				if(!sim) timer.setDelay(1000);
				else timer.setDelay(1);
				reset();
				awayPlayers.get(0).setX(400);
				awayPlayers.get(0).setY(505);
				homePlayers.get(1).setX(400);
				homePlayers.get(1).setY(445);
				restLeft--;
				if(restLeft == 0) {
					rest = false;
					restLeft = 5;
					//
					if(!sim) timer.setDelay(100);
					else timer.setDelay(1);
				}
			}
		}
		else {
			if(!started) {
				if(!puckDrop) {
					puck = new Puck();
					puck.passing();
					boolean homeFOWin;
					if((Math.random())<=homeFOWinPercent) homeFOWin = true;
					else homeFOWin = false;
					if(homeFOWin) {
						hasPuck = 6;
						homeHasPuck = true;
						homePlayers.get(1).givePuck();
						puck.set(homePlayers.get(1).getX(), homePlayers.get(1).getY());
					}
					else {
						hasPuck = 0;
						homeHasPuck = false;
						awayPlayers.get(0).givePuck();
						puck.set(awayPlayers.get(0).getX(), awayPlayers.get(0).getY());
					}
					puckDrop = true;
				}
				homePlayers.get(1).setX(homePlayers.get(1).getX()+10);
				homePlayers.get(1).setY(homePlayers.get(1).getY()-2);
				awayPlayers.get(0).setX(awayPlayers.get(0).getX()-10);
				awayPlayers.get(0).setY(awayPlayers.get(0).getY()+2);
				movingBackTimes--;
				if(movingBackTimes==0) {
					homePlayers.get(1).setX(600);
					homePlayers.get(1).setY(400);
					awayPlayers.get(0).setX(200);
					awayPlayers.get(0).setY(550);
					started = true;
					movingBackTimes = 20;
				}
				if(homeHasPuck) puck.set(homePlayers.get(1).getX(), homePlayers.get(1).getY());
				else puck.set(awayPlayers.get(0).getX(), awayPlayers.get(0).getY());
			}
			else {
				if(homeHasPuck) {
					if((moveTo!=null)&&!(rebound)) {
						puck.move();
						if(shooting) {
							if(awayGoalie!=null) {
								if(awaySave) {
									awayGoalie.all();
									awayGoalie.setX(Math.max(275, Math.min(525,(int)moveTo.getX())));
								}
								else {
									awayGoalie.all();
									if(moveTo.getX()>=400) awayGoalie.setX(275);
									else awayGoalie.setX(525);
								}
							}
							if(puck.getY()==15) {
								goal(true);
							}
							else if(puck.getY()>=54&&puck.getY()<=56) {
								if(awayGoalie!=null&&puck.getX()<=(awayGoalie.getX()+(awayGoalie.getWidth()/2))&&puck.getX()>=(awayGoalie.getX()-(awayGoalie.getWidth()/2))) {
									homeShots++;
									puck.flipDY();
								}
								else if(puck.getX()<=250||puck.getX()>=550) {
									puck.flipDY();
								}
							}
							else {
								for(Player p : homePlayers) {
									if(puck.getX()>=p.getX()-p.getR()&&puck.getX()<=p.getX()+p.getR()&&puck.getY()>=p.getY()-p.getR()&&puck.getY()<=p.getY()+p.getR()) {
										shooting = false;
										rebound = true;
									}
								}
								if(puck.getX()==5||puck.getX()==795) puck.flipDX();
								if(puck.getY()>=455) rest = true;
							}
						}
						else {
							if(puck.getX()<=moveTo.getX()+0.5&&puck.getX()>=moveTo.getX()-0.5&&puck.getY()<=moveTo.getY()+0.5&&puck.getY()>=moveTo.getY()-0.5) {
								if(passingTo!=-1) {
									homePlayers.get(passingTo-5).givePuck();
									hasPuck = passingTo;
									passingTo = -1;
									moveTo = null;
								}
							}
						}
					}
					else {
						if((Math.random())<=homeShotProbability||rebound) {
							rebound = false;
							homeShoot();
						}
						else {
							puck.passing();
							passingTo = (int)(Math.random()*5)+5;
							moveTo = new Point(homePlayers.get(passingTo-5).getX(), homePlayers.get(passingTo-5).getY());
							puck.setMoveTo((int)moveTo.getX(), (int)moveTo.getY(), true);	
							homePlayers.get(hasPuck-5).noPuck();
							hasPuck = -1;
						}
					}
				}
				else {
					if((moveTo!=null)&&!(rebound)) {
						puck.move();
						if(shooting) {
							if(sim) {
								if(goalie!=null) {
									if(homeSave) {
										goalie.all();
										goalie.setX(Math.max(275, Math.min(525,(int)moveTo.getX())));
									}
									else {
										goalie.all();
										if(moveTo.getX()>=400) goalie.setX(275);
										else goalie.setX(525);
									}
								}
							}
							if(puck.getY()==935) {
								goal(false);
							}
							else if(puck.getY()>=894&&puck.getY()<=896) {
								if(goalie!=null&&puck.getX()<=(goalie.getX()+(goalie.getWidth()/2))&&puck.getX()>=(goalie.getX()-(goalie.getWidth()/2))) {
									awayShots++;
									puck.flipDY();
								}
								else if(puck.getX()<=250||puck.getX()>=550) {
									puck.flipDY();
								}
							}
							else {
								for(Player p : awayPlayers) {
									if(puck.getX()>=p.getX()-p.getR()&&puck.getX()<=p.getX()+p.getR()&&puck.getY()>=p.getY()-p.getR()&&puck.getY()<=p.getY()+p.getR()) {
										shooting = false;
										rebound = true;
									}
								}
								if(puck.getX()==5||puck.getX()==795) puck.flipDX();
								if(puck.getY()<=495) rest = true;
							}
						}
						else {
							if(puck.getX()<=moveTo.getX()+0.5&&puck.getX()>=moveTo.getX()-0.5&&puck.getY()<=moveTo.getY()+0.5&&puck.getY()>=moveTo.getY()-0.5) {
								if(passingTo!=-1) {
									awayPlayers.get(passingTo).givePuck();
									hasPuck = passingTo;
									passingTo = -1;
									moveTo = null;
								}
							}
						}
					}
					else {
						if((Math.random())<=awayShotProbability||rebound) {
							rebound = false;
							awayShoot();
						}
						else {
							puck.passing();
							passingTo = (int)(Math.random()*5);
							moveTo = new Point(awayPlayers.get(passingTo).getX(), awayPlayers.get(passingTo).getY());
							puck.setMoveTo((int)moveTo.getX(), (int)moveTo.getY(), true);	
							awayPlayers.get(hasPuck).noPuck();
							hasPuck = -1;
						}
					}
				}
				
			}
			seconds--;
			if(seconds==-1) {
				min--;
				seconds = 59;
			}
			if(min < 0) {
				endPeriod();
			}
			if(period==3&&min<=4) {
				pullAwayGoalie();
				replaceAwayGoalie();
			}
		}
		repaint();
	}
	
	public void awayShoot() {
		shooting = true;
		int xSpot = ((int)(Math.random()*400))+200;
		int ySpot = 950;
		puck.shooting();
		moveTo = new Point(xSpot,ySpot);
		puck.setMoveTo(xSpot, ySpot, false);
		if(hasPuck!=-1) awayPlayers.get(hasPuck).noPuck();
		hasPuck = -1;
		if(sim) {
			if(Math.random()<=savePercent) homeSave = true;
			else homeSave = false;
		}
	}
	
	public void homeShoot() {
		shooting = true;
		int xSpot = ((int)(Math.random()*400))+200;
		int ySpot = 0;
		puck.shooting();
		moveTo = new Point(xSpot,ySpot);
		puck.setMoveTo(xSpot, ySpot, true);
		if(hasPuck>=5) homePlayers.get(hasPuck-5).noPuck();
		hasPuck = -1;
		if(Math.random()<=savePercent) awaySave = true;
		else awaySave = false;
	}
	
	public void goal(boolean home) {
		goal = true;
		rest = true;
		if(home) {
			homeScore++;
			homeShots++;
		}
		else {
			awayScore++;
			awayShots++;
		}
		if(overtime) {
			endPeriod();
		}
	}
	
	public void reset(){
		puck = new Puck();
		moveTo = null;
		playing = true;
		started = false;
		puckDrop = false;
		passingTo = -1;
		hasPuck = -1;
		shooting = false;
		rebound = false;
		for(Player p : awayPlayers)
			p.noPuck();
		for(Player p : homePlayers)
			p.noPuck();
	}
	
	public String getMin() {
		if(min>=10) {
			return ""+min;
		}
		else {
			return "0"+min;
		}
	}
	
	public String getSec() {
		if(seconds>=10) {
			return ""+seconds;
		}
		else {
			return "0"+seconds;
		}
	}
	
	public void endPeriod() {
		if(period>=3) {
			if(homeScore>awayScore) {
				homeWins = true;
				gameOver = true;
				timer.stop();
			}
			else if(homeScore<awayScore) {
				awayWins = true;
				gameOver = true;
				timer.stop();
			}
			else {
				overtime = true;
				period++;
				min = 20;
				seconds = 0;
				rest = true;
			}
		}
		else {
			period++;
			min = 20;
			seconds = 0;
			rest = true;
		}
		repaint();
	}
	
	public void pullAwayGoalie() {
		if(awayScore==homeScore-1&&rest&&awayGoalie!=null) {
			awayGoalie = null;
			homeFOWinPercent -= 0.3;
			awayShotProbability += 0.2;
		}
	}
	
	public boolean stillPlaying() {
		return !gameOver;
	}
	
	public void replaceAwayGoalie() {
		if(awayScore>=homeScore&&rest&&awayGoalie==null) {
			awayGoalie = new Goalie(400,40);
			homeFOWinPercent += 0.3;
			awayShotProbability -= 0.2;
		}
	}
	
	public String getHomeTeam() {
		return homeTeam;
	}
	
	public String getAwayTeam() {
		return awayTeam;
	}
	
	public int getHomeScore() {
		if(playerIsHome) return homeScore;
		else return awayScore;
	}
	
	public int getAwayScore() {
		if(playerIsHome) return awayScore;
		else return homeScore;
	}
	
	public int getHomeShots() {
		if(playerIsHome) return homeShots;
		else return awayShots;
	}

	public int getAwayShots() {
		if(playerIsHome) return awayShots;
		else return homeShots;
	}
	
	public String getPeriodTime() {
		if(stillPlaying()) {
			return "Period "+period+", "+min+":"+seconds;
		}
		else return "Game Final";
	}
	
	public boolean getOvertime() {
		return overtime;
	}
	
	public boolean playerIsHome() {
		return playerIsHome;
	}
	
	public void quit() {
		dispose();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GoalieModeGUI("Away","Home",true);
			}
		});
	}
}
