package Clases;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Conductor {
	// Atributos
	private String nombre; // Nombre
	private String apellidos; // Apellidos
	private int edad; // Edad
	private String dni;
	private Coche coche; // Coche
	
	// Constructor
	public Conductor( String nombre , String apellidos , int edad , String dni , Coche coche  ) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.edad = edad;
		this.dni = dni;
		this.coche = coche;
	}

	// Getters
	public String getNombre() {
		return nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public int getEdad() {
		return edad;
	}
	public String getDni() {
		return dni;
	}
	public Coche getCoche() {
		return coche;
	}
	
	// Setters
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public void setCoche(Coche coche) {
		this.coche = coche;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Conductor: {" +"Nombre: " +nombre +" : " +"Apellidos: " +apellidos +" : " +"Edad: " +edad +" : " +"DNI: " +dni +" : \n\t\t\t\t" +coche +" }";
	}
	
	// Crea una nueva instancia de la clase Conductor
	public static Conductor crearConductor ( Scanner teclado ) {
		// Limpia el buffer
		teclado.nextLine();
		
		// Campos
		String nombre;
		String apellidos;
		String dni;
		int edad;
		
		// Datos
		System.out.println("Nombre: ");
		nombre = teclado.nextLine();
		
		System.out.println("Apellidos: ");
		apellidos = teclado.nextLine();
		
		System.out.println("DNI: ");
		dni = teclado.nextLine();
		try {
			System.out.println("Edad: ");
			edad = teclado.nextInt();
			teclado.nextLine();
		}catch ( InputMismatchException e ) {
			teclado.nextLine();
			return null;
		}
		
		// Devuelve el nuevo objeto
		return new Conductor(nombre, apellidos, edad, dni , null);
	}
	

}
