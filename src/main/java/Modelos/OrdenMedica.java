package Modelos;

public class OrdenMedica implements Comparable<OrdenMedica>{
    private int id;
    private Paciente paciente;
    private String especialidad;
    private String tipoExamen;
    private String descripcion;
    private double valorExamen;
    private boolean autorizada;
    
    public OrdenMedica(int id, Paciente paciente, String especialidad, String tipoExamen, String descripcion,
            double valorExamen, boolean autorizada) {
        super();
        this.id = id;
        this.paciente = paciente;
        this.especialidad = especialidad;
        this.tipoExamen = tipoExamen;
        this.descripcion = descripcion;
        this.valorExamen = valorExamen;
        this.autorizada = autorizada;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getTipoExamen() {
        return tipoExamen;
    }
    
    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public double getValorExamen() {
        return valorExamen;
    }
    
    public void setValorExamen(double valorExamen) {
        this.valorExamen = valorExamen;
    }
    
    public boolean isAutorizada() {
        return autorizada;
    }
    
    public void setAutorizada(boolean autorizada) {
        this.autorizada = autorizada;
    }
    
    @Override
    public String toString() {
        return "OrdenMedica [id=" + id + ", paciente=" + paciente + ", especialidad=" + especialidad + ", tipoExamen="
                + tipoExamen + ", descripcion=" + descripcion + ", valorExamen=" + valorExamen + ", autorizada="
                + autorizada + "]";
    }
    
    @Override
    public int compareTo(OrdenMedica otraOrden) {
        return Integer.compare(this.id, otraOrden.id);
    }
}
