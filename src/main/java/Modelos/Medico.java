package Modelos;



import Estructuras.Lista_Doblemente_Enlazada.ListaDobleEnlazada;
import Modelos.Cita;

public class Medico implements Comparable<Medico>{
    
	@Override
	public String toString() {
		return nombre +" "+ apellido;
	}

	private String id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private ListaDobleEnlazada<Cita> citasProgramadas;

    

    public Medico(String id, String nombre, String apellido, String especialidad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.especialidad = especialidad;
	}

	public Medico() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public ListaDobleEnlazada<Cita> getCitasProgramadas() {
		return citasProgramadas;
	}

	public void setCitasProgramadas(ListaDobleEnlazada<Cita> citasProgramadas) {
		this.citasProgramadas = citasProgramadas;
	}

	   @Override
	    public int compareTo(Medico otroMedico) {
	        // Aquí defines cómo comparar dos médicos entre sí.
	        // Por ejemplo, podrías compararlos por su nombre, su especialidad, etc.
	        // Devuelve un valor negativo si este médico es "menor" que otroMedico,
	        // un valor positivo si es "mayor", y cero si son iguales.
	        return this.nombre.compareTo(otroMedico.getNombre());
	    }
	
	
	public void agregarCita(Cita cita) {
        citasProgramadas.addToEnd(cita);
    }

    public void eliminarCita(Cita cita) {
        citasProgramadas.removeFromMiddle(cita);
    }


}

