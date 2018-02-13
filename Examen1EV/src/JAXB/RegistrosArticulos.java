package JAXB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import Clases.Articulo;

@XmlRootElement( name = "registros" ) 
@XmlType( propOrder = {"articulos"} )
public class RegistrosArticulos {
	// ArrayList de clientes
	private ArrayList<Articulo> articulos;
	
	// Constructor
	public RegistrosArticulos() {
		articulos = new ArrayList<>();
	}
	
	 // Setter del ArrayList de clientes
	public void setArticulos ( ArrayList<Articulo> articulos ) {
		this.articulos = articulos;
	}
	
	// Getter
	@XmlElementWrapper( name = "articulos" )
	@XmlElement( name = "articulo")
	public ArrayList<Articulo> getArticulos ( ) {
		return articulos;
	}
	 // Método para añadir un nuevo cliente
	public void anadirArticulo ( Articulo articulo )  {
		if ( articulo == null )
			throw new IllegalArgumentException("Articulo es null.");
		articulos.add(articulo); // A�ade el cliente
	}
	
	// Método para buscar clientes que tengan el nombre pasado como argumento
	public List<Articulo> buscarPorNombre ( String nombre ){
		List<Articulo> auxClientes = new ArrayList<>();
		// Busca entre los clientes a aquellos que se llamen como " nombre "
		for( Articulo c : articulos )
			if ( c.getNombre().equals(nombre) )
				auxClientes.add(c);
		return auxClientes;
	}
}
