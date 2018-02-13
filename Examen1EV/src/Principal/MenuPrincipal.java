package Principal;

import java.util.InputMismatchException;
import java.util.Scanner;

import FicherosAleatorios.MainFicheroAleatorio;
import Hibernate.MainHibernate;
import JAXB.MainJAXB;
import MySQL.MainMySQL;

public class MenuPrincipal {
	
	// String con el menú principal
	private static final String MENU_OPCIONES = "\n===================================="
			+"\nSELECCIONA UNA DE LAS 4 OPCIONES: "
			+"\n\t1.Ficheros Aleatorios"
			+"\n\t2.JAXB"
			+"\n\t3.MySQL"
			+"\n\t4.Hibernate y MySQL"
			+"\n\t0.Salir"
			+"\n====================================";
	
	// Mensajes de error
	public static final String ERROR_TECLADO = "Opción no válida";
	public static final String NO_HACER = "X";
	
	// Variables para saber cuantas opciones hay en el menú
	private static final byte MIN_OPCION = 0 , MAX_OPCION = 4;
	
	private Scanner teclado; // Para leer la entrada del teclado
	private byte opcion; // Opcion elegida por el usuario
	
	// Opciones de gestión
	private MainFicheroAleatorio ficheroAleatorio;
	private MainJAXB ficheroJAXB;
	private MainMySQL mySql;
	private MainHibernate hibernate;
	
	// Constructor
	public MenuPrincipal() {
		// Instancia el teclado
		teclado = new Scanner(System.in);
		do {
			// Pide una opción
			opcion = pedirOpcion(MIN_OPCION,MAX_OPCION,teclado,MENU_OPCIONES);
			// Filtra la opción
			filtrarOpcion();
		}while( opcion!= 0); // Si la opción es 0 se sale
	}
	
	// Filtra el menú
	private void filtrarOpcion ( ) {
		try {
			switch (opcion) {
			case 0: // Salir
				System.out.println("Hasta luego!");
				break;
			case 1: // Ficheros secuenciales
				ficheroAleatorio = new MainFicheroAleatorio(teclado);
				break;
			case 2: // JAXB
				ficheroJAXB = new MainJAXB(teclado);
				break;
			case 3: // MySQL
				mySql = new MainMySQL(teclado);
				break;
			case 4: // Hibernate
				hibernate = new MainHibernate(teclado);
				break;
			default:
				break;
			}
		// Imprime excepciones, si las hay
		}catch ( Exception e ) {
			System.out.println(e.getMessage());
		}
	}
	
	/* Pide una opción válida por teclado. La opción tiene que estar entre el minOpcion y maxOpcion 
		También recibe un String que será el menú a imprimir */
	public static byte pedirOpcion ( int minOpcion , int maxOpcion , Scanner teclado , String menu ) {
		while ( true)
			try {
				// Imprime el menú
				System.out.println(menu);
				byte auxOpcion = teclado.nextByte();
				// Si la opción seleccionada no es correcta vuelve a pedir
				if ( auxOpcion < minOpcion || auxOpcion > maxOpcion ) {
					System.out.println(ERROR_TECLADO);
					continue;
				}
				teclado.nextLine(); // Limpia el buffer
				return auxOpcion;
			}catch ( InputMismatchException e ) {
				// Imprime el mensaje de error
				System.out.println(ERROR_TECLADO);
				teclado.nextLine();
			}
	}
	
}
