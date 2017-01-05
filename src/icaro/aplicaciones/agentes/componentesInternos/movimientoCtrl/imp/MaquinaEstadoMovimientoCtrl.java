/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package icaro.aplicaciones.agentes.componentesInternos.movimientoCtrl.imp;

import icaro.aplicaciones.Rosace.informacion.Coordinate;
import icaro.aplicaciones.Rosace.informacion.Coste;
import icaro.aplicaciones.Rosace.informacion.RobotStatus1;
import icaro.aplicaciones.Rosace.informacion.VocabularioRosace;
import icaro.aplicaciones.agentes.componentesInternos.movimientoCtrl.ItfUsoMovimientoCtrl;
import icaro.aplicaciones.recursos.recursoVisualizadorEntornosSimulacion.ItfUsoRecursoVisualizadorEntornosSimulacion;
import icaro.aplicaciones.recursos.recursoVisualizadorEntornosSimulacion.imp.LineaObstaculo;
import icaro.infraestructura.entidadesBasicas.NombresPredefinidos;
import icaro.infraestructura.entidadesBasicas.procesadorCognitivo.Informe;
import icaro.infraestructura.entidadesBasicas.procesadorCognitivo.Temporizador;
import icaro.infraestructura.patronAgenteCognitivo.procesadorObjetivos.factoriaEInterfacesPrObj.ItfProcesadorObjetivos;
import icaro.infraestructura.recursosOrganizacion.recursoTrazas.ItfUsoRecursoTrazas;
import icaro.infraestructura.recursosOrganizacion.recursoTrazas.imp.componentes.InfoTraza;
import icaro.infraestructura.recursosOrganizacion.recursoTrazas.imp.componentes.InfoTraza.NivelTraza;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;


/**
 *
 * @author FGarijo
 */
public class MaquinaEstadoMovimientoCtrl implements ItfUsoMovimientoCtrl  {
    private String identComponente;
    private String identEstadoActual;
    public static  enum EstadoMovimientoRobot {Indefinido,RobotParado, RobotEnMovimiento, RobotBloqueado, RobotBloqueadoPorObstaculo,RobotavanceImposible,enDestino,  error}
    //Nombres de las clases que implementan estados del recurso interno
    public static  enum EvalEnergiaRobot {sinEnergia,energiaSuficiente,EnergiaJusta, EnergiaInsuficiente }
    public EstadoAbstractoMovRobot estadoActual;
    public RobotParado estadoRobotParado;
    public String identAgente;
//    public RobotEnMovimiento estadoMovimiento;
    public volatile double velocidadRobot;
    public ItfUsoRecursoTrazas trazas;
    private  Map<EstadoMovimientoRobot, EstadoAbstractoMovRobot> estadosCreados;
    public volatile Coordinate robotposicionActual;
    public volatile Coordinate destinoCoord;
    public volatile String identDestino;
    public double distanciaDestino ;
    protected Integer velocidadCrucero;
    public int energiaRobot;
    public boolean robotEnDestino = false;
    public ItfProcesadorObjetivos itfProcObjetivos;
//    protected HebraMonitorizacionLlegada monitorizacionLlegadaDestino;
    public ItfUsoRecursoVisualizadorEntornosSimulacion itfUsoRecVisEntornosSimul;
    private ArrayList<LineaObstaculo> obstaculosDescubiertos;
    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass().getSimpleName());

    
    public  MaquinaEstadoMovimientoCtrl (){
	this.obstaculosDescubiertos = new ArrayList<LineaObstaculo>();
        estadosCreados = new EnumMap<EstadoMovimientoRobot, EstadoAbstractoMovRobot>(EstadoMovimientoRobot.class) ;
    }
    @Override
    public void inicializarInfoMovimiento(int energRobot, Coordinate coordInicial, double velocidadInicial) {
       this.energiaRobot= energRobot;
       this.robotposicionActual = coordInicial;
       this.velocidadRobot = velocidadInicial;
       if (this.estadoActual==null)estadoActual = this.crearInstanciaEstado2(EstadoMovimientoRobot.RobotParado);
       this.estadoActual.inicializarInfoMovimiento(energiaRobot, coordInicial, velocidadInicial);
    }

    @Override
    public void cambiaVelocidad(double nuevaVelocidadCrucero) {
        this.estadoActual.cambiaVelocidad(nuevaVelocidadCrucero);
    }

    @Override
    public Coordinate getCoordenasDestino() {
        return (Coordinate)this.destinoCoord.clone();
    }

    @Override
    public void bloquear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean estamosEnDestino(String identDestino) {
        return this.robotEnDestino;
    }

    @Override
    public void setRobotStatus(RobotStatus1 robotStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
    public EstadoAbstractoMovRobot getEstadoActual (){
        return  estadoActual ;
    }
    public void SetComponentId (String idComp){
        identComponente = idComp;
    }
     public void SetItfUsoRecursoVisualizadorEntornosSimulacion (ItfUsoRecursoVisualizadorEntornosSimulacion itfVisualEntSim){
        itfUsoRecVisEntornosSimul = itfVisualEntSim;
    }
    public void SetInfoContexto (ItfProcesadorObjetivos itfProcObj){
        identAgente = itfProcObj.getAgentId();
        itfProcObjetivos = itfProcObj;
        if (identComponente ==null) identComponente = identAgente+"."+this.getClass().getSimpleName();
        trazas = NombresPredefinidos.RECURSO_TRAZAS_OBJ;
    }
     public synchronized EstadoAbstractoMovRobot  cambiarEstado (EstadoMovimientoRobot nuevoEstadoId){
      if (!nuevoEstadoId.name().equals(identEstadoActual)) {
        trazas.trazar(identComponente, " se cambia el estado: "+ identEstadoActual+  " al estado : " + nuevoEstadoId , NivelTraza.debug);
        estadoActual = estadosCreados.get(nuevoEstadoId);
        
        if ( estadoActual != null  ) {}
         else estadoActual =  crearInstanciaEstado2(nuevoEstadoId);  
        identEstadoActual = nuevoEstadoId.name();
    }   
      return estadoActual; 
    }
     		               	
    private  EstadoAbstractoMovRobot crearInstanciaEstado1(EstadoMovimientoRobot estadoId) {
        EstadoAbstractoMovRobot objRobotEstado;
        try {
            String identClase = this.getClass().getSimpleName();
            String rutaClase = this.getClass().getName().replace(identClase,estadoId.name() );
            Class claseRobotEstado = Class.forName(rutaClase);
            try {
                objRobotEstado = (EstadoAbstractoMovRobot) claseRobotEstado.newInstance();
            } catch (Exception ex) {
                Logger.getLogger(MaquinaEstadoMovimientoCtrl.class.getName()).log(Level.SEVERE, null, ex);
                 return null;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MaquinaEstadoMovimientoCtrl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
              
        return objRobotEstado;
    }
     private EstadoAbstractoMovRobot crearInstanciaEstado2(EstadoMovimientoRobot estadoId) {
         
         if(estadoId.equals(EstadoMovimientoRobot.RobotBloqueado))estadoActual = new RobotBloqueado(this);
         else if(estadoId.equals(EstadoMovimientoRobot.RobotParado))estadoActual = new RobotParado(this);
            else estadoActual = new RobotEnMovimiento(this);
	 estadoActual.inicializarDependencias1(this.identAgente, itfUsoRecVisEntornosSimul);
         identEstadoActual = estadoId.name();
         estadosCreados.put(estadoId,estadoActual);
         trazas.trazar(identComponente, " se crea el estado: "+ identEstadoActual+  " y se pone la maquina de estados  en este estado  " , NivelTraza.debug);
//         estadoActual.inicializarInfoMovimiento(this.energiaInicialRobot, robotposicionActual, this.velocidadRobot);
         return estadoActual;
         
     }

    public  void inicializarInfoMovimiento(Coordinate coordInicial, float velocidadInicial){
        robotposicionActual =coordInicial;
        velocidadRobot = velocidadInicial;
    } 
    @Override
	public synchronized void moverAdestino(String identDest,Coordinate coordDestino, double velocidadCrucero) {
		this.identDestino= identDest;
                this.destinoCoord = (Coordinate)coordDestino.clone();
                if(this.identEstadoActual.equals(EstadoMovimientoRobot.RobotParado.name())){
                    this.estadoActual = this.cambiarEstado(EstadoMovimientoRobot.RobotEnMovimiento);
                    estadoActual.inicializarInfoMovimiento(this.energiaRobot, this.robotposicionActual, velocidadCrucero);
                    }
		trazas.trazar(identAgente, "Se recibe una  orden de mover a destino."+ identDest + " El robot esta en el estado :"+ identEstadoActual
				+ " CoordActuales =  "+this.robotposicionActual.toString() + " CoordDestino =  " +this.destinoCoord.toString(), InfoTraza.NivelTraza.debug);
		estadoActual.moverAdestino(identDest,coordDestino, velocidadCrucero);
                  }
   
    @Override
	public synchronized void cambiaDestino(String identDest,icaro.aplicaciones.Rosace.informacion.Coordinate coordDestino) {
		estadoActual.cambiaDestino(identDest,coordDestino);
                this.destinoCoord = coordDestino;
                trazas.trazar(identAgente, "Se recibe una  orden de cambiar  a destino. El robot esta en el estado :"+ identEstadoActual
                + " CoordActuales =  "+this.robotposicionActual.toString() + " CoordDestino =  " +this.destinoCoord.toString(), InfoTraza.NivelTraza.debug);
                }

    @Override
        public synchronized void parar(){
		estadoActual.parar(); 
        }

    @Override
	public void continuar(){
		estadoActual.continuar();
	}

        public synchronized void enDestino(){
        // se informa al control de que estamos en el destino. Se cambia el estado a parar
        estadoActual = this.cambiarEstado(MaquinaEstadoMovimientoCtrl.EstadoMovimientoRobot.RobotParado);
//        this.estadoActual.identDestino = identDest;
        this.robotposicionActual=this.destinoCoord;
		Informe informeLlegada = new Informe (identComponente,identDestino, VocabularioRosace.MsgeLlegadaDestino);
		Temporizador informeTemp = new Temporizador (1,itfProcObjetivos,informeLlegada);
		informeTemp.start();
//        estadoActual = this.cambiarEstado(EstadoMovimientoRobot.RobotParado);
	this.robotposicionActual = new Coordinate(destinoCoord.getX(),destinoCoord.getY(),destinoCoord.getZ());
        this.estadoActual.identDestino = identDestino;
//         trazas.trazar(identAgente, "Se informa de llegada al  destino: " +informeLlegada + " El robot esta en el estado :"+ identEstadoActual
//                + " CoordActuales =  "+destinoCoord.toString() , InfoTraza.NivelTraza.error);
        
  
    }
        public synchronized void setEnergiaRobot(int energRobot){
            this.energiaRobot=energRobot;
        }

    @Override
    public synchronized void imposibleAvanzarADestino(){
       estadoActual = this.cambiarEstado(EstadoMovimientoRobot.RobotBloqueado);
    }
    
    @Override
    public synchronized Coordinate getCoordenadasActuales() {
     if(!identEstadoActual.equals(EstadoMovimientoRobot.RobotEnMovimiento)){
         this.robotposicionActual = (Coordinate)this.estadoActual.getCoordenadasActuales();
     }
      return (Coordinate)this.robotposicionActual.clone();
    }

    @Override
    public synchronized void setCoordenadasActuales(Coordinate nuevasCoordenadas) {
        if (nuevasCoordenadas != null){
            this.robotposicionActual =(Coordinate)nuevasCoordenadas.clone();
        }
    }

    @Override
     public  String getIdentEstadoMovRobot(){
         return identEstadoActual;
     }
     
}
