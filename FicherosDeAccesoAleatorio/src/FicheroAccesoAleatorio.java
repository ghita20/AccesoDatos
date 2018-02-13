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
	
	//M�todo para comprobar si un ID de registro existe en el fichero
	public boolean idExiste ( int id ) {
		return id < 1 ? false : (id > numeroRegistros ? false : true);
	}
	
	// Cierra el RandomAccessFile
	protected void cerrar ( ) throws IOException {
		raFichero.close();
	}
	
	// A�ade un nuevo registro con los datos de la Persona
	public void a�adirPersona ( Persona persona ) throws IOException , IllegalArgumentException {
		// A�ade la persona al fichero desde el final de este
		a�adirPersonaDesdePosicion( (int)raFichero.length() , persona );
		// Actualiza la variable con el n�mero de registros existentes
		numeroRegistros = (int)raFichero.length()/BYTES_TOTALES;
	}
	private void a�adirPersonaDesdePosicion ( int posicion , Persona persona ) throws IOException {
		StringBuffer buffer = new StringBuffer();
		if ( persona == null )
			throw new IllegalArgumentException("Persona es null.");
		if ( posicion < 0 || posicion > raFichero.length() ) // Comprueba que la posici�n es v�lida
			throw new IllegalArgumentException("El ID no existe en el fichero.");
		try {
			// Posiciona el puntero en la posici�n
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
	// M�todo publico para modificar los datos de una persona existente en el fichero
	public void modificarPersona ( int id , String nombre , String apellido , int edad , String dni ) throws IllegalArgumentException, IOException{
		if ( !idExiste(id) ) // Comprueba que el id de registro existe
			throw new IllegalArgumentException("El id no existe.");
		Persona personaOriginal = buscarPersona(id);
		// Si los valores de las variables pasadas como par�metro son los valores por defecto 
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
	// M�todo privado que modifica los datos de una persona seg�n su id. 
	// El funcionamiento es casi igual al m�todo 'a�adirPersona', la diferencia es que en vez de empezar por el final empieza por la posici�n del registro a modificar
	private void modificarPersona ( int id , Persona persona ) throws IOException {
		StringBuffer buffer = new StringBuffer();
		if ( !idExiste(id) )
			throw new IllegalAccessError("El id no existe.");
		if ( persona == null )
			throw new IllegalArgumentException("Persona es nulo.");
		int posicion = (id-1) * BYTES_TOTALES;
		
		// Sobreescribe los datos del registro de la Persona con las modificaciones
		a�adirPersonaDesdePosicion(posicion, persona);
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
			// Calcula la posici�n en la que estar� ese id en el fichero
			int posicion = BYTES_TOTALES*(id-1); // (id-1) Ya que los datos empiezan en la primera posici�n no en la �ltima
			if ( posicion>=raFichero.length() )  // Si la posicion es mayor o igual que la longitud el registro no existe
				throw new IllegalArgumentException("ID no existe.");
			
			// Coloca el puntero en la posici�n del ID
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
			throw new EOFException("El fichero est� vac�o.");
		}
		return auxPersona;
	}
	
	// Genera un HashMap ordenado por el ID con el listado de personas que existe en el fichero
	public HashMap<Integer,Persona> listadoPersonas ( ) throws IllegalStateException, IOException{
		HashMap<Integer,Persona> personas = new HashMap<>();
		// Si el numero de registros es 0 el fichero est� vac�o
		if ( numeroRegistros == 0 )
			throw new IllegalStateException("El fichero est� vac�o.");
		
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
		// Comprueba que el id pertenece a alg�n registro
		if ( !idExiste(id) )
			throw new IllegalArgumentException("El ID no existe.");
		
		File auxArchivo = new File("archivoTemp.dat");
		auxArchivo.createNewFile(); // Crea un archivo temporal
		
		// Instancia un nuevo Fichero de Acceso Aleatorio
		FicheroAccesoAleatorio archivoTemp = new FicheroAccesoAleatorio(auxArchivo);
		
		// A�ade los datos de las Personas en orden en el fichero temporal
		for (int i = 1; i <= numeroRegistros; i++) {
			// Se salta a la persona con el ID introducido
			if ( id != i ) {
				Persona auxP = buscarPersona(i);
				archivoTemp.a�adirPersona(auxP);
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
		
		// Instancia la variable raFichero con el fichero actualizado y actualiza el n�mero de registros
		raFichero = new RandomAccessFile(fichero, "rw");
		numeroRegistros = (int)raFichero.length()/BYTES_TOTALES;
		//
	
	}
	
	// M�todo para copiar la informaci�n de un fichero a otro
	private static void copiarTemporalAoriginal ( File fichTemp , File fichOriginal ) throws IOException {
		// Elimina el fichero original y crea un fichero vac�o
		fichOriginal.delete();
		fichOriginal.createNewFile();
		
		// Crea un FileOutputStream para copiar la informaci�n del fichero temporal al fichero original
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
