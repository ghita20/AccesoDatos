import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Clases.Jugador;
import Clases.Personaje;

public class BaseDeDatos {
	private Connection conexion; // Conexion con la DB
	private Statement statement; // Statement para realizar operaciones
	
	// Datos para conectar con la base de datos
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver"; // Driver

	// Datos de usuario
	private static final String USUARIO = "hr"; // Usuario
	private static final String PASSWORD = "hr"; // Contrase�a
	
	// Types
	private static final String CREAR_TYPE_PERSONAJE = "CREATE OR REPLACE TYPE personaje as OBJECT( " +
			"raza VARCHAR2(25), " + 
			"arma VARCHAR2(25), " +
			"damage NUMBER(5)"
			+ ");";
	private static final String CREAR_TYPE_JUGADOR = "CREATE OR REPLACE TYPE jugador as OBJECT( " +
			"nombre VARCHAR2(25), " + 
			"nivel NUMBER(3), " + 
			"vida NUMBER(3), " + 
			"tipo_personaje REF PERSONAJE" + ");";
	
	// Tablas
	private static final String CREAR_TABLA_PERSONAJES = "CREATE TABLE personajes OF personaje";
	private static final String CREAR_TABLA_JUGADORES = "CREATE TABLE jugadores OF jugador";
	
	// Inserci�n
	private static final String INSERTAR_PERSONAJE = "INSERT INTO personajes VALUES( " +"'%s', '%s', %d )";
	private static final String INSERTAR_JUGADOR = "INSERT INTO jugador VALUES( '%s', %d, %d , '%s' "
			+ " )";
	private static final String INSERTAR_JUGADOR2 = "INSERT INTO jugadores VALUES( '%s', %d, %d , "
			+ "( SELECT ref(e) from personajes e where e.raza = '%s' AND ROWNUM = 1 ) "
			+ " )";
	
	// Constructor
	public BaseDeDatos ( ) throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",USUARIO,PASSWORD);
		statement = conexion.createStatement();
		
		// Intenta crear los types, por si no existen en la base de datos...
		try {
			statement.execute(CREAR_TYPE_PERSONAJE);
			System.out.println("Type personaje creado.");
		}catch(SQLException e) { 
			/*System.out.println("Los types ya existen");*/ }
		try {
			statement.execute(CREAR_TYPE_JUGADOR);
			System.out.println("Type jugador creado.");
		}catch(SQLException e) { 
			/*System.out.println("Los types ya existen");*/ }
		
		// Creaci�n de las tablas si no existen....
		try {
			statement.execute(CREAR_TABLA_PERSONAJES);
			System.out.println("Tabla personajes creada.");
		}catch(SQLException e) {}
		try {
			statement.execute(CREAR_TABLA_JUGADORES);
			System.out.println("Tabla jugadores creada.");
		}catch(SQLException e) {}
		
		System.out.println("Conexi�n establecida.");
	}
	
	// Cierra la conexi�n
	public void cerrarConexion ( ) throws SQLException {
		if ( !statement.isClosed() )
			statement.close();
		if ( !conexion.isClosed() ) {
			conexion.close();
			System.out.println("Conexi�n cerrada.");
		}
	}
	
	// Inserta un personaje en la bd
	public void insertarPersonaje ( Personaje personaje ) throws SQLException {
		String sql = String.format(INSERTAR_PERSONAJE, personaje.getRaza(), personaje.getArma(), personaje.getDamage());
		statement.executeUpdate(sql);
	}
	
	// Inserta un jugador en la bd
	public void insertarJugador ( Jugador jugador ) throws SQLException {
		String sql = String.format(INSERTAR_JUGADOR2, jugador.getNombre(), jugador.getNivel(), jugador.getVida(), jugador.getTipoPersonaje().getRaza());
		statement.executeUpdate(sql);
	}
	
	// Devuelve un ArrayList con los personajes existentes en la BD
	public ArrayList<Personaje> getPersonajes ( ) throws SQLException {
		ArrayList<Personaje> personajes = new ArrayList<>();
		// Selecciona todos los datos y la REF de cada personaje
		ResultSet rsPersonajes = statement.executeQuery( "SELECT ref(p) as referencia,p.raza,p.arma,p.damage FROM personajes p");
		
		while ( rsPersonajes.next() ) {
			// Crea los personajes con sus datos y la REF
			personajes.add( new Personaje( rsPersonajes.getObject("referencia").toString(), rsPersonajes.getString("raza") , rsPersonajes.getString("arma") , rsPersonajes.getInt("damage")));
		}
		return personajes;
	}
	
	// Devuelve un ArrayList con los Jugadores existentes en la BD
	public ArrayList<Jugador> getJugadores ( ArrayList<Personaje> personajes ) throws SQLException {
		ArrayList<Jugador> jugadores = new ArrayList<>();
		ResultSet rsJugadores = statement.executeQuery( "select * from jugadores");
		
		while ( rsJugadores.next() ) {
			
			// Referencia a personaje
			String refPersonaje = rsJugadores.getObject(4).toString();
			
			// Campos
			String nombre = rsJugadores.getString("nombre");
			int nivel = rsJugadores.getInt("nivel");
			int vida = rsJugadores.getInt("vida");
			
			// Devuelve el objeto Personaje correspondiente a la REF
			Personaje personaje = getPersonaje(refPersonaje, personajes);
			
			// Crea el jugador
			Jugador jugador = new Jugador(nombre , nivel , vida , personaje);
			jugadores.add( jugador );
		}
		return jugadores;
	}
	
	// Devuelve el Personaje seg�n su REF
	public Personaje getPersonaje ( String ref , ArrayList<Personaje> personajes ) {
		for ( Personaje p : personajes) 
			if ( p.getRef().equals(ref) )
				return p;
		return null;
	}

	
	// Guardar en db
	public void guardarEnDb ( ArrayList<Jugador> jugadores , ArrayList<Personaje> personajes ) throws SQLException {
		// Elimina los datos de la base de datos
		statement.executeUpdate("DELETE FROM jugadores");
		statement.executeUpdate("DELETE FROM personajes");
		
		// Inserta los personajes
		for ( Personaje p : personajes ) {
			insertarPersonaje(p);
			//System.out.println("Personaje insertado");
		}
		// Inserta los jugadores
		for ( Jugador j : jugadores ) {
			insertarJugador(j);
			//System.out.println("Jugador insertado");
		}
		
		// Guarda en la BBDD
		conexion.commit();
	}

}
