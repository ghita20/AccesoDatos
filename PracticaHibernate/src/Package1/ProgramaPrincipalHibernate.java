package Package1;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ProgramaPrincipalHibernate {
	public static final String PEDIR_DNI = "Introduce un DNI: ";
	public static final String PEDIR_NOMBRE = "Introduce un nombre: ";
	public static final String PEDIR_APELLIDO = "Introduce el apellido: ";
	public static final String PEDIR_EDAD = "Introduce una edad: ";
	
	public static final String NUMERO_NO_VALIDO = "\tNúmero no válido.";
	
	private Scanner teclado; // Teclado
	private DataBase baseDeDatos; // Conexi�n a la base de datos
	
	public ProgramaPrincipalHibernate ( ) {
		teclado = new Scanner(System.in);
		
		// Conecta con la base de datos
		baseDeDatos = new DataBase();
		
		// Muestra el men� hasta que el usuario salga del mismo
		while ( true ) {
			int opcion = opcionMenu();
			if ( opcion == 0 ) break;
			eventoOpcion(opcion);
		}
		// Cierra la conexion
		baseDeDatos.close();
	}
	
	// M�todo que devuelve un int con la opci�n elegida por el usuario
	private int opcionMenu ( ) {
		int opcion = -1;
		do {
			imprimirMenu();
			try {
				opcion = teclado.nextInt();
			}catch ( InputMismatchException e ){ 
				opcion = -1;
			}finally {
				// Limpia el Buffer
				teclado.nextLine();
			}
		}while ( opcion>7 || opcion<0 );
			return opcion;
	}
	
	// Imprime la cadena de texto pasada como par�metro y devuelve un entero
	private int pedirEntero ( String cadena ) {
		int auxE ;
		while ( true ) {
			try {
				System.out.println(cadena);
				auxE = teclado.nextInt();
				if ( auxE <= 0 ) // Si el n�mero es menor o igual que 0 genera una excepci�n que es tratada m�s abajo
					throw new InputMismatchException();
				return auxE;
			}catch(InputMismatchException e ) {
				System.out.println(NUMERO_NO_VALIDO);
			}finally {
				teclado.nextLine();
			}
		}
	}	
	// Pedir cadena
	private String pedirCadena ( String mensaje ) {
		System.out.println(mensaje);
		return teclado.nextLine();
	}
	// Seg�n el valor de "opcion" realizar� cierta acci�n
	private void eventoOpcion ( int opcion ) {
		try {
			switch ( opcion ) {
				case 1: // A�ADIR
					anadirPersona();
					break;
				case 2: // ELIMINAR
					eliminarPersona();
					break; 
				case 3: // VER DATOS PERSONA
					verDatosPersona();
					break;
				case 4: // MODIFICAR PERSONA
					modificarPersona();
					break;
				case 5: // VER TODOS LOS REGISTROS
					verTodosLosRegistros();
					break;
			}
		}catch( Exception e ) {
			System.out.println("\t"+e.getMessage());
		}
	}

	// OPCIONES DEL SWITCH PRINCIPAL
	private void anadirPersona() throws Exception{
		Persona auxP = pedirPersona();
		baseDeDatos.anadirPersona(auxP);
	}
	private void eliminarPersona () throws Exception {
		String dni = pedirCadena(PEDIR_DNI);
		baseDeDatos.eliminarPersona(dni);
	}
	private void verDatosPersona() throws Exception {
		String dni = pedirCadena(PEDIR_DNI);
		System.out.println( "\t" +baseDeDatos.datosPersona(dni) );
	}
	private void modificarPersona () throws Exception {
		String auxDni = pedirCadena(PEDIR_DNI);
		// Comprueba que el id existe
		if ( !baseDeDatos.personaExiste(auxDni) )
			throw new Exception(DataBase.ERROR_PERSONA_NO_EXISTE);
		// Datos de la persona
		String nombre = null, apellido = null;
		int edad = -1;

		int auxO = pedirEntero("Que datos quieres modificar? 1.Nombre 2.Apellido 3.Edad 5.Todos");
		switch(auxO) {
		case 1:
			nombre = pedirCadena(PEDIR_NOMBRE);
			break;
		case 2:
			apellido = pedirCadena(PEDIR_APELLIDO);
			break;
		case 3:
			edad = pedirEntero(PEDIR_EDAD);
			break;
		case 5:
			nombre = pedirCadena(PEDIR_NOMBRE);
			apellido = pedirCadena(PEDIR_APELLIDO);
			edad = pedirEntero(PEDIR_EDAD);
			break;
		default : return;
		}
		// Modifica los datos
		baseDeDatos.modificarDatosPersona(auxDni, nombre, apellido, edad);
	}
	
	private void verTodosLosRegistros () throws Exception {
		System.out.println(baseDeDatos.mostrarTodasLasPersonas());
	}
	// END OPCIONES SWITCH
	
	private Persona pedirPersona ( ) throws IllegalStateException , InputMismatchException {
		// Datos de la Persona a a�adir
		String nombre, apellido, dni;
		int edad;
		// Pide los datos por teclado
		try {
			System.out.println(PEDIR_NOMBRE);
			nombre = teclado.nextLine();
						
			System.out.println(PEDIR_APELLIDO);
			apellido = teclado.nextLine();
							
			System.out.println(PEDIR_EDAD);
			edad = teclado.nextInt();
				
			teclado.nextLine();
				
			System.out.println(PEDIR_DNI);
			dni = teclado.nextLine();
				
		}catch( InputMismatchException e ) {
			teclado.nextLine();
			throw new InputMismatchException("El formato de los datos no es valido.");
		}
		// Instancia la persona con los datos facilitados por el usuario
		Persona auxP = new Persona(nombre,apellido,edad,dni);
		return auxP;
	}
	
	private void imprimirMenu ( ) {
		System.out.println("*************************************");
		System.out.println("Elige una opcion:");
		System.out.println("\t1.Anadir Persona");
		System.out.println("\t2.Eliminar Persona");
		System.out.println("\t3.Ver datos de Persona");
		System.out.println("\t4.Modificar Persona");
		System.out.println("\t5.Ver Todos los Registros");
		System.out.println("\t0.Salir");
		System.out.println("*************************************");
	}
	
	
}
