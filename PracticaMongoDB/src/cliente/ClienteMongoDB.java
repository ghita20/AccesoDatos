package cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;

import clases.Coche;
import clases.Conductor;

// Clase que gesitona la conexion con mongoDB. Todos los cambios realizados aqui se guardan directamente en la base de datos.
public class ClienteMongoDB {
	
	// ***ATRIBUTOS ESTATICOS***
	
	// Nombre de la coleccion principal
	private final static String HOST = "localhost";

	// Nombre de la base de datos
	private final static String DATABASE_NAME = "mongo_practica";
	
	// Puerto de la base de datos
	private final static int PUERTO = 27017;

	// Nombre de las colecciones
	private final static String COLECCION_COCHES = "coches";
	private final static String COLECCION_CONDUCTORES = "conductores";

	// Rutas de los ficheros
	private static final String FICHERO_COCHES = "colecciones/coches.json";
	private static final String FICHERO_CONDUCTORES = "colecciones/conductores.json";
	
	
	// ***FIN ATRIBUTOS ESTATICOS***
	
	
	// ***ATRIBUTOS***
	
	// Cliente MONGODB
	private MongoClient clienteMongo; 
	
	// Base de datos
	private MongoDatabase baseDeDatosMongo; 
	
	// Colecciones
	private MongoCollection<Document> coleccionCoches; // Coches
	private MongoCollection<Document> coleccionConductores; // Conductores
	
	// ***FIN ATRIBUTOS***
	
	// ***CONSTRUCTOR***
	public ClienteMongoDB() throws Exception {
		
		// Quita los mensajes de MongoDB
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.OFF);
		
		// Conecta con mongoDB
		clienteMongo = new MongoClient(HOST,PUERTO);
		// Base de datos
		baseDeDatosMongo = clienteMongo.getDatabase(DATABASE_NAME);
		
		// Coleccion COCHES
		coleccionCoches = baseDeDatosMongo.getCollection(COLECCION_COCHES);

		// Coleccion CONDUCTORES
		coleccionConductores = baseDeDatosMongo.getCollection(COLECCION_CONDUCTORES);

		// Asigna "MAX_ID" para que al crear nuevos objetos de tipo coche el id de estos sea mayor al id mas grande de la base de datos
		Coche.asignarMaxId( getMaxId() );
		
		// Mensaje
		System.out.println("Conexion establecida correctamente!");
		
	}

	// Cierra la conexion
	public void cerrarConexion ( )  {
		// Cierra el cliente MongoDB
		clienteMongo.close();
	}
	
	// ***CONSULTAS***
	
	// Devuelve un entero con el id mas alto de los nodos coche. Devolvera 1 si no existen nodos
	public int getMaxId ( ) throws Exception{
		// Consulta que devuelve todos los coches ordenados de manera descendente por el campo "idCoche".
		MongoCursor<Document> resultadoConsulta = coleccionCoches
													.find().sort(descending("idCoche"))
													.projection(include("idCoche")).iterator();
		// Si no existen coches devuelve 0
		if ( !resultadoConsulta.hasNext() ) 
			return 0;
		
		// Recogemos el primer objeto json
		Document auxCoche = resultadoConsulta.next();
		// Recoge el "idCoche"
		int maxId = auxCoche.getInteger("idCoche");
		// Devuelve el resultado
		return maxId;
	}
	
	// Devuelve un String con los conductores que conducen x coche
	public String conductoresQueConducenCoche ( Coche coche ) throws Exception{
		// Salida de la consulta
		String salida = "";

		// Consulta que devuelve todos los conductores que tienen como "ref_coche" el id del coche pasado como argumento
		MongoCursor<Document> resultadoConsulta = coleccionConductores
				.find( eq( "ref_coche",coche.getIdCoche() ) )
				.iterator();

		// Si no hay datos sale del metodo
		if( !resultadoConsulta.hasNext() )
			throw new NullPointerException("No existen conductores que conduzcan ese coche.");

		// Mientras existan resultados
		while ( resultadoConsulta.hasNext() ) {
			// Recoge el resultado
			Document auxConductorJson = resultadoConsulta.next();

			// Objeto conductor auxiliar
			// El parametro "coches" del metodo parsearConductor se lo pasamos como null ya que solo nos interesa los atributos del conductor, nos da igual si le asigna el coche al objeto o no
			Conductor auxConductor = parsearConductor(auxConductorJson,null); 

			// Agrega los datos a la salida de la consulta
			salida += "\t\t" +auxConductor +"\n";
		}
		return salida;
	}

	// Devuelve un String con los datos del Coche mas caro
	public String cocheMasCaro ( ) throws Exception{
		// Consulta que devuelve el coche mas caro. 
		MongoCursor<Document> resultadoConsulta = coleccionCoches
				.find().sort(descending("precio")).limit(1).iterator();

		// Si no existen coches...
		if ( !resultadoConsulta.hasNext() ) 
			throw new NullPointerException("No existen coches en la base de datos."); 

		// Recogemos el primer Document
		Document auxCoche = resultadoConsulta.next();

		// Convertimos el Document a objeto Coche
		Coche objetoCoche = parsearCoche(auxCoche);

		// Devuelve la informacion del coche mas caro
		return "   COCHE MAS CARO\n\t" +objetoCoche;
	}
	
	// Devuelve un Float con el precio medio de los coches
	public float precioMedioCoches ( ) throws Exception {
		// Consulta que devuelve el precio medio de los coches
		MongoCursor<Document> resultadoConsulta = coleccionCoches.aggregate(
				Arrays.asList( group("", avg("precio_medio","$precio") ) )
				).iterator();

		// Si no existen coches...
		if ( !resultadoConsulta.hasNext() ) 
			throw new NullPointerException("No existen coches en la base de datos."); 

		// Recogemos el resultado
		double precioMedio = (Double)resultadoConsulta.next().get("precio_medio");
		
		// Devuelve el precio medio
		return (float)precioMedio;
	}

	// Devuelve un String con los datos del Conductor mas joven
	public String conductorMasJoven ( ) throws Exception {
		// Consulta que devuelve el conductor mas joven
		MongoCursor<Document> resultadoConsulta = coleccionConductores
				.find().sort(ascending("edad")).limit(1).iterator();

		// Si no existen conductores...
		if ( !resultadoConsulta.hasNext() ) 
			throw new NullPointerException("No existen conductores en la base de datos."); 

		// Recogemos el primer Document
		Document auxConductor = resultadoConsulta.next();

		// Convertimos el Document a objeto Conductor
		Conductor objetoConductor = parsearConductor(auxConductor,null);

		// Devuelve la informacion del coche mas caro
		return "   CONDUCTOR MAS JOVEN\n\t" +objetoConductor;
	}
	

	// Devuelve un String con los datos de los coches que tienen una marca x
	public String cochesConMarca ( String marca ) throws Exception {
		// Salida de la consulta
		String salida = "";

		// Consulta que devuelve los coches con x marca
		MongoCursor<Document> resultadoConsulta = coleccionCoches.find(eq("marca",marca)).iterator();
		
		// Comprueba si existen coches
		if ( !resultadoConsulta.hasNext() )
			throw new  NullPointerException("No existen coches con esta marca.");
		
		// Mientras existan resultados
		while ( resultadoConsulta.hasNext() ) {
			// Recoge el resultado
			Document auxCocheJson = resultadoConsulta.next();

			// Objeto coche auxiliar
			Coche auxNuevoCoche = parsearCoche(auxCocheJson);

			// Agrega la informacion del coche a la cadena de salida
			salida += "\t\t" +auxNuevoCoche +"\n";
		}
		return salida;
	}
	
	// ***FIN CONSULTAS***
	
	
	
	// ***IMPORTACION/EXPORTACION DE FICHEROS***

	/* 	Importa en la base de datos los ficheros que tenemos almacenados en el programa. 
		( Este proceso elimina todo lo que tenemos en la base de datos y lo reemplaza con el contenido de los ficheros ) */
	public void importarDesdeFicheros ( ) throws Exception {
		// Primero elimina todo de la base de datos
		eliminarTodosLosDatos();
		
		// Importa desde el fichero en la coleccion coches
		importarDeFicheroJson(FICHERO_COCHES, coleccionCoches);

		// Importa desde el fichero en la coleccion conductores
		importarDeFicheroJson(FICHERO_CONDUCTORES, coleccionConductores);
	}

	// Lee de un fichero almacenado en el programa y lo importa en la coleccion actual
	private void importarDeFicheroJson ( final String RUTA_FICHERO , MongoCollection<Document> coleccion ) throws Exception {
		// Lee de los ficheros almacenados
		File fichero = new File(RUTA_FICHERO);
		// Comprueba que se puede leer
		if(!fichero.canRead()) {
			System.out.println("No se puede leer de " +RUTA_FICHERO );
			return;
		}
		// Instancia un BufferedReader
		BufferedReader bufferedReader = new BufferedReader( new FileReader(fichero) );
		
		// Lee del fichero
		String auxLinea;
		while ( (auxLinea = bufferedReader.readLine()) != null ) {
			// Crea un objeto Document con los datos parseados de la linea del fichero
			Document auxDocument = new Document(Document.parse(auxLinea));
			// Inserta el objeto en la coleccion
			coleccion.insertOne(auxDocument);
		}
		// Cierra el bufferedReader
		bufferedReader.close();
	}

	// Almacena la informacion de la base de datos en unos ficheros dentro de la carpeta "colecciones"
	public void guardarEnFicheros ( ) throws Exception {
		// Almacena Coches
		guardarEnFichero(coleccionCoches, FICHERO_COCHES);
		// Almacena Conductores
		guardarEnFichero(coleccionConductores, FICHERO_CONDUCTORES);
	}
	
	// Almacena una coleccion en un fichero
	private void guardarEnFichero ( MongoCollection<Document> coleccion , final String RUTA_FICHERO) throws IOException {
		// Fichero donde se va a guardar los datos de la colecion
		File fichero = new File(RUTA_FICHERO);
		
		// File Writer
		FileWriter fileWriter = null;
		
		// Consulta que devuelve todos los datos de la coleccion
		ArrayList<Document> documentos = coleccion.find().into(new ArrayList<Document>());
		
		try {
			// Escritura en el fichero
			fileWriter = new FileWriter(fichero);
			// Recorre los documentos y los escribe en el fichero en formato json
			for ( Document d : documentos)
				fileWriter.write( d.toJson() +"\n" ); // Tambien escribe un salto de linea
		}catch ( IOException e ) { // Excepciones
			//System.out.println(e.getMessage());
			GestionEnMemoria.imprimirMensaje("No se ha podido guardar la coleccion " +RUTA_FICHERO );
		} finally {
			if ( fileWriter != null )
					fileWriter.close();
		}
	}
	
	// ***FIN IMPORTACION/EXPORTACION DE FICHEROS***
	
	
	
	// ***INSERCION/BORRADO/DESCARGA***
	
	// Inserta un nuevo coche a la base de datos
	public void insertarCoche( Coche coche) throws Exception{
		// Inserta el coche en la coleccion ( Formato JSON )
		coleccionCoches.insertOne( coche.toJson() );
	}

	// Inserta un nuevo conductor a la base de datos
	public void insertarConductor( Conductor conductor ) throws Exception{
		// Inserta el conductor en la coleccion ( Formato JSON )
		coleccionConductores.insertOne( conductor.toJson() );
	}
	
	// Devuelve un ArrayList con todos los objetos Coche almacenados en la db
	public ArrayList<Coche> descargarCoches ( ) throws Exception {
		// ArrayList auxiliar
		ArrayList<Coche> auxCoches = new ArrayList<>();

		// Consulta que devuelve todos los coches
		MongoCursor<Document> resultadoConsulta = coleccionCoches.find().iterator();
		
		// Mientras existan resultados
		while ( resultadoConsulta.hasNext() ) {
			// Recoge el resultado
			Document auxCocheJson = resultadoConsulta.next();
			
			// Objeto coche auxiliar
			Coche auxNuevoCoche = parsearCoche(auxCocheJson);
			
			// Aniade el coche al arrayList
			auxCoches.add( auxNuevoCoche );
		}
		
		// Devuelve los coches
		return auxCoches;
	}
	
	// Parsea un Document a un objeto de tipo Coche ( usando el constructor sin parametros )
	private Coche parsearCoche ( Document auxCocheJson ) {
		// Nuevo Coche ( Lo instancia con el constructor por defecto para no modificar el MAX_ID ya que queremos asignarsele el que tiene en la base de datos )
		Coche auxCoche = new Coche();

		// Atributos
		String color = auxCocheJson.getString("color");
		String marca = auxCocheJson.getString("marca");
		String modelo = auxCocheJson.getString("modelo");
		double precio = auxCocheJson.getDouble("precio");
		int idCoche = auxCocheJson.getInteger("idCoche");
		
		// Asigna los atributos
		auxCoche.setColor( color );
		auxCoche.setMarca( marca );
		auxCoche.setModelo( modelo );
		auxCoche.setPrecio( (float)precio );
		auxCoche.setIdCoche( idCoche );
		
		// Devulve el coche
		return auxCoche;
	}

	// Devuelve un ArrayList con todos los objetos Conductor almacenados en la db. Es necesario el parametro "coches" para poder conseguir el coche al que el conductor esta vinculado
	public ArrayList<Conductor> descargarConductores ( ArrayList<Coche> coches ) throws Exception {
		// ArrayList auxiliar
		ArrayList<Conductor> auxConductores = new ArrayList<>();

		// Consulta que devuelve todos los coches
		MongoCursor<Document> resultadoConsulta = coleccionConductores.find().iterator();

		// Mientras existan resultados
		while ( resultadoConsulta.hasNext() ) {
			// Recoge el resultado
			Document auxConductorJson = resultadoConsulta.next();

			// Parsea el JSON
			Conductor auxNuevoCoche = parsearConductor(auxConductorJson , coches);

			// Aniade el objeto al arrayList
			auxConductores.add( auxNuevoCoche );
		}
		
		// Devuelve los conductores
		return auxConductores;
	}
	
	// Recoge los campos de un documento Json y los asigna a un nuevo objeto Conductor
	private Conductor parsearConductor ( Document conductorJson , ArrayList<Coche> coches ) {
		// Recoge los Atributos
		String nombre = conductorJson.getString("nombre");
		String apellidos = conductorJson.getString("apellidos");
		String dni = conductorJson.getString("dni");
		Integer refCoche = conductorJson.getInteger("ref_coche"); // Si no tiene coche devolvera null
		int edad = conductorJson.getInteger("edad");

		// Comprueba el coche del conductor
		Coche cocheDelConductor = null;
		if ( refCoche != null && coches != null ) // Si tiene referencia a un coche y se ha pasado el parametro coches
				for ( Coche c : coches ) // Lo busca entre los coches
					if ( c.getIdCoche() == refCoche ) { // Cuando lo encuentra lo almacena en la variable "cocheDelConductor" y sale del for
						cocheDelConductor = c;
						break;
					}
		
		// Crea un objeto conductor con los atributos del json
		Conductor axuConductor = new Conductor(nombre, apellidos, edad, dni, cocheDelConductor);

		// Devulve el objeto
		return axuConductor;
	}

	// Elimina todos los nodos de las colecciones "coches" y "conductores"
	public void eliminarTodosLosDatos ( ) throws Exception {
		// Elimina todos los coches
		coleccionCoches.deleteMany(exists("_id"));
		// Elimina todos los conductores
		coleccionConductores.deleteMany(exists("_id"));
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
	
	// ***FIN INSERCION/BORRADO/DESCARGA**
	

}
