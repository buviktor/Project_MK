window.addEventListener("load", function Adminhome() {
    const url = 'http://localhost:5000/admin/stat/country/0' // + "/" + document.getElementById("rend");
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
