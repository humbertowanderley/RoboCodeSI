package extras;

import java.awt.geom.Point2D;

import robots.Boss;

//Informações sobre o inimigo do lider
public class BossEnemyInfo extends Point2D.Double implements IBossMessage
{
	private static final long serialVersionUID = 4L;
	
	int lastScan;
	double eVelocity;
	double eHeading;
	String eName;
	double eEnergy;
	
	
	public void proccedMessage(Boss robot) 
	{
		BossTarget target = robot.getTarget(eName, eEnergy);
		target.velocityField[lastScan] = eVelocity;			
		target.headingField[lastScan] = eHeading;
		target.energyField[lastScan] = eEnergy;
		
		
		target.x = x + (Math.sin(eHeading) * eVelocity);     // maybe take the headingDiff 
		target.y = y + (Math.cos(eHeading) * eVelocity);
		target.lastScan = Math.max(target.lastScan,lastScan);
		target.eDistance = Point2D.distance(x, y, robot.getMyX(), robot.getMyY());
		
	}


	public int getLastScan() {
		return lastScan;
	}


	public void setLastScan(int lastScan) {
		this.lastScan = lastScan;
	}


	public double geteVelocity() {
		return eVelocity;
	}


	public void seteVelocity(double eVelocity) {
		this.eVelocity = eVelocity;
	}


	public double geteHeading() {
		return eHeading;
	}


	public void seteHeading(double eHeading) {
		this.eHeading = eHeading;
	}


	public String geteName() {
		return eName;
	}


	public void seteName(String eName) {
		this.eName = eName;
	}


	public double geteEnergy() {
		return eEnergy;
	}


	public void seteEnergy(double eEnergy) {
		this.eEnergy = eEnergy;
	}

}