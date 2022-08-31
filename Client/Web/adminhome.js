// ----- Fekhasználók kiírása -----
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
           
          lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.db + "</td></tr>"
            });
        })
        .catch (err => console.log(err));
})

// ----- Felhasználók kiírása Ország, megye, irányítószám, város alapján -----
document.getElementById("gomb2").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/stat' + "/" + document.getElementById("what").value + "/" + document.getElementById("order1").value;
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
        if (document.getElementById("what").value == "country") {
        lista.innerHTML = '<tr><th>Ország</th><th>DB</th></tr>';
        json.forEach(cs => {
            lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.db + "</td></tr>"
                });
        } else if(document.getElementById("what").value == "county") {
        lista.innerHTML = "<tr><th>Ország</th><th>Megye</th><th>DB</th></tr>";
        json.forEach(cs => {
            lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.db + "</td></tr>"
                });
        } else if(document.getElementById("what").value == "city") {
        lista.innerHTML = "<tr><th>Ország</th><th>Megye</th><th>Város</th><th>DB</th></tr>";
        json.forEach(cs => {
            lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td><td>" + cs.db + "</td></tr>"
                });
        } else {
        lista.innerHTML = "<tr><th>Ország</th><th>Megye</th><th>Város</th><th>Irányítószám</th><th>DB</th></tr>";
        json.forEach(cs => {
            lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td><td>" + cs.postcode + "</td><td>" + cs.db + "</td></tr>"
                });
        }        
        })
        .catch (err => console.log(err));
}


