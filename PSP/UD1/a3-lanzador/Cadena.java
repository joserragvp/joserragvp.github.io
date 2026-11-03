
import java.io.File;
import java.util.concurrent.TimeUnit;


/*
PASO 9: CADENA DE PROCESOS

Se ejecuta primero Calculo.java y su salida se guarda en
resultado-<marca>.txt. Si termina correctamente, se comprime
ese fichero con jar.

PASO 10: COMPROBACIÓN DE LOS CÓDIGOS DE SALIDA

El segundo proceso solo se ejecuta si el primero termina
con código 0. Si un proceso falla, se muestra el paso y su
código de salida, y Cadena termina con ese mismo código
mediante System.exit().

Se conserva el fichero TXT si falla la compresión, porque
permite revisar el resultado sin repetir el cálculo.

RESULTADOS DE LAS PRUEBAS

Se ha ejecutado Cadena.java normalmente y ambos procesos
han terminado correctamente.

Al ejecutar java Cadena.java abc, el primer proceso ha
fallado con código 1. Cadena ha devuelto también el código 1,
comprobado con $LASTEXITCODE. El segundo proceso no se ha
ejecutado.

También se ha provocado un fallo de jar intentando comprimir
un fichero inexistente. El segundo proceso ha terminado con
código 1 y se ha decidido conservar el TXT generado.

PASO 11: LÍMITES DE TIEMPO

Se ha establecido un límite de 10 segundos para Calculo.java,
porque el cálculo de números primos puede tardar más, y de
5 segundos para jar, porque la compresión debería ser rápida.

Si un proceso supera su límite, se solicita su finalización
con destroy(). Si no termina en 2 segundos más, se fuerza
su cierre con destroyForcibly(). Se utiliza el código 124
para indicar que se ha agotado el tiempo.

RESULTADO DE LA PRUEBA DEL LÍMITE DE TIEMPO

Se ha ejecutado java Cadena.java 20000000. El cálculo ha
superado los 10 segundos y Cadena ha mostrado el mensaje
de tiempo agotado y ha terminado el paso 1. El segundo
proceso no se ha ejecutado.

Conclusión: se ha comprobado el límite de tiempo del cálculo.
El límite de 5 segundos de jar está implementado, pero no
se ha probado provocando un agotamiento real del tiempo.
*/

public class Cadena {

    public static void main(String[] args) throws Exception {

        long marca = System.currentTimeMillis();

        String desde = "2";
        String hasta = args.length > 0 ? args[0] : "300000";

        String resultado = "resultado-" + marca + ".txt";
        String comprimido = "resultado-" + marca + ".zip";

        File log1 = new File("paso1-" + marca + ".log");
        File log2 = new File("paso2-" + marca + ".log");

        File carpeta = new File(System.getProperty("user.dir"));

        // PASO 1: ejecutar el cálculo
        ProcessBuilder pb1 = new ProcessBuilder(
                "java", "Calculo.java", desde, hasta
        );

        pb1.directory(carpeta);
        pb1.redirectOutput(new File(resultado));
        pb1.redirectError(log1);

        Process proceso1 = pb1.start();

        System.out.println("[paso1] PID " + proceso1.pid()
                + " -> java Calculo.java " + desde + " " + hasta);

        boolean termino1 = proceso1.waitFor(10, TimeUnit.SECONDS);

        if (!termino1) {
            proceso1.destroy();

            if (!proceso1.waitFor(2, TimeUnit.SECONDS)) {
                proceso1.destroyForcibly();
                proceso1.waitFor();
            }

            System.err.println(
                    "ABORTADO en el paso 1 (cálculo): superó los 10 segundos"
            );
            System.exit(124);
        }

        int codigo1 = proceso1.exitValue();

        if (codigo1 != 0) {
            System.err.println(
                    "ABORTADO en el paso 1 (cálculo) con código " + codigo1
            );
            System.exit(codigo1);
        }

        // PASO 2: comprimir el fichero de resultado
        ProcessBuilder pb2 = new ProcessBuilder(
                "jar", "--create", "--no-manifest",
                "--file", comprimido, resultado
        );

        pb2.directory(carpeta);
        pb2.redirectOutput(log2);
        pb2.redirectErrorStream(true);

        Process proceso2 = pb2.start();

        System.out.println("[paso2] PID " + proceso2.pid()
                + " -> jar --create --no-manifest --file "
                + comprimido + " " + resultado);

        boolean termino2 = proceso2.waitFor(5, TimeUnit.SECONDS);

        if (!termino2) {
            proceso2.destroy();

            if (!proceso2.waitFor(2, TimeUnit.SECONDS)) {
                proceso2.destroyForcibly();
                proceso2.waitFor();
            }

            System.err.println(
                    "ABORTADO en el paso 2 (compresión): superó los 5 segundos"
            );
            System.exit(124);
        }

        int codigo2 = proceso2.exitValue();

        if (codigo2 != 0) {
            System.err.println(
                    "ABORTADO en el paso 2 (compresión) con código " + codigo2
            );
            System.err.println(
                    "Se conserva el fichero de texto: " + resultado
            );
            System.exit(codigo2);
        }

        System.out.println("Cadena completada correctamente.");
        System.out.println("Fichero de resultado: " + resultado);
        System.out.println("Fichero ZIP: " + comprimido);
        System.out.println("Registro de errores del paso 1: " + log1.getName());
        System.out.println("Registro del paso 2: " + log2.getName());
    }
}
