package JAXB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Clases.Articulo;
import Clases.Cliente;

public class GestionArticulos {
	private String nombreFichero; // Nombre del fichero
	private RegistrosArticulos registros; // Registros con todos los clientes del fichero XML
	
	// Constructor
	public GestionArticulos( String nombreFichero) throws JAXBException, IOException {
		this.nombreFichero = nombreFichero;
		// Lee los registros del fichero si existe
		registros = leerRegistrosDesdeXml();
	}
	
	public void agregarArticulo( Articulo articulo ) throws JAXBException, IOException  {
		if ( articulo == null )
			throw new IllegalArgumentException("Articulo es null.");
		// Añade el cliente a RegistrosClientes
		registros.anadirArticulo(articulo);
		// Guarda los cambios en el XML
		guardarEnXml(registros);
	}
	
	// Visualiza los datos de los clientes que tienen el nombre pasado como argumento
	public String visualizarDatosCliente ( String nombre ) {
		String salida = "";
		List<Articulo> articulos = registros.buscarPorNombre(nombre);
		for ( Articulo c : articulos )
			salida += "\n" +c;
		// Si no existen clientes devuelve este mensaje
		if ( salida.equals("") )
			salida = "No existen articulos.";
		return salida;
	}

	// Genera un objeto de tipo Registros apartir de un XML
	private RegistrosArticulos leerRegistrosDesdeXml ( ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(RegistrosArticulos.class);
		// Crea un unmarshaller ( para leer )
		Unmarshaller unmarshaller = context.createUnmarshaller();
		File fichero = new File(nombreFichero);
		// Crea el fichero si no existe
		if ( !fichero.exists() ) {
			RegistrosArticulos auxR = new RegistrosArticulos();
			guardarEnXml(auxR); // Lo guarda
			return auxR; // Devuelve el objeto RegistrosClientes
		}
		// Devuelve un objeto con todos los datos
		RegistrosArticulos reg =  (RegistrosArticulos)unmarshaller.unmarshal(new InputStreamReader( new FileInputStream(fichero) ) );
		return reg;
	}

	// Escribe toda la informac�n en el xml
	private void guardarEnXml ( RegistrosArticulos auxR ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(RegistrosArticulos.class);
		// Crea un marshall
		Marshaller marshall = context.createMarshaller();
		// Asigna el formato XML
		marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		// Graba los datos en un fichero xml
		marshall.marshal(auxR, new FileWriter( new File(nombreFichero)));
	}

}
