package robots;
import robocode.*;

public class RoboGenerico extends TeamRobot{

	//Atributos interessantes para as regras devem ser declarados aqui.
	public RoboGenerico()
	{
		super();
		//Inicializa��o dos atributos declarados
	}
	
	public void run()
	{
		
		while(true) // Loop principal
		{
			ahead(50);
			turnRight(90);
		}
		
	}
	
	public void onScannedRobot(ScannedRobotEvent e) // M�todos padr�es como esses devem ser reescritos conforme as regras necessitem
	{
	
	}
	
	//Regras dever�o estar implementadas em m�todos a partir daqui
	
	
}
