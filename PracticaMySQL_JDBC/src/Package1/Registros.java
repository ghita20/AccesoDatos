package Package1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement( name = "registros" ) 
@XmlType( propOrder = {"personas"} )
public class Registros {
	private ArrayList<Persona> personas;
	
	public Registros ( ) {
		personas = new ArrayList<>();
	}
	
	public void setPersonas ( ArrayList<Persona> personas ) {
		this.personas = personas;
	}
	@XmlElementWrapper( name = "personas" )
	@XmlElement( name = "persona")
	public ArrayList<Persona> getPersonas ( ) {
		return personas;
	}
	
	public void añadirPersona ( Persona persona ) throws JAXBException, IOException {
		if ( persona == null )
			throw new IllegalArgumentException("Persona es null.");
		personas.add(persona); // Añade la persona
		guardarEnXml(); // Guarda en el fichero xml los datos
	}
	public void eliminarPersona ( int indice ) throws JAXBException, IOException {
		if ( indice > personas.size() || indice <= 0 )
			throw new IllegalArgumentException("Esa persona no existe.");
		personas.remove(indice-1); // Elimina la persona por el índice
		guardarEnXml(); // Guarda los datos
	}
	public Persona buscarPersona ( int indice ) {
		if ( indice > personas.size() || indice <= 0 )
			throw new IllegalArgumentException("Esa persona no existe.");
		return personas.get(indice-1);
	}
	public void modificarPersona ( String nombre , String apellido , int edad , String dni , int indice ) throws Exception{
		if ( indice > personas.size() || indice <= 0 )
			throw new IllegalArgumentException("Esa persona no existe.");
		Persona auxP = buscarPersona(indice);
		if ( nombre == null )
			nombre = auxP.getNombre();
		if ( apellido == null )
			apellido = auxP.getApellido();
		if ( edad == -1 )
			edad = auxP.getEdad();
		if ( dni == null )
			dni = auxP.getDni();
		Persona nuevaPersona = new Persona(nombre,apellido,edad,dni);
		personas.remove(indice-1); // elimina la persona anterior
		personas.add(indice-1,nuevaPersona); // asigna en su lugar la persona con los nuevos datos
	}
	public String imprimirRegistros ( ) {
		String salida = "";
		int i = 1;
		for( Persona p: personas )
			salida += "\t" +i++ +" " +p+"\n";
		if ( salida.equals("") )
			salida = "No existen registros.";
		return salida;
	}
	
	// Escribe toda la informacón en el xml
	public void guardarEnXml ( ) throws JAXBException, IOException {
		// Creamos un contexto
		JAXBContext context = JAXBContext.newInstance(Registros.class);
		// Crea un marshall
		Marshaller marshall = context.createMarshaller();
		
		// Asigna el formato XML
		marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		// Graba los datos de la librería en un fichero xml
		marshall.marshal(this, new FileWriter( new File("misRegistros.xml")));
	}

}
