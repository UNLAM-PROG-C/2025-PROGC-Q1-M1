package PrograConcu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        int duration = Integer.parseInt(args[0]);
        int interval = Integer.parseInt(args[1]);
        int cameraId = 1;

        String[] zones = {
            "Sotano",
            "Atico",
            "Cocina",
            "Dormitorio",
            "Jardin",
            "Mausoleo"
        };

        List<Process> processes = new ArrayList<>();

        for (String zone : zones)
        {
            ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "PrograConcu.SurveillanceCamera",
                String.valueOf(duration),
                String.valueOf(interval),
                zone,
                String.valueOf(cameraId++)
            );

            processBuilder.inheritIO();
            Process process = processBuilder.start();
            processes.add(process);
        }

        for (Process process : processes)
        {
            process.waitFor();
        }

        System.out.println("Ha finalizado la vigilancia...");
    }
}