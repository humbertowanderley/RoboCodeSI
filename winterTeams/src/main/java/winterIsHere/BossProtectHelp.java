package winterIsHere;

import java.awt.geom.Point2D;

//Pede ajuda ao time
public class BossProtectHelp extends Point2D.Double implements IBossMessage
{
	private static final long serialVersionUID = 3L;

	
	public void proccedMessage(RobotState robot) 
	{
		robot.setProtectHelp(this);
	}

	
}