package Clases;

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "cliente" ) @XmlType( propOrder = {"nombre","dni","telefono","email"} )
public class Cliente {
	// Longitud de los campos nombre , dni y email
	public static final int LONG_NOMBRE = 20, 
			LONG_DNI = 9,
			LONG_EMAIL = 20;
	
	private String nombre; // Nombre
	private String dni; // DNI
	private int telefono; // Teléfono
	private String email; // Email
	
	// Constructor
	public Cliente(String nombre, String dni, int telefono, String email) {
		if ( nombre == null  )
			throw new IllegalArgumentException("El nombre no es válido.");
		if ( dni == null  )
			throw new IllegalArgumentException("El DNI no es válido.");
		if ( email == null  )
			throw new IllegalArgumentException("El email no es válido.");
		this.nombre = nombre;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
	}
	
	// Constructor por defecto
	public Cliente() {
		// TODO Auto-generated constructor stub
	}
	
	// Getters y Setters
	@XmlElement( name = "nombre" )
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@XmlElement( name = "dni" )
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	@XmlElement( name = "telefono" )
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	@XmlElement( name = "email" )
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return nombre + " " +dni +" " + telefono +" " + email;
	}

	// Pide los datos necesarios para crear un nuevo objeto de tipo Cliente
	public static Cliente pedirCliente ( Scanner teclado ) throws Exception{ 
		// TODO:
		String nombre , dni , email;
		int telefono;
		System.out.println("Nombre: ");
		nombre = teclado.nextLine();
		System.out.println("DNI: ");
		dni = teclado.nextLine();
		System.out.println("Teléfono: ");
		try {
			telefono = teclado.nextInt();
		}catch ( InputMismatchException e ) {
			throw new Exception("Número de teléfono no válido.");
		}finally {
			teclado.nextLine();
		}
		System.out.println("Email: ");
		email = teclado.nextLine();
		// Devuelve el nuevo cliente
		return new Cliente(nombre,dni,telefono,email);
	}


}
