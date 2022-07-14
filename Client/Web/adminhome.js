window.addEventListener("load", function Adminhome() {
    const url = 'http://localhost:5000/admin/stat/country/0';
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
      lista.innerHTML = "<tr><th>Ország</th><th>DB</th></tr>";
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.db + "</td></tr>"
            });
        })
        .catch (err => console.log(err));
})

document.getElementById("gomb2").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/stat' + "/" + document.getElementById("what") + "/" + document.getElementById("order");
    const token = 'Bearer: ' + sessionStorage.token
    const lista2 = document.getElementById("lista2")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
      lista2.innerHTML = "<tr><th>Ország</th><th>DB</th></tr>";
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista2.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.db + "</td></tr>"
            });
        })
        .catch (err => console.log(err));
}