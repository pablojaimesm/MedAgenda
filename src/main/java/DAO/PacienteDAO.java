package DAO;

import java.io.*;
import java.util.Comparator;
import Modelos.Paciente;
import Estructuras.Lista_Simple.ListaSimple;

public class PacienteDAO {

    private static final String FILE_PATH = "pacientes.txt";

    private ListaSimple<Paciente> pacientesLista;

    public PacienteDAO() {
        pacientesLista = new ListaSimple<>();
        cargarDatosDesdeArchivo();
    }

    public void agregarPaciente(Paciente paciente) {
        if (existePacienteConIdentificacion(paciente.getIdentificacion())) {
            System.out.println("Ya existe un paciente con este número de identificación.");
            return;
        }
        pacientesLista.append(paciente);
        guardarDatosEnArchivo();
    }

    public void eliminarPaciente(Paciente paciente) {
        pacientesLista.remove(paciente);
        guardarDatosEnArchivo();
    }

    public ListaSimple<Paciente> obtenerTodosLosPacientes() {
        return pacientesLista;
    }

    private void cargarDatosDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String nombre = parts[0].trim();
                String apellido = parts[1].trim();
                int edad = Integer.parseInt(parts[2].trim());
                String identificacion = parts[3].trim();
                String telefono = parts[4].trim();
                Paciente paciente = new Paciente();
                paciente.setNombre(nombre);
                paciente.setApellido(apellido);
                paciente.setEdad(edad);
                paciente.setIdentificacion(identificacion);
                paciente.setTelefono(telefono);
                pacientesLista.append(paciente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarDatosEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Paciente paciente : pacientesLista) {
                bw.write(paciente.getNombre() + ", " + paciente.getApellido() + ", " + paciente.getEdad() + ", " + paciente.getIdentificacion() + ", " + paciente.getTelefono());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ordenarPacientesPorNombre() {
        pacientesLista.sort(Comparator.comparing(Paciente::getNombre));
        guardarDatosEnArchivo();
    }

    public void ordenarPacientesPorApellido() {
        pacientesLista.sort(Comparator.comparing(Paciente::getApellido));
        guardarDatosEnArchivo();
    }

    public boolean existePacienteConIdentificacion(String identificacion) {
        for (Paciente paciente : pacientesLista) {
            if (paciente.getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }
}
