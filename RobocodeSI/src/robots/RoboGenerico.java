package robots;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import extras.*;

import robocode.*;

/*
 * Tabela de Estrategias
 * ----------------------
 * 0 - Pegando o mais fraco.
 
 */

public class RoboGenerico extends TeamRobot{
	
	int estrategiaAtual;
	Inimigo target;
	ArrayList inimigosVivos;

	//Atributos interessantes para as regras devem ser declarados aqui.
	public RoboGenerico()
	{
		super();
		//Inicializa��o dos atributos declarados
		this.estrategiaAtual = 0;
		this.inimigosVivos = new ArrayList();
		this.target = null;
	}
	
	public void run()
	{
//		setAdjustRadarForGunTurn(true);
//		setAdjustGunForRobotTurn(true);
	//	setAdjustRadarForRobotTurn(true);
		
		while(true) // Loop principal
		{
			
			//setTurnRadarRight(45);
	//		setTurnGunRight(45);	//Estrategias de rota��o das partes dos robos n�o est�o implementadas ainda. Estas s�o para testes apenas.
			setTurnRight(45);
				
			if(pertoParede()) {		//Estrategias de movimenta��o n�o est�o implementadas, essa � apenas para testar as outras coisas.
				setBack(200);
			} else {
				setAhead(20);
				setTurnLeft(45);
			}
			
			execute();				//executa a fila de instru��es setadas.
			
		}
		
	}
	
	
	public void onScannedRobot(ScannedRobotEvent e) // M�todos padr�es como esses devem ser reescritos conforme as regras necessitem
	{
		
			
			
			switch (estrategiaAtual)			//Estrategias relacionadas ao evento de encontrar outros robos na arena poder�o ser colocados nesse switch.
			{
				case 0:							//Caso 0: atacar inimigo mais fraco
					inimigosDetectados(e);		//Atualiza lista de inimigos vivos
					if(this.target == null)		
						this.target = escolherAlvoFraco();		//Se ainda n�o ter nenhum alvo, escolhe o mais fraco
					else if(e.getName() == target.getName())	//sen�o, se o alvo detectado � o alvo atual...	
					{
						target.setBearing(e.getBearing());		//Atualiza as informa��es de localiza��o e energia do alvo atual
						target.setEnergy(e.getEnergy());
						target.setHeading(e.getHeading());
						
						mira(this.target.getBearing());			//Mira nele
						
						if(this.target.getEnergy()<12)			//E atira com a for�a necess�ria.
							tiroFatal(target.getEnergy());
						else		
							fogo(this.target.getDistance());
					}				
					break;
			}
			
	}
	
	public void onRobotDeath(RobotDeathEvent e)
	{
		Inimigo roboMorto = new Inimigo(e.getName(),0,0,0,0);
		if(inimigosVivos.contains(roboMorto))
		{
			inimigosVivos.remove(roboMorto);	
		}
		
		if(this.target != null)
			if(this.target.getName() == roboMorto.getName())
				this.target = null;
	}
	
	public void onHitByBullet(HitByBulletEvent e)
	{
		
	}
	
	public void onMessageReceived(MessageEvent m)
	{
		inimigosDetectados((ScannedRobotEvent) m.getMessage());
				
	}
	
	
	//Regras dever�o estar implementadas em m�todos a partir daqui
	
	public Inimigo escolherAlvoFraco()			//Esse m�todo checa dentre os inimigos vivos, aquele que est� mais fraco e retorna seus dados
	{
		double menorVida = 1000;
		Inimigo targetAux = null;
		Inimigo target = null;
		
		for(int i = 0; i < this.inimigosVivos.size(); i++)				//dentre a lista de inimigos vivos, identifica aquele com menor vida.
		{
			targetAux = (Inimigo) this.inimigosVivos.get(i);
			if(targetAux.getEnergy() < menorVida)
			{
				menorVida = targetAux.getEnergy();
				target = targetAux;		
			}
		}
		
		return target;
	}
	
	public void inimigosDetectados(ScannedRobotEvent e)			//Este m�todo atualiza a lista de inimigos vivos conhecidos pelo robo
	{
		Inimigo inimigo;
		int index = 0;
		if(!isTeammate(e.getName()))
		{
			inimigo = new Inimigo(e.getName(),e.getEnergy(),e.getDistance(),e.getHeading(),e.getBearing());
			index = inimigosVivos.indexOf(inimigo);
			
			if(index >= 0)
				inimigosVivos.set(index, inimigo);
			else
				inimigosVivos.add(inimigo);
			
		}
	}
	
	public void esquiva(double enemy)
	{
		//Falta implementar um algoritimo de desvio de balas
		
	}
	
	
	
	public void mira(double Adv) {					//Direciona o canh�o do robo para o inimigo cuja posi��o relativa � passada por par�metro
		double A=getHeading()+Adv-getGunHeading();
		if (!(A > -180 && A <= 180)) {				//Calcula a normaliza��o do �ngulo que deve ser virado pelo canh�o.
			while (A <= -180) {
				A += 360;
			}
			while (A > 180) {
				A -= 360;
			}
		}
		setTurnGunRight(A);
	}
	
	public void fogo(double Distancia) {			//Controla a for�a do tiro conforme a dist�ncia do oponente: mais perto, bala mais forte, distante, bala mais fraca.
		if (Distancia > 200 || getEnergy() < 15) {
			setFire(1);
		} else if (Distancia > 50) {
			setFire(2);
		} else {
			setFire(3);
	    }
	}
	public double anguloRelativo(double ANG) {		//Fun��o que normaliza o �ngulo passado como par�metro.
		if (ANG> -180 && ANG<= 180) {
			return ANG;
		}
		double REL = ANG;
		while (REL<= -180) {
			REL += 360;
		}
		while (ANG> 180) {
			REL -= 360;
		}
		return REL;
	}
	
	public void tiroFatal(double EnergiaIni) {		//dispara um "tiro de mizericordia" quando o inimigo estiver com menos de 12 de vida, com for�a apropriada para mata-lo.
		double Tiro = (EnergiaIni / 4) + .1;
		setFire(Tiro);
	}
	
	public boolean pertoParede() {					//m�todo que faz o rob� andar para traz caso se aproxime de alguma parede.
		return (getX() < 50 || getX() > getBattleFieldWidth() - 50 ||
			getY() < 50 || getY() > getBattleFieldHeight() - 50);
	}

	
	public boolean timeGanhando()		//M�todo ainda em constru��o. ignore-o xD
	{
		int vidaDoTime = 200, vidaInimigos = 200; // precisa pegar os valores no modelo do mundo no tempo atual.
		
		if(vidaDoTime >= vidaInimigos)
		{
			//manter estrategia
			return true;
		}
		else
		{
			//estrategia defensiva
			return false;
		}
		
	}
	
	public boolean inimigosMaisProximos() //M�todo ainda em constru��o. ignore-o xD
	{
		int distanciaAntiga = 0, distanciaAtual = 10;
		
		if(distanciaAntiga > distanciaAtual)
		{
			return true;
		}
		else
			return false;
		
	}
	
	
	
	
}
