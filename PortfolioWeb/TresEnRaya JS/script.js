let tablero = ["", "", "", "", "", "", "", "", ""];
let turno = "X";
let juegoActivo = true;
let contadorTurnos = 0;

let victoriasX = 0;
let victoriasO = 0;
let empates = 0;

let modoJuego = "";

function iniciarJuego(modo) {

    modoJuego = modo;

    // ocultar menú
    document.getElementById("menuInicio").style.display = "none";

    // mostrar juego
    document.getElementById("juego").style.display = "block";

    actualizarMarcador();
    actualizarTurno();
    actualizarBoton();
}

const combinaciones = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6]
];

actualizarBoton();

function cambiar(id) {

    if (!juegoActivo) return;
    if (tablero[id] !== "") return;

    // guardar jugada
    tablero[id] = turno;
    document.getElementById(id).innerHTML =
        `<img src="Fotos/${turno}.png">`;

    contadorTurnos++;

    // comprobar fin de partida
    let ganador = comprobarGanador();

    if (ganador) {
        finalizarPartida("ganador", ganador);
        return;
    }

    if (contadorTurnos === 9) {
        finalizarPartida("empate");
        return;
    }

    // cambiar turno
    turno = (turno === "X") ? "O" : "X";
    actualizarTurno();
    
    // turno de la IA 
    if (modoJuego === "JvIA" && turno === "O" && juegoActivo) {

        bloquearTablero(true);

        setTimeout(() => {
            jugarIA();
            bloquearTablero(false);
        }, 500);

    }
}

function jugarIA() {

    let posicionIA;

    //  intentar ganar
    posicionIA = buscarMejorJugada("O");

    //  bloquear jugador
    if (posicionIA === null) {
        posicionIA = buscarMejorJugada("X");
    }

    //  random
    if (posicionIA === null) {

        let posicionesLibres = [];

        for (let i = 0; i < tablero.length; i++) {

            if (tablero[i] === "") {
                posicionesLibres.push(i);
            }
        }

        let random =
            Math.floor(Math.random() * posicionesLibres.length);

        posicionIA = posicionesLibres[random];
    }

    cambiar(posicionIA);
}

function buscarMejorJugada(simbolo) {

    for (let i = 0; i < tablero.length; i++) {

        // si casilla libre
        if (tablero[i] === "") {

            // simular jugada
            tablero[i] = simbolo;

            // comprobar victoria
            let gana = comprobarGanador();

            // deshacer simulación
            tablero[i] = "";

            // si gana -> devolver posición
            if (gana === simbolo) {
                return i;
            }
        }
    }

    return null;
}

function comprobarGanador() {

    for (let i = 0; i < combinaciones.length; i++) {

        let [a, b, c] = combinaciones[i];

        if (
            tablero[a] !== "" &&
            tablero[a] === tablero[b] &&
            tablero[a] === tablero[c]
        ) {
            return tablero[a];
        }
    }

    return null;
}

function finalizarPartida(tipo, ganador = null) {

    juegoActivo = false;

    if (tipo === "ganador") {

        document.getElementById("contenedorResultado").innerHTML =
            "Gana " + ganador;

        if (ganador === "X") victoriasX++;
        if (ganador === "O") victoriasO++;

    } else if (tipo === "empate") {

        document.getElementById("contenedorResultado").innerHTML =
            "Empate";

        empates++;
    }

    actualizarMarcador();
    actualizarBoton();
    document.getElementById("turnos").innerText = "Fin de partida";
}

function reiniciar() {

    tablero = ["", "", "", "", "", "", "", "", ""];
    turno = "X";
    juegoActivo = true;
    contadorTurnos = 0;

    for (let i = 0; i < 9; i++) {
        document.getElementById(i).innerHTML =
            `<img src="Fotos/Interrogante.png">`;
    }

    document.getElementById("contenedorResultado").innerHTML = "";

    actualizarTurno();
    actualizarBoton();
}

function actualizarMarcador() {
    document.getElementById("Marcador").innerText =
        "MARCADOR" +
        "\nX: " + victoriasX +
        "\nO: " + victoriasO +
        "\nEmpates: " + empates;
}

function actualizarTurno() {
    document.getElementById("turnos").innerText =
        "Turno: " + turno;
}

function actualizarBoton() {

    let btn = document.getElementById("nuevaPartida");

    if (juegoActivo) {
        btn.innerText = "Reiniciar partida";
    } else {
        btn.innerText = "Nueva partida";
    }
}

function bloquearTablero(valor) {
    for (let i = 0; i < 9; i++) {
        document.getElementById(i).style.pointerEvents = valor ? "none" : "auto";
    }
}

actualizarMarcador();
actualizarTurno();











