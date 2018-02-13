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

import Clases.Cliente;

public class GestionClientes {
	private String nombreFichero; // Nombre del fichero
	private RegistrosClientes registros; // Registros con todos los clientes del fichero XML
	
	// Constructor
	public GestionClientes( String nombreFichero) throws JAXBException, IOException {
		this.nombreFichero = nombreFichero;
		// Lee los registros del fichero si existe
		registros = leerRegistrosDesdeXml();
	}
	
	public void agregarCliente( Cliente cliente ) throws JAXBException, IOException  {
		if ( cliente == null )
			throw new IllegalArgumentException("Cliente es null.");
		// Añade el cliente a RegistrosClientes
		registros.anadirCliente(cliente);
		// Guarda los cambios en el XML
		guardarEnXml(registros);
	}
	
	// Visualiza los datos de los clientes que tienen el nombre pasado como argumento
	public String visualizarDatosCliente ( String nombre ) {
		String salida = "";
		List<Cliente> clientes = registros.buscarPorNombre(nombre);
		for ( Cliente c : clientes )
			salida += "\n" +c;
		// Si no existen clientes devuelve este mensaje
		if ( salida.equals("") )
			salida = "No existen clientes.";
		return salida;
				
	}

	// Genera un objeto de tipo Registros apartir de un XML
	private RegistrosClientes leerRegistrosDesdeXml ( ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(RegistrosClientes.class);
		// Crea un unmarshaller ( para leer )
		Unmarshaller unmarshaller = context.createUnmarshaller();
		File fichero = new File(nombreFichero);
		// Crea el fichero si no existe
		if ( !fichero.exists() ) {
			RegistrosClientes auxR = new RegistrosClientes();
			guardarEnXml(auxR); // Lo guarda
			return auxR; // Devuelve el objeto RegistrosClientes
		}
		// Devuelve un objeto con todos los datos
		RegistrosClientes reg =  (RegistrosClientes)unmarshaller.unmarshal(new InputStreamReader( new FileInputStream(fichero) ) );
		return reg;
	}

	// Escribe toda la informac�n en el xml
	private void guardarEnXml ( RegistrosClientes auxR ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(RegistrosClientes.class);
		// Crea un marshall
		Marshaller marshall = context.createMarshaller();
		// Asigna el formato XML
		marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		// Graba los datos de la librer�a en un fichero xml
		marshall.marshal(auxR, new FileWriter( new File(nombreFichero)));
	}

}
