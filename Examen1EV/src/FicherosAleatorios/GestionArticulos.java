package FicherosAleatorios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import Clases.Articulo;
import Clases.Cliente;

public class GestionArticulos {
	// Longitud de los campos nombre , DNI, teléfono y email
	public static final int
		BYTES_ID = 4,
		BYTES_NOMBRE = Articulo.LONG_NOMBRE*2, // Cada carácter ocupa 2 bytes
		BYTES_DESCRIPCION = Articulo.LONG_DESCRIPCION*2,
		BYTES_PRECIO = 8,
		BYTES_STOCK = 4,
		BYTES_TOTALES = BYTES_ID + BYTES_NOMBRE + BYTES_DESCRIPCION + BYTES_PRECIO + BYTES_STOCK; // Cantidad de Bytes que ocupa un registro
	
	private String nombreFichero; // Nombre del fichero
	private File fichero; // Fichero
	private RandomAccessFile raFichero; // Fichero de acceso aleatorio
	
	// Constructor
	public GestionArticulos(String nombreFichero) throws IOException {
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
	public void agregarArticulo ( Articulo articulo ) throws IOException {
		if ( articulo == null )
			throw new IllegalArgumentException("Articulo es null.");
		// Agrega los datos al fichero
		try {
			StringBuffer buffer = new StringBuffer();
			// Posiciona el puntero en la posicion final
			raFichero.seek( raFichero.length() );

			// Escribe los datos en el fichero
			
			// ID articulo
			raFichero.writeInt(articulo.getId());
			
			// Nombre
			buffer = new StringBuffer( articulo.getNombre() );
			buffer.setLength( Articulo.LONG_NOMBRE );
			raFichero.writeChars( buffer.toString() );
			
			// Descripción
			buffer = new StringBuffer( articulo.getDescripcion() );
			buffer.setLength( Articulo.LONG_DESCRIPCION );
			raFichero.writeChars( buffer.toString() );
			
			// Precio
			raFichero.writeDouble( articulo.getPrecio() );
			
			// Stock
			raFichero.writeInt( articulo.getStock() );

		}catch ( IOException e ) {
			throw new IOException("Ha ocurrido un error al escribir en el fichero.");
		}
	}
	
	// Cierra el RandomAccessFile
	public void close() throws IOException {
		raFichero.close();
	}
}
