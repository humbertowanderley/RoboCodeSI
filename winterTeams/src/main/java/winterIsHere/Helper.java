package winterIsHere;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

import robocode.*;


public class Helper {

	// calculate absolute angle from absolute angle y based and relative angle
	public static double absoluteAngle(double baseAngle, double relativeAngle) {
		double angle = (baseAngle + relativeAngle) % 360;

		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	// Transforms absolute angle into a relative angle, from an absolute base angle
	public static double relativeAngle(double baseAngle, double destAngle) {
		double angle = (destAngle - baseAngle) % 360;
		if (angle > 180) {
			angle -= 360;
		} else if (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	// calculate a point's x from its base y, an angle and a distance
	public static double calculateX(double xBase, double absoluteAngle, double distance) {
		double offsetX = (Math.sin(Math.toRadians(absoluteAngle)) * distance);
		return xBase + offsetX;
	}

	// calculate a point's y from its base x , an angle and a distance
	public static double calculateY(double yBase, double absoluteAngle, double distance) {
		double offsetY = (Math.cos(Math.toRadians(absoluteAngle)) * distance);
		return yBase + offsetY;
	}

	// calculate the absolute angle between two points (origin and destination)
	public static double absoluteAngle(double xOrigin, double yOrigin, double xDestination, double yDestination) {
		double offsetX = xDestination - xOrigin;
		double offsetY = yDestination - yOrigin;

		return Math.toDegrees(Math.atan2(offsetX, offsetY));
	}

	// calculate the distance between two points
	public static double distance(double xOrigin, double yOrigin, double xDestination, double yDestination) {
		double offsetX = xDestination - xOrigin;
		double offsetY = yDestination - yOrigin;

		return Math.sqrt(offsetX*offsetX + offsetY*offsetY);
	}  

	// normalizes a bearing to between +180 and -180
	public static double normalizeBearing(double angle) {
		while (angle >  180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}

	// computes the absolute bearing between two points
	public static double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;

		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}

		return bearing;
	}    

	// Calculates the firePower in function of the enemy distance
	public static double firePower(double enemyDistance) {
		return (Math.min(500 / enemyDistance, 3));
	}

	// Calculates the bulletSpeed in function of the firePower
	public static double bulletSpeed(double firePower){
		return (20 - firePower * 3);
	}

	// time calculation (mainly for predictive aiming)
	// distance = rate * time, solved for time
	public static long time(double enemyDistance, double bulletSpeed) {
		return (long)(enemyDistance / bulletSpeed);
	}

	// get the enemy's X
	public static double enemyX(double robotX, double robotHeading, double enemyBearing, double distance){
		double absBearingDeg = robotHeading + enemyBearing;
		if (absBearingDeg < 0) absBearingDeg += 360;
		return (robotX + Math.sin(Math.toRadians(absBearingDeg)) * distance);

	}

	// get the enemy's Y
	public static double enemyY(double robotY, double robotHeading, double enemyBearing, double distance){
		double absBearingDeg = robotHeading + enemyBearing;
		if (absBearingDeg < 0) absBearingDeg += 360;
		return (robotY + Math.cos(Math.toRadians(absBearingDeg)) * distance);

	}

	// predict enemy future X
	public static double enemyFutureX(double enemyX, double enemyHeading, double velocity, long when){
		return (enemyX + Math.sin(Math.toRadians(enemyHeading)) * velocity * when);	
	}

	// predict enemy future Y
	public static double enemyFutureY(double enemyY, double enemyHeading, double velocity, long when){
		return (enemyY + Math.cos(Math.toRadians(enemyHeading)) * velocity * when); 	
	}

	//gambis

	public static void gambis(RobotState estado)
	{
		estado.battleState = 4;
		//		isLeader = (getEnergy() > 150);
		//		myColor = Color.GREEN;
		//		if (isLeader) myColor = Color.RED;
		estado.myBullets = new HashSet<Bullet>();

		estado.setMinion(null);
		estado.setLeader(null);
	
/*		if (estado.getRadarTurnRemaining() == 0.0)
			setTurnRadarRightRadians(Double.MAX_VALUE);	      adicionar esse if nas regras*/		

		try
		{		
			setMainTarget(estado);
			if (estado.isLeader())
			{
				BossLeadScanInfo lInfo = new BossLeadScanInfo();
				lInfo.setLeadScan(estado.getMyTarget().getName());
//				broadcastMessage(lInfo);					adicionar esse nas regras
			}

			//Verifica a distancia em relação aos inimigos e se está em desvantagem numerica
			if ((estado.myTarget.geteDistance() <= 200 && estado.battleState == 4) || (estado.battleState == -1 && estado.isLeader()))
			{
				//System.out.format("[%d] arg help me!\n",getTime());	
				BossProtectHelp pHelp = new BossProtectHelp();

				if (estado.isLeader())
				{
					pHelp.x = estado.guessedX;
					pHelp.y = estado.guessedY;
				}
				else
				{
					double angle;
					double aDist;
					pHelp.x = estado.getX() + Math.sin(angle=Math.atan2(estado.myTarget.x-estado.getX(),estado.myTarget.y-estado.getY())) * (aDist=(estado.myTarget.geteDistance() + 50));
					pHelp.y = estado.getY() + Math.cos(angle) * aDist;
				}

				//Informa ao time que precisa de ajuda
//				broadcastMessage(pHelp);				adicionar esse nas regras
			}

			double mRate = Double.MAX_VALUE;
			double v0 = 0 ;
			double v1 = 0 ;
			boolean isBulletEvade = false;
			boolean isCloseCombat = false;
			double protectDist = 0;

			while ((v0 += estado.getDeltaRiskAngle()) <= estado.getPi360())
			{
				double x = estado.getDIST()*Math.sin(v0)+estado.getMyX();
				double y = estado.getDIST()*Math.cos(v0)+estado.getMyY();

				if(new Rectangle2D.Double(estado.WZ_M,RobotState.WZ_M, estado.WZ_SIZE_M_W, estado.WZ_SIZE_M_H).contains(x,y))
				{
					double r1 = 0;
					double force = estado.getTargetForce();
					if (estado.getProtectHelp() != null)
					{
						isCloseCombat = true;
						protectDist = Point2D.distance(estado.getProtectHelp().x, estado.getProtectHelp().y, estado.getX(), estado.getY());
						force = 10000;
						r1 -= 100000/Point2D.distanceSq(estado.getProtectHelp().x, estado.getProtectHelp().y, x, y);								
					}
					//else /* if (target.isAlive)  para debug*/
					{
						try
						{
							r1 += force/Point2D.distanceSq(estado.getLeader().x, estado.getLeader().y, x, y);
						}
						catch(Exception e1){}

						try
						{							
							r1 += force/Point2D.distanceSq(estado.getMinion().x, estado.getMinion().y, x, y);
						}
						catch(Exception e1){}
					}


					try
					{
						r1 += force/Point2D.distanceSq(estado.getTeamInfo().x, estado.getTeamInfo().y, x, y);

						for (Bullet bullet : estado.getTeamInfo().getTeamBullets())
						{
							if (bullet.isActive())
							{	
								double dist = Point2D.distance(bullet.getX(), bullet.getY(), estado.getMyX(), estado.getMyY());
								if (dist <= estado.getDIST())
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
						r1 += (Math.abs(Math.sin(Math.atan2(estado.getMyTarget().x-x, estado.getMyTarget().y-y) - v0)));						
					}
					else 
					{
						r1 += (Math.abs(Math.cos(Math.atan2(estado.getMyTarget().x-x, estado.getMyTarget().y-y) - v0)));
					}					

					if ( r1 < mRate)
					{	
						mRate = r1;
						v1 = v0;						
					}
				}
			}

		}catch(Exception ex){}
	}
	
	public static void setMainTarget(RobotState estado)
	{
		double tRate;
		try
		{
			tRate = estado.getLeader().energyField[estado.getLeader().getLastScan()]+estado.getLeader().geteDistance()*0.8;
			try
			{	
				estado.myTarget = (tRate < (/*buffy=*/(estado.getMinion().energyField[estado.getMinion().getLastScan()]+estado.getMinion().geteDistance()*0.8)))? estado.getLeader() : estado.getMinion();
			}
			catch (Exception e2)
			{
				estado.setMyTarget(estado.getLeader());
			}
		}
		catch (Exception e1)
		{
			estado.setMyTarget(estado.getMinion());
		}				
		
	}
	
}


