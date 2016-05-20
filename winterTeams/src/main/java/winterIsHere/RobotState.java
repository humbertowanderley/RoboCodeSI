package winterIsHere;

import java.util.HashSet;


import robocode.AdvancedRobot;
import robocode.Bullet;


public class RobotState {
	
	
	
	private static final double FIELD 			= 800.0;
	private static final double WZ 				= 17.0;
	static final double WZ_M 			= 20.0;
	private static final double WZ_SIZE_W 		= FIELD-2*WZ;
	private static final double WZ_SIZE_H 		= FIELD-2*WZ;
	static final double WZ_SIZE_M_W 	= FIELD-2*WZ_M;
	static final double WZ_SIZE_M_H 	= FIELD-2*WZ_M;
	private final static double DIST_REMAIN 	= 20;
	private final static double RADAR_GUNLOCK = 1.0;
	private final static double RADAR_WIDE = 3.0;	
	private final static double TARGET_FORCE = 35000;
	private final static double TARGET_DISTANCE = 700.0;
	private final static double PI_360 = Math.PI *2.0;
	private final static double DELTA_RISK_ANGLE = Math.PI / 20.0;
	private static double DIST = 185;
	private static BossTarget leader;
	private static BossTarget minion;
	static BossTarget myTarget;			
	private static boolean isLeader = true;
	private static double myX;
	private static double myY;
	static HashSet<Bullet> myBullets;
	private static BossProtectHelp protectHelp;
	private static BossTeamInfo teamInfo;
	private static String leadScanTarget; 
	
	static double guessedX;
	static double guessedY;
	
	static int battleState;       // 4 = 2vs2  0 = 1vs1   1 = 2vs1   -1 = 1vs2  
//************************************************************************************************************
    private final double distanceRemaining;
    private final double energy;
    private final double gunHeading;
    private final double gunHeat;
    private final double gunTurnRemaining;
    private final double heading;
    private final int numRounds;
    private final int others;
    private final double radarHeading;
    private final double radarTurnRemaining;
    private final int roundNum;
    private final long time;
    private final double y;
    private final double turnRemaining;
    private final double velocity;
    private final double x;

    public RobotState(AdvancedRobot robot){
        distanceRemaining = robot.getDistanceRemaining();
        energy = robot.getEnergy();
        gunHeading = robot.getGunHeading();
        gunHeat = robot.getGunHeat();
        gunTurnRemaining = robot.getGunTurnRemaining();
        heading = robot.getHeading();
        numRounds = robot.getNumRounds();
        others = robot.getOthers();
        radarHeading = robot.getRadarHeading();
        radarTurnRemaining = robot.getRadarTurnRemaining();
        roundNum = robot.getRoundNum();
        time = robot.getTime();
        turnRemaining = robot.getTurnRemaining();
        velocity = robot.getVelocity();
        x = robot.getX();
        y = robot.getY();
    }
    
        
    public double getDistanceRemaining() {
        return distanceRemaining;
    }

    public double getEnergy() {
        return energy;
    }

    public double getGunHeading() {
        return gunHeading;
    }

    public double getGunHeat() {
        return gunHeat;
    }

    public double getGunTurnRemaining() {
        return gunTurnRemaining;
    }

    public double getHeading() {
        return heading;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public int getOthers() {
        return others;
    }

    public double getRadarHeading() {
        return radarHeading;
    }

    public double getRadarTurnRemaining() {
        return radarTurnRemaining;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public long getTime() {
        return time;
    }

    public double getTurnRemaining() {
        return turnRemaining;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString(){
    	return "State(gunturn:"+gunTurnRemaining+"x:"+x+", y:"+y+", velocity:"+velocity+", energy:"+energy+", heading:"+heading+", distanceRemaining:"+distanceRemaining+")";	
    }


	public static double getDIST() {
		return DIST;
	}


	public static void setDIST(double dIST) {
		DIST = dIST;
	}


	public static BossTarget getLeader() {
		return leader;
	}


	public static void setLeader(BossTarget leader) {
		RobotState.leader = leader;
	}


	public static BossTarget getMinion() {
		return minion;
	}


	public static void setMinion(BossTarget minion) {
		RobotState.minion = minion;
	}


	public static BossTarget getMyTarget() {
		return myTarget;
	}


	public static void setMyTarget(BossTarget myTarget) {
		RobotState.myTarget = myTarget;
	}


	public static boolean isLeader() {
		return isLeader;
	}


	public static void setLeader(boolean isLeader) {
		RobotState.isLeader = isLeader;
	}


	public static double getMyX() {
		return myX;
	}


	public static void setMyX(double myX) {
		RobotState.myX = myX;
	}


	public static double getMyY() {
		return myY;
	}


	public static void setMyY(double myY) {
		RobotState.myY = myY;
	}


	public static HashSet<Bullet> getMyBullets() {
		return myBullets;
	}


	public static void setMyBullets(HashSet<Bullet> myBullets) {
		RobotState.myBullets = myBullets;
	}


	public static BossProtectHelp getProtectHelp() {
		return protectHelp;
	}


	public static void setProtectHelp(BossProtectHelp protectHelp) {
		RobotState.protectHelp = protectHelp;
	}


	public static BossTeamInfo getTeamInfo() {
		return teamInfo;
	}


	public static void setTeamInfo(BossTeamInfo teamInfo) {
		RobotState.teamInfo = teamInfo;
	}


	public static String getLeadScanTarget() {
		return leadScanTarget;
	}


	public static void setLeadScanTarget(String leadScanTarget) {
		RobotState.leadScanTarget = leadScanTarget;
	}


	public static double getGuessedX() {
		return guessedX;
	}


	public static void setGuessedX(double guessedX) {
		RobotState.guessedX = guessedX;
	}


	public static double getGuessedY() {
		return guessedY;
	}


	public static void setGuessedY(double guessedY) {
		RobotState.guessedY = guessedY;
	}


	public static int getBattleState() {
		return battleState;
	}


	public static void setBattleState(int battleState) {
		RobotState.battleState = battleState;
	}


	public static double getField() {
		return FIELD;
	}


	public static double getWz() {
		return WZ;
	}


	public static double getWzM() {
		return WZ_M;
	}


	public static double getWzSizeW() {
		return WZ_SIZE_W;
	}


	public static double getWzSizeH() {
		return WZ_SIZE_H;
	}


	public static double getWzSizeMW() {
		return WZ_SIZE_M_W;
	}


	public static double getWzSizeMH() {
		return WZ_SIZE_M_H;
	}


	public static double getDistRemain() {
		return DIST_REMAIN;
	}


	public static double getRadarGunlock() {
		return RADAR_GUNLOCK;
	}


	public static double getRadarWide() {
		return RADAR_WIDE;
	}


	public static double getTargetForce() {
		return TARGET_FORCE;
	}


	public static double getTargetDistance() {
		return TARGET_DISTANCE;
	}


	public static double getPi360() {
		return PI_360;
	}


	public static double getDeltaRiskAngle() {
		return DELTA_RISK_ANGLE;
	}


	public void setTurnRadarRightRadians(double maxValue) {
		// TODO Auto-generated method stub
		
	}
    
    //*****************************************************************************************************
    
    

}
