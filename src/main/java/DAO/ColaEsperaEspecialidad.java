package DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import Estructuras.Cola.Cola;
import Estructuras.Cola.Nodo;

public class ColaEsperaEspecialidad {
    private String especialidad;
    private Cola<String> idsCitasPagadas;

    public ColaEsperaEspecialidad(String especialidad) {
        this.especialidad = especialidad;
        this.idsCitasPagadas = new Cola<>();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Cola<String> getIdsCitasPagadas() {
        return idsCitasPagadas;
    }

    public void setIdsCitasPagadas(Cola<String> idsCitasPagadas) {
        this.idsCitasPagadas = idsCitasPagadas;
    }

    public void cargarColaDesdeArchivo() {
        Path filePath = Paths.get(especialidad + ".txt");
        boolean archivoExiste = Files.exists(filePath);

        if (archivoExiste) {
            System.out.println("Archivo ya existe, cargando datos...");
        } else {
            try {
                Files.createFile(filePath);
                System.out.println("Archivo creado: " + filePath);
            } catch (IOException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No se pudo crear el archivo", e);
                return;
            }
        }

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                idsCitasPagadas.enqueue(linea);
                System.out.println("Cargando cita desde archivo: " + linea);
            }
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al leer el archivo", e);
        }
    }

    public void agregarCitaPagada(String idCita) {
        idsCitasPagadas.enqueue(idCita);
        guardarColaEnArchivo();
    }

    public void guardarColaEnArchivo() {
        String archivo = especialidad + ".txt";
        System.out.println("Preparando para guardar la cola en el archivo: " + archivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            System.out.println("Comenzando a escribir en el archivo.");
            for (String idCita : idsCitasPagadas) {
                System.out.println("Escribiendo cita en el archivo: " + idCita);
                bw.write(idCita);
                bw.newLine();
            }
            System.out.println("Finalizado el guardado de la cola en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al intentar escribir en el archivo: " + archivo);
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ColaEsperaEspecialidad [especialidad=" + especialidad + ", idsCitasPagadas=" + idsCitasPagadas.mostrarCola() + "]";
    }
}
