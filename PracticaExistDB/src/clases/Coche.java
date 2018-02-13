package clases;

import java.util.InputMismatchException;
import java.util.Scanner;

import util.Leer;

public class Coche {
	
	// Esta variable estatica se utiliza para asignar siempre un id que no se haya utilizado aun en la base de datos
	public static int MAX_ID = 0;
	
	// Atributos
	private int idCoche;
	private String marca; // Marca
	private String modelo; // Modelo
	private String color; // Color
	private float precio; // Precio
	
	// Constructor 
	public Coche(String marca, String modelo, String color, float precio) {
		this.marca = marca;
		this.modelo = modelo;
		this.color = color;
		this.precio = precio;
		// Asigna el id con el valor MAX_ID mas 1 para que el objeto tenga un ID mayor a los anteriores
		idCoche = ++MAX_ID;
	}
	
	// Constructor por defecto. Lo utilizo para poder instanciar coches sin modificar el MAX_ID
	public Coche ( ) {
		
	}
	
	// Getters
	public String getMarca() {
		return marca;
	}

	public String getModelo() {
		return modelo;
	}

	public String getColor() {
		return color;
	}

	public float getPrecio() {
		return precio;
	}
	
	public int getIdCoche() {
		return idCoche;
	}
	
	// Setters
	public void setMarca(String marca) {
		this.marca = marca;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	public void setIdCoche(int idCoche) {
		this.idCoche = idCoche;
	}
	
	@Override
	public String toString() {
		return "ID: " +idCoche +" MARCA: " +marca +" MODELO: " +modelo +" COLOR: " +color + " PRECIO: " +precio;
	}
	
	// Devuelve un ojeto de tipo coche
	public static Coche crearCoche ( ) {
		// Marca
		String marca = Leer.pedirCadena("Marca: ");

		// Modelo
		String modelo = Leer.pedirCadena("Modelo: ");

		// Color
		String color = Leer.pedirCadena("Color: ");

		// Precio
		float precio = Leer.pedirFloat("Precio: ");
		
		// Devuelve el nuevo coche
		return new Coche( marca , modelo , color , precio );
		
	}
	
	// Asigna el ID mas grande que hay en la base de datos
	public static void asignarMaxId ( int id ) {
		MAX_ID = id;
	}
	

}
