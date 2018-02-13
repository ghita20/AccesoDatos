package cliente;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import clases.Coche;
import clases.Conductor;
import util.Leer;
// Clase que gesitona la conexion con existDb. Todos los cambios realizados se guardan directamente en la base de datos.
public class ClienteExistDB {
	
	// ***ATRIBUTOS ESTATICOS***
	
	// Driver
	private final static String DRIVER = "org.exist.xmldb.DatabaseImpl"; 
	
	// Nombre de la coleccion principal
	private final static String COLLECTION_NAME = "existDBpractica";
	
	// Puerto de la base de datos
	private final static int PUERTO = 8989;
	
	// URI (contiene la direccion de la base de datos sin especificar la coleccion)
	private final static String URI = "xmldb:exist://localhost:" +PUERTO  +"/exist/xmlrpc/db/";
	
	// Coches
	private final static String FICHERO_COCHES = "coleccion/coches.xml";
	private final static String COLECCION_COCHES = "coches";

	// Conductores
	private final static String FICHERO_CONDUCTORES = "coleccion/conductores.xml";
	private final static String COLECCION_CONDUCTORES = "conductores";

	// Datos de conexion ( Usuario y Contrase�a )
	private final static String USUARIO = "admin";
	private final static String PASSWORD = "admin";
	
	// ***FIN ATRIBUTOS ESTATICOS***
	
	
	// ***ATRIBUTOS***
	// Query Service
	private XPathQueryService servicioConsultas;
	
	// Coleccion principal
	private Collection coleccion;
	
	// ***FIN ATRIBUTOS***
	
	// ***CONSTRUCTOR***
	public ClienteExistDB() throws Exception {
		// Realiza la conexi�n
		Database database = (Database) Class.forName(DRIVER).newInstance(); // Instancia un nuevo objeto Database
		// Registra la base de datos
		DatabaseManager.registerDatabase(database);
		// Instancia la colecci�n
		coleccion = DatabaseManager.getCollection(URI +COLLECTION_NAME, USUARIO, PASSWORD);
		
		// Si la coleccion no existe, la crea y importa los datos de los ficheros externos que hay en la carpeta "coleccion"
		if ( coleccion == null ) {
			// Conecta con la coleccion padre
			Collection coleccionPrincipal = DatabaseManager.getCollection(URI, USUARIO, PASSWORD);
			CollectionManagementService cms = (CollectionManagementService) coleccionPrincipal.getService("CollectionManagementService", "1.0");
			
			// Crea la nueva coleccion
			coleccion = cms.createCollection(COLLECTION_NAME);
			
			// Mensaje
			System.out.println("La coleccion '" +COLLECTION_NAME +"' ha sido creada correctamente.");
			
			// Cierra la coleccion padre
			coleccionPrincipal.close();
			
			// Por defecto si no existe la coleccion importara los datos de los ficheros que tenemos almacenados en la carpeta "coleccion"
			importarDesdeFicheros();
		}
		
		// Instancia los servicios
		servicioConsultas = (XPathQueryService) coleccion.getService("XPathQueryService", "1.0");
		
		// Asigna "MAX_ID" para que al crear nuevos objetos de tipo coche el id de estos sea mayor al id mas grande de la base de datos
		Coche.asignarMaxId( getMaxId() );
		
	}

	// Cierra la conexion
	public void cerrarConexion ( ) throws XMLDBException {
		coleccion.close();
	}
	
	// ***CONSULTAS***
	
	// Devuelve un entero con el id mas alto de los nodos coche. Devolvera 1 si no existen nodos
	public int getMaxId ( ) throws Exception{
		// Consulta que devuelve el precio medio de los coches
		String query = "max(/coches/coche/@id)";
		// Realiza la consulta
		String resultadoConsulta = consultaConUnSoloResultado(query);

		// Si el resultado es "" quiere decir que no ha devuelto nada
		if ( resultadoConsulta.equals("") )
			return 1; // Por defecto el id sera 1 si no hay ningun coche
		
		// Devuelve el resultado
		return Integer.parseInt(resultadoConsulta);
	}
	
	// Devuelve un String con los conductores que conducen x coche
	public String conductoresQueConducenCoche ( Coche coche ) throws SAXException, IOException, ParserConfigurationException, XMLDBException{
		// Salida de la consulta
		String salida = "";

		// Consulta que devuelve todos los nodos "conductor" de la coleccion "conductores" que tienen como id_coche el id del coche pasado como argumento
		ResourceSet result = servicioConsultas.query("/" +COLECCION_CONDUCTORES  +"/conductor[ id_coche = " +coche.getIdCoche() +"]");

		// Si no hay nodos sale del metodo
		if( result.getSize() == 0 )
			throw new NullPointerException("No existen conductores que conduzcan ese coche.");

		// Parsea el el resultado devuelto por la consulta a XML
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new StringReader( result.getMembersAsResource().getContent().toString() )));

		// Lista de nodos "conductor" devueltos por la consulta
		NodeList nodosResultados = doc.getElementsByTagName("conductor");

		// Recorre todos los nodos
		for ( int i = 0 ; i < nodosResultados.getLength() ; i++ ) {
			// Selecciona el nodo actual
			Element nodoConductor = (Element) nodosResultados.item(i);

			// Recoge los atributos del conductor
			String nombre = nodoConductor.getElementsByTagName("nombre").item(0).getTextContent();
			String apellidos = nodoConductor.getElementsByTagName("apellidos").item(0).getTextContent();
			int edad = Integer.parseInt( nodoConductor.getElementsByTagName("edad").item(0).getTextContent() );
			String dni = nodoConductor.getAttribute("dni");
			
			// Agrega una nueva linea con la informacion del conductor
			salida += "\t\tNOMBRE: " +nombre +" APELLIDOS: " +apellidos +" EDAD: " +edad +" DNI: " +dni +"\n";
		}
		return salida;
	}

	// Devuelve un String con los datos del Coche mas caro
	public String cocheMasCaro ( ) throws SAXException, IOException, ParserConfigurationException, XMLDBException {
		// Consulta que devuelve el nodo coche mas caro
		ResourceSet result = servicioConsultas.query("/coches/coche[ precio = max(/coches/coche/precio) ]");

		// Iterador para recorrer el ResourceSet
		ResourceIterator iterador = result.getIterator();

		// Comprueba si devuelve resultado
		if (!iterador.hasMoreResources())
			throw new NullPointerException("No existen coches en la base de datos."); 
		else {
			// Recoge el primer nodo
			Resource resource = iterador.nextResource();

			// Parsea el recurso a Xml
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(new InputSource(new StringReader( (String) resource.getContent() )));

			// Recoge el primer nodo ( Solo hay uno )
			Element cocheMax = (Element) doc.getElementsByTagName("coche").item(0);
			
			// Recoge los datos y crea un objeto tipo coche
			Coche auxC = parsearCoche(cocheMax);
			
			// Devuelve el resultado de la consulta
			return "   COCHE MAS CARO\n\t" +auxC.toString();
		}
	}
	
	// Devuelve un Float con el precio medio de los coches
	public float precioMedioCoches ( ) throws Exception {
		// Consulta que devuelve el precio medio de los coches
		String query = "avg(/coches/coche/precio)";
		// Realiza la consulta
		String resultadoConsulta = consultaConUnSoloResultado(query);
		
		// Si el resultado es "" quiere decir que no ha devuelto nada
		if ( resultadoConsulta.equals("") )
			throw new NullPointerException("No existen coches.");
		
		// Devuelve el resultado
		return Float.parseFloat(resultadoConsulta);
	}

	// Devuelve un String con los datos del Conductor mas joven
	public String conductorMasJoven ( ) throws Exception {
		// Consulta que devuelve el nodo conductor con edad mas pequenia
		ResourceSet result = servicioConsultas.query("/conductores/conductor[ edad = min(/conductores/conductor/edad) ]");

		// Iterador para recorrer el ResourceSet
		ResourceIterator iterador = result.getIterator();

		// Comprueba si devuelve resultado
		if (!iterador.hasMoreResources())
			throw new NullPointerException("No existen conductores en la base de datos."); 
		else {
			// Recoge el primer nodo
			Resource resource = iterador.nextResource();

			// Parsea el recurso a Xml
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(new InputSource(new StringReader( (String) resource.getContent() )));

			// Recoge el primer nodo ( Solo hay uno )
			Element conductor = (Element) doc.getElementsByTagName("conductor").item(0);

			// Recoge los datos y crea un objeto tipo coche
			String nombre = conductor.getElementsByTagName("nombre").item(0).getTextContent();
			String apellidos = conductor.getElementsByTagName("apellidos").item(0).getTextContent();
			int edad = Integer.parseInt( conductor.getElementsByTagName("edad").item(0).getTextContent() );
			String dni = conductor.getAttribute("dni");

			// Devuelve informacion del coche
			return "   CONDUCTOR MAS JOVEN\n\tNOMBRE: " +nombre +" APELLIDOS: " +apellidos +" EDAD: " +edad +" DNI: " +dni;
		}
	}
	
	// Devuelve un String con el resultado de una consulta que solo devuelve un valor ( consultas de tipo max, avg ..etc )
	private String consultaConUnSoloResultado ( String consulta ) throws Exception{
		// Realiza la consulta
		ResourceSet result = servicioConsultas.query(consulta);

		// Comprueba si devuelve resultado
		if (!result.getIterator().hasMoreResources())
			return "";
		else {

			// Parsea el recurso a Xml
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(new InputSource(new StringReader( (String) result.getMembersAsResource().getContent() )));

			// Recoge el primer nodo ( Solo hay uno, el resultado de la consulta )
			Element resultado = (Element) doc.getChildNodes().item(0);

			// Devuelve el resultado de la consulta
			return resultado.getTextContent();
		}
	}

	// Devuelve un String con los datos de los coches que tienen una marca x
	public String cochesConMarca ( String marca ) throws Exception {
		// Salida de la consulta
		String salida = "";

		// Consulta que devuelve todos los nodos coche que tienen como marca " marca "
		ResourceSet result = servicioConsultas.query("/coches/coche[ @marca = \"" +marca +"\" ]");

		// Si no hay nodos sale del metodo
		if( result.getSize() == 0 )
			throw new  NullPointerException("No existen coches con esta marca.");

		// Parsea el el resultado devuelto por la consulta a XML
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new StringReader( result.getMembersAsResource().getContent().toString() )));

		// Lista de nodos "coche" devueltos por la consulta
		NodeList nodosResultados = doc.getElementsByTagName("coche");

		// Recorre todos los nodos
		for ( int i = 0 ; i < nodosResultados.getLength() ; i++ ) {
			// Selecciona el nodo actual
			Element nodoCoche = (Element) nodosResultados.item(i);

			// Parsea el nodo a objeto tipo coche
			Coche auxC = parsearCoche(nodoCoche);

			// Agrega una nueva linea con la informacion del conductor
			salida += "\t\t" +auxC.toString() +"\n";
		}
		return salida;
	}
	
	// ***FIN CONSULTAS***
	
	
	
	// ***IMPORTACION/EXPORTACION DE FICHEROS***

	/* 	Importa en la base de datos exist los ficheros que tenemos almacenados en el programa. 
		( Este proceso elimina todo lo que tenemos en la base de datos y lo reemplaza con el contenido de los ficheros ) */
	public void importarDesdeFicheros ( ) throws XMLDBException {
		// Importa el fichero XML coches
		importarDeFicheroXml(FICHERO_COCHES, COLECCION_COCHES);

		// Importa el fichero XML conductores
		importarDeFicheroXml(FICHERO_CONDUCTORES, COLECCION_CONDUCTORES);
	}

	// Lee de un fichero almacenado en el programa y lo importa en la coleccion actual
	private void importarDeFicheroXml ( final String RUTA_FICHERO , final String NOMBRE_COLECCION ) throws XMLDBException {
		// Coleccion XML Coches
		XMLResource auxColeccion = (XMLResource) coleccion.createResource(NOMBRE_COLECCION, "XMLResource");
		// Lee de los ficheros almacenados
		File fichero = new File(RUTA_FICHERO);
		if(!fichero.canRead()) {
			System.out.println("No se puede leer de " +fichero.getAbsolutePath() );
			return;
		}
		// Asigna el fichero al XMLResource
		auxColeccion.setContent(fichero);
		// Lo almacena en la colecci�n
		coleccion.storeResource(auxColeccion);
	}

	// Almacena la informaci�n de la base de datos en unos ficheros dentro de la carpeta "coleccion"
	public void guardarEnFicheros ( ) throws Exception {
		// Almacena Coches
		guardarEnFichero(COLECCION_COCHES, FICHERO_COCHES);
		// Almacena Conductores
		guardarEnFichero(COLECCION_CONDUCTORES, FICHERO_CONDUCTORES);
	}
	
	// Almacena una colecci�n en un fichero
	private void guardarEnFichero ( final String NOMBRE_COLECCION , final String NOMBRE_FICHERO ) throws XMLDBException, TransformerFactoryConfigurationError, TransformerException {
		// Localizar un documento
		XMLResource res = (XMLResource) coleccion.getResource(NOMBRE_COLECCION);
		if (res == null) {
			System.out.println("NO EXISTE LA COLECCION '" +NOMBRE_COLECCION +"'");
		} else {

			// Volcado del documento a un �rbol DOM
			Node document = (Node) res.getContentAsDOM();
			Source source = new DOMSource(document);

			// Volcado del documento de memoria a consola
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			
			//Result console = new StreamResult(System.out); // Imprime en consola el resultado
			//ransformer.transform(source, console); // Imprime en consola el resultado

			// Volcado del documento a un fichero
			Result fichero = new StreamResult(new java.io.File(NOMBRE_FICHERO ));
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, fichero);
		}
	}
	
	// ***FIN IMPORTACION/EXPORTACION DE FICHEROS***
	
	
	
	// ***INSERCION/BORRADO/DESCARGA DE NODOS***
	
	// Inserta un nuevo coche a la base de datos
	public void insertarCoche( Coche coche) throws Exception{
		// Comando para insertar un nuevo nodo coche
		String nuevoCoche = 
				"<coche" +" marca=\"" +coche.getMarca()  + "\"" +" id=\"" +coche.getIdCoche()  + "\""   +" >"
					+ "<modelo>" + coche.getModelo() + "</modelo>"
					+ "<color>" + coche.getColor() + "</color>"
					+ "<precio>" + coche.getPrecio() + "</precio>"
				+ "</coche>";
		// Realiza la operacion
		ResourceSet result = servicioConsultas.query("update insert " + nuevoCoche + " into /coches");
	}

	// Inserta un nuevo conductor a la base de datos
	public void insertarConductor( Conductor conductor ) throws Exception{
		// Comando para insertar un nuevo nodo conductor
		String nuevoConductor = 
				"<conductor" +" dni=\"" +conductor.getDni()  + "\" >"
					+ "<nombre>" + conductor.getNombre() + "</nombre>"
					+ "<apellidos>" + conductor.getApellidos() + "</apellidos>"
					+ "<edad>" + conductor.getEdad() + "</edad>"
					+ ( conductor.getCoche() == null ? "":("<id_coche>" + conductor.getCoche().getIdCoche() + "</id_coche>") ) // Solo agrega esta linea si el conductor tiene coche
				+ "</conductor>";
		// Realiza la operacion
		ResourceSet result = servicioConsultas.query("update insert " + nuevoConductor + " into /conductores");
	}
	
	// Devuelve un ArrayList con todos los objetos Coche almacenados en la db
	public ArrayList<Coche> descargarCoches ( ) throws XMLDBException, SAXException, IOException, ParserConfigurationException {
		// ArrayList auxiliar
		ArrayList<Coche> auxCoches = new ArrayList<>();

		// Consulta que devuelve todos los nodos "coche" de la coleccion "coches"
		ResourceSet result = servicioConsultas.query("/" +COLECCION_COCHES +"/coche");
		
		// Si no hay nodos sale del metodo
		if( result.getSize() == 0 )
			return auxCoches;
		
		// Parsea el el resultado devuelto por la consulta a XML
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new StringReader( result.getMembersAsResource().getContent().toString() )));

		// Lista de nodos "coche" devueltos por la consulta
		NodeList nodosResultados = doc.getElementsByTagName("coche");

		// Recorre todos los nodos
		for ( int i = 0 ; i < nodosResultados.getLength() ; i++ ) {
			// Selecciona el nodo actual
			Element nodoCoche = (Element) nodosResultados.item(i);
			
			// Parsea el Element a objeto tipo Coche
			Coche auxCoche = parsearCoche(nodoCoche);

			// Agrega el coche al arrayList
			auxCoches.add(auxCoche);
		}
		return auxCoches;
	}
	
	// Parsea un Element a un objeto de tipo Coche ( usando el constructor sin parametros )
	private Coche parsearCoche ( Element nodoCoche ) {
		// Nuevo Coche ( Lo instancia con el constructor por defecto para no modificar el MAX_ID ya que queremos asignarsele el que tiene en la base de datos )
		Coche auxCoche = new Coche();
		
		// Recoge los atributos del coche
		String marca = nodoCoche.getAttribute("marca");
		String modelo = nodoCoche.getElementsByTagName("modelo").item(0).getTextContent();
		String color = nodoCoche.getElementsByTagName("color").item(0).getTextContent();
		float precio = Float.parseFloat( nodoCoche.getElementsByTagName("precio").item(0).getTextContent() );
		int idCoche = Integer.parseInt( nodoCoche.getAttribute("id") );

		// Asigna los datos recogidos
		auxCoche.setMarca( marca );
		auxCoche.setModelo( modelo );
		auxCoche.setColor( color );
		auxCoche.setPrecio( precio );
		auxCoche.setIdCoche( idCoche );

		// Devulve el coche
		return auxCoche;
	}

	// Devuelve un ArrayList con todos los objetos Conductor almacenados en la db. Es necesario el parametro "coches" para poder conseguir el coche al que el conductor esta vinculado
	public ArrayList<Conductor> descargarConductores ( ArrayList<Coche> coches ) throws XMLDBException, SAXException, IOException, ParserConfigurationException {
		// ArrayList auxiliar
		ArrayList<Conductor> auxConductores = new ArrayList<>();

		// Consulta que devuelve todos los nodos "conductor" de la coleccion "conductores"
		ResourceSet result = servicioConsultas.query("/" +COLECCION_CONDUCTORES  +"/conductor");

		// Si no hay nodos sale del metodo
		if( result.getSize() == 0 )
			return auxConductores;
		
		// Parsea el el resultado devuelto por la consulta a XML
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new StringReader( result.getMembersAsResource().getContent().toString() )));

		// Lista de nodos "conductor" devueltos por la consulta
		NodeList nodosResultados = doc.getElementsByTagName("conductor");

		// Recorre todos los nodos
		for ( int i = 0 ; i < nodosResultados.getLength() ; i++ ) {
			// Selecciona el nodo actual
			Element nodoConductor = (Element) nodosResultados.item(i);

			// Recoge los atributos del conductor
			String nombre = nodoConductor.getElementsByTagName("nombre").item(0).getTextContent();
			String apellidos = nodoConductor.getElementsByTagName("apellidos").item(0).getTextContent();
			int edad = Integer.parseInt( nodoConductor.getElementsByTagName("edad").item(0).getTextContent() );
			String dni = nodoConductor.getAttribute("dni");
			int idCoche = -1;
			try {
				idCoche = Integer.parseInt( nodoConductor.getElementsByTagName("id_coche").item(0).getTextContent() );
			}catch( Exception e ) {
				//System.out.println("No tiene coche asignado...");
			}
			
			// Por defecto el coche es null, por si no tiene ningun coche asignado
			Coche cocheAsignado = null;
			
			// Conseguir el coche del conductor
			if ( idCoche != -1 ) { // Si tiene un id_coche asignado
				// Buscamos el coche con ese id en el array que hemos pedido como parametro
				for ( Coche c : coches )
					if ( c.getIdCoche() == idCoche ) {
						cocheAsignado = c;
						break; // Sale del bucle for ya que hemos encotrado el coche
					}
			}
			
			// Crea el conductor con todos los datos recogidos
			Conductor auxConductor = new Conductor(nombre, apellidos, edad, dni, cocheAsignado);

			// Agrega el conductor al arrayList
			auxConductores.add(auxConductor);
		}
		return auxConductores;
	}

	// Elimina todos los nodos de las colecciones "coches" y "conductores"
	public void eliminarTodosLosDatos ( ) throws XMLDBException {
		// Elimina todos los nodos "coche"
		eliminarNodosColeccion(COLECCION_COCHES, "coche");
		// Elimina todos los nodos "conductor"
		eliminarNodosColeccion(COLECCION_CONDUCTORES, "conductor");
	}
	// Elimina todos los nodos de una coleccion
	public void eliminarNodosColeccion ( final String NOMBRE_COLECCION , String nombreNodo ) throws XMLDBException {
		// Elimina todos los nodos de la coleccion
		servicioConsultas.query("update delete /" +NOMBRE_COLECCION  +"/" +nombreNodo);
	}
	
	// Guarda los datos que tenemos en la memoria en la base de datos
	public void guardarMemoriaEnDB( ArrayList<Coche> coches , ArrayList<Conductor> conductores ) throws Exception {
		// Inserta cada objeto coche
		for ( Coche auxC : coches )
			insertarCoche(auxC);
		// Inserta cada objeto conductor
		for ( Conductor auxC : conductores )
			insertarConductor(auxC);
	}
	
	// ***FIN INSERCION/BORRADO/DESCARGA DE NODOS***

}
