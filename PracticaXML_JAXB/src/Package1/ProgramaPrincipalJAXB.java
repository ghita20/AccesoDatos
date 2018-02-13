package Package1;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ProgramaPrincipalJAXB {
	private Scanner teclado;
	private Registros registro; // Registros
	
	public ProgramaPrincipalJAXB ( ) throws FileNotFoundException, JAXBException {
		teclado = new Scanner(System.in);
		registro = leerRegistrosDesdeXml("misRegistros.xml");
		
		while ( true ) {
			int opcion = opcionMenu();
			if ( opcion == 0 )
				break;
			eventoOpcion(opcion);
		}
		
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
		}while ( opcion>5 || opcion<0 );
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
			switch ( opcion ) {
			case 1: // AÑADIR
				try {
					Persona auxP = pedirPersona();
					registro.añadirPersona(auxP);
				}catch ( Exception e ) {
					System.out.println("\t"+e.getMessage());
				}
				break; // FIN CASE 1
			case 2: // ELIMINAR
				try {
					int indice = pedirEntero("Introduce el ID de la persona: ");
					registro.eliminarPersona(indice);
				}catch ( Exception e ) {
					System.out.println("\t"+e.getMessage());
				}
				break; // FIN CASE 2
				
			case 3: // VER DATOS PERSONA
				try {
					int indice = pedirEntero("Introduce el ID de la persona: ");
					System.out.println( "\t" +registro.buscarPersona(indice) );
				}catch ( Exception e ) {
					System.out.println("\t"+e.getMessage());
				}
				break; // FIN CASE 3
			case 4: // MODIFICAR PERSONA
				try {
					int indice = pedirEntero("Introduce el ID de la persona: ");
					Persona auxP = registro.buscarPersona(indice);
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
					registro.modificarPersona(nombre, apellido, edad, dni, indice);
					
				}catch ( Exception e ) {
					System.out.println("\t"+e.getMessage());
				}
				break; // FIN CASE 4
			case 5: // VER TODOS LOS REGISTROS
				System.out.println(registro.imprimirRegistros());
				break; // FIN CASE 5
			}
		}
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
		try {
			Persona auxP = new Persona(nombre,apellido,edad,dni);
			return auxP;
		}catch ( IllegalStateException e ) { 
			throw e; // Si al instancia la persona algún dato no es válido devuelve el error
		}
	}
	private void imprimirMenu ( ) {
		System.out.println("*************************************");
		System.out.println("Elige una opción:");
		System.out.println("\t1.Añadir Persona");
		System.out.println("\t2.Eliminar Persona");
		System.out.println("\t3.Ver datos de Persona");
		System.out.println("\t4.Modificar Persona");
		System.out.println("\t5.Ver Todos los Registros");
		System.out.println("\t0.Salir");
		System.out.println("*************************************");
	}
	
	// Genera un objeto de tipo Registros apartir de un XML
	private static Registros leerRegistrosDesdeXml ( String ruta ) throws JAXBException, FileNotFoundException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(Registros.class);
		// Crea un unmarshaller ( para leer )
		Unmarshaller unmarshaller = context.createUnmarshaller();
		// Devuelve un objeto con todos los datos
		Registros reg =  (Registros)unmarshaller.unmarshal( new InputStreamReader( new FileInputStream(ruta) ));
		return reg;
	}

}
