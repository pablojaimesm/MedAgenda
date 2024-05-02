package Modelos;

public class Pago {
    private String id;
    private double monto;
    private Paciente paciente;

    public Pago(String id, double monto, Paciente paciente) {
        this.id = id;
        this.monto = monto;
        this.paciente = paciente;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

    
}
