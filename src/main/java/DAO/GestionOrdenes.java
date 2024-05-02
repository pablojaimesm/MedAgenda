package DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Modelos.Paciente;

public class GestionOrdenes {

	
	public static Paciente obtenerPacientePorIdentificacion(String identificacion) {
        String rutaArchivoPacientes = "pacientes.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoPacientes))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 1 && partes[3].equals(identificacion)) {
                    return new Paciente(partes[0], partes[1], Integer.parseInt(partes[2]), partes[3], partes[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }
}
