package Hibernate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import Clases.Articulo;
import Clases.Cliente;
import Clases.Venta;
import Principal.MenuPrincipal;

public class MainHibernate {
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
	
	// Clase que gestiona la conexión con la base de datos
	private GestionHibernate hibernate;
	
	// Número de opciones del menú
	private static final byte MIN_OPCION = 0, MAX_OPCION = 6;
	
	// Constructor
	public MainHibernate(Scanner teclado) throws Exception {
		this.teclado = teclado; // Le pasa el inputStream para no instanciar otro
		hibernate = new GestionHibernate();
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
				hibernate.close();
				System.out.println("Has salido de Hibernate!");
				break;
			case 1: // Nuevo Articulo
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
		hibernate.aniadirVenta(venta);
	}
	
	// Da de alta un articulo nuevo
	private void darDeAltaArticulo() throws Exception {
		Articulo auxArticulo = Articulo.pedirArticulo(teclado);
		hibernate.aniadirArticulo(auxArticulo);
	}

	// Agrega un nuevo cliente
	private void agregarNuevoCliente ( ) throws Exception {
		Cliente auxCliente = Cliente.pedirCliente(teclado);
		hibernate.aniadirCliente(auxCliente);
	}
	
	// Busca entre los clientes existentes
	private void buscarPorCliente ( ) throws IOException, SQLException {
		System.out.println("Nombre: ");
		String auxNombre = teclado.nextLine();
		System.out.println(hibernate.visualizarDatosClientes(auxNombre));
	}
	


}
