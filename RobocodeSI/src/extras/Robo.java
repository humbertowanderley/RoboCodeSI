package extras;

import java.io.*;

public class Robo implements Serializable {

	String name;
	double energy;
	double distance;
	double heading;
	double bearing;
	
	
	public Robo(String name, double energy, double distance, double heading, double bearing)
	{
		this.name = name;
		this.energy = energy;
		this.distance = distance;
		this.heading = heading;
		this.bearing = bearing;
		
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public double getEnergy()
	{
		return this.energy;
	}
	
	public double getDistance()
	{
		return this.distance;
	}
	
	public double getHeading()
	{
		return this.heading;
	}
	
	public double getBearing()
	{
		return this.bearing;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setEnergy(double energy)
	{
		this.energy = energy;
	}
	public void setHeading(double heading)
	{
		this.heading = heading;
	}
	public void setBearing(double bearing)
	{
		this.bearing = bearing;
	}
	
	public boolean equals(Object robo) 
	{
	     if (this.name == ((Robo) robo).getName())
	          return true;
	     else
	    	 return false;
	}

	
}
