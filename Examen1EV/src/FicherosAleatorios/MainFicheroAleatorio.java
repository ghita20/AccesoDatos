package FicherosAleatorios;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import Clases.Articulo;
import Clases.Cliente;
import Clases.Venta;
import Principal.MenuPrincipal;

public class MainFicheroAleatorio {
	// Menu con las opciones
	private static final String MENU_OPCIONES = 
			"\n===================================="
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
	
	// Nombres de los Ficheros
	private static final String FICHERO_CLIENTES = "ficheroClientes.dat";
	private static final String FICHERO_ARTICULOS = "ficheroArticulos.dat";
	private static final String FICHERO_VENTAS = "ficheroVentas.dat";
	
	// RandomAccessFile para cada fichero ( Clientes , Ventas , Articulos )
	private GestionClientes gestionClientes;
	private GestionArticulos gestionArticulos;
	private GestionVentas gestionVentas;
	
	// Número de opciones del menú
	private static final byte MIN_OPCION = 0, MAX_OPCION = 6;
	
	// Constructor
	public MainFicheroAleatorio(Scanner teclado) throws IOException {
		this.teclado = teclado; // Le pasa el inputStream para no instanciar otro
		// Instancia los RandomAccessFile
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
				// Cierra los Ficheros
				gestionArticulos.close();
				gestionClientes.close();
				gestionVentas.close();
				System.out.println("Has salido de FicherosAleatorios");
				break;
			case 1: // Nuevo articulo
				darDeAltaArticulo();
				break;
			case 2: // Nueva Venta
				nuevaVenta();
				break;
			case 3: // Agregar un nuevo cliente
				agregarNuevoCliente();
				break;
			case 4:
				System.out.println(MenuPrincipal.NO_HACER);
				break;
			case 5: // Buscar por nombre de cliente
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
	
	// Nueva venta
	private void nuevaVenta() throws Exception {
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
