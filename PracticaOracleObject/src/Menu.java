

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import Clases.Jugador;
import Clases.Personaje;

public class Menu {
	// Teclado
	private Scanner teclado;
	private byte opcion;
	
	// Gestion
	private Gestion gestion;
	
	// Men� principal
	public static final String MENU = 	"-----------------------------------------"+
										"\n PERSONAJES"+
										"\n\t... 1. Nuevo Personaje"+
										"\n\t... 2. Borrar Personaje"+
										"\n\t... 3. Buscar Personaje"+
										"\n JUGADORES"+
										"\n\t... 4. Nuevo Jugador"+
										"\n\t... 5. Borrar Jugador"+
										"\n\t... 6. Buscar Jugador"+
										"\n OTRAS OPCIONES "+
										"\n\t... 7. Recargar de la base de datos"+
										"\n\t... 8. Visualizar todos los datos"+
										"\n\t... 9. Commit"+
										"\n\t... 10. Salir"+
										"\n------------------------------------------";
	
	// Constructor
	public Menu ( ) throws ClassNotFoundException, SQLException {
		teclado = new Scanner(System.in);
		gestion = new Gestion();
		
		// Imprime el men� y pide una opci�n v�lida
		pedirOpcion();
	}

	// MENU
	
	// Imprime el menú y pide una opción
	private void pedirOpcion ( ) {
		System.out.println(MENU);
		try {
			opcion = teclado.nextByte();
		}catch ( InputMismatchException e ) {
			opcion = -1;
			
		}finally {
			teclado.nextLine();
		}
		
		gestionarOpcion(opcion);
	}
	// Según la opción realizará las operaciones
	private void gestionarOpcion ( byte opcion ) {
		// TODO Auto-generated method stub
		switch ( opcion ) {
		case 1:  // Nuevo Personaje
			try {
				Personaje p = nuevoPersonaje();
				// Si el personaje es correcto lo a�ade al arrayList
				gestion.getPersonajes().add(p);
				System.out.println("Personaje a�adido!");
			}catch ( Exception e ) { System.out.println(e.getMessage());}
			
			break;
		case 2: // Borrar Personaje
			borrarPersonaje();
			break;
		case 3: // Visualizar Personaje
			buscarPersonaje();
			break;
		case 4: // Nuevo Jugador
			try {
				Jugador j = nuevoJugador();
				// Si el personaje es correcto lo a�ade al arrayList
				gestion.getJugadores().add(j);
				System.out.println("Jugador a�adido!");
			}catch ( Exception e ) { System.out.println(e.getMessage());}
			
			break;
		case 5: // Borrar Jugador
			borrarJugador();
			break;
		case 6: // Visualizar Jugador
			buscarJugador();
			break;
		case 7:  // Recargar de la base de datos
			try {
				gestion.recargar();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			break;
		case 8:  // Visualizar todos los datos
			int i = 0;
			// Personajes
			System.out.println("\nPERSONAJES");
			for ( Personaje p : gestion.getPersonajes() ) {
				System.out.println("\t " +p);
				i++;
			}
			
			if ( i == 0 ) 
				System.out.println("No existen personajes.");
			
			// Jugadores
			i = 0;
			System.out.println("\nJugadores");
			for ( Jugador j : gestion.getJugadores() ) {
				System.out.println("\t " +j);
				i++;
			}
			if ( i == 0 ) 
				System.out.println("No existen jugadores.");
			break;
		case 9: // Commit
			try {
				gestion.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			break;
		case 10: // Salir
			System.out.println("Adeuuu");
			return;
		default:
			System.out.println("Opcion no v�lida...");
		}
		
		// Imprime de nuevo el men� y pide una opci�n v�lida
		pedirOpcion();
	}
	
	// FIN MENU
	
	// JUGADOR
	
	// Visualiza los datos de un jugador
	private void buscarJugador() {
		System.out.println("Introduce el nombre: ");
		String nombre = teclado.nextLine();
		int i = 0;
		
		for ( Jugador j : gestion.getJugadores() ) {
			if ( j.getNombre().equals(nombre) ) {
				System.out.println("\t" +j);
				i++;
			}
		}
		// No existe ese jugador
		if ( i == 0 )
			System.out.println("Ese jugador no existe.");
		
	}
	// Borra un jugador
	private void borrarJugador() {
		System.out.println("Elige un jugador para borrar:");
		ArrayList<Jugador> jugadores = gestion.getJugadores();
		
		int i = 0;
		for ( Jugador j : jugadores ) {
			i++;
			System.out.println("\t" +i +") " +j);
		}
		
		// Si no existen jugadores sale del método ya que no se puede borrar a ningún jugador
		if ( i == 0 ) {
			System.out.println("No exiten jugadores...");
			return;
		}
		
		System.out.println();
		
		try {
			int indice = teclado.nextInt();
			// Si el indice no es v�lido se captura la excepci�n debajo
			gestion.getJugadores().remove(indice-1);
		}catch ( Exception e ) {
			System.out.println("Jugador no v�lido...");
			teclado.nextLine();
			return;
		}
		
	}
	// Devuelve un nuevo jugador
	private Jugador nuevoJugador() throws Exception {
		// Nombre
		System.out.println("\tNombre: ");
		String nombre = teclado.nextLine();
		
		try {
			// Nivel
			System.out.println("\tNivel: ");
			int nivel = teclado.nextInt();
			
			// Vida
			System.out.println("\tVida: ");
			int vida = teclado.nextInt();
			
			// Imprime los personajes que se pueden elegir para el jugador
			int i = 0;
			for ( Personaje p : gestion.getPersonajes() ) {
				i++;
				System.out.println("\t" +i +") " +p);
			}
			
			// Si no existe ningún personaje creado genera una excepción
			if ( i == 0 )
				throw new Exception("No existen personajes.");

			System.out.println("Selecciona un personaje: ");
			int indiceP = teclado.nextInt();
			
			// Comprueba que ha seleccionado un personaje v�lido
			if ( indiceP > i || indiceP <= 0 )
				throw new InputMismatchException();
			
			return new Jugador(nombre, nivel, vida, gestion.getPersonajes().get(indiceP-1));
			
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
			throw new Exception("Datos no v�lidos.");
		}
	}
	
	// FIN JUGADOR
	
	// PERSONAJE
	
	// Visualiza los datos de un personaje
	private void buscarPersonaje() {
		// Pide la raza del personaje
		System.out.println("Introduce la raza: ");
		String raza = teclado.nextLine();
		int i = 0;
		
		for ( Personaje p : gestion.getPersonajes() ) {
			if ( p.getRaza().equals(raza) ) {
				System.out.println("\t" +p);
				i++;
			}
		}
		// No existe ese personaje
		if ( i == 0 )
			System.out.println("No existen personajes con esa raza.");
		
	}
	// Devuelve un nuevo personaje
	private Personaje nuevoPersonaje ( ) throws Exception {
		System.out.println("\tRaza: ");
		String raza = teclado.nextLine();

		System.out.println("\tArma: ");
		String arma = teclado.nextLine();

		try {
			System.out.println("\tDa�o: ");
			int damage = teclado.nextInt();
			return new Personaje(raza,arma,damage);
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
			throw new Exception("Datos no v�lidos.");
		}
	}
	// Borra un personaje
	private void borrarPersonaje () {
		System.out.println("Elige un personaje para borrar:");
		ArrayList<Personaje> personajes = gestion.getPersonajes();

		int i = 0;
		for ( Personaje p : personajes ) {
			i++;
			System.out.println("\t" +i +") " +p);
		}

		System.out.println();

		try {
			int indice = teclado.nextInt();
			// Si el indice no es v�lido se captura la excepci�n debajo
			gestion.getPersonajes().remove(indice-1);
		}catch ( Exception e ) {
			System.out.println("Personaje no v�lido...");
			teclado.nextLine();
			return;
		}
	}
	
	// FIN PERSONAJE
}
