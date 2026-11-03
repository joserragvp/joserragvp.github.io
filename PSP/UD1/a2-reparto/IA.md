## Promt

Compara la concurrencia, el paralelismo y los sistemas distribuidos. Define los tres conceptos, incluye una tabla comparativa con al menos cuatro dimensiones y proporciona ejemplos reales de cada uno. Explica también la relación entre concurrencia y paralelismo. Incluye fuentes para verificar la información.

## Respuesta
Definiciones

Concurrencia. Es una forma de estructurar un programa como varias tareas que progresan de manera independiente y se coordinan entre sí. Rob Pike la define como la composición de procesos que se ejecutan de forma independiente. No exige que dos cosas ocurran en el mismo instante: basta con que sus ejecuciones se solapen o se intercalen en el tiempo. 
golang

Paralelismo. Es una forma de ejecutar: varios cálculos (normalmente relacionados) se ejecutan de forma simultánea. Necesita hardware con varias unidades de ejecución, como núcleos, GPU o varias máquinas. 
golang

Sistemas distribuidos. Son sistemas formados por varios ordenadores conectados por red que cooperan mediante mensajes. La definición clásica de Tanenbaum y van Steen dice que es una colección de elementos de cómputo autónomos que se presenta ante sus usuarios como un único sistema coherente. Dos rasgos los distinguen: los nodos fallan de forma independiente y no comparten memoria ni reloj. Lamport lo resumió con una frase muy citada: un sistema distribuido es aquel en el que el fallo de un ordenador que ni sabías que existía puede dejar inutilizable el tuyo. 
scribd

Tabla comparativa
Dimensión	Concurrencia	Paralelismo	Sistemas distribuidos
Qué es	Propiedad de diseño (estructura)	Estrategia de ejecución	Arquitectura de varios nodos en red
Objetivo principal	Gestionar muchas tareas a la vez, con capacidad de respuesta	Acelerar un cálculo usando más recursos	Escalabilidad, disponibilidad y tolerancia a fallos
Hardware necesario	Basta un solo núcleo	Varios núcleos, GPU o procesadores	Varias máquinas conectadas por red
Comunicación	Memoria compartida, canales, colas	Memoria compartida o reparto de datos	Paso de mensajes por red (con latencia y pérdidas posibles)
Problemas típicos	Condiciones de carrera, interbloqueos, sincronización	Reparto de la carga, ley de Amdahl, sobrecarga de coordinación	Fallos parciales, latencia, consistencia, particiones de red (teorema CAP)
Fallos	Un fallo suele afectar a todo el proceso	Igual que en la concurrencia	Los nodos fallan de forma independiente
Ejemplo típico	Servidor atendiendo miles de conexiones	Renderizar un vídeo en 8 núcleos	Un servicio web replicado en varios centros de datos
Ejemplos reales
Concurrencia sin paralelismo. Node.js ejecuta tu JavaScript en un único hilo y, gracias a su bucle de eventos, atiende muchas peticiones intercalándolas mientras espera a disco o red. Los sistemas operativos antiguos de un solo núcleo ya daban sensación de multitarea repartiendo el tiempo de CPU. Pike señala que, aunque solo un gopher esté activo a la vez (paralelismo cero), la solución sigue siendo correcta y concurrente. 
golang
Paralelismo. El entrenamiento de redes neuronales en GPU, el renderizado de gráficos 3D, la codificación de vídeo con varios núcleos o una simulación meteorológica repartida en un superordenador. Muchas de estas cargas son paralelas con poca o ninguna concurrencia compleja, porque aplican la misma operación a trozos distintos de datos.
Sistemas distribuidos. Bases de datos como Cassandra o Google Spanner, los sistemas de ficheros distribuidos (HDFS), las plataformas de streaming repartidas por regiones, blockchain y los clústeres de microservicios. Aquí la red y los fallos parciales son el centro del diseño.
Combinaciones. Un servidor web moderno es concurrente (un bucle de eventos o un pool de hilos), paralelo (varios núcleos) y a menudo distribuido (varias réplicas tras un balanceador). Una consulta SQL sobre un clúster con particiones (sharding) usa los tres a la vez.
Relación entre concurrencia y paralelismo

No son lo mismo ni uno implica al otro:

Concurrencia sin paralelismo: un solo núcleo que alterna tareas.
Paralelismo sin concurrencia: una operación vectorial (SIMD) o un bucle que suma un array repartido entre núcleos, sin tareas independientes que coordinar.
Ambos: varias tareas independientes ejecutándose realmente a la vez en varios núcleos.

La idea central de Pike es que la concurrencia trata de la estructura y el paralelismo de la ejecución. Un buen diseño concurrente descompone el problema en piezas independientes y, si hay hardware disponible, esas piezas pueden ejecutarse en paralelo sin rediseñar el programa. Por eso dice que la concurrencia permite el paralelismo, aunque no lo garantiza: depende del hardware, del sistema operativo y del runtime. Un sistema distribuido, por su parte, es una forma de conseguir paralelismo (y tolerancia a fallos) llevando la concurrencia más allá de una sola máquina, a costa de enfrentarse a la red. 
golang
golang

## Registro de correcciones

### Corrección 1: Relación entre concurrencia y paralelismo

- Afirmación original: «No son lo mismo ni uno implica al otro» y «Paralelismo sin concurrencia».
- Por qué es imprecisa: Esta explicación no sigue el criterio utilizado en el enunciado de la actividad, que establece que el paralelismo implica concurrencia, pero la concurrencia no implica necesariamente paralelismo.
- Corrección: En el marco de esta actividad, varias tareas paralelas se ejecutan simultáneamente y se consideran concurrentes. En cambio, las tareas concurrentes pueden alternarse en un único núcleo sin ejecutarse al mismo tiempo.
- Fuente: Rob Pike, «Concurrency is not Parallelism»: https://go.dev/blog/waza-talk

### Corrección 2: Los sistemas distribuidos no son simplemente una forma de paralelismo

- Afirmación original: «Un sistema distribuido, por su parte, es una forma de conseguir paralelismo (y tolerancia a fallos) llevando la concurrencia más allá de una sola máquina».

- Por qué es imprecisa: Un sistema distribuido puede ejecutar tareas en paralelo, pero distribuir componentes entre varios ordenadores no garantiza que todas las tareas se ejecuten simultáneamente. También puede utilizarse para compartir servicios, almacenar datos o mejorar la disponibilidad.

- Corrección: Un sistema distribuido está formado por varios ordenadores que se comunican mediante una red y cooperan para realizar una función. Puede aprovechar el paralelismo, pero no todo sistema distribuido tiene que ejecutar tareas en paralelo en todo momento.

- Fuente: Microsoft Learn, «What is a distributed application?»: https://learn.microsoft.com/en-us/dotnet/architecture/cloud-native/definition