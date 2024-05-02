package Modelos;

import Estructuras.Cola.Cola;

public class Paciente implements Comparable<Paciente> {
	private String id;
	private String nombre;
    private String apellido;
    private int edad;
    private String identificacion;
    private String telefono;
    private Cola<Cita> citasPendientes; // Usamos tu implementación de Cola

    public Paciente() {
        this.citasPendientes = new Cola<>(); // Inicializamos la cola de citas pendientes
    }
    
    
    
    
    @Override
	public String toString() {
		return "Paciente [ " + nombre + " " + apellido + ", " + identificacion + " ]";
	}

	public Paciente(String nombre, String apellido, int edad, String identificacion, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.identificacion = identificacion;
        this.telefono = telefono;
    }


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public Cola<Cita> getCitasPendientes() {
		return citasPendientes;
	}

	public void setCitasPendientes(Cola<Cita> citasPendientes) {
		this.citasPendientes = citasPendientes;
	}
	
	

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int compareTo(Paciente otroPaciente) {
        return this.nombre.compareTo(otroPaciente.nombre);
    }
    
    // Métodos para manipular la cola de citas pendientes
}