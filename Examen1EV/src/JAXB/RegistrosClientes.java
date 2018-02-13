package JAXB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import Clases.Cliente;

@XmlRootElement( name = "registrosClientes" ) 
@XmlType( propOrder = {"clientes"} )
public class RegistrosClientes {
	// ArrayList de clientes
	private ArrayList<Cliente> clientes;
	
	// Constructor
	public RegistrosClientes() {
		clientes = new ArrayList<>();
	}
	
	 // Setter del ArrayList de clientes
	public void setClientes ( ArrayList<Cliente> clientes ) {
		this.clientes = clientes;
	}
	
	// Getter
	@XmlElementWrapper( name = "clientes" )
	@XmlElement( name = "cliente")
	public ArrayList<Cliente> getClientes ( ) {
		return clientes;
	}
	 // Método para añadir un nuevo cliente
	public void anadirCliente ( Cliente cliente )  {
		if ( cliente == null )
			throw new IllegalArgumentException("Cliente es null.");
		clientes.add(cliente); // A�ade el cliente
	}
	
	// Método para buscar clientes que tengan el nombre pasado como argumento
	public List<Cliente> buscarPorNombre ( String nombre ){
		List<Cliente> auxClientes = new ArrayList<>();
		// Busca entre los clientes a aquellos que se llamen como " nombre "
		for( Cliente c : clientes )
			if ( c.getNombre().equals(nombre) )
				auxClientes.add(c);
		return auxClientes;
	}
}
