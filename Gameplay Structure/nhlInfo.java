import java.util.*;

public class nhlInfo {
	 private int[] nums = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29};
	 private String[] teams = {"Montreal Canadiens", "Ottawa Senators", "Toronto Maple Leafs","Tampa Bay Lightning","Florida Panthers","Buffalo Sabres","Boston Briuns","Detroit Red Wings","New Jersey Devils","Carolina Hurricanes","Philidelphia Flyers","New York Islanders","New York Rangers","Columbus Blue Jackets","Pittsburgh Penguins","Washington Capitals","Colorado Avalanche","Dallas Stars","Winnipeg Jets","Nashville Predators","St. Louis Blues","Minnesota Wild","Chicago Blackhawks","Vancouver Canucks","Arizona Coyotes","Los Angeles Kings","Calgary Flames","San Jose Sharks","Edmonton Oilers","Anaheim Ducks"};
	 private Map<String,Integer> teamToNum;
	 private Map<String,Integer> points;
	 private Map<String,Integer> wins;
	 private Map<String,Integer> loss;
	 private Map<String,Integer> otl;
	 private Map<Integer,String> playoffStructure;
	 private Map<String,Integer> playoffID;
	 private Map<Integer,Integer> playoffWins;
	 private String[] scheduleMatrix;
	 private Set<Integer> atl;
	 private Set<Integer> metro;
	 private Set<Integer> cen;
	 private Set<Integer> pac;
	 
	 private boolean aHigh;
	 private boolean cHigh;
	 
	 public nhlInfo() {
		 buildTeamMap();
		 buildPointMap();
		 buildWinMap();
		 buildLossMap();
		 buildOTLMap();
		 refreshSets();
	 }
	 
	 public void buildTeamMap() {
		 teamToNum = new TreeMap<String,Integer>();
		 for(int i = 0 ; i<30 ; i++) {
			 teamToNum.put(teams[i], i);
		 }
	 }
	
	 public void buildPointMap() {
		 points = new TreeMap<String,Integer>();
		 for(int i = 0 ; i<30 ; i++) {
			 points.put(teams[i], 0);
		 }
	 }
	 
	 public void buildWinMap() {
		 wins = new TreeMap<String,Integer>();
		 for(int i = 0 ; i<30 ; i++) {
			 wins.put(teams[i], 0);
		 }
	 }
	 
	 public void buildLossMap() {
		 loss = new TreeMap<String,Integer>();
		 for(int i = 0 ; i<30 ; i++) {
			 loss.put(teams[i], 0);
		 }
	 }
	 
	 public void buildOTLMap() {
		 otl = new TreeMap<String,Integer>();
		 for(int i = 0 ; i<30 ; i++) {
			 otl.put(teams[i], 0);
		 }
	 }
	 
	 public String[] getTeams() {
		 return teams;
	 }
	 
	 public Map<String,Integer> getTeamMap(){
		 return teamToNum;
	 }
	 
	 public String getRandomTeam() {
		 return teams[(int)(Math.random()*30)];
	 }
	 	 
	 public void buildScheduleMatrix() {
		 scheduleMatrix = new String[30];
		 for(int j = 0 ; j<30 ; j++) {
			 int swap = (int)(Math.random()*30);
			 int here = nums[j];
			 int other = nums[swap];
			 nums[j] = other;
			 nums[swap] = here;
		 }
		 for(int j = 0 ; j<30 ; j++) {
			 scheduleMatrix[j] = teams[nums[j]];
		 }	 
	 }
	 
	 public String[] getScheduleMatrix() {
		 return scheduleMatrix;
	 }
	 
	 public void addPoints(int[] todaysPoints) {
		 for(int i = 0 ; i<30 ; i++) {
			 points.put(teams[i],points.get(teams[i])+todaysPoints[i]);
			 if(todaysPoints[i]==0) loss.put(teams[i],loss.get(teams[i])+1);
			 else if(todaysPoints[i]==1) otl.put(teams[i],otl.get(teams[i])+1);
			 else if(todaysPoints[i]==2) wins.put(teams[i],wins.get(teams[i])+1);
		 }
	 }
	
	 public Map<String,Integer> getPoints() {
		 return points;
	 }
	 
	 public Map<String,Integer> getWins() {
		 return wins;
	 }
	 
	 public Map<String,Integer> getLoss() {
		 return loss;
	 }
	 
	 public Map<String,Integer> getOTL() {
		 return otl;
	 }
	 
	 public void refreshSets() {
		 atl = new TreeSet<Integer>();
		 metro = new TreeSet<Integer>();
		 cen = new TreeSet<Integer>();
		 pac = new TreeSet<Integer>();
	 }
	 
	 public int nextAtlanticStandings() {
		 int high = -1;
		 int highP = -1;
		 for(int i = 0 ; i<8 ; i++) {
			 if((high==-1||points.get(teams[i])>highP)&&!atl.contains(i)) {
				 high = i;
				 highP = points.get(teams[i]);
			 }
		 }
		 atl.add(high);
		 return(high);
	 }
	 
	 public int nextMetroStandings() {
		 int high = -1;
		 int highP = -1;
		 for(int i = 8 ; i<16 ; i++) {
			 if((high==-1||points.get(teams[i])>highP)&&!metro.contains(i)) {
				 high = i;
				 highP = points.get(teams[i]);
			 }
		 }
		 metro.add(high);
		 return(high);
	 }
	 
	 public int nextCentralStandings() {
		 int high = -1;
		 int highP = -1;
		 for(int i = 16 ; i<23 ; i++) {
			 if((high==-1||points.get(teams[i])>highP)&&!cen.contains(i)) {
				 high = i;
				 highP = points.get(teams[i]);
			 }
		 }
		 cen.add(high);
		 return(high);
	 }
	 
	 public int nextPacificStandings() {
		 int high = -1;
		 int highP = -1;
		 for(int i = 23 ; i<30 ; i++) {
			 if((high==-1||points.get(teams[i])>highP)&&!pac.contains(i)) {
				 high = i;
				 highP = points.get(teams[i]);
			 }
		 }
		 pac.add(high);
		 return(high);
	 }
	 
	 public void setPlayoffs() {
		 playoffStructure = new TreeMap<Integer,String>();
		 playoffID = new TreeMap<String,Integer>();
		 playoffWins = new TreeMap<Integer,Integer>();
		 refreshSets();
		 String team1 = teams[nextAtlanticStandings()];
		 String team2 = teams[nextMetroStandings()];
		 if(points.get(team1)>=points.get(team2)) aHigh = true;
		 else aHigh = false;
		 playoffStructure.put(1,team1);
		 playoffStructure.put(3,teams[nextAtlanticStandings()]);
		 playoffStructure.put(4,teams[nextAtlanticStandings()]);
		 playoffStructure.put(5,team2);
		 playoffStructure.put(7,teams[nextMetroStandings()]);
		 playoffStructure.put(8,teams[nextMetroStandings()]);
		 String team3 = teams[nextAtlanticStandings()];
		 String team4 = teams[nextMetroStandings()];
		 String team34;
		 String team5;
		 if(points.get(team3)>=points.get(team4)) {
			 if(aHigh) playoffStructure.put(6,team3);
			 else playoffStructure.put(2,team3);
			 team34 = team4;
			 team5 = teams[nextAtlanticStandings()];
		 }
		 else {
			 if(aHigh) playoffStructure.put(6,team4);
			 else playoffStructure.put(2,team4);
			 team34 = team3;
			 team5 = teams[nextMetroStandings()];
		 }
		 if(points.get(team34)>=points.get(team5)) {
			 if(aHigh) playoffStructure.put(2,team34);
			 else playoffStructure.put(6,team34);
		 }
		 else {
			 if(aHigh) playoffStructure.put(2,team5);
			 else playoffStructure.put(6,team5);
		 }
		 String team6 = teams[nextCentralStandings()];
		 String team7 = teams[nextPacificStandings()];
		 if(points.get(team6)>=points.get(team7)) cHigh = true;
		 else cHigh = false;
		 playoffStructure.put(9,team6);
		 playoffStructure.put(11,teams[nextCentralStandings()]);
		 playoffStructure.put(12,teams[nextCentralStandings()]);
		 playoffStructure.put(13,team7);
		 playoffStructure.put(15,teams[nextPacificStandings()]);
		 playoffStructure.put(16,teams[nextPacificStandings()]);
		 String team8 = teams[nextCentralStandings()];
		 String team9 = teams[nextPacificStandings()];
		 String team89;
		 String team10;
		 if(points.get(team8)>=points.get(team9)) {
			 if(cHigh) playoffStructure.put(14,team8);
			 else playoffStructure.put(10,team8);
			 team89 = team9;
			 team10 = teams[nextCentralStandings()];
		 }
		 else {
			 if(cHigh) playoffStructure.put(14,team9);
			 else playoffStructure.put(10,team9);
			 team89 = team8;
			 team10 = teams[nextPacificStandings()];
		 }
		 if(points.get(team89)>=points.get(team10)) {
			 if(cHigh) playoffStructure.put(10,team89);
			 else playoffStructure.put(14,team89);
		 }
		 else {
			 if(cHigh) playoffStructure.put(10,team10);
			 else playoffStructure.put(14,team10);
		 }
		 playoffWins.put(1, 0);
		 playoffWins.put(2, 0);
		 playoffWins.put(3, 0);
		 playoffWins.put(4, 0);
		 playoffWins.put(5, 0);
		 playoffWins.put(6, 0);
		 playoffWins.put(7, 0);
		 playoffWins.put(8, 0);
		 playoffWins.put(9, 0);
		 playoffWins.put(10, 0);
		 playoffWins.put(11, 0);
		 playoffWins.put(12, 0);
		 playoffWins.put(13, 0);
		 playoffWins.put(14, 0);
		 playoffWins.put(15, 0);
		 playoffWins.put(16, 0);
		 playoffWins.put(21, 0);
		 playoffWins.put(22, 0);
		 playoffWins.put(23, 0);
		 playoffWins.put(24, 0);
		 playoffWins.put(25, 0);
		 playoffWins.put(26, 0);
		 playoffWins.put(27, 0);
		 playoffWins.put(28, 0);
		 playoffWins.put(31, 0);
		 playoffWins.put(32, 0);
		 playoffWins.put(33, 0);
		 playoffWins.put(34, 0);
		 playoffWins.put(41, 0);
		 playoffWins.put(42, 0);
		 buildPlayoffID();
	 }
	 
	 public void buildPlayoffID() {
		 for(int i : playoffStructure.keySet()) {
			 playoffID.put(playoffStructure.get(i), i);
		 }
	 }

	public Map<String, Integer> getPlayoffID() {
		return playoffID;
	}

	public Map<Integer, String> getPlayoffStructure() {
		return playoffStructure;
	}

	public Map<Integer, Integer> getPlayoffWins() {
		return playoffWins;
	}

	public boolean isaHigh() {
		return aHigh;
	}

	public boolean iscHigh() {
		return cHigh;
	}
	
	public boolean inPlayoffs(String team) {
		for(int i = 1 ; i<17 ; i++) {
			if(playoffStructure.get(i).equals(team)) return true;
		}
		return false;
	}
}
