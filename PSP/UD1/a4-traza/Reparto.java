/*
1. Qué hace:
Divide un rango de números en varios fragmentos y utiliza procesos
independientes para calcular cada fragmento.

2. Cómo distribuye:
El rango comprendido entre DESDE y HASTA se divide en partes iguales
según el número de procesos indicado como argumento. Cada proceso
ejecuta Calculo.java con los límites de su fragmento.

3. Medición:
Se ha realizado una ejecución de calentamiento y cinco ejecuciones
válidas para cada número de procesos. Se muestra la mediana.

Procesos | Mediana (ms) | Mejora
---------|--------------|--------
1        | 45.706       | 1,00x
2        | 29.548       | 1,55x
4        | 17.537       | 2,61x
8        | 17.023       | 2,69x
16       | 20.309       | 2,25x

Comparación del paso 5:
 - En paralelo (8 procesos): 17.285 ms.
 - En secuencial (8 procesos): 52.705 ms.

4. Interpretación:
El tiempo disminuye al aumentar el número de procesos, pero la mejora
es cada vez menor. Esto se debe a que existe una parte del trabajo que
no se puede paralelizar y a que crear y coordinar los procesos también
tiene un coste.

Con 8 procesos se obtiene el mejor resultado, con una mediana de
17.023 ms. Al aumentar a 16 procesos, el tiempo sube a 20.309 ms.
Por tanto, en estas pruebas, utilizar más de 8 procesos deja de
mejorar el rendimiento y provoca un empeoramiento.

5. Errores:
Si un proceso hijo termina con un código distinto de cero, el proceso
principal informa del fragmento que ha fallado y del código de salida.
Después de esperar a todos los hijos, aborta la ejecución con código 1.

Si un proceso hijo tarda demasiado o se queda bloqueado, el proceso
principal puede quedarse esperando indefinidamente, ya que no se ha
implementado un tiempo límite de espera.

Decisión: se aborta el reparto completo si falla algún proceso, en
lugar de presentar un resultado parcial. Así se evita mostrar un
resultado incompleto como si fuera correcto. La desventaja es que se
pierde el trabajo realizado por los fragmentos que sí terminaron bien.

Revisión de actividades anteriores:
En la Actividad 2 se comprueban los códigos de salida y se
redirige la salida de los hijos, pero no existe un tiempo
máximo de espera. Además, si un hijo falla, solo se informa
del error y el programa continúa.

En la Actividad 3, Lanzador.java lee la salida antes de
esperar al proceso y comprueba su código de salida, pero no
tiene límite de tiempo. LanzadorFichero.java redirige la
salida a un fichero y utiliza un límite de espera, aunque
no comprueba el resultado final después de forzar el cierre.

En la Actividad 4 se comprueban los códigos de salida y se
redirige la salida de cada hijo a su propio fichero. Sin
embargo, tampoco se ha implementado un tiempo máximo de
espera, por lo que un proceso bloqueado podría dejar esperando
indefinidamente al proceso principal.

*/

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Reparto {

    static final long DESDE = 2, HASTA = 20_000_000; // el rango común

    static void log(int trozo, String msg) {
        System.out.printf(Locale.ROOT, "%tT.%<tL [trozo %d] %s%n",
                new Date(), trozo, msg);
    }

    public static void main(String[] args) throws Exception {
        int n = Integer.parseInt(args[0]); // número de procesos
        long paso = (HASTA - DESDE) / n;
        long t0 = System.nanoTime();
        List<Process> hijos = new ArrayList<>();
        // TODO (3) BUCLE 1: calcular el rango de cada trozo y arrancar su proceso

        for (int i = 0; i < n; i++) {
            long a = DESDE + i * paso;
            long b = (i == n - 1) ? HASTA : a + paso;

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "Calculo.java", "" + a, "" + b);

            pb.redirectErrorStream(true);
            pb.redirectOutput(new File("trazas/trozo-" + i + ".log"));

            hijos.add(pb.start());

            log(i, "lanzado PID " + hijos.get(i).pid()
                    + " rango [" + a + "," + b + ")");
        }

        // TODO (4) BUCLE 2: esperar a todos y comprobar el código de salida de cada uno
        boolean fallo = false;

        for (int i = 0; i < hijos.size(); i++) {
            int codigo = hijos.get(i).waitFor();

            log(i, "terminado con código " + codigo);

            if (codigo != 0) {
                System.err.println(
                        "Trozo " + i + " falló con código " + codigo
                        + ": mira trazas/trozo-" + i + ".log");
                fallo = true;
            }
        }

        if (fallo) {
            System.err.println("ABORTADO: al menos un trozo falló");
            System.exit(1);
        }

        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("TOTAL " + n + " procesos: " + ms + " ms");
    }
}
