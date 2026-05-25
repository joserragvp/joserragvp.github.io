fetch("projects.json")
  .then(res => res.json())
  .then(data => {

    const webContainer = document.getElementById("web-projects");
    const javaContainer = document.getElementById("java-projects");

    data.forEach((project, index) => {

      const link = document.createElement("a");
      link.textContent = project.name;

      if (project.type === "web") {
        link.href = project.url;
        webContainer.appendChild(link);
      }

      if (project.type === "java") {
        link.href = `projects.html?project=${index}`;
        javaContainer.appendChild(link);
      }

    });

  });