package robots;
import robocode.*;

public class RoboGenerico extends TeamRobot{

	//Atributos interessantes para as regras devem ser declarados aqui.
	public RoboGenerico()
	{
		super();
		//Inicialização dos atributos declarados
	}
	
	public void run()
	{
		
		while(true) // Loop principal
		{
			ahead(50);
			turnRight(90);
		}
		
	}
	
	public void onScannedRobot(ScannedRobotEvent e) // Métodos padrões como esses devem ser reescritos conforme as regras necessitem
	{
	
	}
	
	//Regras deverão estar implementadas em métodos a partir daqui
	
	
}
