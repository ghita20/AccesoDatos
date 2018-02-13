package Clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Coche {
	
	// Atributos
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
	
	@Override
	public String toString() {
		return "Coche: {" +"Marca: " +marca +" : " +"Modelo: " +modelo +" : " +"Color: " +color +" : " +"Precio: " +precio +" euros }";
	}
	
	// Devuelve un ojeto de tipo curso
	public static Coche crearCoche ( Scanner teclado ) {
		// Limpia el buffer
		teclado.nextLine();

		// Marca
		System.out.println("Marca: " );
		String marca = teclado.nextLine();

		// Modelo
		System.out.println("Modelo: " );
		String modelo = teclado.nextLine();

		// Color
		System.out.println("Color: " );
		String color = teclado.nextLine();

		// Precio
		try {
			System.out.println("Precio: " );
			float precio = teclado.nextFloat();
			
			// Devuelve el nuevo coche si el precio es correcto
			return new Coche( marca , modelo , color , precio );
		}catch (InputMismatchException e ) {
			// Salto de línea
			teclado.nextLine();
			return null; // Devuelve null
		}
	}
	
	

}
