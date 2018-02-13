package Cliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.neodatis.odb.Values;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import Clases.Conductor;
import Clases.Coche;

public class GestionMenu {
	// Teclado
	private Scanner teclado;
	
	// Gestiona los datos en la memoria
	public GestionDatos gestion;
	
	// Menï¿½s
	public static final String MENU_PRINCIPAL =  "\n---------------------------"
			
			+"\n\n COCHE	"
			+"\n---------------------------"
			+"\n 1- Nuevo Coche	"
			+"\n---------------------------"
			+"\n 2- Borrar Coche	"
			+"\n---------------------------"
			+"\n 3- Modificar Coche	"
			+"\n---------------------------"
			+"\n 4- Visualizar Coches"
			+"\n---------------------------"
			
			+"\n\n CONDUCTOR	"
			+"\n---------------------------"
			+"\n 5- Nueva Conductor "
			+"\n---------------------------"
			+"\n 6- Borrar Conductor	"
			+"\n---------------------------"
			+"\n 7- Modificar Conductor	"
			+"\n---------------------------"
			+"\n 8- Visualizar Conductores"
			+"\n---------------------------"
			+"\n 9- Asignar Coche"
			+"\n---------------------------"
			
			+"\n\n CONSULTAS	"
			+"\n---------------------------"
			+"\n 10- Mostrar Conductores que conducen cierto modelo de Coche "
			+"\n---------------------------"
			+"\n 11- Coche mas caro	"
			+"\n---------------------------"
			+"\n 12- Precio medio de los coches	"
			+"\n---------------------------"
			+"\n 13- Coches de la misma marca"
			+"\n---------------------------"
			+"\n 14- Edad media de los conductores"
			+"\n---------------------------"
			
			+"\n\n OTRAS OPCIONES	"
			+"\n---------------------------"
			+"\n 15- Guardar cambios	"	
			+"\n---------------------------"	
			+"\n 0- Salir	";
	
	// Constructor
	public GestionMenu() throws Exception {
		// Teclado
		teclado = new Scanner(System.in);
		
		// La clase GestionDatos se encargï¿½ de abrir una nueva conexiï¿½n con neodatis
		gestion = new GestionDatos();
		
		// Imprime el menu
		imprimirMenuPrincipal();
	}

	// Imprime el menú y gestiona las opciones
	private void imprimirMenuPrincipal() {
		// Muestra el menu
		System.out.println(MENU_PRINCIPAL);
		
		try {
			// Lee la opciï¿½n del usuario
			int opcion = teclado.nextInt();
			
			// Switch
			switch ( opcion ) {
			
			// NUEVO COCHE
			case 1: nuevoCoche(); break;
			
			// BORRAR COCHE
			case 2: borrarCoche(); break;
			
			// MODIFICAR COCHE
			case 3: modificarCoche(); break;
			
			// VISUALIZAR DATOS COCHES
			case 4: gestion.imprimirDatosCoches(); break;
			
			// NUEVO CONDUCTOR
			case 5: nuevoConductor(); break;
			
			// BORRAR CONDUCTOR
			case 6: borrarConductor(); break;
			
			// MODIFICAR CONDUCTOR
			case 7: modificarConductor(); break;
			
			// VISUALIZAR CONDUCTORES
			case 8: gestion.imprimirConductores(); break;

			// ASIGNAR COCHE A CONDUCTOR
			case 9: asignarCoche(); break;

			// CONDUCTORES QUE CONDUCEN UN CIERTO MODELO DE COCHE
			case 10: conductoresQueConducenModeloCoche(); break;
			
			// COCHE MAS CARO
			case 11: cocheMasCaro(); break;
				
			// PRECIO MEDIO DE LOS COCHES	
			case 12: precioMedioCoches(); break;
				
			// COCHES CON LA MISMA MARCA
			case 13: imprimirCochesMismaMarca(); break;
				
			// EDAD MEDIA DE LOS CONDUCTORES
			case 14: edadMediaConductores(); break;
				
			// GUARDAR CAMBIOS
			case 15: 
				gestion.guardarCambios();
				System.out.println("Cambios guardados!");
				break;
				
			// SALIR	
			case 0: 
				// Cierra la conexiÃ³n
				gestion.neodatis.cerrarConexion();
				System.exit(0); // Cierra el programa sin guardar los cambios
				break;
			}
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
		}finally {
			// Vuelve a imprimir el menu principal hasta que el usuario elija la opciï¿½n "0"
			imprimirMenuPrincipal();
		}
	}
	
	// Opciones CONSULTAS
	private void edadMediaConductores() {
		// Imprime la edad media
		System.out.println("\nEdad media de los conductores: " +gestion.neodatis.edadMediaConductores() +" años.");
	}
	private void imprimirCochesMismaMarca() {
		// Salta de línea
		teclado.nextLine();

		// Pide la marca
		System.out.println("Introduce la marca: " );
		String marca = teclado.nextLine();

		// Resultado
		System.out.println("Coches de la marca: " +marca);
		System.out.println("------------------------------");
		
		// Resultado de la consulta
		ArrayList<Coche> auxCoches = gestion.neodatis.cochesMismaMarca(marca);
		
		if ( auxCoches.size() == 0 )
			System.out.println("No existen coches de esa marca.");
		
		// Imprime los coches
		for ( Coche c : auxCoches )
			System.out.println("\t" +c);
	}
	private void precioMedioCoches() {
		// Imprime el precio medio de los coches
		System.out.println("\nPrecio medio de los coches: " +gestion.neodatis.precioMedioCoches() +" euros.");
	}
	private void cocheMasCaro() {
		// Recoge el coche más caro
		Coche cocheCaro = gestion.neodatis.cocheMasCaro();
		if ( cocheCaro == null )
			System.out.println("No existen coches en la db.");
		else
			System.out.println("El coche mas caro: " +"\t" + cocheCaro +" con un precio de: " +cocheCaro.getPrecio() +" euros.");
	}
	private void conductoresQueConducenModeloCoche() {
		// Salta de línea
		teclado.nextLine();

		// Pide la marca
		System.out.println("Introduce la marca: " );
		String marca = teclado.nextLine();

		// Resultado
		System.out.println("Conductores que conducen un coche con marca: " +marca);
		System.out.println("------------------------------");

		// Recoge los conductores que conducen un coche de esa marca
		ArrayList<Conductor> conductores = gestion.neodatis.conductoresConducenModeloCoche(marca);
		
		if ( conductores.size() == 0 )
			System.out.println("No existen conductores que conduzcan un coche con marca: " +marca);
		
		// Imprime los conductores
		for ( Conductor c : conductores )
			System.out.println("\t" +c);
	}

	

	// Opciones CONDUCTOR
	private void modificarConductor() {
		// Imprime los conductores existentes
		int numConductores = gestion.imprimirConductoresYindice();
		// Si no existen conductores sale del mï¿½todo
		if ( numConductores == 0 ) return;

		System.out.println("Elige el conductor:");

		try {
			// Conductor a modificar
			int conductorModificar = teclado.nextInt();
			// Referencia al conductor escogido
			Conductor auxA = gestion.getConductores().get(conductorModificar);

			System.out.println("Para no modificar un campo hay que asignarle el valor '0' ");

			// Teclado
			teclado.nextLine();

			// Datos
			System.out.println("Nombre: ");
			String nombre = teclado.nextLine();
			
			System.out.println("Apellidos: ");
			String apellidos = teclado.nextLine();
			
			System.out.println("DNI: ");
			String dni = teclado.nextLine();
			
			System.out.println("Edad: ");
			int edad = teclado.nextInt();
			teclado.nextLine();

			// Modifica los campos
			if ( !nombre.equals("0") )
				auxA.setNombre(nombre);
			if ( !apellidos.equals("0") )
				auxA.setApellidos(apellidos);
			if ( edad != 0 )
				auxA.setEdad(edad);
			if ( !dni.equals("0") )
				auxA.setDni(dni);

		}catch ( Exception e ) {
			teclado.nextLine();
			System.out.println("Datos no vï¿½lidos.");
		}
	}
	private void borrarConductor() {
		// Muestra los conductores existentes
		int numConductores = gestion.imprimirConductoresYindice();
		// Si no existen conductores sale del mï¿½todo
		if ( numConductores == 0 ) return;
		
		System.out.println("Elige el conductor:");
		
		try {
			// Conductor a eliminar
			int conductorEliminar = teclado.nextInt();
			gestion.getConductores().remove(conductorEliminar); // Si el ï¿½ndice no es correcto genera una excepciï¿½n
		}catch ( Exception e ) {
			teclado.nextLine();
			System.out.println("Conductor no vï¿½lido.");
		}
	}
	private void nuevoConductor() {
		// Pide los datos para crear un nuevo objeto conductor
		Conductor a = Conductor.crearConductor(teclado);
		if ( a == null )
			System.out.println("Datos no vÃ¡lidos.");
		else {
			try {
				// AÃ±ade el nuevo conductor
				gestion.getConductores().add(a);
				System.out.println("Conductor aÃ±adido con Ã©xito!");
			} catch (Exception e) {
				// Imprime mensajes de error
				System.out.println(e.getMessage());
			}
		}
	}
	private void asignarCoche() {
		// Conductor
		Conductor conductor = null;
		
		// Coche 
		Coche coche = null;

		// Muestra los conductores existentes
		int numConductores = gestion.imprimirConductoresYindice();
		// Si no existen conductores sale del mï¿½todo
		if ( numConductores == 0 ) return;

		System.out.println("Elige el conductor:");

		try {
			// Conductor elegido
			int conductorElegido = teclado.nextInt();
			conductor = gestion.getConductores().get(conductorElegido); // Si el ï¿½ndice no es correcto genera una excepciï¿½n
		}catch ( Exception e ) {
			teclado.nextLine();
			System.out.println("Conductor no vï¿½lido.");
			return;
		}

		// Imprime los coches existentes
		int numCoches = gestion.imprimirCochesYindice();
		// Si no existen coches sale del mï¿½todo
		if ( numCoches == 0 ) return;

		System.out.println("Elige el coche que quieras asignar al conductor: ");

		try {
			// Coche elegido
			int cocheElegido = teclado.nextInt();
			coche = gestion.getCoches().get(cocheElegido); // Si el ï¿½ndice no es correcto genera una excepciï¿½n
		}catch ( Exception e ) {
			teclado.nextLine();
			System.out.println("Coche no vï¿½lido.");
			return;
		}

		// Asigna el coche al conductor
		conductor.setCoche( coche );
		System.out.println("Coche asignado con ï¿½xito.");
	}

	// Opciones COCHE
	private void nuevoCoche() {
		Coche c = Coche.crearCoche(teclado); // Pide los datos para crear un nuevo coche
		if ( c == null ) // Si es null el coche no es vï¿½lido
			System.out.println("Datos no vï¿½lidos.");
		else {
			try {
				// AÃ±ade el nuevo coche a la memoria
				gestion.getCoches().add(c);
				System.out.println("Coche agregado con Ã©xito!");
			} catch (Exception e) {
				// Imprime mensaje de error
				System.out.println(e.getMessage());
			}
		}
	}
	private void borrarCoche() {
		// Imprime los coches existentes
		int numCursos = gestion.imprimirCochesYindice();
		// Si no existen coches sale del mï¿½todo
		if ( numCursos == 0 ) return;

		System.out.println("Elige el coche:");

		try {
			// Coche a eliminar
			int cocheEliminar = teclado.nextInt();
			gestion.getCoches().remove(cocheEliminar); // Si el ï¿½ndice no es correcto genera una excepciï¿½n
		}catch ( Exception e ) {
			teclado.nextLine();
			System.out.println("Curso no vÃ¡lido.");
		}
	}
	private void modificarCoche() {
		// Imprime los coches existentes
		int numCoches = gestion.imprimirCochesYindice();
		// Si no existen coches sale del mï¿½todo
		if ( numCoches == 0 ) return;

		System.out.println("Elige el coche:");

		try {
			// Curso a modificar
			int cocheModificar = teclado.nextInt();
			// Referencia al curso escogido
			Coche auxC = gestion.getCoches().get(cocheModificar);
			System.out.println("Para no modificar un campo hay que asignarle el valor '0' ");
			
			// Teclado
			teclado.nextLine();
			
			// Marca
			System.out.println("Marca: " );
			String marca = teclado.nextLine();

			// Modelo
			System.out.println("Modelo: " );
			String modelo = teclado.nextLine();

			// Color
			System.out.println("Color: " );
			String color = teclado.nextLine();
			
			// Precio
			System.out.println("Precio: " );
			float precio = teclado.nextFloat();
			
			teclado.nextLine();
			
			// Modifica los campos del curso si los nuevos datos no tienen el valor '0'
			if ( !marca.equals("0") )
				auxC.setMarca(marca);
			
			// Modelo
			if ( !modelo.equals("0")  )
				auxC.setModelo(modelo);
			
			// Color
			if ( !color.equals("0") )
				auxC.setColor(color);
			
			// Precio
			if ( precio != 0 )
				auxC.setPrecio(precio);
		}catch ( Exception e ) {
			teclado.nextLine();
			System.out.println("Datos no vï¿½lidos.");
		}
	}
	
}
