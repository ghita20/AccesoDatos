import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class ProgramaPrincipal {
	private Scanner teclado; // Scanner para leer del teclado
	private FicheroAccesoAleatorio ficheroAle; // Permite realizar operaciones sobre el fichero 
	
	public ProgramaPrincipal() throws IOException {
		// TODO Auto-generated method stub
		teclado = new Scanner(System.in);
		// Crea un apuntador al fichero donde se almacenan los datos
		File archivo = new File("fichero.dat");
		// Variable que almacena la opci�n del men� elegida por el usuario
		int opcion;
		
		// Si no existe el fichero lo crea. Si existe utilizar� la informaci�n de este.
		if ( !archivo.exists() ) {
			System.out.println("\tNo se ha encontrado ning�n fichero. Se crear� uno nuevo.");
			archivo.createNewFile();
		}
		try {
			ficheroAle = new FicheroAccesoAleatorio(archivo);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		}
		do {
			opcion = opcionMenu(); // Pide una opci�n del men�
			eventoOpcion(opcion); // Seg�n la opci�n elegida hace algo
		}while ( opcion!= 0);
		// Cierra el fichero
		ficheroAle.cerrar();
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
		}while ( opcion>5 || opcion<0 );
		return opcion;
	}
	
	// Seg�n el valor de "opcion" realizar� cierta acci�n
	private void eventoOpcion ( int opcion ) {
		switch ( opcion ) {
		case 1: // A�ADIR
			try {
				Persona auxP = pedirPersona(); // Pide los datos para instanciar un persona. Si hay alg�n error lo imprime y vuelve al men�
				ficheroAle.a�adirPersona(auxP); // Si los datos introducidos son correctos, a�ade un nuevo registro
			}catch ( Exception e ) { 
				// Imprime los errores que puedan generar los m�todos pedirPersona() y a�adirPersona()
				System.out.println("\t"+e.getMessage());
			}
			break; // FIN CASE 1
			
		case 2: // ELIMINAR
			int auxId = pedirEntero("Introduce el ID de la persona a eliminar: ");
			try {
				ficheroAle.eliminarPersona(auxId); // Intenta eliminar el registro
				System.out.println("\tRegistro eliminado con �xito."); 
			}catch ( Exception e ) { // Si hay alg�n error lo imprime
				System.out.println("\t"+e.getMessage());
			}
			break; // FIN CASE 2
			
		case 3: // VER DATOS PERSONA
			auxId = pedirEntero("Introduce el ID de la persona : ");
			try {
				Persona auxPersona = ficheroAle.buscarPersona(auxId);
				System.out.println( "\t" +auxPersona );
			}catch ( Exception e ) { 
				System.out.println("\t"+e.getMessage());
			} 
			break; // FIN CASE 3
		case 4:
			try {
				/* 	Dejamos unos valores por defecto para saber si se van a modificar esos datos o no.
				 	Si el usuario no quiere modificar el apellido por ejemplo, este se quedar� con el valor 'null'.
				 	En el m�todo modificarPersona() se comprobar� si el valor de apellido es 'null', si es as�, 
				 	significa que no se va a modificar el campo apellido y dejar� el ya existente. */
				
				String nombre = null , apellido = null, dni = null; 
				int edad = -1;
				// Comprueba que el ID existe
				auxId = pedirEntero("Introduce el ID de la persona : ");
				if ( !ficheroAle.idExiste(auxId) ) {
					System.out.println("\tEl ID no existe.");
					return;
				} // Fin ID
				int auxOpcModificar = pedirEntero("Que quieres modificar?: \n\t1.Nombre 2.Apellido 3.Edad 4.DNI 5.Todo ");
				if ( auxOpcModificar >= 1 && auxOpcModificar<=5 ) {
						switch ( auxOpcModificar ) {
						case 1:  // Nombre
							System.out.println("Introduce un nombre: ");
							nombre = teclado.nextLine();
							if(!Persona.nombreApellidoValido(nombre)) {
								System.out.println("\t"+"El nombre no es v�lido.");
								return;
							}
							break;
						case 2: // Apellido
							System.out.println("Introduce un apellido: ");
							apellido = teclado.nextLine();
							if(!Persona.nombreApellidoValido(apellido)) {
								System.out.println("\tEl apellido no es v�lido.");
								return;
							}
							break;
						case 3: // Edad
							System.out.println("Introduce la edad: ");
							try {
								edad = teclado.nextInt();
							}catch ( InputMismatchException e ) {
								System.out.println("\tFormato no v�lido.");
								return;
							}finally {
								teclado.nextLine();
							}
							break;
						case 4: // Salario
							System.out.println("Introduce el DNI: ");
							dni = teclado.nextLine();
							if ( !Persona.dniValido(dni) ) {
								System.out.println("\tDNI no v�lido.");
								return;
							}
							break;
						case 5: // Pide todos los datos
							Persona auxPersona = pedirPersona();
							nombre = auxPersona.getNombre();
							apellido = auxPersona.getApellido();
							edad = auxPersona.getEdad();
							dni = auxPersona.getDni();
							break; 
						}
					// Modifica los datos de la persona
					ficheroAle.modificarPersona(auxId, nombre, apellido, edad, dni);
				}else { 
					System.out.println("\tOpci�n no v�lida."); return;
				}
			}catch(Exception e ) { // Imprime los errores si los hay.
				System.out.println("\t"+e.getMessage());
			}
			break; // FIN CASE 4
		case 5: // VER TODOS LOS REGISTROS
			System.out.println("\nREGISTROS");
			try {
				HashMap<Integer,Persona> personas = ficheroAle.listadoPersonas();
				// Imprime los Datos de las personas y el indice, que ser� su id de registro
				for (Iterator iterator = personas.entrySet().iterator(); iterator.hasNext();) {
					Map.Entry comp = (Map.Entry)iterator.next();
					System.out.println( "\t" +comp.getKey() +" " +comp.getValue() );
				}
			} catch (Exception e) {
				System.out.println("\t"+e.getMessage());
			} 
			System.out.println("");
			break; // FIN CASE 5
		}
	}
	
	private Persona pedirPersona ( ) throws IllegalStateException , InputMismatchException {
		// Datos de la Persona a a�adir
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
			throw new InputMismatchException("El formato de los datos no es v�lido.");
		}
		try {
			Persona auxP = new Persona(nombre,apellido,edad,dni);
			return auxP;
		}catch ( IllegalStateException e ) { 
			throw e; // Si al instancia la persona alg�n dato no es v�lido devuelve el error
		}
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
				System.out.println("\t"+"N�mero no v�lido.");
			}finally {
				teclado.nextLine();
			}
		}
	}
	
	private static void imprimirMenu ( ) {
		System.out.println("*************************************");
		System.out.println("Elige una opci�n:");
		System.out.println("\t1.A�adir Persona");
		System.out.println("\t2.Eliminar Persona");
		System.out.println("\t3.Ver datos de Persona");
		System.out.println("\t4.Modificar Persona");
		System.out.println("\t5.Ver Todos los Registros");
		System.out.println("\t0.Salir");
		System.out.println("*************************************");
	}

}
