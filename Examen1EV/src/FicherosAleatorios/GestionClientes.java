package FicherosAleatorios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import Clases.Cliente;

public class GestionClientes {
	
	// Longitud de los campos nombre , DNI, teléfono y email
	public static final int
			BYTES_NOMBRE = Cliente.LONG_NOMBRE*2,
			BYTES_DNI = Cliente.LONG_DNI*2,
			BYTES_EMAIL = Cliente.LONG_EMAIL*2,
			BYTES_TELEFONO = 4,
			BYTES_TOTALES = BYTES_NOMBRE + BYTES_DNI + BYTES_EMAIL + BYTES_TELEFONO; // Cantidad de Bytes que ocupa un registro

	private String nombreFichero; // Nombre del fichero
	private File fichero; // Fichero
	private RandomAccessFile raFichero; // Fichero de acceso aleatorio
	
	// Constructor
	public GestionClientes(String nombreFichero) throws IOException {
		// Guarda el nombre del fichero
		this.nombreFichero = nombreFichero;
		// Instancia el fichero
		fichero = new File(nombreFichero);
		// Si el fichero no existe lo crea
		if ( !fichero.exists() ) 
			fichero.createNewFile();
		raFichero = new RandomAccessFile(fichero, "rw");
	}
	
	// Agregar un cliente
	public void agregarCliente ( Cliente cliente ) throws IOException {
		if ( cliente == null )
			throw new IllegalArgumentException("Cliente es null.");
		// Agrega los datos al fichero
		try {
			StringBuffer buffer = new StringBuffer();
			// Posiciona el puntero en la posicion final
			raFichero.seek( raFichero.length() );
			
			// Escribe los datos en el fichero
			// Nombre
			buffer = new StringBuffer( cliente.getNombre() );
			buffer.setLength( Cliente.LONG_NOMBRE );
			raFichero.writeChars( buffer.toString() );
			
			// DNI
			buffer = new StringBuffer( cliente.getDni() );
			buffer.setLength( Cliente.LONG_DNI );
			raFichero.writeChars( buffer.toString() );
			
			// Telefono
			raFichero.writeInt( cliente.getTelefono() );
			
			// Email
			buffer = new StringBuffer( cliente.getEmail() );
			buffer.setLength( Cliente.LONG_EMAIL );
			raFichero.writeChars( buffer.toString() );
			
		}catch ( IOException e ) {
			throw new IOException("Ha ocurrido un error al escribir en el fichero.");
		}
	}
	
	public String visualizarDatosCliente ( String nombre ) throws IOException {
		// Variable para almacenar la salida
		String salida = "";
		// ArrayList para almacenar los clientes que existan con ese nombre ( puede haber más de uno )
		List<Cliente> clientes = new ArrayList<>();
		 // Compruebo que exista al menos un registro
		if ( raFichero.length() < BYTES_TOTALES )
			throw new IllegalArgumentException("No existen registros.");
		// Empieza a leer desde la posicion 0
		int auxPosicion = 0;
		long numeroRegistros = raFichero.length() / BYTES_TOTALES;
		// Leo los registros
		for ( long i = 0 ; i < numeroRegistros ; i++) {
			// Coloca el puntero en la posición 
			raFichero.seek(auxPosicion);
			// Lee el nombre
			char[] aux = new char[Cliente.LONG_NOMBRE];
			for (int j = 0; j < aux.length; j++)
				aux[j] = raFichero.readChar();
			String auxNombre = new String(aux).trim();
			// Aumento la posición ya que acabo de leer el nombre
			auxPosicion += BYTES_NOMBRE;
			// Si el nombre es el mismo, leo el cliente y lo añado al arrayList
			if ( auxNombre.equals(nombre) ) {
				// Coloco el puntero al principio del registro
				raFichero.seek( raFichero.getFilePointer() - BYTES_NOMBRE  );
				auxPosicion -= BYTES_NOMBRE;
				// Añade el cliente
				clientes.add(clienteDesdePosicion(raFichero.getFilePointer()));
				// Añado los bytes totales ya leidos
				auxPosicion += BYTES_TOTALES;
			}else {
				// Me salto los siguiente bytes para poder leer el siguiente registro
				auxPosicion += BYTES_DNI +  BYTES_EMAIL + BYTES_TELEFONO;
			}		
		}
		// Almacena la información de los clientes
		for ( Cliente c : clientes )
			salida += "\n" +c;
		// Devuelve un mensaje por si no existen clientes con ese nombre
		if ( salida.equals("") )
			salida = "No existen clientes con ese nombre.";
		return salida;
	}
	
	private Cliente clienteDesdePosicion ( long posicion ) throws IOException {
		String nombre , dni , email;
		int telefono;
		
		if ( posicion > raFichero.length() || posicion < 0 )
			throw new IllegalArgumentException("La posición no existe en el fichero.");
		// Coloca el puntero en la posición
		raFichero.seek(posicion);
		
		// Lee el nombre
		char[] aux = new char[Cliente.LONG_NOMBRE];
		for (int i = 0; i < aux.length; i++)
			aux[i] = raFichero.readChar();
		nombre = new String(aux).trim();
		
		// Lee el DNI
		aux = new char[Cliente.LONG_DNI];
		for (int i = 0; i < aux.length; i++)
			aux[i] = raFichero.readChar();
		dni = new String(aux).trim();
		
		// Lee el telefono
		telefono = raFichero.readInt();
		
		// Lee el email
		aux = new char[Cliente.LONG_EMAIL];
		for (int i = 0; i < aux.length; i++)
			aux[i] = raFichero.readChar();
		email = new String(aux).trim();

		// Instancia el cliente con los datos leídos
		return new Cliente(nombre,dni,telefono,email);
		
	}

	// Cierra el RandomAccessFile
	public void close() throws IOException {
		raFichero.close();
	}
	

}
