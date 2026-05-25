// VARIABLES
let eleccionJugador = "";

let victoriasJugador = 0;
let victoriasRival = 0;
let empates = 0;


function jugar(eleccion)
{
    eleccionJugador = eleccion;

    quitarSeleccion();

    document.getElementById(eleccion)
        .classList.add("seleccionado");
}



function ejecutarJuego()
{
    if(eleccionJugador == "")
    {
        alert("Elige Piedra, Papel o Tijera");
        return;
    }

    bloquearBotones(true);

    const eleccionEnemigo = obtenerEleccionEnemigo();

    mostrarImagenEnemigo(eleccionEnemigo);

    const ganador = calcularGanador(eleccionJugador, eleccionEnemigo);

    actualizarMarcador(ganador);

    mostrarResultado(ganador);

    setTimeout(reiniciarRonda, 1000);
}




function obtenerEleccionEnemigo()
{
    const opciones = ["Piedra", "Papel", "Tijera"];

    const numeroRandom = Math.floor(Math.random() * 3);

    return opciones[numeroRandom];
}


function mostrarImagenEnemigo(eleccion)
{
    document.getElementById("Enemigo").src =
        "Fotos/" + eleccion + ".png";
}


function calcularGanador(jugador, enemigo)
{
    if(jugador == enemigo)
    {
        return "Empate";
    }

    if(
        (jugador == "Piedra" && enemigo == "Tijera") ||
        (jugador == "Papel" && enemigo == "Piedra") ||
        (jugador == "Tijera" && enemigo == "Papel")
    )
    {
        return "Jugador";
    }

    return "Rival";
}


function actualizarMarcador(ganador)
{
    if(ganador == "Jugador")
    {
        victoriasJugador++;
    }
    else if(ganador == "Rival")
    {
        victoriasRival++;
    }
    else
    {
        empates++;
    }

    document.getElementById("Marcador").value =
        "Jugador: " + victoriasJugador +
        "\nRival: " + victoriasRival +
        "\nEmpates: " + empates;
}


function mostrarResultado(ganador)
{
    document.getElementById("Ganador").value =
        "Ganador: " + ganador;
}


function reiniciarRonda()
{
    document.getElementById("Enemigo").src =
        "Fotos/cerebro.png";

    quitarSeleccion();

    eleccionJugador = "";

    bloquearBotones(false);
}


function quitarSeleccion()
{
    document.getElementById("Piedra")
        .classList.remove("seleccionado");

    document.getElementById("Papel")
        .classList.remove("seleccionado");

    document.getElementById("Tijera")
        .classList.remove("seleccionado");
}


function bloquearBotones(valor)
{
    document.getElementById("Piedra").disabled = valor;
    document.getElementById("Papel").disabled = valor;
    document.getElementById("Tijera").disabled = valor;
    document.getElementById("Ejecutar").disabled = valor;
}