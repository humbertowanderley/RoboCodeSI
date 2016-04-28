package extras;

import java.awt.geom.Point2D;
import java.util.HashSet;

import robocode.Bullet;
import robots.Boss;

//Armazena o status das balas disparadas pelo time
public class BossTeamInfo extends Point2D.Double implements IBossMessage 
{
	private static final long serialVersionUID = 2L;
	
	HashSet<Bullet> teamBullets;
	
	
	public void proccedMessage(Boss robot) 
	{
		Boss.setTeamInfo(this);

	}


	public HashSet<Bullet> getTeamBullets() {
		return teamBullets;
	}


	public void setTeamBullets(HashSet<Bullet> teamBullets) {
		this.teamBullets = teamBullets;
	}
	
	
}