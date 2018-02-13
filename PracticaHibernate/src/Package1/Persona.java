package Package1;
public class Persona {
	private String nombre; // Nombre de la persona
	private String apellido; // Apellido
	private int edad; // Edad
	private String dni; // DNI
	
	// Constructor
	public Persona ( String nombre , String apellido , int edad , String dni ) throws IllegalStateException{
		if ( !nombreApellidoValido(nombre) )
			throw new IllegalStateException("Nombre no v�lido.");
		if ( !nombreApellidoValido(apellido) )
			throw new IllegalStateException("Apellido no v�lido.");
		if ( edad<=0 )
			throw new IllegalStateException("Edad no es v�lida.");
		if ( !dniValido(dni) )
			throw new IllegalStateException("DNI no v�lido.");
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.dni = dni;
	}
	public Persona() {
		// TODO Auto-generated constructor stub
	}
	
	// M�todo para comprobar que el nombre o el apellido es v�lido.
	// Para que sea v�lido s�lo puede tener car�cteres alfab�ticos y un espacio entre cada nombre o apellido.
	protected static boolean nombreApellidoValido ( String cadena ) {
		if ( cadena==null ) return false; // Cadena nula
		if ( cadena.equals("") ) return false; // Cadena vac�a
		if ( cadena.endsWith(" ") || cadena.startsWith(" ") ) return false;  // No puede comenzar o acabar con espacio
		
		for (int i = 0; i < cadena.length(); i++) {
			char c = cadena.charAt(i);
			if ( !Character.isAlphabetic(c) ) {
				// Puede contener un espacio
				if ( c == ' ' ) {
					// Si tiene dos espacio ya no es v�lido.
					// cadena.charAt(i+1) No puede generar NullPointerException ya que no puede haber un espacio como �ltimo caracter.
					if ( cadena.charAt(i+1) == ' ' )  
						return false;
					return true;
				}
				return false;
			}
		}
		return true;
	}
	// DNI v�lido: 8N�meros - 1Letra
	protected static boolean dniValido ( String dni ) {
		if ( dni == null )
			return false;
		if ( dni.length()!=9 )
			return false;
		String auxNumero = dni.substring(0, 8);
		int numero;
		try {
			numero = Integer.parseInt(auxNumero);
		}catch ( NumberFormatException e ) {
			return false;
		}
		if ( !Character.isLetter(dni.charAt(8)) )
			return false;
		return true;
	}
	// Getters y Setters
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getDni ( ) {
		return dni;
	}
	public void setDni ( String dni ) {
		this.dni = dni;
	}
	// Fin getters y setters

	public String toString ( ) {
		return dni +" | " +nombre + " " + apellido + " " +edad +" años.";
	}
	
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if ( ! (obj instanceof Persona) )
			return false;
		Persona p = (Persona)obj;
		return p.dni.equals(dni);
	}
	
//	public static void main ( String[] args ) {
//		Persona per = new Persona("Paco P�l�rez", "perez", 10, 1,1);
//	}
}
