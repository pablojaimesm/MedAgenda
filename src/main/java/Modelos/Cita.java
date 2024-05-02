package Modelos;

import java.util.Comparator;
import java.util.Objects;

public class Cita implements Comparable<Cita>{
    private String id;
    private String fecha;
    private String hora;
    private Medico medicoAsignado;
    private Paciente paciente;
    private String especialidad;
    private String motivo;
    private String ticket;
    private double valorConsulta;
    private boolean pagado;

    public Cita(String id, String fecha, String hora, Medico medicoAsignado, Paciente paciente,
                String especialidad, String motivo, String ticket, double valorConsulta, boolean pagado) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.medicoAsignado = medicoAsignado;
        this.paciente = paciente;
        this.especialidad = especialidad;
        this.motivo = motivo;
        this.ticket = ticket;
        this.valorConsulta = valorConsulta;
        this.pagado = pagado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public double getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(double valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Medico getMedicoAsignado() {
        return medicoAsignado;
    }

    public void setMedicoAsignado(Medico medicoAsignado) {
        this.medicoAsignado = medicoAsignado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public int compareTo(Cita otraCita) {
        // Ordenar primero por especialidad y luego por fecha y hora
        int comparacion = this.especialidad.compareTo(otraCita.especialidad);
        if (comparacion == 0) {
            comparacion = this.fecha.compareTo(otraCita.fecha);
            if (comparacion == 0) {
                comparacion = this.hora.compareTo(otraCita.hora);
            }
        }
        return comparacion;
    }
    
    
        
   
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita cita = (Cita) obj;
        return Objects.equals(id, cita.id);
        // O cualquier otro campo que determine la igualdad de las citas de manera única.
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
        // Esto es opcional para tu caso actual, pero es una buena práctica implementarlo.
    }

    @Override
    public String toString() {
        return "Cita [id=" + id + ", fecha=" + fecha + ", hora=" + hora + ", medicoAsignado=" + medicoAsignado
                + ", paciente=" + paciente + ", especialidad=" + especialidad + ", motivo=" + motivo + ", ticket="
                + ticket + ", valorConsulta=" + valorConsulta + ", pagado=" + pagado + "]";
    }
    
    
    public static Comparator<Cita> fechaYHoraComparator = new Comparator<Cita>() {
        @Override
        public int compare(Cita cita1, Cita cita2) {
            // Comparar primero por hora y luego por fecha
            System.out.println("Comparando cita1: " + cita1);
            System.out.println("Comparando cita2: " + cita2);
            
            int comparacionHora = cita1.getHora().compareTo(cita2.getHora());
            if (comparacionHora != 0) {
                return comparacionHora; // Si las horas son diferentes, devuelve la comparación de horas
            }
            // Si las horas son iguales, compara por fecha
            int comparacionFecha = cita1.getFecha().compareTo(cita2.getFecha());
            System.out.println("Comparación por hora: " + comparacionHora);
            System.out.println("Comparación por fecha: " + comparacionFecha);
            
            return comparacionFecha;
        }


    };

}
