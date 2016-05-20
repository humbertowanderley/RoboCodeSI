package winterIsHere;

import java.awt.geom.Point2D;

//Guarda informa��es sobre o historico de mudan�a de velocidade dos inimigos e suas energias
public class BossTarget extends Point2D.Double 
{	
	private static final long serialVersionUID = 5L;
	int lastScan;
	public double [] velocityField = new double[6000];
	public double [] headingField = new double[6000];
	public double [] energyField = new double[6000];      

	
	double eDistance;
	String name;
	
	
	//Pega a velocidade media do alvo
	public double getAvgVelocity()
	{
		double lastVelocity = velocityField[lastScan];
		
		if (lastVelocity == 0) return 0;   // this should be adjusted to probability depend on avg dir changes
		
		double count = 0;
		double result = 0;
		for (int i=0;i<=lastScan;i++)
		{
			double velocity; 
			if (Math.signum(velocity=velocityField[i]) == Math.signum(lastVelocity))
			{
				result+=velocity;
				count++;
			}
		}
		return result/count;
	}


	public int getLastScan() {
		return lastScan;
	}


	public void setLastScan(int lastScan) {
		this.lastScan = lastScan;
	}


	public double[] getVelocityField() {
		return velocityField;
	}


	public void setVelocityField(double[] velocityField) {
		this.velocityField = velocityField;
	}


	public double[] getHeadingField() {
		return headingField;
	}


	public void setHeadingField(double[] headingField) {
		this.headingField = headingField;
	}


	public double[] getEnergyField() {
		return energyField;
	}


	public void setEnergyField(double[] energyField) {
		this.energyField = energyField;
	}


	public double geteDistance() {
		return eDistance;
	}


	public void seteDistance(double eDistance) {
		this.eDistance = eDistance;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}