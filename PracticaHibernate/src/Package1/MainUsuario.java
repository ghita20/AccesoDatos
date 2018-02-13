package Package1;

/* 	Aplicaci�n que permite realizar operaciones sobre una base de datos MYSQL con Hibernate.
 	La base de datos almacena en una tabla los datos de una Persona: NOMBRE - APELLIDO - EDAD - DNI
 	El usuario puede A�adir, Modificar, Eliminar y Visualizar los datos de la BBDD.
*/
public class MainUsuario {

	public static void main(String[] args) {
		try {
			ProgramaPrincipalHibernate programa = new ProgramaPrincipalHibernate();
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
