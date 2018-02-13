package Package1;
/* 	Aplicación que permite realizar operaciones sobre una base de datos MYSQL.
 	La base de datos almacena en una tabla los datos de una Persona: NOMBRE - APELLIDO - EDAD - DNI
 	El usuario puede Añadir, Modificar, Eliminar y Visualizar los datos de la BBDD.
*/
public class MainUsuario {

	public static void main(String[] args) {
		try {
			ProgramaPrincipalJDBC programa = new ProgramaPrincipalJDBC();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-FIN-.");
	}

}
