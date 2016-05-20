package winterIsHere;

import java.io.Serializable;


//Informa ao time status de monitoramento dos inimigos
public class BossLeadScanInfo implements Serializable,IBossMessage
{
	

	private static final long serialVersionUID = 1L;

	String leadScan;
	
	public void proccedMessage(RobotState robot) 
	{
		robot.setLeadScanTarget(leadScan);
	}
	
	public String getLeadScan() {
		return leadScan;
	}

	public void setLeadScan(String leadScan) {
		this.leadScan = leadScan;
	}
}