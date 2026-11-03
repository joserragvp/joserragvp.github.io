
import java.io.File;
import java.util.concurrent.TimeUnit;

/*
PASO 7: SOLUCIÓN DEL INTERBLOQUEO REDIRIGIENDO LA SALIDA

La salida del proceso hijo se guarda en un fichero mediante
redirectOutput(), evitando que se llene la tubería de salida.

RESULTADO DE LA PRUEBA

Se ha ejecutado un listado recursivo de C:\Windows.
El proceso ha terminado con código 0 y la salida se ha guardado
en salida-lanzador.log.

PASO 8: AÑADIR UN LÍMITE DE TIEMPO

Se utiliza waitFor(3, TimeUnit.SECONDS) para esperar como máximo
3 segundos. Si el proceso no termina, se intenta detener con
destroy(). Si no termina en 5 segundos más, se fuerza su cierre
con destroyForcibly().

RESULTADO DE LA PRUEBA

Se ha ejecutado el comando java Calculo.java 2 20000000
con un límite de espera de 3 segundos.

El cálculo no ha terminado dentro del tiempo establecido.
El programa ha intentado detener el proceso y ha mostrado
el mensaje "El hijo no terminó en 3 s: se ha cortado".

Conclusión: waitFor(3, TimeUnit.SECONDS) permite limitar
el tiempo de espera. Si el proceso no termina a tiempo,
se utiliza destroy() para solicitar su finalización y,
si es necesario, destroyForcibly() para forzar el cierre.
 */
public class LanzadorFichero {

    public static void main(String[] args) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "java", "Calculo.java", "2", "20000000"
        );

        pb.directory(new File(System.getProperty("user.dir")));

        File salida = new File("calculo-tiempo.log");

        pb.redirectOutput(salida);
        pb.redirectErrorStream(true);

        Process proceso = pb.start();

        System.out.println("PID del hijo: " + proceso.pid());
        System.out.println("Esperando como máximo 3 segundos...");

        if (!proceso.waitFor(3, TimeUnit.SECONDS)) {

            proceso.destroy();  // pide que termine

            if (!proceso.waitFor(5, TimeUnit.SECONDS)) {
                proceso.destroyForcibly();  // lo mata sin avisar
            }
            System.err.println("El hijo no terminó en 3 s: se ha cortado");

        } else {
            System.out.println("El hijo terminó antes de los 3 segundos.");
            System.out.println("Código de salida: " + proceso.exitValue());
        }
    }
}
