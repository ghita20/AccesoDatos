import java.io.IOException;
/* 	Aplicación que permite realizar operaciones sobre los datos de un fichero.
 	El fichero almacena por cada registro los datos de una Persona: NOMBRE - APELLIDO - EDAD - DNI
 	El usuario puede Añadir, Modificar, Eliminar y Visualizar datos de los registros.
*/
public class MainUsuario {

	public static void main(String[] args) {
		try {
			ProgramaPrincipal programa = new ProgramaPrincipal();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-FIN-.");
	}

}
