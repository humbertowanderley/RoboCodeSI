package extras;

import java.io.Serializable;

import robots.Boss;

//Informa ao time status de monitoramento dos inimigos
public class BossLeadScanInfo implements Serializable,IBossMessage
{
	

	private static final long serialVersionUID = 1L;

	String leadScan;
	
	public void proccedMessage(Boss robot) 
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