package Clases;

public class Jugador {
	// Campos
	private String nombre;
	private int nivel;
	private int vida;
	private Personaje tipoPersonaje;
	
	// Constructor
	public Jugador(String nombre, int nivel, int vida, Personaje tipoPersonaje) {
		super();
		this.nombre = nombre;
		this.nivel = nivel;
		this.vida = vida;
		this.tipoPersonaje = tipoPersonaje;
	}
	
	//Getters y Setters
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getVida() {
		return vida;
	}
	public void setVida(int vida) {
		this.vida = vida;
	}
	public Personaje getTipoPersonaje() {
		return tipoPersonaje;
	}
	public void setTipoPersonaje(Personaje tipoPersonaje) {
		this.tipoPersonaje = tipoPersonaje;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre +" LVL: " +nivel +" VIDA: " +vida +" PERSONAJE: " +tipoPersonaje.getRaza();
	}
	
	

}
