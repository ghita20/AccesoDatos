package Cliente;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.neodatis.odb.Objects;

import Clases.Conductor;
import Clases.Coche;

// Clase encargada de gestionar los datos con los que trabajaremos en memoria
public class GestionDatos {
	// Conexion a neodatis
	protected ClienteNeodatis neodatis;
	
	// Coches y conductores
	private ArrayList<Coche> coches;
	private ArrayList<Conductor> conductores;
	
	// Constructor
	public GestionDatos() throws Exception {
		// Abre la conexion
		neodatis = new ClienteNeodatis();
		
		// Coches y conductores
		coches = new ArrayList<>();
		conductores = new ArrayList<>();
		
		// Recoge los datos almacenados en neodatis
		recargarDatos();
	}
	public ArrayList<Coche> getCoches ( ) {
		return coches;
	}
	public ArrayList<Conductor> getConductores ( ) {
		return conductores;
	}
	
	// Elimina los datos almacenados en la memoria y los reemplaza con los datos almacenados en neodatis
	public void recargarDatos ( ) {
		// Elimina los datos almacenados en memoria
		coches.clear();
		conductores.clear();
		
		// Descarga los Coches
		Objects<Coche> auxCoches = neodatis.getCoches();
		for ( Coche c : auxCoches )
			coches.add(c);
		
		// Descarga los Conductores
		Objects<Conductor> auxConductores = neodatis.getConductores();
		for ( Conductor a : auxConductores )
			conductores.add(a);
	}

	// Elimina los datos almacenados en neodatis y los reemplaza con los que tenemos en memoria
	public void guardarCambios ( ) {
		// Elimina los coches y conductores existentes
		neodatis.borrarTodosLosCoches();
		neodatis.borrarTodosLosConductores();
		
		// Almacena los objetos que tenemos en memoria
		for ( Coche c : coches )
			neodatis.aniadirCoche(c);
		for ( Conductor a : conductores )
			neodatis.aniadirConductor(a);
	}
	
	// Muestra todos los datos de los coches que tenemos en memoria
	public void imprimirDatosCoches ( ) {
		System.out.println("\n**********************************************");
		if ( coches.size() == 0 )
			System.out.println("No existen coches.");
		for ( Coche c : coches ) {
			// Imprime datos del coche
			System.out.println( c );
		}
		System.out.println("**********************************************\n");
	}
	
	// Muestra todos los datos de los coches que tenemos en memoria y devuelve el número de coches
	public int imprimirCochesYindice ( ) {
		System.out.println("\n**********************************************");
		if ( coches.size() == 0 )
			System.out.println("No existen coches.");
		int i = 0;
		// Imprime los datos de los coches
		for ( i = 0 ; i < coches.size() ; i++)
			System.out.println("\t" +i +") " +coches.get(i) );
		
		System.out.println("**********************************************\n");
		return i;
	}
	
	// Imprime los conductores existentes
	public void imprimirConductores ( ) {
		System.out.println("\n**********************************************");
		if ( conductores.size() == 0 )
			System.out.println("No existen conductores.");
		int i = 0;
		// Imprime los datos de los conductores
		for ( i = 0 ; i < conductores.size() ; i++)
			System.out.println("\t" +i +") " +conductores.get(i) );
		
		System.out.println("**********************************************\n");
	}

	// Muestra todos los datos de los conductores que tenemos en memoria y devuelve el número de conductores
	public int imprimirConductoresYindice ( ) {
		System.out.println("\n**********************************************");
		if ( conductores.size() == 0 )
			System.out.println("No existen conductores.");
		int i = 0;
		// Imprime los datos de los conductores
		for ( i = 0 ; i < conductores.size() ; i++)
			System.out.println("\t" +i +") " +conductores.get(i) );

		System.out.println("**********************************************\n");
		return i;
	}
}
