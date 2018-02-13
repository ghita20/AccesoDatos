import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class FicheroAccesoAleatorio {
	// Longitud de los campos nombre , apellido y DNI
	public static final int LONG_NOMBRE = 20, 
			LONG_APELLIDO = 20, 
			LONG_DNI = 9,
			BYTES_TOTALES = 102; // BytesTotales es la cantidad de Bytes que ocupa un registro. nombre40 + apellido40 + edad4 + dni18
	private File fichero;
	private RandomAccessFile raFichero;
	private int numeroRegistros;
	
	public FicheroAccesoAleatorio ( File fichero ) throws FileNotFoundException, IOException {
		if ( !fichero.exists() )
			throw new FileNotFoundException("El fichero no existe.");
		this.fichero = fichero;
		raFichero = new RandomAccessFile(fichero, "rw");
		numeroRegistros = (int)raFichero.length()/BYTES_TOTALES;
	}
	
	//Método para comprobar si un ID de registro existe en el fichero
	public boolean idExiste ( int id ) {
		return id < 1 ? false : (id > numeroRegistros ? false : true);
	}
	
	// Cierra el RandomAccessFile
	protected void cerrar ( ) throws IOException {
		raFichero.close();
	}
	
	// Añade un nuevo registro con los datos de la Persona
	public void añadirPersona ( Persona persona ) throws IOException , IllegalArgumentException {
		// Añade la persona al fichero desde el final de este
		añadirPersonaDesdePosicion( (int)raFichero.length() , persona );
		// Actualiza la variable con el número de registros existentes
		numeroRegistros = (int)raFichero.length()/BYTES_TOTALES;
	}
	private void añadirPersonaDesdePosicion ( int posicion , Persona persona ) throws IOException {
		StringBuffer buffer = new StringBuffer();
		if ( persona == null )
			throw new IllegalArgumentException("Persona es null.");
		if ( posicion < 0 || posicion > raFichero.length() ) // Comprueba que la posición es válida
			throw new IllegalArgumentException("El ID no existe en el fichero.");
		try {
			// Posiciona el puntero en la posición
			raFichero.seek( posicion );
			
			// Nombre
			buffer = new StringBuffer( persona.getNombre() );
			buffer.setLength( LONG_NOMBRE );
			raFichero.writeChars( buffer.toString() );
			
			// Apellido
			buffer = new StringBuffer( persona.getApellido() );
			buffer.setLength( LONG_APELLIDO );
			raFichero.writeChars( buffer.toString() );
			
			// Edad
			raFichero.writeInt( persona.getEdad() );
			
			// DNI
			raFichero.writeChars( persona.getDni() );
			
		} catch (IOException e) {
			throw new IOException("Ha ocurrido un error al escribir en el fichero.");
		}
	}
	// Método publico para modificar los datos de una persona existente en el fichero
	public void modificarPersona ( int id , String nombre , String apellido , int edad , String dni ) throws IllegalArgumentException, IOException{
		if ( !idExiste(id) ) // Comprueba que el id de registro existe
			throw new IllegalArgumentException("El id no existe.");
		Persona personaOriginal = buscarPersona(id);
		// Si los valores de las variables pasadas como parámetro son los valores por defecto 
		// significa que no se quiere modificar dichos valores, por tanto les asigna el valor que ya tienen en el registro
		if ( nombre == null )
			nombre = personaOriginal.getNombre();
		if ( apellido == null )
			apellido = personaOriginal.getApellido();
		if ( edad == -1 )
			edad = personaOriginal.getEdad();
		if ( dni == null )
			dni = personaOriginal.getDni();
		modificarPersona(id,new Persona(nombre,apellido,edad,dni));
	}
	// Método privado que modifica los datos de una persona según su id. 
	// El funcionamiento es casi igual al método 'añadirPersona', la diferencia es que en vez de empezar por el final empieza por la posición del registro a modificar
	private void modificarPersona ( int id , Persona persona ) throws IOException {
		StringBuffer buffer = new StringBuffer();
		if ( !idExiste(id) )
			throw new IllegalAccessError("El id no existe.");
		if ( persona == null )
			throw new IllegalArgumentException("Persona es nulo.");
		int posicion = (id-1) * BYTES_TOTALES;
		
		// Sobreescribe los datos del registro de la Persona con las modificaciones
		añadirPersonaDesdePosicion(posicion, persona);
	}
	
	public Persona buscarPersona ( int id ) throws FileNotFoundException , EOFException , IllegalArgumentException, IOException {
		Persona auxPersona = null;
		// Datos de la persona
		String nombre , apellido, dni;
		int edad;
		// 
		if ( !idExiste(id)  )
			throw new IllegalArgumentException("ID no existe.");
		try {
			// Calcula la posición en la que estará ese id en el fichero
			int posicion = BYTES_TOTALES*(id-1); // (id-1) Ya que los datos empiezan en la primera posición no en la última
			if ( posicion>=raFichero.length() )  // Si la posicion es mayor o igual que la longitud el registro no existe
				throw new IllegalArgumentException("ID no existe.");
			
			// Coloca el puntero en la posición del ID
			raFichero.seek(posicion);
			
			char[] aux = new char[LONG_NOMBRE];
			
			// Lee el nombre
			for (int i = 0; i < aux.length; i++)
				aux[i] = raFichero.readChar();
			nombre = new String(aux).trim();
			// FIN nombre
				
			// Lee el apellido
			aux = new char[LONG_APELLIDO];
			for (int i = 0; i < aux.length; i++)
				aux[i] = raFichero.readChar();
			apellido = new String(aux).trim();
			// FIN apellido
				
			// Edad
			edad = raFichero.readInt();
			
			// DNI
			aux = new char[LONG_DNI];
			for (int i = 0; i < aux.length; i++)
				aux[i] = raFichero.readChar();
			dni = new String(aux).trim();
			// Fin DNI
			
			// Instancia la persona
			auxPersona = new Persona(nombre, apellido, edad, dni);
			
		}catch ( EOFException e ) {
			throw new EOFException("El fichero está vacío.");
		}
		return auxPersona;
	}
	
	// Genera un HashMap ordenado por el ID con el listado de personas que existe en el fichero
	public HashMap<Integer,Persona> listadoPersonas ( ) throws IllegalStateException, IOException{
		HashMap<Integer,Persona> personas = new HashMap<>();
		// Si el numero de registros es 0 el fichero está vacío
		if ( numeroRegistros == 0 )
			throw new IllegalStateException("El fichero está vacío.");
		
		for (int i = 1; i <= numeroRegistros; i++) {
			// Busca la persona y la guarda en el HashMap
			Persona auxP = buscarPersona(i);
			personas.put(i, auxP);
			//
		}
		return personas;
	}
	
	// Elimina el registro de una persona 
	public void eliminarPersona ( int id ) throws IOException, IllegalArgumentException {
		// Comprueba que el id pertenece a algún registro
		if ( !idExiste(id) )
			throw new IllegalArgumentException("El ID no existe.");
		
		File auxArchivo = new File("archivoTemp.dat");
		auxArchivo.createNewFile(); // Crea un archivo temporal
		
		// Instancia un nuevo Fichero de Acceso Aleatorio
		FicheroAccesoAleatorio archivoTemp = new FicheroAccesoAleatorio(auxArchivo);
		
		// Añade los datos de las Personas en orden en el fichero temporal
		for (int i = 1; i <= numeroRegistros; i++) {
			// Se salta a la persona con el ID introducido
			if ( id != i ) {
				Persona auxP = buscarPersona(i);
				archivoTemp.añadirPersona(auxP);
			}
		}
		// Cierra el fichero temporal
		archivoTemp.cerrar();
		
		this.cerrar(); // Cierra el RandomAcces instanciado con el fichero actual para poder asignarle el fichero con los datos nuevos.
		
		// Copia la informacion del fichero temporal al fichero principal
		copiarTemporalAoriginal(auxArchivo, fichero);
		auxArchivo.delete(); // Borra el archivo temporal
		//
		
		archivoTemp.cerrar(); // Cierra el Fichero de Acceso Aleatorio temporal creado anteriormente
		
		// Instancia la variable raFichero con el fichero actualizado y actualiza el número de registros
		raFichero = new RandomAccessFile(fichero, "rw");
		numeroRegistros = (int)raFichero.length()/BYTES_TOTALES;
		//
	
	}
	
	// Método para copiar la información de un fichero a otro
	private static void copiarTemporalAoriginal ( File fichTemp , File fichOriginal ) throws IOException {
		// Elimina el fichero original y crea un fichero vacío
		fichOriginal.delete();
		fichOriginal.createNewFile();
		
		// Crea un FileOutputStream para copiar la información del fichero temporal al fichero original
		FileOutputStream fileOut = new FileOutputStream(fichOriginal); 
		FileInputStream fileIn = new FileInputStream(fichTemp); 
		int c;
		while ( ( c = fileIn.read() )!=-1 ) // Lee del fichero temporal
			fileOut.write(c); // Escribe en el fichero Original
		fileIn.close();
		fileOut.close();
		//
		
	}

}
