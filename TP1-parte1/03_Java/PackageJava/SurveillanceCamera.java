package PrograConcu;

public class SurveillanceCamera {
  public static void main(String[] args) throws InterruptedException {
    int duration = Integer.parseInt(args[0]);
    int interval = Integer.parseInt(args[1]);
    String zone = args[2];
    int cameraId = Integer.parseInt(args[3]);

    long startTime = System.currentTimeMillis();
    long runtime = duration * 1000L;
    int paranormalEvents = 0;

    while (System.currentTimeMillis() - startTime < runtime) {
      int probability = (int) (Math.random() * 10) + 1;

      try {
        if (probability <= 3) {
          System.out.println(
              "Camara: " + cameraId + " Zona: " + zone + " Evento: Sin actividad");
        } else if (probability <= 5) {
          System.out.println(
              "Camara: " + cameraId + " Zona: " + zone + " Evento: Movimiento detectado");
          paranormalEvents++;
        } else if (probability <= 7) {
          System.out.println(
              "Camara: " + cameraId + " Zona: " + zone + " Evento: Anomalia termica");
          paranormalEvents++;
        } else if (probability <= 9) {
          System.out.println(
              "Camara: " + cameraId + " Zona: " + zone + " Evento: Ruido detectado");
          paranormalEvents++;
        } else {
          System.out.println(
              "Camara: " + cameraId + " Zona: " + zone + " Evento: Sombra extrania");
          paranormalEvents++;
        }

        Thread.sleep(interval * 1000L);

      } catch (InterruptedException e) {
        System.out.println("Camara interrumpida");
        break;
      }
    }

    System.out.println(
        "La camara " + cameraId + " en la zona " + zone
            + " tuvo " + paranormalEvents + " eventos paranormales");
  }
}