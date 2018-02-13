package FicherosAleatorios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import Clases.Articulo;
import Clases.Venta;

public class GestionVentas {
	// Longitud de los campos DNI y idArticulo
	public static final int
	BYTES_ID = 4,
	BYTES_DNI = Venta.LONG_DNI*2, // Cada carácter ocupa 2 bytes
	BYTES_TOTALES = BYTES_ID + BYTES_DNI; // Cantidad de Bytes que ocupa un registro

	private String nombreFichero; // Nombre del fichero
	private File fichero; // Fichero
	private RandomAccessFile raFichero; // Fichero de acceso aleatorio

	// Constructor
	public GestionVentas(String nombreFichero) throws IOException {
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
	public void agregarVenta ( Venta venta ) throws IOException {
		if ( venta == null )
			throw new IllegalArgumentException("Venta es null.");
		// Agrega los datos al fichero
		try {
			StringBuffer buffer = new StringBuffer();
			// Posiciona el puntero en la posicion final
			raFichero.seek( raFichero.length() );

			// Escribe los datos en el fichero

			// ID articulo
			raFichero.writeInt(venta.getId());

			// DNI
			buffer = new StringBuffer( venta.getDni() );
			buffer.setLength( Articulo.LONG_NOMBRE );
			raFichero.writeChars( buffer.toString() );


		}catch ( IOException e ) {
			throw new IOException("Ha ocurrido un error al escribir en el fichero.");
		}
	}
	
	// Cierra el RandomAccessFile
	public void close() throws IOException {
		raFichero.close();
	}
}
