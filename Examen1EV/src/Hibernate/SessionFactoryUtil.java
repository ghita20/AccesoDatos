package Hibernate;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {

	// Factoría que gestiona las sesiones abiertas:
	private static SessionFactory sessionFactory;
	
	static {
		try {
			//Crea la factoria de sesiones a partir del fichero de configuración hibernate.cfg.xml:
			Logger log = Logger.getLogger("org.hibernate");
		    log.setLevel(Level.OFF); 
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}catch (Throwable ex){
			System.err.println("Fallo en la creación de SessionFactory: " + ex);
		}
	}
	
	// obtener la factoría de sesiones
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	// cerrar la factoría de sesiones
	public static void closeSessionFactory() {
		getSessionFactory().close();
	}

}
