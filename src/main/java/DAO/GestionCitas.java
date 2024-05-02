package DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import Estructuras.Lista_Doblemente_Enlazada.EspecialidadSecuenciaPair;
import Estructuras.Lista_Doblemente_Enlazada.ListaDobleEnlazada;
import Estructuras.Lista_Doblemente_Enlazada.ListaDobleEnlazadaOperations;
import Estructuras.Lista_Simple.ListaSimple;
import Estructuras.Lista_Simple.Nodo;
import Modelos.Cita;
import Modelos.Medico;
import Modelos.Paciente;

public class GestionCitas implements ListaDobleEnlazadaOperations<EspecialidadSecuenciaPair> {

    private ListaDobleEnlazada<EspecialidadSecuenciaPair> secuenciaPorEspecialidad;
    private static final String RUTA_ARCHIVO_CITAS = "citas.txt";
    private static ListaSimple<Cita> listaCitas = new ListaSimple<>();


    
    
    public static ListaSimple<Cita> getListaCitas() {
		return listaCitas;
	}

	public static void setListaCitas(ListaSimple<Cita> listaCitas) {
		GestionCitas.listaCitas = listaCitas;
	}

	private int contadorIdCita ;
    

	public GestionCitas() {
	    listaCitas = new ListaSimple<>();
	    secuenciaPorEspecialidad = new ListaDobleEnlazada<>();
	    contadorIdCita = cargarUltimoIdCita(); 
	    try {
	        cargarCitasDesdeArchivo();
	        System.out.println("Citas cargadas desde el archivo:");
	        Nodo<Cita> actual = listaCitas.getHead();
	        while (actual != null) {
	            System.out.println(actual.getValue().toString());
	            actual = actual.getNext();
	        }
	    } catch (NullPointerException e) {
	        System.out.println("El archivo de citas está vacío. No hay citas para mostrar.");
	    }
	}


    @Override
    public void addToFront(EspecialidadSecuenciaPair pair) {
        secuenciaPorEspecialidad.addToFront(pair);
    }

    @Override
    public void addToEnd(EspecialidadSecuenciaPair pair) {
        secuenciaPorEspecialidad.addToEnd(pair);
    }

    @Override
    public void insertInMiddle(EspecialidadSecuenciaPair pair) {
        secuenciaPorEspecialidad.insertInMiddle(pair);
    }

    @Override
    public void removeFromFront() {
        secuenciaPorEspecialidad.removeFromFront();
    }

    @Override
    public void removeFromEnd() {
        secuenciaPorEspecialidad.removeFromEnd();
    }

    @Override
    public void removeFromMiddle(EspecialidadSecuenciaPair pair) {
        secuenciaPorEspecialidad.removeFromMiddle(pair);
    }

    @Override
    public int searchElement(EspecialidadSecuenciaPair pair) {
        return secuenciaPorEspecialidad.searchElement(pair);
    }

    @Override
    public int getSize() {
        return secuenciaPorEspecialidad.getSize();
    }

    @Override
    public boolean isEmpty() {
        return secuenciaPorEspecialidad.isEmpty();
    }

    @Override
    public String displayList() {
        return secuenciaPorEspecialidad.displayList();
    }

    @Override
    public EspecialidadSecuenciaPair get(int index) throws IndexOutOfBoundsException {
        return secuenciaPorEspecialidad.get(index);
    }
    
    private int cargarUltimoIdCita() {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO_CITAS))) {
            String linea;
            int ultimoId = 0;
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) { // Verificar si la línea no está vacía
                    String[] partes = linea.split(", ");
                    if (partes.length >= 1) {
                        int id = Integer.parseInt(partes[0]);
                        if (id > ultimoId) {
                            ultimoId = id;
                        }
                    }
                }
            }
            return ultimoId + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }



    public Cita registrarCita(String especialidad, String motivo, Medico medico, Paciente paciente, LocalDate fecha, LocalTime hora) {
        String ticket = generarTicket(especialidad, LocalDateTime.of(fecha, hora));
        double valorConsulta = asignarValorConsulta(especialidad);
        System.out.println("Registrando cita:");
        System.out.println("Especialidad: " + especialidad);
        System.out.println("Motivo: " + motivo);
        System.out.println("Medico: " + medico.toString());
        System.out.println("Paciente: " + paciente.toString());
        System.out.println("Fecha: " + fecha.toString());
        System.out.println("Hora: " + hora.toString());
        contadorIdCita++;
        Cita cita = new Cita(String.valueOf(contadorIdCita), fecha.toString(), hora.toString(), medico, paciente, especialidad, motivo, ticket, valorConsulta, false);
        System.out.println("Cita creada: " + cita.toString());
        incrementarSecuenciaPorEspecialidad(especialidad);
        getListaCitas().append(cita); 
        return cita;
    }



    private String generarTicket(String especialidad, LocalDateTime fechaHora) {
        int secuencia = obtenerSecuenciaPorEspecialidad(especialidad);
        String inicialesEspecialidad = obtenerInicialesEspecialidad(especialidad);
        String fechaHoraString = fechaHora.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        return inicialesEspecialidad + fechaHoraString + String.format("%03d", secuencia);
    }

    private String obtenerInicialesEspecialidad(String especialidad) {
        String[] palabras = especialidad.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String palabra : palabras) {
            iniciales.append(palabra.charAt(0));
        }
        return iniciales.toString().toUpperCase();
    }



    private void incrementarSecuenciaPorEspecialidad(String especialidad) {
        int secuencia = obtenerSecuenciaPorEspecialidad(especialidad);
        secuenciaPorEspecialidad.removeFromFront(); 
        secuenciaPorEspecialidad.addToFront(new EspecialidadSecuenciaPair(especialidad, secuencia + 1)); 
    }

    private int obtenerSecuenciaPorEspecialidad(String especialidad) {
        try (BufferedReader br = new BufferedReader(new FileReader("medicos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 4 && partes[3].equals(especialidad)) {
                    return Integer.parseInt(partes[0]); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1; 
    }
    
    
    private double asignarValorConsulta(String especialidad) {
        try (BufferedReader br = new BufferedReader(new FileReader("medicos.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 4 && partes[3].equals(especialidad)) {
                    if (especialidad.equals("Medicina General")) {
                        return 50; 
                    } else {
                        return 100; 
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    
    
    public void guardarCitasEnArchivo() {
        System.out.println("Guardando citas en el archivo...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO_CITAS, true))) {
            Cita nuevaCita = getListaCitas().getTail().getValue();
            System.out.println("Guardando cita en archivo: " + nuevaCita.toString());
            writer.write(nuevaCita.getId() + ", ");
            writer.write(nuevaCita.getFecha() + ", ");
            writer.write(nuevaCita.getHora() + ", ");
            writer.write(nuevaCita.getMedicoAsignado().getId() + ", ");
            writer.write(nuevaCita.getPaciente().getIdentificacion() + ", ");
            writer.write(nuevaCita.getEspecialidad() + ", ");
            writer.write(nuevaCita.getMotivo() + ", ");
            writer.write(nuevaCita.getTicket() + ", ");
            writer.write(nuevaCita.getValorConsulta() + ", ");
            writer.write(nuevaCita.isPagado() ? "Pagado" : "No Pagado");
            writer.newLine();
            System.out.println("Cita guardada correctamente en el archivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void cargarCitasDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO_CITAS))) {
            String linea;
            boolean citasCargadas = false; // Bandera para verificar si se cargaron citas
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 10) { // Verificar si hay al menos 10 partes
                    String id = partes[0];
                    String fecha = partes[1];
                    String hora = partes[2];
                    String medicoId = partes[3];
                    String pacienteIdentificacion = partes[4];
                    String especialidad = partes[5];
                    String motivo = partes[6];
                    String ticket = partes[7];
                    double valorConsulta = Double.parseDouble(partes[8].replace(',', '.'));
                    boolean pagado = partes[9].equals("Pagado");
                    System.out.println("Línea leída del archivo: " + linea);
                    System.out.println("ID: " + id);
                    System.out.println("Fecha: " + fecha);
                    System.out.println("Hora: " + hora);
                    System.out.println("Medico ID: " + medicoId);
                    System.out.println("Paciente Identificación: " + pacienteIdentificacion);
                    System.out.println("Especialidad: " + especialidad);
                    System.out.println("Motivo: " + motivo);
                    System.out.println("Ticket: " + ticket);
                    System.out.println("Valor Consulta: " + valorConsulta);
                    System.out.println("Pagado: " + pagado);
                    Medico medico = obtenerMedicoPorId(medicoId); 
                    Paciente paciente = obtenerPacientePorIdentificacion(pacienteIdentificacion);
                    Cita cita = new Cita(id, fecha, hora, medico, paciente, especialidad, motivo, ticket, valorConsulta, pagado);
                    getListaCitas().append(cita);
                    System.out.println("Cita agregada a la lista: " + cita);
                    citasCargadas = true; // Indicar que se cargaron citas
                } else {
                    System.out.println("Error al leer la línea del archivo: la línea no tiene el formato esperado.");
                }
            }
            // Verificar si se cargaron citas o si el archivo está vacío
            if (!citasCargadas) {
                System.out.println("El archivo de citas está vacío.");
            } else {
                System.out.println("Citas cargadas desde el archivo.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo de citas no se ha encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    
    
    public static void actualizarArchivoCitas(ListaSimple<Cita> listaCitas) {
        try {
            FileWriter writer = new FileWriter("citas.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            Nodo<Cita> actual = listaCitas.getHead();
            while (actual != null) {
                bufferedWriter.write(actual.getValue().toString());
                bufferedWriter.newLine();
                actual = actual.getNext();
            }
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void actualizarArchivoCitas2(ListaSimple<Cita> listaCitas) {
        try {
            FileWriter writer = new FileWriter("citas.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            Nodo<Cita> actual = listaCitas.getHead();
            while (actual != null) {
                Cita cita = actual.getValue();
                String pagado = cita.isPagado() ? "Pagado" : "No Pagado";
                String linea = String.format("%s, %s, %s, %s, %s, %s, %s, %s, %.2f, %s",
                    cita.getId(), cita.getFecha(), cita.getHora(), cita.getMedicoAsignado().getId(),
                    cita.getPaciente().getIdentificacion(), cita.getEspecialidad(), cita.getMotivo(),
                    cita.getTicket(), cita.getValorConsulta(), pagado);
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
                actual = actual.getNext();
            }

            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void eliminarCitaDelArchivo(String idCita, String nombreArchivo) {
        File archivoEntrada = new File(nombreArchivo);
        File archivoTemporal = new File("temp.txt");

        try (Scanner scanner = new Scanner(archivoEntrada);
             FileWriter escritor = new FileWriter(archivoTemporal)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(", ");
                if (!partes[0].equals(idCita)) {
                    escritor.write(linea + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al intentar eliminar la cita del archivo: " + e.getMessage());
            e.printStackTrace();
        }
        if (archivoEntrada.delete()) {
            if (!archivoTemporal.renameTo(archivoEntrada)) {
                System.err.println("Error al intentar renombrar el archivo temporal.");
            }
        } else {
            System.err.println("Error al intentar eliminar el archivo original.");
        }
    }





    public static Medico obtenerMedicoPorId(String id) {
        String rutaArchivoMedicos = "medicos.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoMedicos))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 1 && partes[0].equals(id)) {
                    return new Medico(partes[0], partes[1], partes[2], partes[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }

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

    public static void marcarComoPagada(String ticket) {
        Cita cita = buscarCitaPorTicket(ticket);
        if (cita != null) {
            cita.setPagado(true); 
            actualizarArchivoCitas2(getListaCitas()); 
            System.out.println("La cita con el ticket " + ticket + " ha sido marcada como pagada.");
        } else {
            System.out.println("No se encontró ninguna cita con el ticket proporcionado.");
        }
    }


    public static Cita buscarCitaPorId(String id) {
        Nodo<Cita> actual = GestionCitas.getListaCitas().getHead();
        while (actual != null) {
            Cita cita = actual.getValue();
            if (cita.getId().equals(id)) {
                return cita;
            }
            actual = actual.getNext();
        }
        return null; 
    }
    
    
    public static Cita buscarCitaPorTicket(String ticket) {
        Nodo<Cita> actual = GestionCitas.getListaCitas().getHead();
        while (actual != null) {
            Cita cita = actual.getValue();
            if (cita.getTicket().equals(ticket)) {
                return cita;
            }
            actual = actual.getNext();
        }
        return null; 
    }






}

