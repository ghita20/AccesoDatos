package cliente;

public class Main {
	// Programa Principal
	public static void main(String[] args) {
		try {
			GestionEnMemoria programaPrincipal = new GestionEnMemoria();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}
}
