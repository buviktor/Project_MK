document.getElementById("gomb").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/users' + "/" + document.getElementById("active").value + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista")
    const össz = document.getElementById("össz")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
        lista.innerHTML = "<tr><th>Név</th><th>E-mail</th><th>Ország</th><th>Megye</th><th>Város</th><th>Irányítószám</th><th>Dátum</th></tr>";
        json.forEach(cs => {
            if(cs!=json[0]) {
                lista.innerHTML += "<tr><td>" + cs.name + "</td><td>" + cs.email + "</td><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td><td>" + cs.postcode + "</td><td>" + cs.date + "</td></tr>"
            }
            else if(cs=json[0]){
                össz.innerHTML = "<h4> Felhasználók száma: " + cs.number + "</h4>"
            }
        });
        })
        .catch (err => console.log(err));
}