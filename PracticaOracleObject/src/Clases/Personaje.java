package Clases;

public class Personaje {
	// Campos
	private String ref;
	private String raza;
	private String arma;
	private int damage;
	
	// Constructor
	public Personaje(String ref , String nombre, String arma, int damage) {
		super();
		this.ref = ref;
		this.raza = nombre;
		this.arma = arma;
		this.damage = damage;
	}
	public Personaje(String nombre, String arma, int damage) {
		super();
		ref = "";
		this.raza = nombre;
		this.arma = arma;
		this.damage = damage;
	}

	// Getters y Setters
	
	public String getRaza() {
		return raza;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public String getArma() {
		return arma;
	}

	public void setArma(String arma) {
		this.arma = arma;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return raza +" ARMA: " +arma +" DAÑO: " +damage;
	}
	
	

}
