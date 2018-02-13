package MySQL;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Clases.Articulo;
import Clases.Cliente;
import Clases.Venta;

public class BaseDeDatos {
	private Connection conexion; // Conexion con la DB
	private Statement statement; // Statement para realizar operaciones
	
	// Datos para conectar con la base de datos
	private static final String DRIVER = "com.mysql.jdbc.Driver"; // Driver
	private static final String NOMBRE_SCHEMA = "EXAMEN"; // Nombre de la base de datos
	private static final String FORMATO_RUTA = "jdbc:mysql://localhost:3306%s?useSSL=false"; // Formato para conextar con la DB
	private static final String CREAR_SCHEMA = "CREATE DATABASE " +NOMBRE_SCHEMA;

	// Tabla y Columnas de esta
	private static final String TABLA_CLIENTES = "CLIENTES"; // Nombre de la tabla
	private static final String CREAR_CLIENTES = "CREATE TABLE " +TABLA_CLIENTES +" ( nombre varchar(25), " // Creación de la tabla Clientes
			+ "dni VARCHAR(9) , "
			+ "telefono INT , "
			+ "email VARCHAR(30) )";
	
	private static final String TABLA_ARTICULOS = "ARTICULOS"; // Nombre de la tabla
	private static final String CREAR_ARTICULOS = "CREATE TABLE " +TABLA_ARTICULOS +" ( id INT, " // Creación de la tabla Clientes
			+ "nombre VARCHAR(25) , "
			+ "descripcion VARCHAR(40) , "
			+ "precio DOUBLE , "
			+ "stock INT )";
	
	private static final String TABLA_VENTAS = "VENTAS"; // Nombre de la tabla
	private static final String CREAR_VENTAS = "CREATE TABLE " +TABLA_VENTAS +" ( id INT, " // Creación de la tabla Clientes
			+ "dni VARCHAR(9)) ";

	// Datos del Usuario de la DB
	private static final String USUARIO = "root"; // Usuario
	private static final String PASSWORD = "d4rkkn3sk15"; // Contraseña
	
	public BaseDeDatos() throws ClassNotFoundException, SQLException {
		// Carga el Driver JDBC
		Class.forName(DRIVER); 
		// Establece la conexi�n con la Db sin especificar el schema
		conexion = DriverManager.getConnection( String.format(FORMATO_RUTA, "" ), USUARIO,PASSWORD);
		// Instancia Statement
		statement = conexion.createStatement();

		// Intenta crear la base de datos, si existe no la crea.
		try {
			statement.execute(CREAR_SCHEMA);
			System.out.println("Base de datos creada con �xito.");
		}catch(SQLException e) { /*System.out.println("La base de datos ya existe.");*/ }

		// Conecta de nuevo con la base de datos especificando el schema
		conexion = DriverManager.getConnection(String.format(FORMATO_RUTA, "/"+NOMBRE_SCHEMA ), USUARIO, PASSWORD);
		statement = conexion.createStatement();

		// Intenta crear las tablas, si existen no las crea.
		try {
			statement.execute(CREAR_CLIENTES);
			System.out.println("Tabla Clientes con éxito.");
		}catch(SQLException e) { /*System.out.println("La tabla ya existe.");*/ }
		try {
			statement.execute(CREAR_ARTICULOS);
			System.out.println("Tabla Articulos con éxito.");
		}catch(SQLException e) { /*System.out.println("La tabla ya existe.");*/ }
		try {
			statement.execute(CREAR_VENTAS);
			System.out.println("Tabla Ventas con éxito.");
		}catch(SQLException e) { /*System.out.println("La tabla ya existe.");*/ }

	}
	
	// Añadir nuevo cliente
	public void aniadirCliente ( Cliente cliente ) throws SQLException {
		String sql = "INSERT INTO " +TABLA_CLIENTES +"(nombre,dni,telefono,email) VALUES ( "
						+ "'" +cliente.getNombre() +"', "
						+ "'" +cliente.getDni() +"', "
						+ "'" +cliente.getTelefono() +"', "
						+ "'" +cliente.getEmail() +"')";
		int filas = statement.executeUpdate(sql);
		// Si no ha insertado la fila lanza una excepci�n
		if ( filas == 0 )
			throw new SQLException("No se ha podido insertar el registro.");
	}
	
	// Añadir nueva venta
	public void aniadirVenta ( Venta venta ) throws SQLException {
		String sql = "INSERT INTO " +TABLA_VENTAS +"(id,dni) VALUES ( "
						+ "'" +venta.getId() +"', "
						+ "'" +venta.getDni() +"')";
		int filas = statement.executeUpdate(sql);
		// Si no ha insertado la fila lanza una excepci�n
		if ( filas == 0 )
			throw new SQLException("No se ha podido insertar el registro.");
	}
	
	// Añadir nuevo articulo
	public void aniadirArticulo ( Articulo articulo ) throws SQLException {
		String sql = "INSERT INTO " +TABLA_ARTICULOS +"(id,nombre,descripcion,precio,stock) VALUES ( "
						+ "'" +articulo.getId() +"', "
						+ "'" +articulo.getNombre() +"', "
						+ "'" +articulo.getDescripcion() +"', "
						+ "'" +articulo.getPrecio() +"', "
						+ "'" +articulo.getStock() +"')";
		int filas = statement.executeUpdate(sql);
		// Si no ha insertado la fila lanza una excepci�n
		if ( filas == 0 )
			throw new SQLException("No se ha podido insertar el registro.");
	}
	
	// Visualizar datos clientes con x nombre
	public String visualizarDatosClientes ( String nombre ) throws SQLException {
		String consulta = "SELECT * FROM " + TABLA_CLIENTES +" WHERE nombre = '" +nombre +"'";
		// Ejecuta la consulta
		ResultSet rs = statement.executeQuery(consulta);
		String salida = "";
		// Recoge la información del resultSet
		while ( rs.next() )
			salida += "\n"+rs.getString(1) +" " +rs.getString(2) +" " +rs.getString(3) +" " +rs.getString(4);
		if ( salida.equals("") )
			salida = "No existen datos.";
		return salida;
	}
	
	public void close() throws SQLException {
		conexion.close();
	}
	

	
}
