
import java.io.File;

/*
PASO 5: COMPROBACIÓN DEL CÓDIGO DE SALIDA

Se ha probado la ejecución correcta con el comando java -version,
que devuelve el código de salida 0.

Después, se ha provocado un error utilizando el comando
java -opcion-que-no-existe.

El proceso ha mostrado un mensaje de error y ha terminado con
el código de salida 1. El programa ha detectado el fallo y ha
mostrado el mensaje "Ejecución fallida".

Conclusión: waitFor() devuelve el código de salida del proceso hijo.
Un código 0 indica que la ejecución ha terminado correctamente,
mientras que un código distinto de 0 indica que se ha producido
un error.


PASO 7: SOLUCIÓN DEL INTERBLOQUEO LEYENDO LA SALIDA

Se lee la salida del proceso hijo antes de llamar a waitFor().
De esta forma, se evita que el hijo se quede bloqueado porque
el búfer de salida está lleno.

Se cuentan las líneas leídas para comprobar cuánta salida
ha generado el proceso.

RESULTADO DE LA PRUEBA

Se ha ejecutado un listado recursivo del directorio C:\Windows
leyendo la salida del proceso hijo antes de llamar a waitFor().

El programa ha leído 606144 líneas y ha terminado con el código
de salida 0, mostrando "Ejecución correcta".

Conclusión: leer la salida mientras se genera permite evitar
el bloqueo que puede producirse cuando el búfer de salida se llena.
 */
public class Lanzador {

    public static void main(String[] args) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", "dir", "/s", "C:\\Windows"
        );

        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectErrorStream(true);

        Process proceso = pb.start();

        int lineas = 0;

        try (var in = proceso.inputReader()) {
            while (in.readLine() != null) {
                lineas++;
            }
        }

        int codigo = proceso.waitFor();

        System.out.println("PID del hijo: " + proceso.pid());
        System.out.println("Líneas leídas: " + lineas);
        System.out.println("Código: " + codigo);

        if (codigo == 0) {
            System.out.println("Ejecución correcta");
        } else {
            System.out.println("Ejecución fallida");
        }
    }
}
