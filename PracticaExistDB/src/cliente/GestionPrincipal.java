package cliente;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import clases.Coche;
import clases.Conductor;
import util.Leer;

public class GestionPrincipal {
	
	// Cliente ExistDB
	private ClienteExistDB exitsDB;
	
	// Menu
	private static final String MENU_PRINCIPAL = 
			 "\n\n*************************** "
			+"\n      MENU PRINCIPAL	      "
			+"\n***************************   "
			+"\n 1- Coches	                  "
			+"\n---------------------------   "
			+"\n 2- Conductores	              "
			+"\n---------------------------   "
			+"\n 3- Consultas	              "
			+"\n---------------------------   "
			+"\n 4- Importar desde ficheros	  "	// de ficheros a existDB y despues carga en memoria
			+"\n---------------------------   "	
			+"\n 5- Exportar a ficheros	      "	// de existDB a ficheros
			+"\n---------------------------   "
			+"\n 6- Guardar cambios           "	// de memoria a existDB
			+"\n---------------------------   "	
			+"\n 0- Salir	                \n";
	// Secundario - Coches
	private static final String MENU_SECUNDARIO_COCHES =  
			 "\n\n***************************"
			+"\n\tMENU COCHES	"
			+"\n***************************"
			+"\n 1- Nuevo Coche	"
			+"\n---------------------------"
			+"\n 2- Borrar Coche	"
			+"\n---------------------------"
			+"\n 3- Modificar Coche	"
			+"\n---------------------------"
			+"\n 4- Visualizar Coches"
			+"\n---------------------------"
			+"\n 0- Volver	\n";
	// Secundario - Conductores
	private static final String MENU_SECUNDARIO_CONDUCTORES = 
			 "\n\n***************************"
			+"\n     MENU CONDUCTORES	   "
			+"\n***************************"
			+"\n 1- Nueva Conductor "
			+"\n---------------------------"
			+"\n 2- Borrar Conductor	"
			+"\n---------------------------"
			+"\n 3- Modificar Conductor	"
			+"\n---------------------------"
			+"\n 4- Visualizar Conductores"
			+"\n---------------------------"
			+"\n 5- Asignar Coche"
			+"\n---------------------------"
			+"\n 0- Volver	\n";
	// Secundario - Consultas
	private static final String MENU_SECUNDARIO_CONSULTAS = 
			 "\n\n***************************"
			+"\n      MENU CONSULTAS	   "
			+"\n***************************"
			+"\n 1- Conductores que tienen x Coche "
			+"\n---------------------------"
			+"\n 2- Coche mas caro	"
			+"\n---------------------------"
			+"\n 3- Precio medio de los coches	"
			+"\n---------------------------"
			+"\n 4- Coches de la misma marca"
			+"\n---------------------------"
			+"\n 5- Conductor mas joven"
			+"\n---------------------------"
			+"\n 0- Volver	\n";
	

	// ArrayList con los coches que tenemos en memoria
	private ArrayList<Coche> coches;
	
	// ArrayList con los conductores que tenemos en memoria
	private ArrayList<Conductor> conductores;
	
	
	
	// ***CONSTRUCTOR***
	public GestionPrincipal() throws Exception {
		// Conexion con ExistDB
		exitsDB = new ClienteExistDB();
		
		// Despues de conectar con la db carga en memoria todos los objetos
		coches = exitsDB.descargarCoches();
		conductores = exitsDB.descargarConductores(coches);
		
		// Imprime el menu principal
		imprimirMenuPrincipal();
		
	}

	// ***MENU PRINCIPAL***
	
	// Imprime el men� y gestiona las opciones
	private void imprimirMenuPrincipal() {
		// Muestra el menu
		System.out.println(MENU_PRINCIPAL);
		try {
			// Lee la opcion del usuario
			int opcion = Leer.pedirEnteroValidar("Elige una opcion: ");
			
			// Switch
			switch ( opcion ) {
			
			case 1: // Coches 
				imprimirMenuCoches();
				break;
				
			case 2: // Conductores 
				imprimirMenuConductores();
				break;
				
			case 3: // Consultas 
				imprimirMenuConsultas();
				break;
				
			case 4: // Importar desde ficheros a existDB ( Importa los datos de los ficheros a la base de datos y despues los recoge en la memoria )
				exitsDB.importarDesdeFicheros(); // Este proceso reemplaza la base de datos con el contenido de los ficheros "coleccion/coches y coleccion/conductores"
				// Descarga los nuevos datos de la db
				coches = exitsDB.descargarCoches();
				conductores = exitsDB.descargarConductores(coches);
				// Mensaje
				imprimirMensaje("Se han importado los datos de los ficheros a la base de datos y se han a�adido a la memoria.");
				break;
				
			case 5: // Exportar los datos de existDB a ficheros ( Exporta lo que hay en la base de datos, no lo que hay en memoria )
				exitsDB.guardarEnFicheros();
				imprimirMensaje("Datos exportados con exito!");
				break;
				
			case 6: // Guardar cambios en existDB
				// Primero elimina todos los nodos de la base de datos
				exitsDB.eliminarTodosLosDatos();
				// Inserta los datos que tenemos almacenados en memoria
				exitsDB.guardarMemoriaEnDB(coches,conductores);
				// Mensaje de confirmacion
				imprimirMensaje("Cambios guardados correctamente!");
				break;
				
			case 0: 
				// Cierra la conexion
				exitsDB.cerrarConexion();
				System.exit(0); // Cierra el programa
				break;
				
			default: // Opcion incorrecta
				imprimirMensaje("Opcion incorrecta!");
			} // FIN SWITCH
			
		}catch ( Exception e ){ // Excepciones
			imprimirMensaje(e.getMessage());
			//e.printStackTrace();
		}finally {
			// Vuelve a imprimir el menu principal hasta que el usuario elija la opcion "0"
			imprimirMenuPrincipal();
		}
	}

	// ***FIN MENU PRINCIPAL***
	
	
	// ***MENUS SECUNDARIOS***
	
	// Imprime el menu secundario COCHES y gestiona las opciones
	private void imprimirMenuCoches() throws Exception{
		// Muestra el menu secundario
		System.out.println(MENU_SECUNDARIO_COCHES);
		// Pide una opcion
		int opcionElegida = Leer.pedirEnteroValidar("Elige una opcion: ");
		// Switch
		switch ( opcionElegida ) {
		case 1: // Nuevo Coche ( Agrega el coche en la memoria
			nuevoCoche();
			break;
		case 2: // Borrar Coche
			borrarCoche();
			break;
		case 3: // Modificar Coche
			modificarCoche();
			break;
		case 4: // Visualizar Coches
			imprimirCochesConIndice(); // Imprime los coches
			break;
		case 0: // Volver
			return;
			
		default: // Si la opcion no es valida vuelve a imprimir el menu secundario
			imprimirMensaje("Opcion incorrecta!");
			imprimirMenuCoches();
		}
	}
	
	// Imprime el menu secundario CONDUCTORES y gestiona las opciones
	private void imprimirMenuConductores() throws Exception {
		// Muestra el menu secundario
		System.out.println(MENU_SECUNDARIO_CONDUCTORES);
		// Pide una opcion
		int opcionElegida = Leer.pedirEnteroValidar("Elige una opcion: ");
		// Switch
		switch ( opcionElegida ) {
		case 1: // Nuevo Conductor
			nuevoConductor();
			break;
		case 2: // Borrar Conductor
			borrarConductor();
			break;
		case 3: // Modificar Conductor
			modificarConductor();
			break;
		case 4: // Visualizar Conductores
			imprimirConductoresConIndice(); // Imprime todos los conductores
			break;
		case 5: // Asignar Coche
			asignarCoche();
			break;
		case 0: // Volver
			return;

		default: // Si la opcion no es valida vuelve a imprimir el menu secundario
			imprimirMensaje("Opcion incorrecta!");
			imprimirMenuConductores();
		}
	}
	
	// Imprime el menu secundario CONSULTAS y gestiona las opciones
	private void imprimirMenuConsultas() throws Exception {
		// Muestra el menu secundario
		System.out.println(MENU_SECUNDARIO_CONSULTAS);
		// Pide una opcion
		int opcionElegida = Leer.pedirEnteroValidar("Elige una opcion: ");
		// Switch
		switch ( opcionElegida ) {
		case 1: // Conductores que tienen x Coche
			consultaConductoresCoche();
			break;
		case 2: // Coche mas caro
			consultaCocheMasCaro();
			break;
		case 3: // Precio medio de los coches
			consultaPrecioMedioCoches();
			break;
		case 4: // Coches de la misma marca
			consultaCochesMismaMarca();
			break;
		case 5: // Conductor mas joven
			conductorMasJoven();
			break;
		case 0: // Volver
			return;

		default: // Si la opcion no es valida vuelve a imprimir el menu secundario
			imprimirMensaje("Opcion incorrecta!");
			imprimirMenuConsultas();
		}
	}

	// ***FIN MENUS SECUNDARIOS***
	

	// OPCIONES CONSULTAS - ( TODOS LAS CONSULTAS SE REALIZAN SOBRE LA BASE DE DATOS )
	private void consultaConductoresCoche() throws Exception {
		// Pide un coche
		Coche cocheElegido = pedirCoche(); // Puede generar excepcion si se introducen datos erroneos
		// Realiza la consulta
		imprimirMensaje( "   CONDUCTORES QUE CONDUCEN EL COCHE CON ID: " +cocheElegido.getIdCoche() +"\n" + exitsDB.conductoresQueConducenCoche(cocheElegido) );
	}
	
	// Imprime el precio medio de los coches
	private void consultaPrecioMedioCoches() throws Exception{
		// Realiza la consulta
		imprimirMensaje("Precio medio de los coches: " + exitsDB.precioMedioCoches() +" euros.");
	}
	
	// Imprime el coche mas caro
	private void consultaCocheMasCaro() throws SAXException, IOException, ParserConfigurationException, XMLDBException {
		// Consulta
		imprimirMensaje(exitsDB.cocheMasCaro());
	}
	
	// Imprime los datos del conductor mas Joven
	private void conductorMasJoven() throws Exception {
		// Realiza la consulta
		imprimirMensaje( exitsDB.conductorMasJoven() );
	}
	
	// Imprime los datos de los coches que tienen la misma marca
	private void consultaCochesMismaMarca() throws Exception {
		String marca = Leer.pedirCadena("Introduce la marca: ");
		// Realiza la consulta
		imprimirMensaje("   COCHES CON MARCA: " +marca +" \n" +exitsDB.cochesConMarca(marca));
	}
	
	// OPCIONES CONDUCTOR - ( TODOS LOS CAMBIOS SE REALIZAN SOBRE LOS OBJETOS QUE TENEMOS EN MEMORIA )
	private void nuevoConductor() {
		Conductor c = Conductor.crearConductor(); // Pide los datos para crear un nuevo conductor
		try {
			// Aniade el nuevo coche a la base de datos
			conductores.add(c);
			imprimirMensaje("Conductor agregado con exito!");
		} catch (Exception e) {
			// Imprime mensaje de error
			imprimirMensaje(e.getMessage());
		}
	}
	// Borrar Conductor
	private void borrarConductor() throws Exception {
		try {
			// Pide un conductor valido
			Conductor conductor = pedirConductor();
			// Lo elimina del arrayList ( Se ha sobreescrito el metodo equals del Conductor para que el arrayList pueda saber que objeto borrar )
			conductores.remove(conductor);
			// Mensaje
			imprimirMensaje("Conductor eliminado con exito!");
		}catch ( NullPointerException e ) { // Mensajes de error
			imprimirMensaje( e.getMessage() );
		}
	}
	
	// Modificar Conductor
	private void modificarConductor() {
		// Imprime los conductores existentes
		int numConductores = imprimirConductoresConIndice();
		// Si no existen conductores sale del metodo
		if ( numConductores == 0 ) return;
		try {
			// Conductor a modificar
			int conductorModificar = Leer.pedirEnteroValidar("Elige un conductor: ");
			// Referencia al conductor escogido
			Conductor auxC = conductores.get(conductorModificar); // Si el indice no es correcto lanza excepcion

			// Mensaje informativo
			imprimirMensaje("\tPara no modificar un campo hay que asignarle el valor '0' ");

			// Nombre
			String nombre = Leer.pedirCadena("Nombre: ");
			// Apellidos
			String apellidos = Leer.pedirCadena("Apellidos: ");
			// DNI
			String dni = Leer.pedirCadena("DNI: ");
			
			// Edad
			int edad = -1;
			// Miestras la edad no sea valida, pide que se inserte la edad
			while ( edad == -1 )
				edad = Leer.pedirEnteroValidar("Edad: "); // Si se teclea una letra por ejemplo, el metodo pedirEnteroValidar devuelve -1...etc
			
			// Modifica el conductor si asi se quiere
			if ( !nombre.equals("0") )
				auxC.setNombre(nombre);
			if ( !apellidos.equals("0") )
				auxC.setApellidos(apellidos);
			if ( edad != 0 )
				auxC.setEdad(edad);
			if ( !dni.equals("0") )
				auxC.setDni(dni);
			
		}catch ( Exception e ) { // Excepciones
			imprimirMensaje("Conductor no valido.");
		}
	}
	// Imprime los conductores y devuelve el numero de conductores existentes
	private int imprimirConductoresConIndice ( ) {
		int i = 0;
		String cadena = ""; // Cadena vacia
		for ( Conductor c : conductores )
			cadena += "\n" +i++ +") " +c;
		// Si no existen conductores imprime el mensaje:
		if ( i == 0 )
			imprimirMensaje("No existen conductores.");
		else // Si existe mas de un conductor imprime los conductores y el indice de cada uno
			imprimirMensaje(cadena);
		return i; // Devuelve el numero de conductores
	}
	// Metodo para asignar un coche a un conductor
	private void asignarCoche() {
		// Conductor
		Conductor conductor = null;

		// Coche 
		Coche coche = null;

		// Muestra los conductores existentes
		int numConductores = imprimirConductoresConIndice();
		// Si no existen conductores sale del metodo
		if ( numConductores == 0 ) return;
		// Pide al usuario que elija un conductor, si elige mal saldra del metodo
		try {
			// Conductor elegido
			int conductorElegido = Leer.pedirEnteroValidar("Elige el conductor: ");
			conductor = conductores.get(conductorElegido); // Si el indice no es valido lanza una excepcion
		}catch ( Exception e ) { // Excepciones
			imprimirMensaje("Conductor no valido.");
			return;
		}

		try {
			// Pide un coche
			coche = pedirCoche();
		}catch ( NullPointerException e ) { // Si los datos son erroneos imprime el mensaje y sale del metodo
			imprimirMensaje(e.getMessage());
			return;
		}
		// Asigna el coche al conductor
		conductor.setCoche( coche );
		// Mensaje de eeeexito
		imprimirMensaje("Coche asignado con exito.");
	}

	
	// OPCIONES COCHE - ( TODOS LOS CAMBIOS SE REALIZAN SOBRE LOS OBJETOS QUE TENEMOS EN MEMORIA )
	private void nuevoCoche() {
		Coche c = Coche.crearCoche(); // Pide los datos para crear un nuevo coche
		try {
			// Aniade el nuevo coche a la base de datos
			coches.add(c);
			imprimirMensaje("Coche agregado con exito!");
		} catch (Exception e) {
			// Imprime mensaje de error
			imprimirMensaje(e.getMessage());
		}
	}
	// Imprime los coches y devuelve el numero de coches existentes
	private int imprimirCochesConIndice ( ) {
		int i = 0;
		String cadena = ""; // Cadena vacia
		for ( Coche c : coches )
			cadena += "\n" +i++ +") " +c;
		// Si no existen coches imprime el mensaje:
		if ( i == 0 )
			imprimirMensaje("No existen coches.");
		else // Si existe mas de un coche imprime los coches y el indice de cada uno
			imprimirMensaje(cadena);
		return i; // Devuelve el numero de coches
	}
	// Borrar Coche
	private void borrarCoche() throws Exception {
		// Imprime los coches existentes
		int numCoches = imprimirCochesConIndice();
		// Si no existen coches sale del metodo
		if ( numCoches == 0 ) return;
		try {
			// Coche a eliminar
			int cocheEliminar = Leer.pedirEnteroValidar("Elige un coche: ");
			int idCoche = coches.remove(cocheEliminar).getIdCoche(); // Si el indice no es correcto lanza una excepcion
			// Elimina la referencia a los conductores que tienen ese coche
			for ( Conductor conductor : conductores )
				if ( conductor.getCoche()!=null && conductor.getCoche().getIdCoche() == idCoche ) 
					conductor.setCoche( null ); // Se le elimina la referencia al coche
			// Mensaje
			imprimirMensaje("Coche eliminado con exito!");
		}catch ( Exception e ) {
			imprimirMensaje("Coche no valido.");
		}
	}
	// Modificar Coche
	private void modificarCoche() {
		// Imprime los coches existentes
		int numCoches = imprimirCochesConIndice();
		// Si no existen coches sale del metodo
		if ( numCoches == 0 ) return;
		try {
			// Coche a modificar
			int cocheModificar = Leer.pedirEnteroValidar("Elige un coche: ");
			// Referencia al coche escogido
			Coche auxC = coches.get(cocheModificar); // Si el indice no es correcto lanza excepcion
			
			// Mensaje informativo
			imprimirMensaje("\tPara no modificar un campo hay que asignarle el valor '0' ");
			
			// Marca
			String marca = Leer.pedirCadena("Marca: ");

			// Modelo
			String modelo = Leer.pedirCadena("Modelo: ");

			// Color
			String color = Leer.pedirCadena("Color: ");
			
			// Precio
			float precio = Leer.pedirFloat("Precio: ");
			
			// Modifica los campos del coche si los nuevos datos no tienen el valor '0'
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
			
		}catch ( Exception e ) { // Excepciones
			imprimirMensaje("Datos no validos.");
		}
	}

	
	// ***OTROS METODOS***
	// Imprime un mensaje en pantalla
	private void imprimirMensaje( String mensaje ) {
		System.out.println( 
				"\n\n***************************"
						+"\n" +mensaje
						+"\n***************************");
	}

	// Metodo para pedir un Coche de entre los que tenemos actualmente en memoria
	private Coche pedirCoche ( ) throws NullPointerException {
		// Imprime los coches existentes
		int numCoches = imprimirCochesConIndice();
		// Lanza una excepcion
		if ( numCoches == 0 )
			throw new NullPointerException("No existen coches.");
		try {
			// Coche elegido
			int cocheElegido = Leer.pedirEnteroValidar("Elige el coche: ");
			Coche coche = coches.get(cocheElegido); // Si el indice no es valido lanza una excepcion
			return coche;
		}catch ( Exception e ) { // Excepciones
			throw new NullPointerException("Coche no valido."); // Lanza una excepcion
		}
	}
	
	// Metodo para pedir un Conductor de entre los que tenemos actualmente en memoria
	private Conductor pedirConductor ( ) {
		// Muestra los conductores existentes
		int numConductores = imprimirConductoresConIndice();
		// Si no existen conductores lanza una excepcion
		if ( numConductores == 0 ) 
			throw new NullPointerException("No existen conductores.");
		// Pide al usuario que elija un conductor, si elige mal generara una excepcion
		try {
			// Conductor elegido
			int conductorElegido = Leer.pedirEnteroValidar("Elige el conductor: ");
			Conductor conductor = conductores.get(conductorElegido); // Si el indice no es valido lanza una excepcion
			return conductor;
		}catch ( Exception e ) { // Excepciones
			throw new NullPointerException("Conductor no valido.");
		}
	}
	
	// ***FIN OTROS METODOS***
}
