package DAO;

import Modelos.Medico;
import Estructuras.Lista_Simple.ListaSimple;
import Estructuras.Lista_Simple.Nodo;
import java.io.*;
import java.util.Scanner;

public class GestionMedicos {

    private ListaSimple<Medico> listaMedicos;
    private final String archivoMedicos = "medicos.txt";

    public GestionMedicos() {
        listaMedicos = new ListaSimple<>();
        cargarMedicosDesdeArchivo();
    }

    public ListaSimple<Medico> getListaMedicos() {
        return listaMedicos;
    }

    public void agregarMedico(Medico medico) {
        listaMedicos.append(medico);
        guardarMedicosEnArchivo(listaMedicos, archivoMedicos);
    }

    public void eliminarMedico(String idMedico) {
        Nodo<Medico> current = listaMedicos.getHead();
        while (current != null) {
            if (current.getValue().getId().equals(idMedico)) {
                listaMedicos.remove(current.getValue());
                guardarMedicosEnArchivo(listaMedicos, idMedico);
                return;
            }
            current = current.getNext();
        }
    }

    private void cargarMedicosDesdeArchivo() {
        try (Scanner scanner = new Scanner(new File(archivoMedicos))) {
            while (scanner.hasNextLine()) {
                String[] medicoInfo = scanner.nextLine().split(",");
                if (medicoInfo.length == 5) {
                    Medico medico = new Medico();
                    medico.setId(medicoInfo[0].trim());
                    medico.setNombre(medicoInfo[1].trim());
                    medico.setApellido(medicoInfo[2].trim());
                    medico.setEspecialidad(medicoInfo[3].trim());
                    listaMedicos.append(medico);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de médicos no encontrado. Se creará uno nuevo al agregar médicos.");
        }
    }

    private void guardarMedicosEnArchivo(ListaSimple<Medico> listaMedicos, String rutaArchivo) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoMedicos))) {
                Nodo<Medico> current = listaMedicos.getHead();
                while (current != null) {
                    bw.write(current.getValue().getId() + ", " +
                             current.getValue().getNombre() + ", " +
                             current.getValue().getApellido() + ", " +
                             current.getValue().getEspecialidad());
                    bw.newLine();
                    current = current.getNext();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    public static void main(String[] args) {
        GestionMedicos gestionMedicos = new GestionMedicos();
        gestionMedicos.agregarMedico(new Medico("1", "Juan", "Perez", "Pediatra"));
        gestionMedicos.agregarMedico(new Medico("2", "Maria", "Gonzalez", "Cardiologo"));
        gestionMedicos.agregarMedico(new Medico("3", "Carlos", "Lopez", "Dermatologo"));
        gestionMedicos.agregarMedico(new Medico("4", "Carlos", "Perez", "General"));
        gestionMedicos.agregarMedico(new Medico("5", "Julieta", "Torres", "General"));
        System.out.println("Lista de médicos:");
        ListaSimple<Medico> listaMedicos = gestionMedicos.getListaMedicos();
        for (Medico medico : listaMedicos) {
            System.out.println(medico.getId() + ": " + medico.getNombre() + " " + medico.getApellido() +
                               " - Especialidad: " + medico.getEspecialidad());
        }
    }
}

