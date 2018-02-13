package JAXB;

import java.io.IOException;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import Clases.Articulo;
import Clases.Cliente;
import Clases.Venta;
import Principal.MenuPrincipal;

public class MainJAXB {
	// Menu con las opciones
	private static final String MENU_OPCIONES = "\n===================================="
			+"\nELIGE UNA OPCIÓN: "
			+"\n\t1.Dar de alta un artículo"
			+"\n\t2.Venta de articulos"
			+"\n\t3.Dar de alta un cliente"
			+"\n\t4.Visualizar el stock de la tienda"
			+"\n\t5.Buscar por cliente"
			+"\n\t6.Insertar valores por defecto"
			+"\n\t0.Salir"
			+"\n====================================";
	private Scanner teclado; // Para leer la entrada del teclado
	private byte opcion; // Opcion elegida por el usuario
	
	// Nombres de los ficheros XML
	private static final String FICHERO_CLIENTES = "ficheroClientes.xml";
	private static final String FICHERO_ARTICULOS = "ficheroArticulos.xml";
	private static final String FICHERO_VENTAS = "ficheroVentas.xml";
	
	// Gestión de cada fichero xml ( Clientes, Articulos , Ventas )
	private GestionClientes gestionClientes;
	private GestionArticulos gestionArticulos;
	private GestionVentas gestionVentas;
	
	// Número de opciones
	private static final byte MIN_OPCION = 0, MAX_OPCION = 6;
	
	// Constructor
	public MainJAXB(Scanner teclado) throws IOException, JAXBException {
		this.teclado = teclado; // Le pasa el inputStream para no instanciar otro
		// Instancia los gestores
		gestionClientes = new GestionClientes(FICHERO_CLIENTES);
		gestionArticulos = new GestionArticulos(FICHERO_ARTICULOS);
		gestionVentas = new GestionVentas(FICHERO_VENTAS);
		do {
			// Pide una opción
			opcion = Principal.MenuPrincipal.pedirOpcion(MIN_OPCION, MAX_OPCION, teclado,MENU_OPCIONES);
			// Filtra la opción
			filtrarOpcion();
		}while( opcion!= 0); // Si la opción es 0 se sale
		
	}

	// Filtra el menú
	private void filtrarOpcion ( ) {
		try {
			switch (opcion) {
			case 0: // Salir
				System.out.println("Has salido de JAXB!");
				break;
			case 1: // 
				darDeAltaArticulo();
				break;
			case 2: // 
				nuevaVenta();
				break;
			case 3: // Agregar un nuevo cliente
				agregarNuevoCliente();
				break;
			case 4:
				System.out.println(MenuPrincipal.NO_HACER);
				break;
			case 5: // Busca por nombre de cliente
				buscarPorCliente();
				break;
			case 6:
				System.out.println(MenuPrincipal.NO_HACER);
				break;
			default:
				break;
			}
		}catch ( Exception e ) {
			System.out.println(e.getMessage());
		}
	}
	
	private void nuevaVenta() throws Exception {
		// TODO Auto-generated method stub
		Venta venta = Venta.pedirVenta(teclado);
		gestionVentas.agregarVenta(venta);
	}
	
	// Da de alta un articulo nuevo
	private void darDeAltaArticulo() throws Exception {
		Articulo auxArticulo = Articulo.pedirArticulo(teclado);
		gestionArticulos.agregarArticulo(auxArticulo);

	}
	
	// Agrega un nuevo cliente
	private void agregarNuevoCliente ( ) throws Exception {
		Cliente auxCliente = Cliente.pedirCliente(teclado);
		gestionClientes.agregarCliente(auxCliente);
	}
	
	// Busca entre los clientes existentes
	private void buscarPorCliente ( ) throws IOException {
		System.out.println("Nombre: ");
		String auxNombre = teclado.nextLine();
		System.out.println(gestionClientes.visualizarDatosCliente(auxNombre));
	}

}
