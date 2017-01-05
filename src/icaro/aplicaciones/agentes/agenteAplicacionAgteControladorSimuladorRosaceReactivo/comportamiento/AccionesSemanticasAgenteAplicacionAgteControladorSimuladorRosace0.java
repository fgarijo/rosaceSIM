package icaro.aplicaciones.agentes.agenteAplicacionAgteControladorSimuladorRosaceReactivo.comportamiento;

//import icaro.infraestructura.entidadesBasicas.EventoRecAgte;
import icaro.aplicaciones.Rosace.informacion.*;
import icaro.aplicaciones.Rosace.utils.ConstantesRutasEstadisticas;
import icaro.aplicaciones.recursos.recursoCreacionEntornosSimulacion.ItfUsoRecursoCreacionEntornosSimulacion;
import icaro.aplicaciones.recursos.recursoPersistenciaEntornosSimulacion.ItfUsoRecursoPersistenciaEntornosSimulacion;
import icaro.aplicaciones.recursos.recursoPersistenciaEntornosSimulacion.imp.ReadXMLTestSequence;
import icaro.aplicaciones.recursos.recursoVisualizadorEntornosSimulacion.ItfUsoRecursoVisualizadorEntornosSimulacion;
import icaro.aplicaciones.recursos.recursoVisualizadorEntornosSimulacion.imp.EscenarioSimulacionRobtsVictms;
import icaro.infraestructura.entidadesBasicas.NombresPredefinidos;
import icaro.infraestructura.entidadesBasicas.procesadorCognitivo.Informe;
import icaro.infraestructura.patronAgenteReactivo.control.acciones.AccionesSemanticasAgenteReactivo;
import icaro.infraestructura.recursosOrganizacion.configuracion.ItfUsoConfiguracion;
import icaro.infraestructura.recursosOrganizacion.recursoTrazas.imp.componentes.InfoTraza;
import icaro.infraestructura.recursosOrganizacion.recursoTrazas.imp.componentes.InfoTraza.NivelTraza;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
//This agent class need the next imports in order to use resources

//Other imports used by this Agent
//#start_nodespecialImports:specialImports <--specialImports-- DO NOT REMOVE THIS
//#end_nodespecialImports:specialImports <--specialImports-- DO NOT REMOVE THIS
public class AccionesSemanticasAgenteAplicacionAgteControladorSimuladorRosace0 extends AccionesSemanticasAgenteReactivo {

    // Include in this section global variables used in this Agent
    // #start_nodeglobalVariables:globalVariables <--globalVariables-- DO NOT
    // REMOVE THIS
    private ReadXMLTestSequence rXMLTSeq; // para leer del fichero de xml de victimas
    private NodeList nodeLst;      // estructura en memoria con todos los nodos de
    // las victimas que hay en el fichero xml
    private int numMensajesEnviar; // numero total de nodos que hay en nodeLst
    private ItfUsoRecursoVisualizadorEntornosSimulacion itfUsoRecursoVisualizadorEntornosSimulacion;   //Para visualizar graficas de estadisticas
    private InfoEquipo equipo;
    private String identificadorEquipo;
    private String modeloOrganizativo;
    private ArrayList identsAgtesEquipo;
//	private ComunicacionAgentes comunicacion;
    private boolean stop = false; // Parar la simulacion
//	private ItfUsoRepositorioInterfaces itfUsoRepositorioInterfaces;
    private ItfUsoConfiguracion itfconfig;
    private String rutaFicheroVictimasTest;
    private String rutaFicheroRobotsTest;
    private long tiempoInicialDeLaSimulacion = 0;      //Variable para obtener el tiempo al iniciar la simulacion
    private int numeroVictimasEntorno = 0;            //Numero de victimas actuales que se han introducido en el entorno    
    private int numeroVictimasAsignadas = 0;          //Numero de victimas asignadas a robots
    private int numeroVictimasDiferentesSimulacion; //Numero de victimas diferentes que van a intervenir en el proceso de simulacion
    private int numeroRobotsSimulacion = 0;         //Numero de robots diferentes que van a intervenir en el proceso de simulacion
    private int intervaloSecuencia = 5000;                 //Intervalo uniforme de envio de la secuencia de victimas
    private ArrayList<Victim> victimasDefinidas;     //Victimas definidas en la simulacion 
    private Map<String, Victim> victims2Rescue ;      //Victimas que han sido enviadas al equipo   
    private Map<String, String> victimasAsignadas = new HashMap<String, String>();   //Victimas que han sido asignadas a algun robot, es decir, algun robot se ha hecho responsable para salvarla
    private Map<String, InfoAgteAsignacionVictima> infoVictimasAsignadas;
    private ItfUsoRecursoCreacionEntornosSimulacion itfUsoRecursoCreacionEntornosSimulacion = null;
    private ItfUsoRecursoPersistenciaEntornosSimulacion itfUsoRecursoPersistenciaEntornosSimulacion;
    private InfoSerieResultadosSimulacion infoContxVict;
//    private InfoAsignacionVictima infoAsigVictima;
    private InfoCasoSimulacion infoCasoSimul;
    private InfoEntornoCasoSimulacion infoEntornoCaso;
    private int indexvictimasAsignadasEstadistica = 0;
    private int contadorRobotsQueContestanFinsimulacion = 0;
    final int nMM = this.numMensajesEnviar; // numeroMaximoDeMensajes a  enviar										
//    final int intervaloSecuencia;
    // #end_nodeglobalVariables:globalVariables <--globalVariables-- DO NOT
    // REMOVE THIS
    private GestionCasosSimulacion gestionCasosSimulacion;
    private boolean peticionTerminacionSimulacionUsuario = false;
    private boolean robotEstatusEquipoInicializado = false;
    private EscenarioSimulacionRobtsVictms escenarioActual;
    private String identFicheroEscenario;
    private int ordenesEnviadas;

    // AccionA is the action initial executed when agent manager sends the comenzar event
    public void AccionComenzar() {
        //Inicializar las interfaces a los recursos que se van a utilizar
        //----------------------------------------------------------------------------------------------------------------
        // INICIALIZACION DE VARIABLES RELACIONADAS CON LAS VICTIMAS
        //----------------------------------------------------------------------------------------------------------------			
        // Lectura del fichero de victimas
        // Recuperar la ruta del fichero de victimas del escenario
        //itfUsoRepositorioInterfaces = ClaseGeneradoraRepositorioInterfaces.instance();


        //----------------------------------------------------------------------------------------------------------------
        // Inicializar variables para la comunicacion, el identificadorEquipo,
        // los identificadores de los agentes del equipo
        //----------------------------------------------------------------------------------------------------------------	
        try {
            itfconfig = (ItfUsoConfiguracion) this.itfUsoRepositorio.obtenerInterfaz(NombresPredefinidos.NOMBRE_ITF_USO_CONFIGURACION);
            itfUsoRecursoPersistenciaEntornosSimulacion = (ItfUsoRecursoPersistenciaEntornosSimulacion) this.itfUsoRepositorio.obtenerInterfaz(NombresPredefinidos.ITF_USO + "RecursoPersistenciaEntornosSimulacion1");
            itfUsoRecursoVisualizadorEntornosSimulacion = (ItfUsoRecursoVisualizadorEntornosSimulacion) this.itfUsoRepositorio.obtenerInterfaz(NombresPredefinidos.ITF_USO + "RecursoVisualizadorEntornosSimulacion1");
            itfUsoRecursoVisualizadorEntornosSimulacion.setIdentAgenteAReportar(this.nombreAgente);
            itfUsoRecursoVisualizadorEntornosSimulacion.setItfUsoPersistenciaSimulador(itfUsoRecursoPersistenciaEntornosSimulacion);
            identFicheroEscenario = itfconfig.getValorPropiedadGlobal(VocabularioRosace.NombreFicheroEscenarioSimulacion);
            identificadorEquipo = itfconfig.getValorPropiedadGlobal(VocabularioRosace.NombrePropiedadGlobalIdentEquipo);
            equipo = new InfoEquipo(this.nombreAgente, identificadorEquipo);
            identsAgtesEquipo = equipo.getTeamMemberIDs(); // Se obtienen de la configuracion
            comunicator = this.getComunicator();
            this.obtenerEscenarioSimulacion();
            gestionCasosSimulacion = new GestionCasosSimulacion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        trazas.trazar(this.nombreAgente, "Accion AccionComenzar completada ....", NivelTraza.debug);
    }
   public void obtenerEscenarioSimulacion(){
       // Se intenta primero obtenerlo de  la descripcion de la orgaizacion y de la persistencia 
       // Si no se obtiene nada se le pide a la interfaz de usuario
       try {
        if (identFicheroEscenario != null){
                escenarioActual = itfUsoRecursoPersistenciaEntornosSimulacion.obtenerInfoEscenarioSimulacion(identFicheroEscenario);
               
            }
            if (escenarioActual != null){
                escenarioActual.renombrarIdentRobts(identsAgtesEquipo);
                itfUsoRecursoVisualizadorEntornosSimulacion.mostrarVentanaControlSimulador(escenarioActual);
                this.inicializarEstatusRobotsEquipo();
                victims2Rescue = escenarioActual.getVictims();
                numeroVictimasDiferentesSimulacion = victims2Rescue.size();
                this.informaraMiAutomata("escenarioDefinidoValido", escenarioActual);
            }
            else{
                modeloOrganizativo = itfconfig.getValorPropiedadGlobal(VocabularioRosace.NOMBRE_PROPIEDAD_GLOBAL_MODELO_ORGANIZATIVO);
                this.numeroRobotsSimulacion = identsAgtesEquipo.size();
                itfUsoRecursoVisualizadorEntornosSimulacion.obtenerEscenarioSimulacion(modeloOrganizativo, numeroRobotsSimulacion);
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
       trazas.trazar(this.nombreAgente, "Accion obtenerEscenarioSimulacion completada ....", NivelTraza.debug);
       
   }
   public void  ValidarEscenarioRecibido (EscenarioSimulacionRobtsVictms escenario){
        try {
            if (escenario !=null){
                victims2Rescue = escenario.getVictims();
                numeroVictimasDiferentesSimulacion = victims2Rescue.size();
                if(this.numeroRobotsSimulacion !=escenario.getNumRobots() ){
                    
                }else {
                    escenarioActual = escenario;
                    escenarioActual.renombrarIdentRobts(identsAgtesEquipo);
                    itfUsoRecursoVisualizadorEntornosSimulacion.mostrarEscenarioMovimiento(escenarioActual);
                    this.informaraMiAutomata("escenarioDefinidoValido", escenarioActual);
                }
        }
            this.inicializarEstatusRobotsEquipo();
            this.itfUsoRecursoVisualizadorEntornosSimulacion.mostrarIdentsEquipoRobots(identsAgtesEquipo);
            
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
   }

    //Esta accion semantica se ejecuta cuando se envia el input "sendSequenceOfSimulatedVictimsToRobotTeam" en el 
    //metodo sendSequenceOfSimulatedVictimsToRobotTeam de la clase NotificacionEventosRecursoGUI3	
    public void sendSequenceOfSimulatedVictimsToRobotTeam(Integer intervaloSec) {
        this.intervaloSecuencia = intervaloSec;
//        final int interv = intervaloSecuencia;
        trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente, "Accion SendSequenceOfSimulatedVictimsToRobotTeam  ...."
                + intervaloSecuencia, InfoTraza.NivelTraza.debug));
        //--------------------------------------------------------------------------------------------------------------------
        // Inicializar y recuperar la referencia al recurso de estadisticas y el visualizador de estadisticas
        // Inicializar el numero de victimas diferentes que hay en la simulacion en el recurso PersistenciaEntornosSimulacion
        //--------------------------------------------------------------------------------------------------------------------
        long tiempoActual = 0;

        try {
            //    itfUsoRecursoPersistenciaEntornosSimulacion.setNumeroVictimasDiferentesSimulacion(rXMLTSeq);
            //	this.numeroVictimasDiferentesSimulacion = itfUsoRecursoPersistenciaEntornosSimulacion.getNumeroVictimasDiferentesSimulacion();
//            victimasDefinidas = this.itfUsoRecursoPersistenciaEntornosSimulacion.getVictimasArescatar();
//            numeroVictimasDiferentesSimulacion = victimasDefinidas.size();
            infoVictimasAsignadas = new HashMap<String, InfoAgteAsignacionVictima>();
            this.numeroRobotsSimulacion =this.escenarioActual.getNumRobots();
            trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente,
                    "Accion SendSequenceOfSimulatedVictimsToRobotTeam  .... "
                    + "Simulacion de tipo " + identificadorEquipo + " ; "
                    + numeroRobotsSimulacion + " robots en la simulacion ; "
                    + numeroVictimasDiferentesSimulacion + " Victimas Diferentes Simulacion "
                    + " (numero Mensajes a Enviar " + this.numMensajesEnviar + ") "
                    + " ; frecuencia de envio " + intervaloSecuencia + " milisegundos", InfoTraza.NivelTraza.debug));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(infoCasoSimul==null)inicializarInfoCasoSimulacion();

        //----------------------------------------------------------------------------------------------------------------	
        // Crear hilo responsable de realizar el envio de la secuencia de victimas a intervalos regulares de tiempo
        //----------------------------------------------------------------------------------------------------------------	

        //final ReadXMLTestSequence rNXM = rXMLTSeq;
        //final NodeList nl = nodeLst;
        Thread t = new Thread() {

            @Override
            public void run() {
                int i = 0;
                Victim victima;
                 Object[] victimas= victims2Rescue.values().toArray();
                while ((i < numeroVictimasDiferentesSimulacion) && (stop == false)) {
                    //      victima = createNewVictim(rXMLTSeq, nodeLst, i);
                    victima = (Victim)victimas[i];
                    OrdenCentroControl ccOrder = new OrdenCentroControl("ControlCenter", VocabularioRosace.MsgOrdenCCAyudarVictima, victima);
                    // Escribir nueva linea de estadistica en el fichero de llegada de victimas
                    String victimId=victima.getName();
                     InfoRescateVictima infoAsigVictima = new InfoRescateVictima(victimId);
                    try {
//                        long tActual = System.currentTimeMillis();
                        
                        //Lo siguiente se hacia en escribeEstadisticaFicheroXMLTRealLlegadaVictimas //EN EL FUTURO HABRIA QUE INTENTAR QUITARLO DE ESE METODO
                        Victim valor = victims2Rescue.put(victimId, victima);
                        if (valor == null) //no estaba insertado
                        {
                            incrementarNumeroVictimasActuales();
                        }
                        //Actualiza el fichero EstadisticasLlegadaVictimas.xml
//                        itfUsoRecursoPersistenciaEntornosSimulacion.escribeEstadisticaFicheroXMLTRealLlegadaVictimas(tActual, victima);
                        //Anotar informacion en el buffer de estadisticas de victimas que llegan al entorno
                       
//                        infoAsigVictima.setNrovictimasenentorno(numeroVictimasEntorno);
                        infoAsigVictima.setTiempoPeticion(System.currentTimeMillis());
//                        infoAsigVictima.setVictimId(victimId);
                        
                        ///////
                        //        VictimaLlegadaEstadistica victLlegEst = new VictimaLlegadaEstadistica();
                        //        victLlegEst.setVictima(victima);
                        //        victLlegEst.setTiempoActual(tActual);
                        //        victLlegEst.setNrovictimas(numeroVictimasEntorno);
                        //	victimasLlegadaEstadistica.add(i-1, victLlegEst);  //el buffer empieza en la posicion 0
                        //       victimasLlegadaEstadistica.add(i, victLlegEst);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (identificadorEquipo.equals("robotSubordinado")) //VocabularioRosace.IdentEquipoJerarquico
                    {
                        comunicator.enviarInfoAotroAgente(ccOrder, VocabularioRosace.IdentAgteDistribuidorTareas);
                    } else {
                        comunicator.informaraGrupoAgentes(ccOrder, identsAgtesEquipo);
                    }
                    infoCasoSimul.addInfoAsignacionVictima(infoAsigVictima);
                    i++;
                    try {
                        this.sleep(intervaloSecuencia);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }// fin del while. Se calcula el tiempo medio de envio de victimas
                int tiempoMedioEnvio= (int)(System.currentTimeMillis() - tiempoInicialDeLaSimulacion)/numeroVictimasDiferentesSimulacion;
                infoCasoSimul.setTiempoMedioEnvioPeticiones(tiempoMedioEnvio);
                // Se han enviado todas las victimas
                // Cerrar el fichero de estadistica en el fichero de llegada de victimas

            }
        };
        t.start();
    }
    
public void sendSimulatedVictimToRobotTeam(String idVictima) {
    trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente, "Accion SendVictimToRobotTeam  .... "
                + idVictima, InfoTraza.NivelTraza.debug));
    if(infoCasoSimul==null)inicializarInfoCasoSimulacion();
    if(victims2Rescue == null)victims2Rescue =escenarioActual.getVictims();
    Victim victima = victims2Rescue.get(idVictima);
    OrdenCentroControl ccOrder = new OrdenCentroControl("ControlCenter", VocabularioRosace.MsgOrdenCCAyudarVictima, victima);
    comunicator.informaraGrupoAgentes(ccOrder, identsAgtesEquipo);
    InfoRescateVictima infoRescVictima = new InfoRescateVictima(idVictima);
    infoRescVictima.setTiempoPeticion(System.currentTimeMillis());
    infoCasoSimul.addInfoAsignacionVictima(infoRescVictima);
    ordenesEnviadas++;
            if(ordenesEnviadas==numeroVictimasDiferentesSimulacion){
                this.visualizarYguardarResultadosCaso();
            }
}
    //Esta accion semantica se ejecuta cuando se envia el input "victimaAsignadaARobot" en la  
    //tarea sincrona GeneraryEncolarObjetivoActualizarFoco del agente Subordinado
    //Esta accion semantica se ejecuta cuando se envia el input "victimaAsignadaARobot" en la  
    //tarea sincrona EncolarObjetivoActualizarFoco del agente Igualitario (robotMasterIA)	
//    public void VictimaAsignadaARobot(Long tiempoReportado, String refVictima, String nombreAgenteEmisor, Integer miEvaluacion) {
    
private void inicializarInfoCasoSimulacion(){
    
//   long  tiempoActual = System.currentTimeMillis();
            tiempoInicialDeLaSimulacion = System.currentTimeMillis();
//            String identCaso = identFicheroEscenario + tiempoActual;
          infoCasoSimul= gestionCasosSimulacion.crearCasoSimulacion(identFicheroEscenario);
            //     infoContxVict = new InfoContextoAsignacionVictima(identificadorEquipo, numeroVictimasDiferentesSimulacion, numeroRobotsSimulacion,  this.intervaloSecuencia); 
//            infoEntornoCaso = new InfoEntornoCasoSimulacion(identificadorEquipo, numeroRobotsSimulacion, numeroVictimasDiferentesSimulacion, intervaloSecuencia);
//            infoEntornoCaso.setTiempoInicioSimulacion(tiempoInicialDeLaSimulacion);
//            infoCasoSimul = new InfoCasoSimulacion(identCaso,escenarioActual.getIdentEscenario());
            infoCasoSimul.setInfoCasoSimulacion(identificadorEquipo,escenarioActual.getmodeloOrganizativo(),escenarioActual.getNumRobots(),escenarioActual.getNumVictimas());
            infoCasoSimul.setTiempoInicioEnvioPeticiones(tiempoInicialDeLaSimulacion);
}
public void victimaAsignadaARobot(InfoRescateVictima infoAsignacion) {
        try {
            long tiempoReportado = (long)infoAsignacion.getTiempoAsignacion();
            String refVictima = infoAsignacion.getvictimaId();
            String nombreAgenteEmisor = infoAsignacion.getRobotRescatadorId();
            int miEvaluacion=(int) infoAsignacion.getcosteRescate();
            trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente,
                    "Accion VictimaAsignadaARobot  ... " + "tiempoActual->" + tiempoReportado + " ; refVictima->"
                    + refVictima + " ; nombreAgenteEmisor->" + nombreAgenteEmisor + " ; miEvaluacion->" + miEvaluacion, InfoTraza.NivelTraza.debug));
            itfUsoRecursoVisualizadorEntornosSimulacion.mostrarVictimaRescatada(refVictima);
            if (infoCasoSimul!=null){
//            InfoAsignacionVictima infoAsigVictima = infoCasoSimul.getInfoAsignacionVictima(refVictima);
//            infoAsigVictima.setEvaluacion(miEvaluacion);
//            infoAsigVictima.setRobotId(nombreAgenteEmisor);
//            infoRescate.setTiempoAsignacion(tiempoReportado - tiempoInicialDeLaSimulacion); // tiempo reportado
//            infoAsigVictima.setNrovictimastotalasignadas(infoCasoSimul.getnumeroVictimasAsignadas());
//            infoAsigVictima.setNrovictimasenentorno(infoCasoSimul.getnumeroVictimasEntorno());
//            itfUsoRecursoPersistenciaEntornosSimulacion.guardarInfoAsignacionVictima(infoAsigVictima);
//            itfUsoRecursoVisualizadorEntornosSimulacion.mostrarVictimaRescatada(refVictima);
            infoCasoSimul.addInfoAsignacionVictima(infoAsignacion);
//            if (infoCasoSimul.todasLasVictimasAsignadas()) {
////                notificarFinSimulacion();
//                visualizarYguardarResultadosCaso();
//            }
            }
        } catch (Exception ex) {
            Logger.getLogger(AccionesSemanticasAgenteAplicacionAgteControladorSimuladorRosace0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void notificarFinCasoSimulacion() {

        try {
//            FinSimulacion finalSimulacion = new FinSimulacion();
            Informe informeFinCaso =new Informe(this.nombreAgente,"notificarFin Caso", VocabularioRosace.MsgeFinCasoSimulacion);
            comunicator.informaraGrupoAgentes(informeFinCaso, identsAgtesEquipo);
            trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente,
                    "Se notifica el fin de la simulacion a los agentes del Equipo:identsAgtesEquipo->" + identsAgtesEquipo,
                    InfoTraza.NivelTraza.info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visualizarYguardarResultadosCaso() {
        try {
            // ArrayList<PuntoEstadistica> llegada = new ArrayList();
            // ArrayList<PuntoEstadistica> asignacion = new ArrayList();
            // Pasarle al visualizador infoSerie y que saque los valores
            itfUsoRecursoVisualizadorEntornosSimulacion.visualizarTiemposRescatePorRobot(infoCasoSimul);
            itfUsoRecursoVisualizadorEntornosSimulacion.visualizarLlegadaYasignacionVictimas(infoCasoSimul);
            itfUsoRecursoVisualizadorEntornosSimulacion.visualizarCosteEnergiaRescateVicitimas(infoCasoSimul);
//            itfUsoRecursoVisualizadorEntornosSimulacion.crearVisorGraficasLlegadaYasignacionVictimas(this.numeroRobotsSimulacion, this.numeroVictimasDiferentesSimulacion, this.intervaloSecuencia, this.identificadorEquipo); // parametros definicion titulos		                                                                        
//            ArrayList<PuntoEstadistica> llegada = infoCasoSimul.getSerieDatosParaVisualizar(InfoCasoSimulacion.SerieDatosTiempoPeticion);
//            ArrayList<PuntoEstadistica> asignacion = infoCasoSimul.getSerieDatosParaVisualizar(InfoCasoSimulacion.SerieDatosTiempoAsignacion);
//            ArrayList<PuntoEstadistica> rescate = infoCasoSimul.getSerieDatosParaVisualizar(InfoCasoSimulacion.SerieDatosTiempoRescate);
//            itfUsoRecursoVisualizadorEntornosSimulacion.visualizarLlegadaYasignacionVictimas(llegada, asignacion, rescate);
//            itfUsoRecursoVisualizadorEntornosSimulacion.crearVisorGraficasTiempoAsignacionVictimas(this.numeroRobotsSimulacion, this.numeroVictimasDiferentesSimulacion, this.intervaloSecuencia, this.identificadorEquipo); // parametros definicion titulos		                                                                        
//            ArrayList<PuntoEstadistica> elapsed = infoCasoSimul.getSerieElapsed().getserieResultadosSimulacion();
//            itfUsoRecursoVisualizadorEntornosSimulacion.visualizarTiempoAsignacionVictimas(elapsed);
//            itfUsoRecursoPersistenciaEntornosSimulacion.guardarSerieResultadosSimulacion(infoCasoSimul.getSerieAsignacion());
//            itfUsoRecursoPersistenciaEntornosSimulacion.guardarSerieResultadosSimulacion(infoCasoSimul.getSerieElapsed());
            itfUsoRecursoPersistenciaEntornosSimulacion.guardarInfoCasoSimulacion(this.infoCasoSimul);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void procesarInfoVictimaAsignada(InfoAgteAsignacionVictima infoAsigVict) {
        // el robot que se ha quedado con la victima informa sobre los detalles de la asignacion
        // este agente incorpora el contexto de asigancion de la victima
        String idVictima= infoAsigVict.getVictimId();
        trazas.trazar(this.nombreAgente,
                "Info Asignacion Victima  ... " + "tiempoActual->" +infoAsigVict.getTiempoAsignacion()  + " ; refVictima->"
                + idVictima + " ; nombreAgenteEmisor->" + infoAsigVict.getRobotId() + " ; miEvaluacion->" + infoAsigVict.getEvaluacion(), InfoTraza.NivelTraza.debug);
        InfoRescateVictima infoAsigVictima = infoCasoSimul.getInfoRescateVictima(idVictima);
        infoAsigVictima.setcosteEstimadoEnAsignacion(infoAsigVict.getEvaluacion());
        infoAsigVictima.setRobotRescatadorId(infoAsigVict.getRobotId());
        infoAsigVictima.setTiempoAsignacion(infoAsigVict.getTiempoAsignacion());
        try {
            itfUsoRecursoVisualizadorEntornosSimulacion.mostrarVictimaRescatada(idVictima);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        infoCasoSimul.addInfoAsignacionVictima(infoAsigVictima);
//        if (infoCasoSimul.todasLasVictimasAsignadas()) {
//            this.informaraMiAutomata(VocabularioRosace.informacionFinSimulacion, null)
//        }

    }
     public void procesarInfoVictimaRescatada(InfoAgteRescateVictima infoRstVict) {
        // el robot que se ha quedado con la victima informa sobre los detalles de la asingnacion
        // este agente incorpora el contexto de asigancion de la victima
        
        trazas.trazar(this.nombreAgente,
                "Info Salvacion Victima  ... " + "tiempoRescate->" + infoRstVict.getTiempoRescate() + " ; refVictima->"
                + infoRstVict.getVictimId() + " ; nombreAgenteEmisor->" + infoRstVict.getRobotId() + " ; Energia Consumida en rescate->" + infoRstVict.getCosteRescate(), InfoTraza.NivelTraza.debug);
        InfoRescateVictima infoRescVictima = infoCasoSimul.getInfoRescateVictima(infoRstVict.getVictimId());
        infoRescVictima.setcosteRescate(infoRstVict.getCosteRescate()); // en terminos de energia consumida
        infoRescVictima.setRobotRescatadorId(infoRstVict.getRobotId());
        infoRescVictima.setTiempoRescate(infoRstVict.getTiempoRescate());
        infoCasoSimul.addInfoRescateVictima(infoRescVictima);
        if (infoCasoSimul.todasLasVictimasRescatadas()) {
            try {
//                infoCasoSimul.ordenarInfoRescateVictimas();
                itfUsoRecursoPersistenciaEntornosSimulacion.guardarInfoCasoSimulacion(this.infoCasoSimul);
//                this.visualizarYguardarResultadosCaso();
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            
            this.informaraMiAutomata(VocabularioRosace.informacionFinSimulacion, null);
        }

    }
    //Esta accion semantica se ejecuta cuando se envia el input "finSimulacion" en la  
    //tarea sincrona FinalizarSimulacion del agente Subordinado y el igualitario
    //Nos permite generar un fichero EstadisticaFinalSimulacionAsignacionMisionV2.xml que resume que victimas han sido asignadas a cada robot.

    public void FinSimulacion(String robot, ArrayList idsVictimasFinalesAsignadas, Double tiempoTotalCompletarMisionAtenderVictimasFinalesAsignadas) {
// Definir las series a visualizar y los datos resumen del escenario
//        Visluarizar los graficos
        this.visualizarYguardarResultadosCaso();
        trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente, "Accion FinSimulacion  .... "
                + "robot->" + robot + " ; idsVictimasFinalesAsignadas->" + idsVictimasFinalesAsignadas
                + " ; tiempoTotalCompletarMisionAtenderVictimasFinalesAsignadas->" + tiempoTotalCompletarMisionAtenderVictimasFinalesAsignadas, InfoTraza.NivelTraza.debug));
        try {
            ArrayList<InfoAgteAsignacionVictima> infoAsignVictms = new ArrayList();
            infoAsignVictms = itfUsoRecursoPersistenciaEntornosSimulacion.obtenerInfoAsignacionVictimas();
            contadorRobotsQueContestanFinsimulacion++;
            if (contadorRobotsQueContestanFinsimulacion == identsAgtesEquipo.size())
                this.itfUsoRecursoVisualizadorEntornosSimulacion.mostrarResultadosFinSimulacion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void crearTempSolicitarDefinicionEscenario(){
        int  tiempoEsperaDefinicionEscenario = 10000;
        this.generarTimeOut(tiempoEsperaDefinicionEscenario, "timeoutEsperaDefinicionEscenario", nombreAgente, nombreAgente);
        
    }
    public void solicitarDefinicionEscenario(){
        try {
            itfUsoRecursoVisualizadorEntornosSimulacion.notificarRecomendacion ("Sin escenario Definido", "No se puede iniciar la simulacion sin definir un escenario",
                    "Abrir un escenario existente o crear uno nuevo");
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }

    public void mostrarEscenarioActualSimulado() {

        trazas.aceptaNuevaTraza(new InfoTraza(this.nombreAgente, "Accion MostrarEscenarioActualSimulado  ....", InfoTraza.NivelTraza.debug));
        try {
//            itfUsoRecursoCreacionEntornosSimulacion.MostrarEscenarioActualSimulado();
            itfUsoRecursoVisualizadorEntornosSimulacion.mostrarEscenarioMovimiento(escenarioActual);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   public void  mostrarRobotsActivos (){
        try {
            itfUsoRecursoVisualizadorEntornosSimulacion.mostrarIdentsEquipoRobots(this.identsAgtesEquipo);
        } catch (Exception ex) {
            Logger.getLogger(AccionesSemanticasAgenteAplicacionAgteControladorSimuladorRosace0.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    public void  PararRobot (String idRobot){
        OrdenParada orden = new OrdenParada(nombreAgente);
//        orden.setMensajePeticion(VocabularioRosace.MsgePeticionParar);
       this.comunicator.enviarInfoAotroAgente(orden, idRobot);
   }
    
    public void  inicializarEstatusRobotsEquipo (){
        // se envia a cada robot su estatus particular en el escenario : situacion, energia, rol y demas
         String identRobot; 
         RobotStatus1 estatusRobot;
        for (Object identsAgtesEquipo1 : identsAgtesEquipo) {
            identRobot = (String) identsAgtesEquipo1;
            estatusRobot = (RobotStatus1)escenarioActual.getRobotInfo(identRobot).clone();
//            RobotStatus estatusRobotCopia = (RobotStatus)estatusRobot.clone();
            comunicator.enviarInfoAotroAgente(estatusRobot, identRobot);
        }
        robotEstatusEquipoInicializado = true;
    }

    // Include in this section other (private) methods used in this agent
    // #start_nodelocalMethods:localMethods <--localMethods-- DO NOT REMOVE THIS
    /**
     * El metodo clasificaError es necesario declararlo, aunque no exista una
     * accion semantica explicita para realizar el tratamiento de errores
     */
    @Override
    public void clasificaError() {
        // TODO Auto-generated method stub
    }

    // ---------------------------------------------------
    // ----------- Metodos auxiliares -----------
    // ---------------------------------------------------
    private void incrementarNumeroVictimasActuales() {
        this.numeroVictimasEntorno++;
    }

    private void incrementarNumeroVictimasAsignadas() {
        this.numeroVictimasAsignadas++;
    }

    private void mostrarVentanaAlertaFinSimulacion() {

        String directorioTrabajo = System.getProperty("user.dir");  //Obtener directorio de trabajo      		

        String msg = "FIN DE LA SIMULACION !!!.\n";
        msg = msg + "Se ha completado la captura de todas las estadisticas para la simulacion actual.\n";
        msg = msg + "Los ficheros de estadisticas se encuentran en el directorio " + directorioTrabajo + "/" + ConstantesRutasEstadisticas.rutaDirectorioEstadisticas + "\n";
        msg = msg + "Los ficheros de estadisticas son los siguientes:\n";
        msg = msg + directorioTrabajo + "/" + ConstantesRutasEstadisticas.rutaficheroXMLEstadisticasLlegadaVictimas + "\n";
        msg = msg + directorioTrabajo + "/" + ConstantesRutasEstadisticas.rutaficheroXMLEstadisticasAsignacionVictimas + "\n";
        msg = msg + directorioTrabajo + "/" + ConstantesRutasEstadisticas.rutaficheroXMLRepartoTareasRobotsYTiempoCompletarlasV2 + "\n";
        msg = msg + directorioTrabajo + "/" + ConstantesRutasEstadisticas.rutaficheroXMLEstadisticasLlegadaYAsignacionVictimas + "\n";
        msg = msg + directorioTrabajo + "/estadisticas/" + "EstIntLlegadaYAsignacionVictims" + "FECHA.xml" + "\n";

        JOptionPane.showMessageDialog(null, msg);
    }

    private void informarResultadosSimulacion() {
        try {
            // visualizamos los resultados
//            this.itfUsoRecursoVisualizadorEntornosSimulacion.visualizarLlegadaYasignacionVictimas(identsAgtesEquipo, identsAgtesEquipo);
            this.itfUsoRecursoVisualizadorEntornosSimulacion.visualizarTiempoAsignacionVictimas(identsAgtesEquipo);
            // guardamos los resultados para poder consultarlos

        } catch (Exception ex) {
            Logger.getLogger(AccionesSemanticasAgenteAplicacionAgteControladorSimuladorRosace0.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}