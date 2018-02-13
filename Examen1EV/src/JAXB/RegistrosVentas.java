package JAXB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import Clases.Venta;

@XmlRootElement( name = "registros" ) 
@XmlType( propOrder = {"ventas"} )
public class RegistrosVentas {
	// ArrayList de clientes
	private ArrayList<Venta> ventas;
	
	// Constructor
	public RegistrosVentas() {
		ventas = new ArrayList<>();
	}
	
	 // Setter del ArrayList de clientes
	public void setVentas ( ArrayList<Venta> ventas ) {
		this.ventas = ventas;
	}
	
	// Getter
	@XmlElementWrapper( name = "ventas" )
	@XmlElement( name = "venta")
	public ArrayList<Venta> getVentas ( ) {
		return ventas;
	}
	 // Método para añadir una nueva venta
	public void anadirVenta ( Venta venta )  {
		if ( venta == null )
			throw new IllegalArgumentException("Articulo es null.");
		ventas.add(venta); // A�ade la venta
	}

}
