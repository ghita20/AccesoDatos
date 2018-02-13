package Cliente;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import Clases.Conductor;
import Clases.Coche;
import Servidor.Servidor;

// Establece la conexion con neodatis y permite realizar consultas y operaciones sobre la base de datos.
public class ClienteNeodatis {
	// ODB
	private ODB odb;
	
	// Constructor
	public ClienteNeodatis() throws Exception {
		odb = null;
		// Abre la conexion con el servidor
		odb = ODBFactory.openClient(Servidor.HOST, Servidor.PUERTO, Servidor.ALIAS);
		System.out.println("Conexion establecida....");
	}
	
	// Cierra la conexion con el servidor
	public void cerrarConexion ( ) {
		if ( odb != null )
			odb.close(); // Cierra la conexion
	}
	
	// Aï¿½ade un nuevo curso
	public void aniadirCoche ( Coche coche ) {
		// Almacena en la DB
		odb.store(coche);
		odb.commit();
	}
	
	// AÃ±ade nuevo conductor
	public void aniadirConductor ( Conductor conductor ) {
		// Almacena en la DB
		odb.store(conductor);
		odb.commit();
	}

	// Devuelve todos los coches almacenados
	public Objects<Coche> getCoches ( ) {
		// Consulta que devuelve los objetos de tipo Coche
		return odb.getObjects( Coche.class );
	}

	// Devuelve todos los conductores almacenados
	public Objects<Conductor> getConductores ( ) {
		// Consulta que devuelve los objetos de tipo Conductor
		return odb.getObjects( Conductor.class );
	}
	
	// Elimina todos los Coches
	public void borrarTodosLosCoches ( ) {
		// Selecciona todos los coches
		Objects<Coche> cursos = odb.getObjects( Coche.class );
		// Los elimina
		for ( Coche c : cursos )
			odb.delete( c );
		// Guarda los cambios
		odb.commit();
	}

	// Elimina todos los Conductores
	public void borrarTodosLosConductores ( ) {
		// Selecciona todos los conductores
		Objects<Conductor> alumnos = odb.getObjects( Conductor.class );
		// Los elimina
		for ( Conductor a : alumnos )
			odb.delete( a );
		// Guarda los cambios
		odb.commit();
	}

	// Consultas
	public int edadMediaConductores ( ) {
		// Values
		Values valores = odb.getValues( new ValuesCriteriaQuery( Conductor.class )
				.avg("edad", "media_edades"));
		BigDecimal value = (BigDecimal) valores.nextValues().getByAlias("media_edades");
		
		// Devuelve la media
		return (int)value.longValue();
	}
	
	public float precioMedioCoches ( ) {
		// Values
		Values valores = odb.getValues( new ValuesCriteriaQuery( Coche.class )
				.avg("precio", "media_precios"));
		BigDecimal value = (BigDecimal) valores.nextValues().getByAlias("media_precios");
		
		// Devuelve el precio medio
		return value.longValue();
	}
	
	public Coche cocheMasCaro ( ) {
		// Devuelve el precio mÃ¡ximo 
		Values valores = odb.getValues( new ValuesCriteriaQuery( Coche.class )
				.max("precio", "max_precio"));
		BigDecimal value = (BigDecimal) valores.nextValues().getByAlias("max_precio");

		// Query
		ICriterion crLike = Where.equal("precio", value.longValue() );

		// Realiza la consulta y recoge el primer coche
		IQuery query = new CriteriaQuery( Coche.class, crLike);
		Coche coche = (Coche) odb.getObjects(query).getFirst();

		// Devuelve la media
		return coche;
	}
	
	// Devuelve los coches de una marca determinada
	public ArrayList<Coche> cochesMismaMarca ( String marca ) {
		// Criterion
		ICriterion crLike = Where.equal("marca", marca );

		// Consulta 
		IQuery query = new CriteriaQuery( Coche.class, crLike);

		// Crea un Array List vacío
		ArrayList<Coche> auxCoches = new ArrayList<>();
		
		// Recoge los objetos de neodatis
		Objects<Coche> objCoches = odb.getObjects(query);
		
		// Los añade al arrayList
		for ( Coche c : objCoches)
			auxCoches.add(c);
		
		// Devuelve los coches que tienen esa marca
		return auxCoches;
	}
	
	// Devuelve los Conductores que conducen un cierto modelo de coche
	public ArrayList<Conductor> conductoresConducenModeloCoche ( String marca ) {
		// Criterion - Que la marca del coche sea "marca"
		ICriterion crLike = Where.iequal("coche.marca", marca );

		// Consulta 
		IQuery query = new CriteriaQuery( Conductor.class, crLike);

		// Crea un Array List vacío
		ArrayList<Conductor> auxConductores = new ArrayList<>();

		// Recoge los objetos de neodatis
		Objects<Conductor> objConductores = odb.getObjects(query);

		// Los añade al arrayList
		for ( Conductor c : objConductores)
			auxConductores.add(c);

		// Devuelve los conductores
		return auxConductores;
	}

}
