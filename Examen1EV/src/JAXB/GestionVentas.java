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
import Clases.Venta;

public class GestionVentas {
	private String nombreFichero; // Nombre del fichero
	private RegistrosVentas registros; // Registros con todos los clientes del fichero XML
	
	// Constructor
	public GestionVentas( String nombreFichero) throws JAXBException, IOException {
		this.nombreFichero = nombreFichero;
		// Lee los registros del fichero si existe
		registros = leerRegistrosDesdeXml();
	}
	
	public void agregarVenta( Venta venta ) throws JAXBException, IOException  {
		if ( venta == null )
			throw new IllegalArgumentException("Venta es null.");
		// Añade la venta al Registro
		registros.anadirVenta(venta);
		// Guarda los cambios en el XML
		guardarEnXml(registros);
	}


	// Genera un objeto de tipo Registros apartir de un XML
	private RegistrosVentas leerRegistrosDesdeXml ( ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(RegistrosVentas.class);
		// Crea un unmarshaller ( para leer )
		Unmarshaller unmarshaller = context.createUnmarshaller();
		File fichero = new File(nombreFichero);
		// Crea el fichero si no existe
		if ( !fichero.exists() ) {
			RegistrosVentas auxR = new RegistrosVentas();
			guardarEnXml(auxR); // Lo guarda
			return auxR; // Devuelve el objeto
		}
		// Devuelve un objeto con todos los datos
		RegistrosVentas reg =  (RegistrosVentas)unmarshaller.unmarshal(new InputStreamReader( new FileInputStream(fichero) ) );
		return reg;
	}

	// Escribe toda la informac�n en el xml
	private void guardarEnXml ( RegistrosVentas auxR ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(RegistrosVentas.class);
		// Crea un marshall
		Marshaller marshall = context.createMarshaller();
		// Asigna el formato XML
		marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		// Graba los datos de la librer�a en un fichero xml
		marshall.marshal(auxR, new FileWriter( new File(nombreFichero)));
	}

}
