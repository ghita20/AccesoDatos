package Cliente;

public class Main {
	// Programa Principal
	public static void main(String[] args) {
		try {
			GestionMenu programaPrincipal = new GestionMenu();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("\nRecuerda que es necesario tener abierto el servidor neodatis para extablecer la conexión...");
		}
	}

}
