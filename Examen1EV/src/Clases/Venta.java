package Clases;

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "venta" ) @XmlType( propOrder = {"id","dni"} )
public class Venta {
	// Longitud de los campos nombre , dni y email
	public static final int LONG_DNI = 9;
	
	private int id; // ID del artículo
	private String dni; // DNI del cliente
	
	// Constructor
	public Venta(int id, String dni) {
		if ( dni == null )
			throw new IllegalStateException("Dni es null.");
		this.id = id;
		this.dni = dni;
	}
	
	// Constructor por defecto
	public Venta() {}
	
	// Getters y Setters
	@XmlElement( name = "id" )
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@XmlElement( name = "dni" )
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	// Pide datos para crear un nuevo objeto de tipo venta
	public static Venta pedirVenta ( Scanner teclado ) throws Exception {
		// Atributos
		int idArticulo;
		String dni;
		System.out.println("ID articulo: ");
		try {
			idArticulo = teclado.nextInt();
			// Gestiona el error
		}catch ( InputMismatchException e ) {
			throw new Exception("ID articulo no valido.");
		}finally {
			teclado.nextLine();
		}
		System.out.println("DNI: ");
		dni = teclado.nextLine();
		// Devuelve un nuevo objeto venta
		return new Venta(idArticulo, dni);
	}
}
