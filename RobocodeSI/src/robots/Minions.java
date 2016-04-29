package robots;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import extras.*;
import robocode.*;
import robocode.util.Utils;


public class Minions extends TeamRobot implements Droid
{
	
	
	@Override 
	public void run()
	{
		setAllColors(Color.white);		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);


		
		while(true)	
		{
			if(getEnergy() > 5)
			{
				if(pertoParede()) {	
					setTurnRight(180);
					setAhead(100);
				} else {
					setAhead(50);
					setTurnRight(45);
				}
				
				turnGunRight(20);
				fire(1);
				
			}
			else
			{
				if(!pertoParede())
				{
					ahead(100);
					back(100);
				}
				else
				{
					back(50);
				}
			}
			
			
			
			
			execute();
		}
	}		
	@Override
	public void onMessageReceived(MessageEvent e)
	{
		if(e.getMessage() instanceof Ponto)
		{
		
			Ponto p = (Ponto) e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx, dy));
			
			// Turn gun to target
			turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
			// Fire hard!
			fire(3);
			
			if(pertoParede()) {	
				setTurnRight(180);
				setAhead(100);
			} else {
				setAhead(50);
				setTurnRight(45);
			}
			execute();
					
		}
			
		
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
	
	//método que faz o robô andar para traz caso se aproxime de alguma parede.
	public boolean pertoParede() {					
		return (getX() < 50 || getX() > getBattleFieldWidth() - 50 ||
			getY() < 50 || getY() > getBattleFieldHeight() - 50);
	}	

	
	
}














