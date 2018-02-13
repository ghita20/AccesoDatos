package Package1;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDeDatos {
	private Connection conexion; // Conexion con la DB
	private Statement statement; // Statement para realizar operaciones
	private DatabaseMetaData metaDatos;
	
	// Datos para conectar con la base de datos
	private static final String DRIVER = "com.mysql.jdbc.Driver"; // Driver
	private static final String NOMBRE_SCHEMA = "PRACTICA3"; // Nombre de la base de datos
	private static final String FORMATO_RUTA = "jdbc:mysql://localhost:3306%s?useSSL=false"; // Formato para conextar con la DB
	private static final String CREAR_SCHEMA = "CREATE DATABASE " +NOMBRE_SCHEMA;
	
	// Tabla y Columnas de esta
	private static final String NOMBRE_TABLA = "PERSONAS"; // Nombre de la tabla
	private static final String CREAR_TABLA = "CREATE TABLE " +NOMBRE_TABLA +" ( id INT PRIMARY KEY AUTO_INCREMENT, " // El ID es autoincrementable
												+ "nombre VARCHAR(25) , "
												+ "apellidos VARCHAR(30) , "
												+ "edad INT , "
												+ "dni VARCHAR(9) )";
	
	// Datos del Usuario de la DB
	private static final String USUARIO = "root"; // Usuario
	private static final String PASSWORD = "d4rkkn3sk15"; // Contraseña
	
	// Constructor
	public BaseDeDatos() throws ClassNotFoundException, SQLException {
		// Carga el Driver JDBC
		Class.forName(DRIVER); 
		// Establece la conexión con la Db sin especificar el schema
		conexion = DriverManager.getConnection( String.format(FORMATO_RUTA, "" ), USUARIO,PASSWORD);
		// Instancia Statement
		statement = conexion.createStatement();
		// Metadatos
		metaDatos = conexion.getMetaData();
	
		// Intenta crear la base de datos, si existe no la crea.
		try {
			statement.execute(CREAR_SCHEMA);
			System.out.println("Base de datos creada con éxito.");
		}catch(SQLException e) { /*System.out.println("La base de datos ya existe.");*/ }
		
		// Conecta de nuevo con la base de datos especificando el schema
		conexion = DriverManager.getConnection(String.format(FORMATO_RUTA, "/"+NOMBRE_SCHEMA ), USUARIO, PASSWORD);
		statement = conexion.createStatement();
		
		// Intenta crear la tabla PERSONAS, si existe no la crea.
		try {
			statement.execute(CREAR_TABLA);
			System.out.println("Tabla creada con éxito.");
		}catch(SQLException e) { /*System.out.println("La tabla ya existe.");*/ }
		
	}
	
	// Añade un nuevo registro a la base de datos.
	public void anadirPersona ( Persona persona ) throws SQLException {
		String sql = "INSERT INTO " +NOMBRE_TABLA +"(nombre,apellidos,edad,dni) VALUES ( "
						+ "'" +persona.getNombre() +"', "
						+ "'" +persona.getApellido() +"', "
						+ "'" +persona.getEdad() +"', "
						+ "'" +persona.getDni() +"')";
		int filas = statement.executeUpdate(sql);
		// Si no ha insertado la fila lanza una excepción
		if ( filas == 0 )
			throw new SQLException("No se ha podido insertar el registro.");
	}
	
	// Elimina el registro de un id
	public void eliminarPersona ( int id ) throws SQLException {
		// Comprueba que el id existe en la base de datos
		if ( !idExiste(id) )
			throw new IllegalArgumentException("El ID no existe.");
		String sql = "DELETE FROM "+NOMBRE_TABLA +" WHERE id = " +id;
		// Ejecuta la orden sql
		statement.executeUpdate(sql);
	}
	
	// Devuelve un ResultSet con la información correspondiente al id pasado como argumento
	public ResultSet buscarPersona ( int id ) throws SQLException {
		// Comprueba que el id existe en la base de datos
		if ( !idExiste(id) )
			throw new IllegalArgumentException("El ID no existe.");
		String sql = "SELECT * FROM " +NOMBRE_TABLA +" WHERE id = " +id;
		return statement.executeQuery(sql);
	}
	
	// Modifica los datos de una persona si el id existe. Para que no se modifique algún campo el valor debe ser ( strings == null y int == -1 )
	public void modificarDatosPersona ( int id , String nombre , String apellidos , int edad , String dni ) throws SQLException , IllegalArgumentException {
		// Comprueba que el id existe en la base de datos
		if ( !idExiste(id) )
			throw new IllegalArgumentException("El ID no existe.");
		if ( nombre == null && apellidos == null && edad == -1 && dni == null )
			throw new IllegalArgumentException("Es necesario introducir al menos un dato válido para modificar el registro.");
		
		String sql = "UPDATE " + NOMBRE_TABLA +" SET ";
		// Variable para comprobar si es la primera columna a insertar, si es la segunda colocará una , antes de los datos
		boolean primeraColumna = true;
		// Comprueba las columnas a modificar y crea la orden SQL
		if ( nombre!= null ) {
			if ( !Persona.nombreApellidoValido(nombre) )
				throw new IllegalArgumentException("Nombre no válido.");
			sql += "nombre = '" +nombre +"'";
			primeraColumna = false;
		}
		if ( apellidos!= null ) {
			if ( !Persona.nombreApellidoValido(apellidos) )
				throw new IllegalArgumentException("Apellido no válido.");
			sql += (!primeraColumna?" , ":"") +"apellidos = '" +apellidos +"'";
			primeraColumna = false;
		}
		if ( edad != -1 ) {
			if ( edad <= 0 )
				throw new IllegalArgumentException("Edad no válida.");
			sql += (!primeraColumna?" , ":"") +"edad = " +edad;
			primeraColumna = false;
		}
		if ( dni!= null ) {
			if ( !Persona.dniValido(dni) )
				throw new IllegalArgumentException("DNI no válido.");
			sql += (!primeraColumna?" , ":"") +"dni = '" +dni +"'";
		}
		// Condición de la orden sql
		sql += " WHERE id = " +id;
		// Ejecura el comando SQL
		statement.executeUpdate(sql);
	}
	
	// Devuelve una cadena con los resultados que existen en el ResultSet pasado como argumento
	public static String resultSetToString ( ResultSet rs ) throws NullPointerException, SQLException {
		String salida = "";
		while ( rs.next() )
			salida += "\n"+rs.getString(1) +" " +rs.getString(2) +" " +rs.getString(3) +" " +rs.getString(4) +" " +rs.getString(5);
		if ( salida.equals("") )
			throw new NullPointerException("No existen datos.");
		return salida;
	}
	
	// Devuelve un ResultSet con todos los datos de la tabla
	public ResultSet mostrarTodosLosDatos () throws SQLException {
		String sql = "SELECT * FROM " +NOMBRE_TABLA;
		return statement.executeQuery(sql);
	}
	
	// Devuelve true si el id existe en la tabla Personas
	public boolean idExiste ( int id ) throws SQLException {
		String sql = "SELECT COUNT(*) FROM " +NOMBRE_TABLA +" WHERE id = " +id;
		// Ejecuta la consulta
		ResultSet rs = statement.executeQuery(sql);
		rs.first();
		// Almacena la primera columna del RS
		int total = rs.getInt(1);
		return total>=1;
	}
	
	// Metadatos de conexión
	public String datosConexion ( ) throws SQLException {
		String 	salida = "\nMETADATOS DE CONEXIÓN";
				salida += "\n\tNombre: " +metaDatos.getDatabaseProductName();
				salida += "\n\tDriver: " +metaDatos.getDriverName();
				salida += "\n\tUrl: " +metaDatos.getURL();
				salida += "\n\tUsuario: " +metaDatos.getUserName();
		return salida;
	}
	
	// Metadatos Base de datos
	public String datosDB () throws SQLException {
		String esquema = "" , tabla = "", tipo = "";
		String 	salida = "INFORMACIÓN SOBRE LA TABLA " +NOMBRE_TABLA;
		
		// Información de las columnas
		salida +="\nCOLUMNAS";
		ResultSetMetaData rsmd = statement.executeQuery("SELECT * FROM " +NOMBRE_TABLA).getMetaData();
		int nColumnas = rsmd.getColumnCount();
		for (int i = 1; i <= nColumnas; i++) {
			String tNombre = rsmd.getColumnName(i);
			String tTipo = rsmd.getColumnTypeName(i);
			String tLength = ""+ rsmd.getColumnDisplaySize(i);
			String tNula = rsmd.isNullable(i)==0?"no":"si";
			salida += "\n\tNombre: " +tNombre +" Tipo: " +tTipo +" Longitud MAX: " +tLength +" Puede ser nula?: " +tNula;
		}
		return salida;
	}
	
	// Termina la conexión
	public void close () throws SQLException {
		statement.close();
		conexion.close();
	}
	
//	public static void main(String[] args) {
//		try {
//			BaseDeDatos e = new BaseDeDatos();
////			e.anadirPersona(new Persona("aaa","aaa",13333330,"98987876e"));
//			System.out.println(e.idExiste(1));
//			System.out.println(resultSetToString(e.buscarPersona(1)));
//			e.modificarDatosPersona(1, "sd", "ssss", 7, "66666666h");
////			e.eliminarPersona(7);
////			e.reordenarId();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
