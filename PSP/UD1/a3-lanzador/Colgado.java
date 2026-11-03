import java.io.File;

/*
PASO 6: REPRODUCCIÓN DEL INTERBLOQUEO

Se ha ejecutado un listado recursivo del directorio C:\Windows
sin leer la salida del proceso hijo.

El proceso padre ha mostrado el PID del hijo y el mensaje
"Esperando a que termine...", pero no ha terminado durante
los 15 segundos de observación.

Finalmente, se ha detenido la ejecución manualmente con Ctrl + C.

Este comportamiento es compatible con un interbloqueo: el proceso
hijo puede quedarse bloqueado al llenarse el búfer de salida,
mientras el proceso padre espera a que termine mediante waitFor().
*/

public class Colgado {
    public static void main(String[] args) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", "dir", "/s", "C:\\Windows"
        );

        pb.directory(new File(System.getProperty("user.dir")));

        Process proceso = pb.start();

        System.out.println("PID del hijo: " + proceso.pid());
        System.out.println("Esperando a que termine...");

        int codigo = proceso.waitFor();

        System.out.println("Código: " + codigo);
    }
}
