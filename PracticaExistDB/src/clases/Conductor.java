package clases;

import java.util.InputMismatchException;
import java.util.Scanner;

import util.Leer;

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
	public static Conductor crearConductor (  ) {
		// Datos
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
		
		// Devuelve el nuevo objeto
		return new Conductor(nombre, apellidos, edad, dni , null);
	}
	
	// Para encontrar objetos de tipo coche dentro de un ArrayList
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if ( obj instanceof Conductor ) {
			Conductor objC = (Conductor)obj;
			if ( objC.dni.equals(dni) 
					&& objC.nombre.equals(nombre) 
					&& objC.apellidos.equals(apellidos)
					&& objC.edad == edad)
				return true;
		}
		return super.equals(obj);
	}
	

}
