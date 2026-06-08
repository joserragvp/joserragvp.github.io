let estado = "inicio";
let aciertos = 0;
let fallos = 0;
let aciertosSeguidos = 0;
let numeroVasos = 3;
let siguienteNumeroVasos = 3;

const sonidoVictoria = new Audio("sonidos/Victoria.mp3");
const sonidoDerrota = new Audio("sonidos/Derrota.mp3");



sonidoVictoria.volume = 0.5;
sonidoDerrota.volume = 0.75;

document.getElementById("volVictoria").value = sonidoVictoria.volume;
document.getElementById("volDerrota").value = sonidoDerrota.volume;

actualizarMarcador();


function toggleAjustes() {
    document.getElementById("menuAjustes")
        .classList.toggle("oculto");
}

function cambiarModoOscuro() {
    document.body.classList.toggle("dark");
}

function cambiarVolVictoria() {
    sonidoVictoria.volume =
        parseFloat(document.getElementById("volVictoria").value);
}

function cambiarVolDerrota() {
    sonidoDerrota.volume =
        parseFloat(document.getElementById("volDerrota").value);
}

async function mostrarPelotaInicio() {

    estado = "mostrando";

    const vasoConPelota =
        document.querySelector(".pelota").parentElement;

    const imagenVaso =
        vasoConPelota.querySelector(".vaso");

    imagenVaso.classList.add("levantado");

    await new Promise(resolve =>
        setTimeout(resolve, 1500)
    );

    const vasoCentro = imagenVaso;

    const esperaBajada = new Promise(resolve =>
        vasoCentro.addEventListener(
            "transitionend",
            resolve,
            { once: true }
        )
    );

    vasoCentro.classList.remove("levantado");

    await esperaBajada;

    mezclar();

}

async function intercambiar(a, b) {

    const vasoA = document.getElementById("vaso" + a);
    const vasoB = document.getElementById("vaso" + b);

    const velocidad = obtenerVelocidad();

    vasoA.style.transition = `left ${velocidad}ms ease`;
    vasoB.style.transition = `left ${velocidad}ms ease`;

    const leftA = vasoA.offsetLeft;
    const leftB = vasoB.offsetLeft;

    vasoA.style.left = leftB + "px";
    vasoB.style.left = leftA + "px";

    await Promise.all([
        new Promise(resolve =>
            vasoA.addEventListener("transitionend", resolve, { once: true })
        ),
        new Promise(resolve =>
            vasoB.addEventListener("transitionend", resolve, { once: true })
        )
    ]);
}

function levantar(i) {

    if (estado !== "esperando") {
        return;
    }

    estado = "resultado";

    const vaso = document.getElementById("vaso" + i);

    vaso.querySelector(".vaso").classList.add("levantado");

    const mensaje = document.getElementById("mensaje");

    if (vaso.querySelector(".pelota")) {
        
        sonidoVictoria.currentTime = 0;
        sonidoVictoria.play();
        
        mensaje.textContent = "¡Has acertado!";
        aciertos++;
        aciertosSeguidos++;

        if (aciertosSeguidos >= 3) {
            siguienteNumeroVasos = 4;
        }

    } else {
        
        sonidoDerrota.currentTime = 0;
        sonidoDerrota.play();
        
        mensaje.textContent = "Has fallado";
        fallos++;
        aciertosSeguidos = 0;
        siguienteNumeroVasos = 3;


    }
    actualizarMarcador();
}


function nuevaPartida() {

    document.getElementById("nuevaPartida").disabled = true;

    numeroVasos = siguienteNumeroVasos;
    actualizarModoVasos();

    estado = "inicio";



    document.getElementById("mensaje").textContent = "";

    const vasos = document.querySelectorAll(".contenedorVaso");


    document.querySelectorAll(".vaso")
        .forEach(v => v.classList.remove("levantado"));


    vasos[0].innerHTML = `
        <img class="vaso" src="fotos/vasoTrilero_180.png">
    `;

    vasos[1].innerHTML = `
        <img class="vaso" src="fotos/vasoTrilero_180.png">
        <img class="pelota" src="fotos/pelota.png">
    `;

    vasos[2].innerHTML = `
        <img class="vaso" src="fotos/vasoTrilero_180.png">
    `;

    if (vasos[3]) {
        vasos[3].innerHTML = `
        <img class="vaso" src="fotos/vasoTrilero_180.png">
    `;
    }

    mostrarPelotaInicio();

}



async function mezclar() {

    console.log("EMPIEZA MEZCLA");

    estado = "mezclando";

    for (let i = 0; i < 10; i++) {

        const a = Math.floor(Math.random() * numeroVasos);

        let b = Math.floor(Math.random() * numeroVasos);

        while (a === b) {
            b = Math.floor(Math.random() * numeroVasos);
        }

        await intercambiar(a, b);

        await new Promise(resolve =>
            setTimeout(resolve, obtenerVelocidad())
        );
    }

    estado = "esperando";

    document.getElementById("nuevaPartida").disabled = false;
}

function obtenerVelocidad() {
    
    const n = Math.floor(aciertosSeguidos / 2);

    const resultado = (80 * (n + 5) / (n + 1)) * (1 + (numeroVasos - 3) * 0.1);

    console.log(resultado)

    return resultado;
}

function actualizarMarcador() {
    document.getElementById("Marcador").innerText =
        "MARCADOR" +
        "\nAciertos: " + aciertos +
        "\nFallos: " + fallos +
        "\nRacha Aciertos: " + aciertosSeguidos
}

function actualizarModoVasos() {
    console.log("ACTUALIZANDO VASOS");
    if (numeroVasos === 3) {

        document.getElementById("vaso3")
            .className = "contenedorVasoOculto"

        document.getElementById("vaso0").style.left = "0px";
        document.getElementById("vaso1").style.left = "200px";
        document.getElementById("vaso2").style.left = "400px";

    } else {

        document.getElementById("vaso3")
             .className = "contenedorVaso";

        document.getElementById("vaso0").style.left = "-100px";
        document.getElementById("vaso1").style.left = "100px";
        document.getElementById("vaso2").style.left = "300px";
        document.getElementById("vaso3").style.left = "500px";
    }
}

actualizarMarcador();