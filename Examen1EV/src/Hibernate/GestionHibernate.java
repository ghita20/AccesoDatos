package Hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Clases.Articulo;
import Clases.Cliente;
import Clases.Venta;

public class GestionHibernate {
	private Session session; // Session
	private Transaction transaction; // Transacción
	
	// Constructor
	public GestionHibernate() {
		// Crea una nueva sesión
		session = SessionFactoryUtil.getSessionFactory().openSession();
		transaction = null;
	}
	
	// Close
	public void close() { session.close(); }
	
	// Añadir cliente
	public void aniadirCliente ( Cliente cliente ) {
		// Realiza la transacción
		transaction = session.beginTransaction();
		session.save(cliente);
		transaction.commit();
	}
	
	// Añadir venta
	public void aniadirVenta( Venta venta ) {
		// Realiza la transacción
		transaction = session.beginTransaction();
		session.save(venta);
		transaction.commit();
	}
	
	// Añadir articulo
	public void aniadirArticulo( Articulo articulo) {
		// Realiza la transacción
		transaction = session.beginTransaction();
		session.save(articulo);
		transaction.commit();
	}
	
	// Visualizar datos clientes con nombre x
	public String visualizarDatosClientes( String nombre ) {
		String salida = "";
		List<Cliente> result = session.createQuery("from Cliente c where c.nombre like '" +nombre +"'").list();
		for ( Cliente p : result )
			salida += "\n" +p.toString();
		// Devuelve un mensaje si no existen clientes con ese nombre
		if ( salida.equals("") )
			salida = "No existen clientes con ese nombre.";
		return salida;

	}

}
