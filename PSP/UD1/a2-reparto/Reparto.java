/*
Actividad 2 - Predicción previa a la medición

Si un proceso tarda T, mi predicción es:
- 1 proceso: T.
- 2 procesos: aproximadamente T/2.
- 4 procesos: aproximadamente T/4.
- 8 procesos: aproximadamente T/8.

Creo que al aumentar los procesos el tiempo disminuirá,
aunque el coste de arrancarlos y los recursos del equipo
pueden impedir que la mejora sea proporcional.

Resultados de las mediciones:

Procesos | Mediana (ms) | Mejora 
---------|--------------|-----------
1        | 24.107       | 1,00x
2        | 15.476       | 1,56x
4        | 8.894        | 2,71x
8        | 6.334        | 3,81x

Interpretación de los resultados:

Mi predicción era que el tiempo disminuiría de forma proporcional
al número de procesos. Pero, los resultados muestran que
la mejora es menor de la que yo esperada en un principio. Con 8 procesos se tarda 6.334 ms,
frente a los 24.107 ms de un proceso, lo que supone una mejora
aproximada de 3,81 veces, y no de 8 veces.

Esto se debe, entre otros factores, al coste de crear y gestionar
los procesos y a la competencia por los recursos del equipo.
Por tanto, aunque aumentar el número de procesos reduce el tiempo
de ejecución, la mejora no es proporcional al número de procesos.

Concurrencia, paralelismo y sistemas distribuidos:

Concurrencia: varias tareas progresan en un mismo periodo, alternándose o simultáneamente.
Paralelismo: varias tareas se ejecutan al mismo tiempo.
Sistema distribuido: varios equipos cooperan mediante una red.

Comparación:
Dimensión    | Concurrencia       | Paralelismo        | Distribuido
Ejecución    | Tareas se alternan | Tareas simultáneas | Varios equipos
Memoria      | Puede compartirse  | Puede compartirse  | Separada por equipo
Comunicación | Mecanismos internos| Entre tareas       | Por red
Fallos       | Afectan a tareas    | Afectan al equipo  | Fallos independientes

En esta actividad, el paralelismo implica concurrencia, pero no al revés: las tareas concurrentes pueden alternarse en un solo núcleo.

Ejemplos y fuentes:
Concurrencia: Go utiliza goroutines y canales. https://go.dev/blog/waza-talk
Paralelismo: Java ofrece ForkJoinPool para dividir y ejecutar tareas. https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ForkJoinPool.html
Distribuido: los microservicios se comunican por red. https://learn.microsoft.com/en-us/dotnet/architecture/cloud-native/definition

Reparto ejecuta varios procesos en un equipo. Con cuatro portátiles sería un sistema distribuido que necesitaría coordinación y comunicación por red.

*/



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Reparto {
    static final long DESDE = 2, HASTA = 20_000_000; // el rango común

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
            pb.redirectOutput(new File("trozo-" + i + ".log"));

            hijos.add(pb.start());
        }

        // TODO (4) BUCLE 2: esperar a todos y comprobar el código de salida de cada uno

        for (int i = 0; i < hijos.size(); i++) {
            int codigo = hijos.get(i).waitFor();

            if (codigo != 0) {
                System.err.println(
                        "Error en el trozo " + i + ", codigo de salida: " + codigo);
            }
        }

        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("TOTAL " + n + " procesos: " + ms + " ms");
    }
}