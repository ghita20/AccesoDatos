package Package1;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ProgramaPrincipalJDBC {
	private Scanner teclado; // Teclado
	private BaseDeDatos baseDeDatos; // Conexión a la base de datos
	
	public ProgramaPrincipalJDBC ( ) throws ClassNotFoundException, SQLException  {
		teclado = new Scanner(System.in);
		
		// Conecta con la base de datos
		baseDeDatos = new BaseDeDatos();
		
		// Muestra el menú hasta que el usuario salga del mismo
		while ( true ) {
			int opcion = opcionMenu();
			if ( opcion == 0 ) break;
			eventoOpcion(opcion);
		}
		// Cierra la conexion
		baseDeDatos.close();
	}
	
	// Método que devuelve un int con la opción elegida por el usuario
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
	
	// Imprime la cadena de texto pasada como parámetro y devuelve un entero
	private int pedirEntero ( String cadena ) {
		int auxE ;
		while ( true ) {
			try {
				System.out.println(cadena);
				auxE = teclado.nextInt();
				if ( auxE <= 0 ) // Si el número es menor o igual que 0 genera una excepción que es tratada más abajo
					throw new InputMismatchException();
				return auxE;
			}catch(InputMismatchException e ) {
				System.out.println("\t"+"Número no válido.");
			}finally {
				teclado.nextLine();
			}
		}
	}	
	// Según el valor de "opcion" realizará cierta acción
	private void eventoOpcion ( int opcion ) {
		try {
			switch ( opcion ) {
				case 1: // AÑADIR
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
				case 6: // DATOS CONEXIÓN
					imprimirDatosConexion();
					break;
				case 7: // INFORMACIÓN SOBRE LA TABLA
					imprimirInfoTabla();
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
		int id = pedirEntero("Introduce el ID de la persona: ");
		baseDeDatos.eliminarPersona(id);
	}
	private void verDatosPersona() throws Exception {
		int indice = pedirEntero("Introduce el ID de la persona: ");
		ResultSet rs = baseDeDatos.buscarPersona(indice);
		System.out.println( "\t" +BaseDeDatos.resultSetToString(rs) );
	}
	private void modificarPersona () throws Exception {
		int indice = pedirEntero("Introduce el ID de la persona: ");
		// Comprueba que el id existe
		if ( !baseDeDatos.idExiste(indice) )
			throw new Exception("ID no existe.");
		// Datos de la persona
		String nombre = null, apellido = null, dni = null;
		int edad = -1;

		int auxO = pedirEntero("Que datos quieres modificar? 1.Nombre 2.Apellido 3.Edad 4.DNI 5.Todos");
		switch(auxO) {
		case 1:
			System.out.println("Introduce un nombre: ");
			nombre = teclado.nextLine();
			break;
		case 2:
			System.out.println("Introduce el apellido: ");
			apellido = teclado.nextLine();
			break;
		case 3:
			try {
				System.out.println("Introduce la edad: ");
				edad = teclado.nextInt();
			}catch ( InputMismatchException e ) {
				teclado.nextLine();
				System.out.println("Edad no válida.");
				return;
			}
			break;
		case 4:
			System.out.println("Introduce un DNI: ");
			dni = teclado.nextLine();
			break;
		case 5:
			// Pide todos los datos de una Persona
			Persona auxPersona = pedirPersona();
			// los asigna a las variables auxiliares
			nombre = auxPersona.getNombre();
			apellido = auxPersona.getApellido();
			edad = auxPersona.getEdad();
			dni = auxPersona.getDni();
		}
		// Modifica los datos
		baseDeDatos.modificarDatosPersona(indice,nombre, apellido, edad, dni);
	}
	private void verTodosLosRegistros () throws Exception {
		ResultSet rs = baseDeDatos.mostrarTodosLosDatos();
		System.out.println(BaseDeDatos.resultSetToString(rs));
	}
	private void imprimirDatosConexion () throws Exception {
		System.out.println(baseDeDatos.datosConexion());
	}
	private void imprimirInfoTabla() throws Exception {
		System.out.println(baseDeDatos.datosDB());
	}
	// END OPCIONES SWITCH
	
	private Persona pedirPersona ( ) throws IllegalStateException , InputMismatchException {
		// Datos de la Persona a añadir
		String nombre, apellido, dni;
		int edad;
		// Pide los datos por teclado
		try {
			System.out.println("Introduce el nombre: ");
			nombre = teclado.nextLine();
						
			System.out.println("Introduce el apellido: ");
			apellido = teclado.nextLine();
							
			System.out.println("Introduce la edad: ");
			edad = teclado.nextInt();
				
			teclado.nextLine();
				
			System.out.println("Introduce el DNI: ");
			dni = teclado.nextLine();
				
		}catch( InputMismatchException e ) {
			teclado.nextLine();
			throw new InputMismatchException("El formato de los datos no es válido.");
		}
		// Instancia la persona con los datos facilitados por el usuario
		Persona auxP = new Persona(nombre,apellido,edad,dni);
		return auxP;

	}
	private void imprimirMenu ( ) {
		System.out.println("*************************************");
		System.out.println("Elige una opción:");
		System.out.println("\t1.Añadir Persona");
		System.out.println("\t2.Eliminar Persona");
		System.out.println("\t3.Ver datos de Persona");
		System.out.println("\t4.Modificar Persona");
		System.out.println("\t5.Ver Todos los Registros");
		System.out.println("\t6.Ver Datos de Conexión");
		System.out.println("\t7.Ver Información sobre la tabla");
		System.out.println("\t0.Salir");
		System.out.println("*************************************");
	}

}
