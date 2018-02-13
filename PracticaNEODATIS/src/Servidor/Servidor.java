package Servidor;

import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ODBServer;

public class Servidor {
	
	// Host, Alias, Nombre DB, Puerto
	public final static String HOST = "localhost";
	public final static String ALIAS = "neodatisDB"; // Alias
	public final static int PUERTO = 8000;
	private final static String NOMBRE_DB = "baseNeodatis.test"; // Nombre del fichero
	
	// Main
	public static void main(String[] args) {
		ODBServer server = null;
		// Crea el servidor en el Puerto 8000
		server = ODBFactory.openServer(8000);
		// BD a usar y el alias
		server.addBase(ALIAS, NOMBRE_DB);
		// Enciende el servidor
		server.startServer(true);

		System.out.println("Servidor iniciado correctamente.");
	}
}
