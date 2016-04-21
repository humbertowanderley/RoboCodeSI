package extras;
import java.util.*;

//Essa classe deve implementar o modelo do mundo visto pelos robos.

public class ModeloMundo {
	
	ArrayList inimigosVivos;				//Lista dos inimigos vivos no mundo
	ArrayList aliadosVivos;					//Lista dos aliados vivos no mundo
	//ArrayList evolucaoMundo;				//Lista de estados do mundo (como ele foi se modificando no tempo)
	Robo target;							//Alvo atual no mundo
	long time;								//Tempo da ultima atualização do mundo
	
	public ModeloMundo()					//Construtor inicializa atributos do mundo
	{
		this.inimigosVivos = new ArrayList();
		this.aliadosVivos = new ArrayList();
		
		this.time = 0;	
	}
	
	
	
	
	//Geters e Seters
	public ArrayList getInimigosVivos()
	{
		return this.inimigosVivos;
	}
	public ArrayList getAliadosVivos()
	{
		return this.aliadosVivos;
	}
	public long getTime()
	{
		return this.time;
	}
	
	public Robo getTarget()
	{
		return this.target;
	}
	
	public void setTime(long time)
	{
		this.time = time;
	}
	
	public void setTarget(Robo target)
	{
		this.target = target;	
	}
	
	//Comparação entre dois estados de mundo é feita baseada no tempo.
	public boolean equals(Object mundo) 
	{
	     if (this.time == ((ModeloMundo) mundo).time)
	          return true;
	     else
	    	 return false;
	}
	

}
