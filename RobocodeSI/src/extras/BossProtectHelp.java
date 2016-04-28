package extras;

import java.awt.geom.Point2D;

import robots.Boss;
//Pede ajuda ao time
public class BossProtectHelp extends Point2D.Double implements IBossMessage
{
	private static final long serialVersionUID = 3L;

	
	public void proccedMessage(Boss robot) 
	{
		robot.setProtectHelp(this);
	}
	
}