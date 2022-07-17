document.getElementById("gomb").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/users' + "/" + document.getElementById("active").value + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista")
    const lista2 = document.getElementById("lista2")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
        lista.innerHTML = "<tr><th>Név</th><th>E-mail</th><th>Ország</th><th>Megye</th><th>Város</th><th>Irányítószám</th><th>Dátum</th><th>DB</th></tr>";
        json.forEach(cs => {
            if(cs!=json[0]) 
            lista.innerHTML += "<tr><td>" + cs.name + "</td><td>" + cs.email + "</td><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td><td>" + cs.postcode + "</td><td>" + cs.date + "</td></tr>"
            });

        json.forEach(cs => {
            lista2.innerHTML += "<p>" + cs.number + "</p>";
            })
        })
        .catch (err => console.log(err));
}