package robots;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;

import extras.*;
import robocode.*;
import robocode.util.Utils;


public class Boss extends TeamRobot
{
	private static final double FIELD 			= 800.0;
	private static final double WZ 				= 17.0;
	private static final double WZ_M 			= 20.0;
	private static final double WZ_SIZE_W 		= FIELD-2*WZ;
	private static final double WZ_SIZE_H 		= FIELD-2*WZ;
	private static final double WZ_SIZE_M_W 	= FIELD-2*WZ_M;
	private static final double WZ_SIZE_M_H 	= FIELD-2*WZ_M;
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
	private static BossTarget myTarget;			
	private static boolean isLeader = true;
	private static double myX;
	private static double myY;
	private static HashSet<Bullet> myBullets;
	private static BossProtectHelp protectHelp;
	private static BossTeamInfo teamInfo;
	private static String leadScanTarget; 
	
	static double guessedX;
	static double guessedY;
	
	static int battleState;       // 4 = 2vs2  0 = 1vs1   1 = 2vs1   -1 = 1vs2  
	
	@Override 
	public void run()
	{
		setAllColors(Color.black);		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		battleState = 4;
//		isLeader = (getEnergy() > 150);
//		myColor = Color.GREEN;
//		if (isLeader) myColor = Color.RED;
		myBullets = new HashSet<Bullet>();
		
		minion = null;
		leader = null;
		
		while(true)	
		{
			if (getRadarTurnRemaining() == 0.0) setTurnRadarRightRadians(Double.MAX_VALUE);			
			
			try
			{		
				setMainTarget();
				if (isLeader)
				{
					BossLeadScanInfo lInfo = new BossLeadScanInfo();
					lInfo.setLeadScan(myTarget.getName());
					broadcastMessage(lInfo);
				}
				
					//Verifica a distancia em relação aos inimigos e se está em desvantagem numerica
				if ((myTarget.geteDistance() <= 200 && battleState == 4) || (battleState == -1 && isLeader))
				{
					//System.out.format("[%d] arg help me!\n",getTime());	
					BossProtectHelp pHelp = new BossProtectHelp();
					
					if (isLeader)
					{
						pHelp.x = guessedX;
						pHelp.y = guessedY;
					}
					else
					{
						double angle;
						double aDist;
						pHelp.x = getX() + Math.sin(angle=Math.atan2(myTarget.x-getX(),myTarget.y-getY())) * (aDist=(myTarget.geteDistance() + 50));
						pHelp.y = getY() + Math.cos(angle) * aDist;
					}
					
					//Informa ao time que precisa de ajuda
					broadcastMessage(pHelp);
				}
			
				double mRate = Double.MAX_VALUE;
				double v0 = 0 ;
				double v1 = 0 ;
				boolean isBulletEvade = false;
				boolean isCloseCombat = false;
				double protectDist = 0;
				
				while ((v0 += DELTA_RISK_ANGLE) <= PI_360)
				{
					double x = DIST*Math.sin(v0)+myX;
					double y = DIST*Math.cos(v0)+myY;
					
					if(new Rectangle2D.Double(WZ_M,WZ_M, WZ_SIZE_M_W, WZ_SIZE_M_H).contains(x,y))
					{
						double r1 = 0;
						double force = TARGET_FORCE;
						if (protectHelp != null)
						{
							isCloseCombat = true;
							protectDist = Point2D.distance(protectHelp.x, protectHelp.y, getX(), getY());
							force = 10000;
							r1 -= 100000/Point2D.distanceSq(protectHelp.x, protectHelp.y, x, y);								
						}
						//else /* if (target.isAlive)  para debug*/
						{
							try
							{
								r1 += force/Point2D.distanceSq(leader.x, leader.y, x, y);
							}
							catch(Exception e1){}
							
							try
							{							
								r1 += force/Point2D.distanceSq(minion.x, minion.y, x, y);
							}
							catch(Exception e1){}
						}

											
						try
						{
							r1 += force/Point2D.distanceSq(teamInfo.x, teamInfo.y, x, y);
							
							for (Bullet bullet : teamInfo.getTeamBullets())
							{
								if (bullet.isActive())
								{	
									double dist = Point2D.distance(bullet.getX(), bullet.getY(), myX, myY);
									if (dist <= DIST)
									{

										r1 += 100000/Point2D.distanceSq(bullet.getX(), bullet.getY(), x, y);
										isBulletEvade = true;
									}
								
								}
							}
						}
						catch(Exception ex){}
							
						if (isCloseCombat)
						{
							r1 += (Math.abs(Math.sin(Math.atan2(myTarget.x-x, myTarget.y-y) - v0)));						
						}
						else 
						{
							r1 += (Math.abs(Math.cos(Math.atan2(myTarget.x-x, myTarget.y-y) - v0)));
						}					
						
						if ( r1 < mRate)
						{	
							mRate = r1;
							v1 = v0;						
						}
					}
				}
				
				if(pertoParede()) {	
					setAhead(100);
				}
				else if(Math.abs(getDistanceRemaining()) <= DIST_REMAIN || isBulletEvade || isCloseCombat || mRate > 1.3)
				{
					setTurnRightRadians(Math.tan(v1 -= getHeadingRadians() + ((isCloseCombat) ? Math.sin(protectDist/30.0)/10.0 : 0)));
					setAhead(DIST*Math.cos(v1));
				}

			}
			catch(Exception ex){}
			execute();
		}
	}

	
	@Override
	public void onHitByBullet(HitByBulletEvent e)
	{
		setBack(100);
		setTurnRight(45);
		execute();
		
	}
	@Override
	public void onWin(WinEvent e)
	{
		//dancinha
		
		setAhead(5);
		setTurnRight(360D);
		setTurnGunLeft(360D);
		setTurnRadarRight(30D);
	
	}
	
	
	@Override 
	public void onStatus(StatusEvent e)
	{
		//informa ao time o status no campo de batalha
		try 
		{		
			myX = getX();
			myY = getY();
			protectHelp = null;
			teamInfo = null;
			
			BossTeamInfo tInfo = new BossTeamInfo();
			tInfo.setTeamBullets(myBullets); 
			tInfo.x = myX;
			tInfo.y = myY;
			broadcastMessage(tInfo);
			
		} 
		catch (Exception e2) {}
	}
	
	
				
	@Override
	public void onScannedRobot(ScannedRobotEvent e)
	{	
		try
		{
			String name = e.getName();
			if (isTeammate(name)) 
					return;   // all team infos per msg
			//passa as coordenadas do alvo para o time
			double enemyBearing = this.getHeading() + e.getBearing();
			double enemyX = getX() + e.getDistance()* Math.sin(Math.toRadians(enemyBearing));
			double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
			broadcastMessage(new Ponto(enemyX, enemyY));
	
			BossTarget enemy = getTarget(name,e.getEnergy());	
			
			int scanTime = (int) getTime();
			double v3;

			enemy.seteDistance(e.getDistance());
			enemy.setLastScan(scanTime);
			enemy.x = myX + Math.sin((v3=(getHeadingRadians() + e.getBearingRadians())))*e.getDistance();
			enemy.y = myY + Math.cos(v3)*e.getDistance();
			enemy.velocityField[scanTime] = e.getVelocity();
			enemy.headingField[scanTime] = e.getHeadingRadians();
			enemy.energyField[scanTime] = e.getEnergy();
			
			BossEnemyInfo eInfo = new BossEnemyInfo();
			eInfo.setLastScan(enemy.getLastScan()); 
			eInfo.x = enemy.x; 
			eInfo.y = enemy.y;
			eInfo.seteEnergy(enemy.velocityField[scanTime]);
			eInfo.seteHeading(enemy.headingField[scanTime]);
			eInfo.seteEnergy(enemy.energyField[scanTime]);
			eInfo.seteName(enemy.getName());
			broadcastMessage(eInfo);
		
			setMainTarget();
			doGun();

			 // (2vs1 && heat)  || 1vs2 - 1vs1 || not leadscan     .... 
			try
			{
				if ( (battleState == 1 && getGunHeat() < RADAR_GUNLOCK)  || battleState <= 0 || !leadScanTarget.equals(name))  
				{
					doRadar(v3);
				}
			}
			catch (Exception e1)
			{
				// exception trown only for my leader and if the leader hasn't send his scan msg			
				doRadar(Math.atan2(myTarget.x-myX,myTarget.y-myY));
			}
		}
		catch(Exception ex){}
	}

	@Override
	public void onMessageReceived(MessageEvent event)
	{
		((IBossMessage) event.getMessage()).proccedMessage(this);
	}
		
	@Override
	public void onRobotDeath(RobotDeathEvent event)
	{
		String name;
		battleState = getOthers()-3;
		if (isTeammate(name=event.getName()))
		{
			battleState+=2;
			return;
		}
				
		if (leader != null && name.equals(leader.getName()))  // null check for last dead if leader died first
		{
			leader = null;
		}
		else
		{
			minion = null;
		}
	}
	//prepara para atirar 	
	protected void doGun()
	{
		long i;	
		double bPower = Math.min(Rules.MAX_BULLET_POWER,TARGET_DISTANCE/myTarget.geteDistance());
		double v2 = myTarget.getAvgVelocity();			
		double xg = myTarget.x -myX;
		double yg = myTarget.y -myY;
		double gHead = myTarget.headingField[myTarget.getLastScan()];
		double lastHead = myTarget.headingField[Math.max(myTarget.getLastScan()-1,0)];
		if (gHead == 0) 
		{
			gHead = lastHead;
			lastHead = myTarget.headingField[Math.max(myTarget.getLastScan()-2,0)];
		}	
		double headDiff; 
		if (Math.abs(headDiff = (gHead - lastHead)) > 0.161442955809475 || gHead == 0) headDiff = 0; 

		i = 0;	
		
		while(++i*(20.0-3.0*bPower) < Math.hypot(xg,yg))
		{
			xg += (Math.sin(gHead) * v2);
			yg += (Math.cos(gHead) * v2);
			
			if (!new Rectangle2D.Double(WZ,WZ, WZ_SIZE_W, WZ_SIZE_H).contains(xg+myX,yg+myY) /*|| check*/)
			{	
				v2 = -v2;
			}
		    gHead += headDiff;
		}
		
		if (getGunTurnRemainingRadians() == 0)
		{
			Bullet bullet;
			if ((bullet=setFireBullet(bPower)) != null)
			{
				myBullets.add(bullet);
			}
		}
		
		guessedX = getX() + xg;
		guessedY = getY() + yg;
		
		setTurnGunRightRadians(Utils.normalRelativeAngle(Math.atan2(xg,yg) - getGunHeadingRadians()));
	}
	
	public void doRadar(double angle)
	{
		setTurnRadarRightRadians(Utils.normalRelativeAngle(angle-getRadarHeadingRadians())*RADAR_WIDE);
	}
	
	// --------------------------------------------- Funções aux ----------------------------------------------------------------
	public static BossTarget getTarget(String name,double energy)
	{
		try 
		{
			try
			{
				if (name.equals(leader.getName()))
				{
					return leader;
				}
			}
			catch (Exception e1)
			{
				if (energy > 150)
				{
					leader = new BossTarget();
					leader.setName(name);
					return leader;
				}
			}
			if (name.equals(minion.getName()))
			{
				return minion;
			}
		} 
		catch (Exception e2) 
		{
			minion = new BossTarget();
			minion.setName(name);
		}
		return minion;
	}
	
	public void setMainTarget()
	{
		double tRate;
		try
		{
			tRate = leader.energyField[leader.getLastScan()]+leader.geteDistance()*0.8;
			try
			{	
				myTarget = (tRate < (/*buffy=*/(minion.energyField[minion.getLastScan()]+minion.geteDistance()*0.8)))? leader : minion;
			}
			catch (Exception e2)
			{
				myTarget = leader;
			}
		}
		catch (Exception e1)
		{
			myTarget = minion;
		}				
		
	}
	
	@Override
	public void onHitRobot(HitRobotEvent e)
	{
		if(isTeammate(e.getName()))
		{
			if(!pertoParede())
				setBack(120);
		}
		
	}
	
	@Override
	public void onHitWall(HitWallEvent e)
	{
		setBack(100);
		
	}
	
	public boolean pertoParede() {					//método que faz o robô andar para traz caso se aproxime de alguma parede.
		return (getX() < 50 || getX() > getBattleFieldWidth() - 50 ||
			getY() < 50 || getY() > getBattleFieldHeight() - 50);
	}


	public static BossTarget getLeader() {
		return leader;
	}


	public static void setLeader(BossTarget leader) {
		Boss.leader = leader;
	}


	public static BossTarget getMinion() {
		return minion;
	}


	public static void setMinion(BossTarget minion) {
		Boss.minion = minion;
	}


	public static BossTarget getMyTarget() {
		return myTarget;
	}


	public static void setMyTarget(BossTarget myTarget) {
		Boss.myTarget = myTarget;
	}


	public static boolean isLeader() {
		return isLeader;
	}


	public static void setLeader(boolean isLeader) {
		Boss.isLeader = isLeader;
	}


	public static double getMyX() {
		return myX;
	}


	public static void setMyX(double myX) {
		Boss.myX = myX;
	}


	public static double getMyY() {
		return myY;
	}


	public static void setMyY(double myY) {
		Boss.myY = myY;
	}


	public static HashSet<Bullet> getMyBullets() {
		return myBullets;
	}


	public static void setMyBullets(HashSet<Bullet> myBullets) {
		Boss.myBullets = myBullets;
	}


	public static BossProtectHelp getProtectHelp() {
		return protectHelp;
	}


	public static void setProtectHelp(BossProtectHelp protectHelp) {
		Boss.protectHelp = protectHelp;
	}


	public static BossTeamInfo getTeamInfo() {
		return teamInfo;
	}


	public static void setTeamInfo(BossTeamInfo teamInfo) {
		Boss.teamInfo = teamInfo;
	}


	public static String getLeadScanTarget() {
		return leadScanTarget;
	}


	public static void setLeadScanTarget(String leadScanTarget) {
		Boss.leadScanTarget = leadScanTarget;
	}


	public static double getGuessedX() {
		return guessedX;
	}


	public static void setGuessedX(double guessedX) {
		Boss.guessedX = guessedX;
	}


	public static double getGuessedY() {
		return guessedY;
	}


	public static void setGuessedY(double guessedY) {
		Boss.guessedY = guessedY;
	}


	public static int getBattleState() {
		return battleState;
	}


	public static void setBattleState(int battleState) {
		Boss.battleState = battleState;
	}
	
	
	
}














