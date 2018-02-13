package Clases;

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "articulo" ) @XmlType( propOrder = {"id","nombre","descripcion","precio","stock"} )
public class Articulo {
	public static final int 
		LONG_NOMBRE = 20,
		LONG_DESCRIPCION = 40;
	
	private int id; // ID Articulo
	private String nombre; // Nombre
	private String descripcion; // Descripcion
	private double precio; // Precio
	private int stock; // Stock
	
	// Constructor
	public Articulo(int id, String nombre, String descripcion, double precio, int stock) {
		if ( nombre == null  )
			throw new IllegalStateException("Nombre es null.");
		if ( descripcion == null  )
			throw new IllegalStateException("Descripcion es null.");
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
	}
	
	// Constructor por defecto
	public Articulo() {
		
	}
	
	// Getters y Setters
	@XmlElement( name = "id" )
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@XmlElement( name = "nombre" )
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@XmlElement( name = "descripcion" )
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@XmlElement( name = "precio" )
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	@XmlElement( name = "stock" )
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	// Pide los datos necesarios para crear un nuevo objeto de tipo Articulo
	public static Articulo pedirArticulo ( Scanner teclado ) throws Exception {
		int id , stock;
		String nombre, descripcion;
		double precio;
		System.out.println("Nombre: ");
		nombre = teclado.nextLine();

		System.out.println("Decripcion: ");
		descripcion = teclado.nextLine();

		System.out.println("ID: ");
		try {
			id = teclado.nextInt();
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
			throw new Exception("El ID no es válido.");
		}
		try {
			System.out.println("Stock: ");
			stock = teclado.nextInt();
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
			throw new Exception("El Stock no es válido.");
		}

		System.out.println("Precio: ");
		try {
			precio = teclado.nextDouble();
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
			throw new Exception("El Precio no es válido.");
		}
		// Devuelve el nuevo articulo
		return new Articulo(id, nombre, descripcion, precio, stock);
	}
}
