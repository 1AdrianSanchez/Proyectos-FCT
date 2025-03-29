package Model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class BorrarBackups {
    private static final String DIRECTORIO = "C:\\Users\\adrian.sanchez\\Desktop\\Prueba\\CopiasTecnicos";
    private static final String ARCHIVO_GUARDADO = "C:\\Users\\adrian.sanchez\\Desktop\\Prueba\\backups_borrados_log.txt";

//    private static final String DIRECTORIO = "F:\\DirBaseCopiasManuales";
//    private static final String ARCHIVO_GUARDADO = "F:\\backups_borrados_log.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int antiguedad;
        File directorio = new File(DIRECTORIO);
        File archivoLog = new File(ARCHIVO_GUARDADO);

        System.out.println("Usando el directorio: "+DIRECTORIO);

        //Preguntar al usuario para saber a partir de que fecha borrar
        System.out.println("¿A partir de cuantos dias de antiguedad desea borrar los backups?");
        antiguedad = sc.nextInt();

        //Verificar si la carpeta existe
        if (!directorio.exists() || !directorio.isDirectory()) {
            System.out.println("El directorio no existe");
            return;
        }

        //Asegurarse de que que se crea el txt
        try {
            if (!archivoLog.exists()) {
                archivoLog.createNewFile();
                System.out.println("Archivo txt creado");
            }
        } catch (IOException e) {
            System.out.println("Error al crear el txt");
        }

        //Obtener fecha limite
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - antiguedad);
        Date umbral = calendar.getTime();

        //Obtener los archivos en el directorio
        File[] subdirectorios = directorio.listFiles();
        if (subdirectorios == null || subdirectorios.length == 0) {
            System.out.println("No existen subdirectorios en el directorio: "+directorio);
            return;
        }

        //Filtro para eliminar los archivos antiguos (anteriores a la fecha proporcionada)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoLog, true))) {

            //Busco dentro de los subdirectorios de F:\DirBaseCopiasManuales
            for (File subdirectorio : subdirectorios) {

                if (subdirectorio.isDirectory()) {

                    //Obtengo los archivos de los subdirectorios
                    File[] archivos = subdirectorio.listFiles((dir, name) -> name.toLowerCase().endsWith(".7za") || name.toLowerCase().endsWith(".bak"));

                    if (archivos != null && archivos.length > 0) {

                        for (File file : archivos) {

                            //Escribo en el txt los archivos borrados
                            if (file.lastModified() < umbral.getTime()) {
                                file.delete();
                                System.out.println("Archivo: "+file.getName()+" eliminado");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String fecha = sdf.format(new Date());
                                bw.write("Archivo eliminado: " + file.getName() + " Fecha de borrado: " + fecha);
                                bw.newLine();
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo txt");
        }

        System.out.println("Fin");

    }
}
