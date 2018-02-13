package Package1;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DataBase {
	public static final String ERROR_PERSONA_NO_EXISTE = "La persona no existe.";
	public static final String ERROR_PERSONA_EXISTE = "La persona ya existe.";
	
	private Session session;
	private Transaction transaction;
	
	public DataBase() {
		// TODO Auto-generated constructor stub
		session = SessionFactoryUtil.getSessionFactory().openSession();
		transaction = null;
	}
	public void close() {
		// TODO Auto-generated method stub
		session.close();
	}
	
	public void anadirPersona( Persona persona ) {
		// Comprueba si existe una persona con ese dni
		if ( personaExiste(persona.getDni()))
			throw new IllegalArgumentException(ERROR_PERSONA_EXISTE);
		// Realiza la transacción
		transaction = session.beginTransaction();
		session.save(persona);
		transaction.commit();
	}
	
	public void eliminarPersona( String dni ) {
		// TODO Auto-generated method stub
		// Comprueba que la persona exista
		if (!personaExiste(dni))
			throw new IllegalArgumentException(ERROR_PERSONA_NO_EXISTE);
		// Elimina la persona
		transaction = session.beginTransaction();
		session.delete(getPersona(dni));
		transaction.commit();
	}
	
	public void modificarDatosPersona( String dni , String nombre , String apellidos , int edad ) {
		// TODO Auto-generated method stub
		// Comprueba que la persona exista
		if (!personaExiste(dni))
			throw new IllegalArgumentException(ERROR_PERSONA_NO_EXISTE);
		Persona auxP = getPersona(dni);
		if ( nombre != null )
			auxP.setNombre(nombre);
		if ( apellidos != null )
			auxP.setApellido(apellidos);
		if ( edad != -1 )
			auxP.setEdad(edad);
		// Guarda los cambios
		transaction = session.beginTransaction();
		session.update(auxP);
		transaction.commit();

	}
	
	public String mostrarTodasLasPersonas() {
		// TODO Auto-generated method stub
		String salida = "";
		List<Persona> result = session.createQuery("from Persona").list();
		for ( Persona p : result )
			salida += "\n" +p.toString();
		return salida;
	}
	
	public boolean personaExiste ( String dni ) {
		List<Persona> result = session.createQuery("from Persona where dni like '"+dni+"'").list();
		// Comprueba que la persona existe
		if(result.size()==0)
			return false;
		return true;
	}
	
	private Persona getPersona ( String dni ) {
		List<Persona> result = session.createQuery("from Persona where dni like '"+dni+"'").list();
		// Comprueba que la persona existe
		if(result.size()==0)
			throw new IllegalArgumentException(ERROR_PERSONA_NO_EXISTE);
		return result.get(0);
	}
	
	public String datosPersona( String dni ) {
		// TODO Auto-generated method stub
		if (!personaExiste(dni))
			throw new IllegalArgumentException(ERROR_PERSONA_NO_EXISTE);
		return getPersona(dni).toString();
	}

}
