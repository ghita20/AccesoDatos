package util;

import java.io.*;

public class Leer {

	static public String pedirCadena(String texto) {
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(
				System.in));
		String dato="";
		boolean error = true;
		while (error) {
			try {
				System.out.println(texto);
				dato = dataIn.readLine();
				error=false;
			} catch (IOException e) {
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error = true;
			}
		}
		return dato;

	}
	
	static public int pedirEnteroValidar(String texto) {
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(
				System.in));
		int dato=0;
		boolean error = true;
		while (error) {
			try {
				System.out.println(texto);
				dato = Integer.parseInt(dataIn.readLine());
				error=false;
			} catch (IOException e) {
				return -1;
			} catch(NumberFormatException e){
				return -1;
			}
		}
		return dato;

	}
	
	static public double decimal(String texto) {
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(
				System.in));
		double dato=0;
		boolean error = true;
		while (error) {
			try {
				System.out.println(texto);
				dato = Double.parseDouble(dataIn.readLine());
				error=false;
			} catch (IOException e) {
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error = true;
			} catch(NumberFormatException e){
				System.out.println("El dato introducido no es decimal");
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error=true;
			}
		}
		return dato;

	}
	static public float pedirFloat(String texto) {
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(
				System.in));
		float dato=0;
		boolean error = true;
		while (error) {
			try {
				System.out.println(texto);
				dato = Float.parseFloat(dataIn.readLine());
				error=false;
			} catch (IOException e) {
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error = true;
			} catch(NumberFormatException e){
				System.out.println("El dato introducido no es entero");
				System.out.println("Vuelve a inroducir el dato, por favor: ");
				error=true;
			}
		}
		return dato;

	}
}
