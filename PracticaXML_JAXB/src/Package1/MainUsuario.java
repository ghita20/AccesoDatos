package Package1;
/* 	Aplicación que permite realizar operaciones sobre los datos de un fichero en formato XML.
 	El fichero almacena por cada registro los datos de una Persona: NOMBRE - APELLIDO - EDAD - DNI
 	El usuario puede Añadir, Modificar, Eliminar y Visualizar datos de los registros.
*/
public class MainUsuario {

	public static void main(String[] args) {
		try {
			ProgramaPrincipalJAXB programa = new ProgramaPrincipalJAXB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-FIN-.");
	}

}
