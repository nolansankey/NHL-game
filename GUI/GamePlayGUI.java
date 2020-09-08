import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.*;
import java.util.*;

import javax.swing.SwingUtilities;

public class GamePlayGUI extends DrawingGUI {
	private static final int width=1600, height=950;
	private String mode;
	private GoalieModeGUI goalieMode;
	private SimpleGoalieGUI simpleGoalie;
	private SimGame game;
	private SimGame realGame;
	private nhlInfo seasonInfo;
	private int year = 2017;
	private String name = "";
	private int games = -1;
	private String gamesToPlayString = "";
	private int gamesToPlay = 0;
	private int gamesPlayed = 0;
	private boolean draft = true;
	private boolean teamChangeUsed = false;
	private String team = "";
	private int  teamID = -1;
	private int playoffRound = 0;
	private int playoffRoundGame = 0;
	private String playoffOpponent = "";
	
	private final Color BLACK = new Color(0,0,0);
	private final Color RED = new Color(255,0,0);
	
	private int[] todaysPoints;
	private String homeTeam;
	private String awayTeam;
	private int awayGoals;
	private int homeGoals;
	private int awayShots;
	private int homeShots;
	private String periodTime;
	private boolean overtime;
	private String RhomeTeam;
	private String RawayTeam;
	private int RawayGoals;
	private int RhomeGoals;
	private int RawayShots;
	private int RhomeShots;
	private String RperiodTime;
	private boolean Rovertime;
	
	private boolean enteringName = false;
	private boolean enteringGamesToPlay = false;
	private boolean enteringTeam = false;
	
	private boolean opening = true;
	private boolean settings = false;
	private boolean teamSelect = false;
	private boolean menu = false;
	private boolean inGame = false;
	private boolean simGame = false;
	private boolean gameInfo = false;
	private boolean schedule = false;
	private boolean standings = false;
	private boolean playoffs = false;
	private boolean inPlayoffs = false;
	private boolean playoffBracket = false;
	private boolean seasonEnd = false;
	
	public GamePlayGUI() {
		super("Hockey", width, height);
		seasonInfo = new nhlInfo();
	}
	
	public void draw(Graphics g) {
		if(opening) {
			normalFont(g);
			g.drawLine(0, 150, 1600, 150);
			g.drawLine(0, 550, 800, 550);
			g.drawLine(800, 150, 800, 950);
			printSimpleString(g,"Select Mode",800,75);
			printSimpleString(g,"Goalie (Simple)",400,350);
			printSimpleString(g,"Goalie (Advanced)",400,750);
			printSimpleString(g,"Sim",1200,550);
		}
		else if(settings) {
			normalFont(g);
			g.drawLine(0, 225, 1600, 225);
			g.drawLine(0, 375, 1600, 375);
			g.drawLine(0, 525, 1600, 525);
			g.drawLine(0, 675, 1600, 675);
			g.fillRect(0, 825, 1600, 950);
			g.drawLine(600, 225, 600, 825);
			g.drawLine(850, 375, 850, 525);
			g.drawLine(1100, 375, 1100, 525);
			g.drawLine(1350, 375, 1350, 525);
			g.drawLine(1100, 675, 1100, 825);
			printSimpleString(g,"Settings",800,125);
			printSimpleString(g,"Name",300,300);
			printSimpleString(g,"Games",300,450);
			printSimpleString(g,"Games Played",300,600);
			printSimpleString(g,"Team Select",300,750);
			if(!enteringName) g.setColor(RED);
			printSimpleString(g,name,1100,300);
			normalFont(g);
			if(games == 1) g.setColor(RED);
			printSimpleString(g,"1",725,450);
			normalFont(g);
			if(games == 10) g.setColor(RED);
			printSimpleString(g,"10",975,450);
			normalFont(g);
			if(games == 40) g.setColor(RED);
			printSimpleString(g,"40",1225,450);
			normalFont(g);
			if(games == 82) g.setColor(RED);
			printSimpleString(g,"82",1475,450);
			normalFont(g);
			if(!enteringGamesToPlay) {
				g.setColor(RED);
				if(gamesToPlay!=0) printSimpleString(g,""+gamesToPlay,1100,600);
				normalFont(g);
			}
			else {
				printSimpleString(g,gamesToPlayString,1100,600);
			}
			if(draft) g.setColor(RED);
			printSimpleString(g,"NHL Draft",850,750);
			normalFont(g);
			if(!draft) g.setColor(RED);
			printSimpleString(g,"PickTeam",1350,750);
			normalFont(g);
		}
		else if(teamSelect) {
			normalFont(g);
			printSimpleString(g,"Team",800,400);
			if(draft&&team == "") team = draft();
			if(draft) printSimpleString(g,team,800,550);
			if(!draft) {
				if(!enteringTeam) g.setColor(RED);
				printSimpleString(g,team,800,550);
				normalFont(g);
			}
		}
		else if(menu) {
			normalFont(g);
			g.drawLine(0, 250, 1600, 250);
			g.drawLine(800, 250, 800, 950);
			g.setFont(new Font("Ariel",Font.BOLD,46));
			printSimpleString(g,name,800,125);
			normalFont(g);
			printSimpleString(g,""+year,75,125);
			printSimpleString(g,team,350,125);
			if(!playoffs) {
				printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[seasonInfo.getTeamMap().get(team)])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[seasonInfo.getTeamMap().get(team)])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[seasonInfo.getTeamMap().get(team)]),1500,125);
				String division = "";
				int place = 1;
				refreshStandings();
				if(seasonInfo.getTeamMap().get(team)<8) {
					division = "Atlantic";
					while(seasonInfo.nextAtlanticStandings()!=seasonInfo.getTeamMap().get(team)) place++;
				}
				else if(seasonInfo.getTeamMap().get(team)>7&&seasonInfo.getTeamMap().get(team)<16) {
					division = "Metropolitan";
					while(seasonInfo.nextMetroStandings()!=seasonInfo.getTeamMap().get(team)) place++;
				}
				else if(seasonInfo.getTeamMap().get(team)>15&&seasonInfo.getTeamMap().get(team)<23) {
					division = "Central";
					while(seasonInfo.nextCentralStandings()!=seasonInfo.getTeamMap().get(team)) place++;
				}
				else if(seasonInfo.getTeamMap().get(team)>22) {
					division = "Pacific";
					while(seasonInfo.nextPacificStandings()!=seasonInfo.getTeamMap().get(team)) place++;
				}
				printSimpleString(g,"#"+place+" "+division,1250,125);
				printSimpleString(g,"Standings",1200,600);
			}
			else {
				if(playoffRound==1) printSimpleString(g,"Playoffs: First Round",1250,125);
				if(playoffRound==2) printSimpleString(g,"Playoffs: Second Round",1250,125);
				if(playoffRound==3) printSimpleString(g,"Playoffs: Conference Final",1250,125);
				if(playoffRound==4) printSimpleString(g,"Playoffs: Stanley Cup",1250,125);
				if(inPlayoffs) {
					if(playoffRoundGame==0) printSimpleString(g,"0-0",1500,125);
					else printSimpleString(g,""+seasonInfo.getPlayoffWins().get(seasonInfo.getPlayoffID().get(team))+"-"+seasonInfo.getPlayoffWins().get(seasonInfo.getPlayoffID().get(playoffOpponent)),1500,125);
				}
				else if (playoffRound==1&&seasonInfo.inPlayoffs(team)) printSimpleString(g,"0-0",1500,125);
				printSimpleString(g,"Playoff Bracket",1200,425);
				printSimpleString(g,"Standings",1200,775);
				g.drawLine(800, 600, 1600, 600);
			}
			printSimpleString(g,"Play",400,600);
			
		}
		else if(gameInfo) {
			normalFont(g);
			printSimpleString(g,"Game Info",800,150);
			printSimpleString(g,RawayTeam,400,350);
			printSimpleString(g,RhomeTeam,1200,350);
			g.setFont(new Font("Ariel",Font.BOLD,140));
			printSimpleString(g,""+RawayGoals,400,475);
			printSimpleString(g,""+RhomeGoals,1200,475);
			normalFont(g);
			printSimpleString(g,""+RawayShots,400,600);
			printSimpleString(g,""+RhomeShots,1200,600);
			printSimpleString(g,RperiodTime,800,800);
			if(Rovertime) printSimpleString(g,"Overtime",800,850);
		}
		else if(standings) {
			refreshStandings();
			normalFont(g);
			g.drawLine(800, 0, 800, 950);
			g.drawLine(0, 475, 1600, 475);
			g.setColor(RED);
			printSimpleString(g,"Atlantic",400,25);
			g.setColor(BLACK);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,75);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,75);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,75);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,125);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,125);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,125);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,175);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,175);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,175);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,225);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,225);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,225);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,275);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,275);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,275);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,325);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,325);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,325);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,375);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,375);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,375);
			teamID = seasonInfo.nextAtlanticStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,425);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,425);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,425);
			g.setColor(RED);
			printSimpleString(g,"Metropolitan",400,500);
			g.setColor(BLACK);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,550);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,550);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,550);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,600);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,600);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,600);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,650);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,650);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,650);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,700);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,700);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,700);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,750);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,750);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,750);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,800);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,800);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,800);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,850);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,850);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,850);
			teamID = seasonInfo.nextMetroStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],200,900);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),550,900);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),700,900);
			g.setColor(RED);
			printSimpleString(g,"Central",1200,25);
			g.setColor(BLACK);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,75);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,75);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,75);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,125);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,125);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,125);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,175);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,175);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,175);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,225);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,225);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,225);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,275);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,275);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,275);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,325);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,325);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,325);
			teamID = seasonInfo.nextCentralStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,375);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,375);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,375);
			g.setColor(RED);
			printSimpleString(g,"Pacific",1200,500);
			g.setColor(BLACK);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,550);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,550);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,550);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,600);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,600);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,600);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,650);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,650);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,650);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,700);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,700);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,700);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,750);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,750);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,750);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,800);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,800);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,800);
			teamID = seasonInfo.nextPacificStandings();
			printSimpleString(g,seasonInfo.getTeams()[teamID],1000,850);
			printSimpleString(g,seasonInfo.getWins().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getLoss().get(seasonInfo.getTeams()[teamID])+"-"+seasonInfo.getOTL().get(seasonInfo.getTeams()[teamID]),1350,850);
			printSimpleString(g,""+seasonInfo.getPoints().get(seasonInfo.getTeams()[teamID]),1500,850);
		}
		else if(playoffBracket) {
			g.setFont(new Font("Ariel",Font.BOLD,25));
			printSimpleString(g,"A1",30,150);
			printSimpleString(g,"A2",30,250);
			printSimpleString(g,"A3",30,300);
			if(seasonInfo.isaHigh()) printSimpleString(g,"WC2",30,200);
			else printSimpleString(g,"WC1",30,200);
			printSimpleString(g,"M1",30,350);
			printSimpleString(g,"M2",30,450);
			printSimpleString(g,"M3",30,500);
			if(seasonInfo.isaHigh()) printSimpleString(g,"WC1",30,400);
			else printSimpleString(g,"WC2",30,400);
			printSimpleString(g,"C1",30,550);
			printSimpleString(g,"C2",30,650);
			printSimpleString(g,"C3",30,700);
			if(seasonInfo.iscHigh()) printSimpleString(g,"WC2",30,600);
			else printSimpleString(g,"WC1",30,600);
			printSimpleString(g,"P1",30,750);
			printSimpleString(g,"P2",30,850);
			printSimpleString(g,"P3",30,900);
			if(seasonInfo.isaHigh()) printSimpleString(g,"WC1",30,800);
			else printSimpleString(g,"WC2",30,800);
			if(playoffRound>=1) {
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(1),220,150);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(2),220,200);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(3),220,250);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(4),220,300);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(5),220,350);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(6),220,400);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(7),220,450);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(8),220,500);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(9),220,550);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(10),220,600);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(11),220,650);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(12),220,700);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(13),220,750);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(14),220,800);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(15),220,850);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(16),220,900);
				g.setColor(RED);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(1),415,150);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(2),415,200);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(3),415,250);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(4),415,300);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(5),415,350);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(6),415,400);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(7),415,450);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(8),415,500);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(9),415,550);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(10),415,600);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(11),415,650);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(12),415,700);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(13),415,750);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(14),415,800);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(15),415,850);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(16),415,900);
				g.setColor(BLACK);
			}
			if(playoffRound>=2) {
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(21),610,175);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(22),610,275);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(23),610,375);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(24),610,475);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(25),610,575);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(26),610,675);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(27),610,775);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(28),610,875);
				g.setColor(RED);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(21),805,175);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(22),805,275);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(23),805,375);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(24),805,475);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(25),805,575);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(26),805,675);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(27),805,775);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(28),805,875);
				g.setColor(BLACK);
			}
			if(playoffRound>=3) {
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(31),1000,225);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(32),1000,425);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(33),1000,625);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(34),1000,825);
				g.setColor(RED);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(31),1195,225);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(32),1195,425);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(33),1195,625);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(34),1195,825);
				g.setColor(BLACK);
			}
			if(playoffRound>=4) {
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(41),1390,325);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(42),1390,725);
				g.setColor(RED);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(41),1585,325);
				printSimpleString(g,""+seasonInfo.getPlayoffWins().get(42),1585,725);
				g.setColor(BLACK);
			}
			if(playoffRound>=5) {
				g.setColor(RED);
				printSimpleString(g,seasonInfo.getPlayoffStructure().get(50),1300,900);
			}
			normalFont(g);
			printSimpleString(g,"Playoff Bracket",800,75);
			g.drawLine(0,125,1600,125);
			g.drawLine(430,125,430,950);
			g.drawLine(820,125,820,950);
			g.drawLine(1210,125,1210,860);
			g.drawLine(0,225,430,225);
			g.drawLine(0,325,820,325);
			g.drawLine(0,425,430,425);
			g.drawLine(0,525,1210,525);
			g.drawLine(0,625,430,625);
			g.drawLine(0,725,820,725);
			g.drawLine(0,825,430,825);
			g.fillRect(0, 925, 1050, 25);
			g.fillRect(1050, 940, 500, 10);
			g.fillRect(1550, 925, 50, 25);
			g.setColor(RED);
			g.drawLine(1050, 860, 1050, 940);
			g.drawLine(1550, 860, 1550, 940);
			g.drawLine(1050, 860, 1550, 860);
			g.drawLine(1050, 940, 1550, 940);
			g.setColor(BLACK);
			normalFont(g);
		}
		else if(seasonEnd) {
			normalFont(g);
			printSimpleString(g,"Season Over",800,125);
			printSimpleString(g,"Next Season",400,600);
			printSimpleString(g,"Request Trade",1200,600);
			g.drawLine(0, 250, 1600, 250);
			g.drawLine(800, 250, 800, 950);
			
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
	
	public void normalFont(Graphics g) {
		g.setFont(new Font("Ariel",Font.BOLD,36));
		g.setColor(BLACK);
	}
	
	public void handleKeyPress(char k) {
		if(enteringName) {
			if(k != '\n') {
				name = name + k;
				if(k == '\b') name = "";
			}
			else enteringName = false;
		}
		else if(enteringGamesToPlay) {
			if(k != '\n') {
				gamesToPlayString = gamesToPlayString + k;
				if(k == '\b') gamesToPlayString = "";
			}
			else {
				enteringGamesToPlay = false;
				gamesToPlay = Integer.parseInt(gamesToPlayString);
			}
		}
		else if(enteringTeam) {
			if(k != '\n') {
				team = team + k;
				if(k == '\b') team = "";
			}
			else {
				enteringTeam = false;
			}
		}
		else if(k=='q') {
			
		}
		else if(k == 'z') {
			if(teamSelect&&!teamChangeUsed) {
				team = "";
				teamChangeUsed = true;
			}
		}
		else if(k=='\n') {
			goOn();
		}
		repaint();
	}
	
	public void handleMousePress(int x, int y) {
		if(opening) {
			if(x<=800) {
				if(y>=150 && y<=550) mode = "simpleGOALIE";
				if(y>=550) mode = "advGOALIE";
			}
			else {
				mode = "SIM";
			}
			opening = false;
			settings = true;
		}
		else if(settings) {
			if(y>225&&y<375) {
				if(x>600) enteringName = true;
			}
			else if(y>375&&y<525) {
				if(x>600&&x<850) games = 1;
				else if(x>850&&x<1100) games = 10;
				else if(x>1100&&x<1350) games = 40;
				else if(x>1350&&x<1600) games = 82;
			}
			else if(y>525&&y<675) {
				if(x>600) enteringGamesToPlay = true;
			}
			else if(y>675&&y<825) {
				if(x>600&&x<1100) draft = true;
				else if(x>1100&&x<1600) draft = false;
			}
		}
		else if(teamSelect&&!draft) {
			enteringTeam = true;
		}
		else if(menu) {
			if(!inGame) {
				if(y>250&&x<800) {
					playGame();
				}
				else if(x>800&&y>250&&y<600) {
					menu = false;
					if(playoffs) playoffBracket=true;
					else standings = true;
				}
				else if(x>800&&y>600&&y<950) {
					menu = false;
					standings = true;
				}
			}
			else {
				goOn();
			}
		}
		else if(seasonEnd) {
			if(y>250&&x<800) {
				nextSeason();
			}
			else if(y>250&&x>800) {
				requestTrade();
			}
		}
		repaint();
	}
	
	public void playGame() {
		if(!playoffs) {
			seasonInfo.buildScheduleMatrix();
			String[] todaysGames = seasonInfo.getScheduleMatrix();
			todaysPoints = new int[30];
			for(int i = 0 ; i<30 ; i=i+2) {
				if(todaysGames[i].equals(team)) {
					if(games-gamesPlayed<=gamesToPlay) {
						if(mode.equals("advGOALIE")) {
							goalieMode = new GoalieModeGUI(team,todaysGames[i+1],false);
							inGame = true;
						}
						else if(mode.equals("simpleGOALIE")) {
							simpleGoalie = new SimpleGoalieGUI(team,todaysGames[i+1],false);
							inGame = true;
						}
						else {
							menu = false;
							gameInfo = true;
							realGame = new SimGame(team,todaysGames[i+1]);
							RhomeTeam = realGame.getHomeTeam();
							RawayTeam = realGame.getAwayTeam();
							RhomeGoals = realGame.getHomeScore();
							RawayGoals = realGame.getAwayScore();
							RhomeShots = realGame.getHomeShots();
							RawayShots = realGame.getAwayShots();
							RperiodTime = "Game Final";
							Rovertime = realGame.getOvertime();
							givePoints(RawayTeam,RhomeTeam,RawayGoals,RhomeGoals,Rovertime);
							inGame = true;
							repaint();
						}	
					}
					else {
						simGame = true;
						game = new SimGame(team,todaysGames[i+1]);
						homeTeam = game.getHomeTeam();
						awayTeam = game.getAwayTeam();
						homeGoals = game.getHomeScore();
						awayGoals = game.getAwayScore();
						homeShots = game.getHomeShots();
						awayShots = game.getAwayShots();
						periodTime = "Game Final";
						overtime = game.getOvertime();
					}
				}
				else if(todaysGames[i+1].equals(team)) {
					if(games-gamesPlayed<=gamesToPlay) {
						if(mode.equals("advGOALIE")) {
							goalieMode = new GoalieModeGUI(todaysGames[i],team,true);
							inGame = true;
						}
						else if(mode.equals("simpleGOALIE")) {
							simpleGoalie = new SimpleGoalieGUI(todaysGames[i],team,true);
							inGame = true;
						}
						else {
							menu = false;
							gameInfo = true;
							realGame = new SimGame(todaysGames[i],team);
							RhomeTeam = realGame.getHomeTeam();
							RawayTeam = realGame.getAwayTeam();
							RhomeGoals = realGame.getHomeScore();
							RawayGoals = realGame.getAwayScore();
							RhomeShots = realGame.getHomeShots();
							RawayShots = realGame.getAwayShots();
							RperiodTime = "Game Final";
							Rovertime = realGame.getOvertime();
							givePoints(RawayTeam,RhomeTeam,RawayGoals,RhomeGoals,Rovertime);
							inGame = true;
							repaint();
						}	
					}
					else {
						simGame = true;
						game = new SimGame(todaysGames[i],team);
						homeTeam = game.getHomeTeam();
						awayTeam = game.getAwayTeam();
						homeGoals = game.getHomeScore();
						awayGoals = game.getAwayScore();
						homeShots = game.getHomeShots();
						awayShots = game.getAwayShots();
						periodTime = "Game Final";
						overtime = game.getOvertime();
					}
				}
				else {
					simGame = true;
					game = new SimGame(todaysGames[i],todaysGames[i+1]);
					homeTeam = game.getHomeTeam();
					awayTeam = game.getAwayTeam();
					homeGoals = game.getHomeScore();
					awayGoals = game.getAwayScore();
					homeShots = game.getHomeShots();
					awayShots = game.getAwayShots();
					periodTime = "Game Final";
					overtime = game.getOvertime();
				}
				if(simGame) givePoints(awayTeam,homeTeam,awayGoals,homeGoals,overtime);
				simGame = false;
			}
			gamesPlayed++;
			if(!inGame) {
				addPoints();
				if(gamesPlayed==games) {
					playoffs = true;
					seasonInfo.setPlayoffs();
					playoffRound=1;
				}
			}
			
		}
		else {
			playoffRoundGame++;
			if(playoffRoundGame<=7) {
				inPlayoffs = false;
				if(playoffRound==1) {
					for(int i = 1 ; i<17 ; i=i+2) {
						if(seasonInfo.getPlayoffStructure().get(i).equals(team)||seasonInfo.getPlayoffStructure().get(i+1).equals(team)) inPlayoffs = true;
						if(playoffRoundGame==1||playoffRoundGame==2||playoffRoundGame==5||playoffRoundGame==7) {
							homeTeam = seasonInfo.getPlayoffStructure().get(i);
							awayTeam = seasonInfo.getPlayoffStructure().get(i+1);
						}
						else {
							awayTeam = seasonInfo.getPlayoffStructure().get(i);
							homeTeam = seasonInfo.getPlayoffStructure().get(i+1);
						}
						playPlayoffGame();
					}
				}
				else if(playoffRound==2) {
					for(int i = 21 ; i<29 ; i=i+2) {
						if(seasonInfo.getPlayoffStructure().get(i).equals(team)||seasonInfo.getPlayoffStructure().get(i+1).equals(team)) inPlayoffs = true;
						if(playoffRoundGame==1||playoffRoundGame==2||playoffRoundGame==5||playoffRoundGame==7) {
							if(seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i))>=seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i+1))) {
								homeTeam = seasonInfo.getPlayoffStructure().get(i);
								awayTeam = seasonInfo.getPlayoffStructure().get(i+1);
							}
							else {
								homeTeam = seasonInfo.getPlayoffStructure().get(i+1);
								awayTeam = seasonInfo.getPlayoffStructure().get(i);
							}
							
						}
						else {
							if(seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i))>=seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i+1))) {
								awayTeam = seasonInfo.getPlayoffStructure().get(i);
								homeTeam = seasonInfo.getPlayoffStructure().get(i+1);
							}
							else {
								awayTeam = seasonInfo.getPlayoffStructure().get(i+1);
								homeTeam = seasonInfo.getPlayoffStructure().get(i);
							}
						}
						playPlayoffGame();
					}
				}
				else if(playoffRound==3) {
					for(int i = 31 ; i<35 ; i=i+2) {
						if(seasonInfo.getPlayoffStructure().get(i).equals(team)||seasonInfo.getPlayoffStructure().get(i+1).equals(team)) inPlayoffs = true;
						if(playoffRoundGame==1||playoffRoundGame==2||playoffRoundGame==5||playoffRoundGame==7) {
							if(seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i))>=seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i+1))) {
								homeTeam = seasonInfo.getPlayoffStructure().get(i);
								awayTeam = seasonInfo.getPlayoffStructure().get(i+1);
							}
							else {
								homeTeam = seasonInfo.getPlayoffStructure().get(i+1);
								awayTeam = seasonInfo.getPlayoffStructure().get(i);
							}
							
						}
						else {
							if(seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i))>=seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(i+1))) {
								awayTeam = seasonInfo.getPlayoffStructure().get(i);
								homeTeam = seasonInfo.getPlayoffStructure().get(i+1);
							}
							else {
								awayTeam = seasonInfo.getPlayoffStructure().get(i+1);
								homeTeam = seasonInfo.getPlayoffStructure().get(i);
							}
						}
						playPlayoffGame();
					}
				}
				else if(playoffRound==4) {
					if(seasonInfo.getPlayoffStructure().get(41).equals(team)||seasonInfo.getPlayoffStructure().get(42).equals(team)) inPlayoffs = true;
					if(playoffRoundGame==1||playoffRoundGame==2||playoffRoundGame==5||playoffRoundGame==7) {
						if(seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(41))>=seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(42))) {
							homeTeam = seasonInfo.getPlayoffStructure().get(41);
							awayTeam = seasonInfo.getPlayoffStructure().get(42);
						}
						else {
							homeTeam = seasonInfo.getPlayoffStructure().get(42);
							awayTeam = seasonInfo.getPlayoffStructure().get(41);
						}
						
					}
					else {
						if(seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(41))>=seasonInfo.getPoints().get(seasonInfo.getPlayoffStructure().get(42))) {
							awayTeam = seasonInfo.getPlayoffStructure().get(41);
							homeTeam = seasonInfo.getPlayoffStructure().get(42);
						}
						else {
							awayTeam = seasonInfo.getPlayoffStructure().get(42);
							homeTeam = seasonInfo.getPlayoffStructure().get(41);
						}
					}
					playPlayoffGame();
				}
				if(playoffRoundGame==7) {
					nextRound();
				}
			}
		}
	}
	
	public void nextRound() {
		if(playoffRound==1) {
			for(int i = 1 ; i<17 ; i=i+2) {
				if(seasonInfo.getPlayoffWins().get(i)==4) {
					if(seasonInfo.getPlayoffStructure().get(i+1).equals(team)) inPlayoffs = false;
					String theTeam = seasonInfo.getPlayoffStructure().get(i);
					seasonInfo.getPlayoffID().put(theTeam, 20+((i+1)/2));
					seasonInfo.getPlayoffStructure().put(20+((i+1)/2), theTeam);
				}
				else {
					if(seasonInfo.getPlayoffStructure().get(i).equals(team)) inPlayoffs = false;
					String theTeam = seasonInfo.getPlayoffStructure().get(i+1);
					seasonInfo.getPlayoffID().put(theTeam, 20+((i+1)/2));
					seasonInfo.getPlayoffStructure().put(20+((i+1)/2), theTeam);
				}
			}
		}
		else if(playoffRound==2) {
			for(int i = 21 ; i<29 ; i=i+2) {
				if(seasonInfo.getPlayoffWins().get(i)==4) {
					if(seasonInfo.getPlayoffStructure().get(i+1).equals(team)) inPlayoffs = false;
					String theTeam = seasonInfo.getPlayoffStructure().get(i);
					seasonInfo.getPlayoffID().put(theTeam, 20+((i+1)/2));
					seasonInfo.getPlayoffStructure().put(20+((i+1)/2), theTeam);
				}
				else {
					if(seasonInfo.getPlayoffStructure().get(i).equals(team)) inPlayoffs = false;
					String theTeam = seasonInfo.getPlayoffStructure().get(i+1);
					seasonInfo.getPlayoffID().put(theTeam, 20+((i+1)/2));
					seasonInfo.getPlayoffStructure().put(20+((i+1)/2), theTeam);
				}
			}
		}
		else if(playoffRound==3) {
			for(int i = 31 ; i<35 ; i=i+2) {
				if(seasonInfo.getPlayoffWins().get(i)==4) {
					if(seasonInfo.getPlayoffStructure().get(i+1).equals(team)) inPlayoffs = false;
					String theTeam = seasonInfo.getPlayoffStructure().get(i);
					seasonInfo.getPlayoffID().put(theTeam, 25+((i+1)/2));
					seasonInfo.getPlayoffStructure().put(25+((i+1)/2), theTeam);
				}
				else {
					if(seasonInfo.getPlayoffStructure().get(i).equals(team)) inPlayoffs = false;
					String theTeam = seasonInfo.getPlayoffStructure().get(i+1);
					seasonInfo.getPlayoffID().put(theTeam, 25+((i+1)/2));
					seasonInfo.getPlayoffStructure().put(25+((i+1)/2), theTeam);
				}
			}
		}
		else if(playoffRound==4) {
			if(seasonInfo.getPlayoffWins().get(41)==4) {
				if(seasonInfo.getPlayoffStructure().get(42).equals(team)) inPlayoffs = false;
				String theTeam = seasonInfo.getPlayoffStructure().get(41);
				seasonInfo.getPlayoffID().put(theTeam, 250);
				seasonInfo.getPlayoffStructure().put(50, theTeam);
			}
			else {
				if(seasonInfo.getPlayoffStructure().get(41).equals(team)) inPlayoffs = false;
				String theTeam = seasonInfo.getPlayoffStructure().get(42);
				seasonInfo.getPlayoffID().put(theTeam, 50);
				seasonInfo.getPlayoffStructure().put(50, theTeam);
			}
			playoffs = false;
			menu = false;
			playoffBracket = true;
		}
		playoffRound++;
		playoffRoundGame = 0;
	}
	
	public void playPlayoffGame() {
		if(seasonInfo.getPlayoffWins().get(seasonInfo.getPlayoffID().get(awayTeam))<4&&seasonInfo.getPlayoffWins().get(seasonInfo.getPlayoffID().get(homeTeam))<4) {
			if(awayTeam.equals(team)) {
				playoffOpponent = homeTeam;
				if(mode.equals("advGOALIE")) {
					goalieMode = new GoalieModeGUI(team,homeTeam,false);
					inGame = true;
				}
				else if(mode.equals("simpleGOALIE")) {
					simpleGoalie = new SimpleGoalieGUI(team,homeTeam,false);
					inGame = true;
				}
				else {
					menu = false;
					gameInfo = true;
					realGame = new SimGame(team,homeTeam);
					RhomeTeam = realGame.getHomeTeam();
					RawayTeam = realGame.getAwayTeam();
					RhomeGoals = realGame.getHomeScore();
					RawayGoals = realGame.getAwayScore();
					RhomeShots = realGame.getHomeShots();
					RawayShots = realGame.getAwayShots();
					RperiodTime = "Game Final";
					Rovertime = realGame.getOvertime();
					playoffGameOver(RawayTeam,RhomeTeam,RawayGoals,RhomeGoals,Rovertime);
					inGame = true;
					repaint();
				}
			}
			else if(homeTeam.equals(team)) {
				playoffOpponent = awayTeam;
				if(mode.equals("advGOALIE")) {
					goalieMode = new GoalieModeGUI(awayTeam,team,true);
					inGame = true;
				}
				else if(mode.equals("simpleGOALIE")) {
					simpleGoalie = new SimpleGoalieGUI(awayTeam,team,true);
					inGame = true;
				}
				else {
					menu = false;
					gameInfo = true;
					realGame = new SimGame(awayTeam,team);
					RhomeTeam = realGame.getHomeTeam();
					RawayTeam = realGame.getAwayTeam();
					RhomeGoals = realGame.getHomeScore();
					RawayGoals = realGame.getAwayScore();
					RhomeShots = realGame.getHomeShots();
					RawayShots = realGame.getAwayShots();
					RperiodTime = "Game Final";
					Rovertime = realGame.getOvertime();
					playoffGameOver(RawayTeam,RhomeTeam,RawayGoals,RhomeGoals,Rovertime);
					inGame = true;
					repaint();
				}
			}
			else {
				game = new SimGame(awayTeam,homeTeam);
				homeTeam = game.getHomeTeam();
				awayTeam = game.getAwayTeam();
				homeGoals = game.getHomeScore();
				awayGoals = game.getAwayScore();
				homeShots = game.getHomeShots();
				awayShots = game.getAwayShots();
				periodTime = "Game Final";
				overtime = game.getOvertime();
				playoffGameOver(awayTeam,homeTeam,awayGoals,homeGoals,overtime);
			}
		}
		
	}
	
	public void givePoints(String aTeam, String hTeam, int aGoals, int hGoals, boolean ot) {
		if(hGoals>aGoals) {
			todaysPoints[seasonInfo.getTeamMap().get(hTeam)] = 2;
			if(ot) todaysPoints[seasonInfo.getTeamMap().get(aTeam)] = 1;
			else todaysPoints[seasonInfo.getTeamMap().get(aTeam)] = 0;
		}
		else if(hGoals<aGoals) {
			todaysPoints[seasonInfo.getTeamMap().get(aTeam)] = 2;
			if(ot) todaysPoints[seasonInfo.getTeamMap().get(hTeam)] = 1;
			else todaysPoints[seasonInfo.getTeamMap().get(hTeam)] = 0;
		}
	}
	
	public void playoffGameOver(String aTeam, String hTeam, int aGoals, int hGoals, boolean ot) {
		if(hGoals>aGoals) {
			seasonInfo.getPlayoffWins().put(seasonInfo.getPlayoffID().get(hTeam),seasonInfo.getPlayoffWins().get(seasonInfo.getPlayoffID().get(hTeam))+1);
		}
		else {
			seasonInfo.getPlayoffWins().put(seasonInfo.getPlayoffID().get(aTeam),seasonInfo.getPlayoffWins().get(seasonInfo.getPlayoffID().get(aTeam))+1);
		}
	}
	
	public void goOn() {
		if(settings) {
			settings = false;
			teamSelect = true;
		}
		else if(teamSelect) {
			teamSelect = false;
			menu = true;
		}
		else if(menu) {
			if(inGame)
				endGame();
		}
		else if(gameInfo) {
			inGame = false;
			gameInfo = false;
			if(!playoffs) addPoints();
			menu = true;
		}
		else if(schedule) {
			schedule = false;
			menu = true;
		}
		else if(standings) {
			standings = false;
			menu = true;
		}
		else if(playoffBracket) {
			playoffBracket = false;
			if(playoffs) menu = true;
			else seasonEnd = true;
		}
	}
	
	public void endGame() {
		if(mode.equals("advGOALIE")) {
			RhomeTeam = goalieMode.getHomeTeam();
			RawayTeam = goalieMode.getAwayTeam();
			if(goalieMode.stillPlaying()) {
				if(goalieMode.playerIsHome()==true) {
					RhomeGoals = 0;
					RawayGoals = 5;
					RhomeShots = 0;
					RawayShots = 5;
					RperiodTime = "Game Final";
					Rovertime = false;
				}
				else {
					RhomeGoals = 5;
					RawayGoals = 0;
					RhomeShots = 5;
					RawayShots = 0;
					RperiodTime = "Game Final";
					Rovertime = false;
				}	
			}
			else {
				RhomeGoals = goalieMode.getHomeScore();
				RawayGoals = goalieMode.getAwayScore();
				RhomeShots = goalieMode.getHomeShots();
				RawayShots = goalieMode.getAwayShots();
				RperiodTime = goalieMode.getPeriodTime();
				Rovertime = goalieMode.getOvertime();
			}	
			goalieMode.quit();
		}
		if(mode.equals("simpleGOALIE")) {
			RhomeTeam = simpleGoalie.getHomeTeam();
			RawayTeam = simpleGoalie.getAwayTeam();
			RhomeGoals = simpleGoalie.getHomeScore();
			RawayGoals = simpleGoalie.getAwayScore();
			RhomeShots = simpleGoalie.getHomeShots();
			RawayShots = simpleGoalie.getAwayShots();
			RperiodTime = simpleGoalie.getPeriodTime();
			Rovertime = simpleGoalie.getOvertime();
		}
		if(!playoffs) givePoints(RawayTeam,RhomeTeam,RawayGoals,RhomeGoals,Rovertime);
		else playoffGameOver(RawayTeam,RhomeTeam,RawayGoals,RhomeGoals,Rovertime);
		menu = false;
		gameInfo = true;
		repaint();
	}
	
	public void addPoints() {
		seasonInfo.addPoints(todaysPoints);
		if(gamesPlayed==games) {
			playoffs = true;
			seasonInfo.setPlayoffs();
			playoffRound=1;
		}
	}
	
	public String draft() {
		return seasonInfo.getRandomTeam();
	}
	
	public void refreshStandings() {
		teamID = -1;
		seasonInfo.refreshSets();
	}
	
	public void nextSeason() {
		year++;
		seasonInfo = new nhlInfo();
		menu = true;
		inGame = false;
		simGame = false;
		gameInfo = false;
		schedule = false;
		standings = false;
		playoffs = false;
		playoffBracket = false;
		seasonEnd = false;
		gamesPlayed = 0;
		playoffRound = 0;
		playoffRoundGame = 0;
	}
	
	public void requestTrade() {
		team = draft();
		nextSeason();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GamePlayGUI();
			}
		});
	}
}
