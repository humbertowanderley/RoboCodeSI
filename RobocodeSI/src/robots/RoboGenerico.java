package robots;
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
	ModeloMundo mundo;
	int direcaoMov;	//representa a direção que o robo vai andar 1 frente -1 pra trás

	
	
	//Atributos interessantes para as regras devem ser declarados aqui.
	public RoboGenerico()
	{
		super();
		//Inicialização dos atributos declarados
		this.mundo = new ModeloMundo();
		this.estrategiaAtual = 0;
		this.direcaoMov=1;
		
	
	}
	
	
	public void run()
	{
	//	setAdjustRadarForGunTurn(true);
	//	setAdjustGunForRobotTurn(true);
	//	setAdjustRadarForRobotTurn(true);
		
		while(true) // Loop principal
		{
			
	//		setTurnRadarRight(45);
			setTurnGunRight(20);	//Estratégias de rotação das partes dos robos não estão implementadas ainda. Estas são para testes apenas.
			//setTurnRight(45);

			if(pertoParede()) {		//Estratégias de movimentação não estão implementadas, essa é apenas para testar as outras coisas.
				setTurnRight(180);
				setAhead(100);
			} else {
				//setAhead(20);
				//setTurnLeft(45);
			}
			
			execute();				//executa a fila de instruções setadas.
			
		}
		
	}
	
	
	public void onScannedRobot(ScannedRobotEvent e) // Métodos padrões como esses devem ser reescritos conforme as regras necessitem
	{
		
			
		setTurnRight(e.getBearing()+90-30*direcaoMov);	//Colocando robo numa posição para esquivar mais fácil

			switch (estrategiaAtual)			//Estrategias relacionadas ao evento de encontrar outros robos na arena poderão ser colocados nesse switch.
			{
				case 0:							//Caso 0: atacar inimigo mais fraco
					inimigosDetectados(e);		//Atualiza lista de inimigos vivos
					if(this.mundo.getTarget() == null)		
						this.mundo.setTarget(escolherAlvoFraco());		//Se ainda não ter nenhum alvo, escolhe o mais fraco
					else if(e.getName() == this.mundo.getTarget().getName())	//senão, se o alvo detectado é o alvo atual...	
					{
						this.mundo.getTarget().setBearing(e.getBearing());		//Atualiza as informações de localização e energia do alvo atual
						double ant =this.mundo.getTarget().getEnergy();
						this.mundo.getTarget().setEnergy(e.getEnergy());
						this.mundo.getTarget().setHeading(e.getHeading());
						
						mira(this.mundo.getTarget().getBearing());			//Mira nele
						esquiva(ant-e.getEnergy(),e);
						
						if(this.mundo.getTarget().getEnergy()<12)			//E atira com a força necessária.
							tiroFatal(this.mundo.getTarget().getEnergy());
						else		
							fogo(this.mundo.getTarget().getDistance());
					}				
					break;
			}
			
			atualizarMundo();
			
	}
	
	public void onRobotDeath(RobotDeathEvent e)					//Método chamado quando algum robo morre na arena.
	{
		Robo roboMorto = new Robo(e.getName(),0,0,0,0);	//Identifica o nome do robô morto.
		if(this.mundo.getInimigosVivos().contains(roboMorto))					
		{
			this.mundo.getInimigosVivos().remove(roboMorto);					//Se for um inimigo, atualiza a lista de robosVivos.
		}
		else if(this.mundo.getAliadosVivos().contains(roboMorto))
		{
			this.mundo.getAliadosVivos().remove(roboMorto);
		}
		
		if(this.mundo.getTarget() != null)
			if(this.mundo.getTarget().getName() == roboMorto.getName())	//Se for o robô que era o alvo atual
				this.mundo.setTarget(null); //Fica sem alvo para buscar outro.
		
		atualizarMundo();
	}
	
	public void onHitByBullet(HitByBulletEvent e)				//O que acontece se o nosso robo levar tiros?
	{
		//função ainda não terminada
		if(pertoParede()) {
			setTurnRight(180);
			setAhead(100);
		} else {
			setAhead(90*direcaoMov);
		}
	}
	
	public void onMessageReceived(MessageEvent m)				//Método chamado quando alguem do time passa uma mensagem
	{
				
	}
	

	
	
	//Regras deverão estar implementadas em métodos a partir daqui
	
	public void atualizarMundo()
	{
		this.mundo.setTime(getTime());
		this.mundo.adicionarEvolucao(this.mundo);	
	}
	
	public Robo escolherAlvoFraco()			//Esse método checa dentre os inimigos vivos, aquele que está mais fraco e retorna seus dados
	{
		double menorVida = 1000;
		Robo targetAux = null;
		Robo target = null;
		
		for(int i = 0; i < this.mundo.getInimigosVivos().size(); i++)				//dentre a lista de inimigos vivos, identifica aquele com menor vida.
		{
			targetAux = (Robo) this.mundo.getInimigosVivos().get(i);
			if(targetAux.getEnergy() < menorVida)
			{
				menorVida = targetAux.getEnergy();
				target = targetAux;		
			}
		}
		
		return target;
	}
	
	public void inimigosDetectados(ScannedRobotEvent e)			//Este método atualiza a lista de inimigos vivos conhecidos pelo robo
	{
		Robo inimigo;
		int index = 0;
		if(!isTeammate(e.getName()))
		{
			inimigo = new Robo(e.getName(),e.getEnergy(),e.getDistance(),e.getHeading(),e.getBearing());
			index = this.mundo.getInimigosVivos().indexOf(inimigo);
			
			if(index >= 0)
				this.mundo.getInimigosVivos().set(index, inimigo);
			else
				this.mundo.getInimigosVivos().add(inimigo);
		}
	}
	
	public void esquiva(double energiaDif,ScannedRobotEvent e)	//recebe a diferença de energia do inimigo
	{
		if(energiaDif>0 && energiaDif<=3)
		{
			setMaxVelocity(8);		//seta a velocidade máxima para esquivar
			direcaoMov=-direcaoMov;	//mudar a direção
			setAhead((e.getDistance()/4 +25)*direcaoMov);	//move para determinada direcão
		}
		
	}
	
	
	
	public void mira(double Adv) {					//Direciona o canhão do robo para o inimigo cuja posição relativa é passada por parâmetro
		double A=getHeading()+Adv-getGunHeading();
		if (!(A > -180 && A <= 180)) {				//Calcula a normalização do ângulo que deve ser virado pelo canhão.
			while (A <= -180) {
				A += 360;
			}
			while (A > 180) {
				A -= 360;
			}
		}
		setTurnGunRight(A);
	}
	
	public void fogo(double Distancia) {			//Controla a força do tiro conforme a distância do oponente: mais perto, bala mais forte, distante, bala mais fraca.
		if (Distancia > 200 || getEnergy() < 15) {
			setFire(1);
		} else if (Distancia > 50) {
			setFire(2);
		} else {
			setFire(3);
	    }
	}
	public double anguloRelativo(double ANG) {		//Função que normaliza o ângulo passado como parâmetro.
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
	
	public void tiroFatal(double EnergiaIni) {		//dispara um "tiro de mizericordia" quando o inimigo estiver com menos de 12 de vida, com força apropriada para mata-lo.
		double Tiro = (EnergiaIni / 4) + .1;
		setFire(Tiro);
	}
	
	public boolean pertoParede() {					//método que faz o robô andar para traz caso se aproxime de alguma parede.
		return (getX() < 50 || getX() > getBattleFieldWidth() - 50 ||
			getY() < 50 || getY() > getBattleFieldHeight() - 50);
	}

	
	public boolean timeGanhando()		//Método ainda em construção. ignore-o xD
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
	
	public boolean inimigosMaisProximos() //Método ainda em construção. ignore-o xD
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
