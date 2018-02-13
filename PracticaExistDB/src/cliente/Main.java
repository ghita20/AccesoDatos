package cliente;

public class Main {
	// Programa Principal
	public static void main(String[] args) {
		try {
			GestionPrincipal programaPrincipal = new GestionPrincipal();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}

}
