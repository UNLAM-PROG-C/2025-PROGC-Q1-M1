package PrograConcu;

public class CamaraDeVigilancia {
    public static void main(String[] args) throws InterruptedException {
        int duracion = Integer.parseInt(args[0]);
        int intervalo = Integer.parseInt(args[1]);
        String zona = args[2];
        int id = Integer.parseInt(args[3]);

        long inicio = System.currentTimeMillis();
        long tiempo = duracion * 1000L;
        int cantidad_avistamientos = 0;

        while (System.currentTimeMillis() - inicio < tiempo) {
            int probabilidad = (int)(Math.random() * 10) + 1;
            try {
                if(probabilidad <= 3) {
                    System.out.println("Camara: "+id+" Zona: "+ zona+ " Evento: Sin actividad");
                } else if(probabilidad <= 5) {
                    System.out.println("Camara: "+id+" Zona: "+ zona+ " Evento: Movimiento detectado");
                    cantidad_avistamientos++;
                } else if(probabilidad <= 7) {
                    System.out.println("Camara: "+id+" Zona: "+ zona+ " Evento: Anomalia termica");
                    cantidad_avistamientos++;
                } else if(probabilidad <= 9) {
                    System.out.println("Camara: "+id+" Zona: "+ zona+ " Evento: Ruido detectado");
                    cantidad_avistamientos++;
                } else {
                    System.out.println("Camara: "+id+" Zona: "+ zona+ " Evento: Sombra extrania");
                    cantidad_avistamientos++;
                }
                Thread.sleep(intervalo * 1000L);
            } catch (InterruptedException e) {
                System.out.println("Camara interrumpida");
                break;
            }
        }

        System.out.println("La camara "+id+" en la zona "+ zona+ " tuvo "+ cantidad_avistamientos +" avistamientos paranormales");
    }
}