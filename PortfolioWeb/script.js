const title = document.getElementById("title");
const description = document.getElementById("description");
const image = document.getElementById("image");
const selector = document.getElementById("selector");

let projects = [];
let searchParams = new URLSearchParams(window.location.search);


fetch("projects.json")
  .then(res => res.json())
  .then(data => {

    // solo Java
    projects = data.filter(p => p.type === "java");

    projects.forEach((project, index) => {
      const option = document.createElement("option");
      option.value = index;
      option.textContent = project.name;
      selector.appendChild(option);
    });

    showProject(searchParams.get("project"));

  
  });


// mostrar proyecto
function showProject(i) {
    title.textContent = projects[i].name;
    description.textContent = projects[i].description;
    image.src = projects[i].image;
    selector.value = i;
}

// cambio selector
selector.addEventListener("change", (e) => {
    showProject(e.target.value);
});