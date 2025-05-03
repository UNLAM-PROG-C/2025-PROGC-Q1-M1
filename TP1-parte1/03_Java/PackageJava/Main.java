package PrograConcu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        int duracion = Integer.parseInt(args[0]);
        int intervalo = Integer.parseInt(args[1]);
        int id = 1;

        String[] zonas = {"Sotano", "Atico", "Cocina", "Dormitorio", "Jardin", "Mausoleo"};

        List<Process> procesos = new ArrayList<>();

        for (String zona : zonas) {
            ProcessBuilder pb = new ProcessBuilder(
                "java", "ProgaConcu.CamaraDeVigilancia",
                String.valueOf(duracion),
                String.valueOf(intervalo),
                zona,
                String.valueOf(id++)
            );

            pb.inheritIO(); // para que el output se vea en consola
            Process p = pb.start();
            procesos.add(p);
        }

        // esperar a que todos los procesos terminen
        for (Process p : procesos) {
            p.waitFor();
        }

        System.out.println("Ha finalizado la vigilancia...");
    }
}