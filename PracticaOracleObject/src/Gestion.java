import java.sql.SQLException;
import java.util.ArrayList;

import Clases.Jugador;
import Clases.Personaje;

public class Gestion {
	
	// Objetos
	private ArrayList<Personaje> personajes;
	private ArrayList<Jugador> jugadores;
	
	// DB
	private BaseDeDatos db;
	
	// Constructor
	public Gestion ( ) throws ClassNotFoundException, SQLException {
		// Conecta con la DB
		db = new BaseDeDatos();
		
		// Descarga los datos almacenados en la base de datos
		personajes = db.getPersonajes();
		jugadores = db.getJugadores( personajes );
		
	}

	// Getters
	public ArrayList<Personaje> getPersonajes() {
		return personajes;
	}
	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}
	
	// Elimina los personajes y jugadores almacenados en los ArrayLists y los recarga con los datos de la db
	public void recargar() throws SQLException {
		// Limpia los ArrayLists
		personajes.clear();
		jugadores.clear();
		
		// Descarga los personajes y jugadores de la DB y los alacena en los arryaLists
		// Es importante descargar primero los personajes para poder tenerlos a la hora de instanciar los Jugadores
		personajes = db.getPersonajes();
		jugadores = db.getJugadores(personajes);
	}
	
	// Guarda en la db
	public void commit ( ) throws SQLException {
		// Guarda los jugadores y los personajes modificados por el usuario en la db
		db.guardarEnDb(jugadores, personajes);
	}
 

}
