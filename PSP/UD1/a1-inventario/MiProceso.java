
import java.io.FileInputStream;

/*
Actividad 1 - Inventario de procesos

1. Procesos: 213 segun Get-Process. Procesadores logicos: 12.
2. Listado de procesos realizado con tasklist.
3. Clasificacion:
   - chrome.exe: aplicacion de usuario, navegador web.
   - Code.exe: aplicacion de usuario, editor de codigo.
   - explorer.exe: proceso auxiliar, gestiona escritorio y ventanas.
   - svchost.exe: servicio del sistema, aloja servicios de Windows.
   - MsMpEng.exe: servicio del sistema, proteccion antivirus.
4. MiProceso muestra su PID y espera al teclado; se localizo por PID
   en el Administrador de tareas.
5. CPU casi nula durante la espera porque el programa esta bloqueado
   esperando la entrada del teclado.
6. Se leyeron 100.000.000 bytes de prueba-grande.bin. La CPU observada
   fue del 0 % o no apreciable; la pausa artificial reducia el uso.
7. Java se finalizo desde el Administrador de tareas. PowerShell mostro
   LASTEXITCODE = 0, pero no confirma el codigo de esa ejecucion.
8. Eleccion: hilo programado dentro del servidor.
   - Comparte estado en memoria: si; necesita gestionar las reservas.
   - Puede tumbar al servidor: si, si no se controlan los errores.
   - Debe correr con el servidor parado: no.
   - Frecuencia: cada minuto.
   Costes de la opcion:
   - Menor aislamiento ante fallos graves.
   - No funciona si el servidor esta apagado.
   - Comparte CPU y memoria con el servidor.
*/

public class MiProceso {
    public static void main(String[] args) throws Exception {
        System.out.println("PID: " + ProcessHandle.current().pid());
        int n = Runtime.getRuntime().availableProcessors();
        System.out.println("Núcleos: " + n);
        System.out.println("Pulsa Intro para continuar...");
        System.in.read();

        // Prueba del apartado 6: lectura de un archivo grande.
        try (FileInputStream archivo = new FileInputStream("prueba-grande.bin")) {

            byte[] buffer = new byte[8192];
            long total = 0;
            int leidos;

            while ((leidos = archivo.read(buffer)) != -1) {
                total += leidos;
            }

            System.out.println("Bytes leídos: " + total);
        }

        System.out.println("Programa finalizado.");
    }
}
