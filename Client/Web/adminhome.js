// window.addEventListener("load", function Adminhome() {
//     const url = 'http://localhost:5000/admin/stat/country/0';
//     const token = 'Bearer: ' + sessionStorage.token
//     const lista = document.getElementById("lista")
//     fetch(url, {
//         method: 'GET',
//         headers: {
//             'Authorization': token
//         }
//     })

//     .then((response) => response.json())
//     .then(json => {
//       lista.innerHTML = "<tr><th>Ország</th><th>DB</th></tr>";
//       json.forEach(cs => {
//           if(cs!=json[0]) 
//           lista.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.db + "</td></tr>"
//             });
//         })
//         .catch (err => console.log(err));
// })

document.getElementById("gomb2").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/stat' + "/" + document.getElementById("what").value + "/" + document.getElementById("order").value;
    const token = 'Bearer: ' + sessionStorage.token
    const lista1 = document.getElementById("lista1")
    const lista2 = document.getElementById("lista2")
    const lista3 = document.getElementById("lista3")
    const lista4 = document.getElementById("lista4")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
        // lista1.innerHTML = "<tr><th>Ország</th><th>DB</th></tr>";
        // json.forEach(cs => {
        //     lista1.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.db + "</td></tr>"
        //         });

        // lista2.innerHTML = "<tr><th>Ország</th><th>Megye</th><th>DB</th></tr>";
        // json.forEach(cs => {
        //     lista2.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.db + "</td></tr>"
        //         });

        // lista3.innerHTML = "<tr><th>Ország</th><th>Megye</th><th>Város</th><th>DB</th></tr>";
        // json.forEach(cs => {
        //     lista3.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td><td>" + cs.db + "</td></tr>"
        //         });

        lista4.innerHTML = "<tr><th>Ország</th><th>Megye</th><th>Város</th><th>Irányítószám</th><th>DB</th></tr>";
        json.forEach(cs => {
            lista4.innerHTML += "<tr><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td><td>" + cs.postcode + "</td><td>" + cs.db + "</td></tr>"
                });
        })
        .catch (err => console.log(err));
}

//Új kategória hozzáadása
document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/stat/county/0';
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': token,
            "Content-type": "application/json;charset=utf-8"
        },
        body: JSON.stringify({
            "categori": document.getElementById("categori").value,
        })
    })
        .then((response) => response.json())
        .then(json => document.getElementById("uzenet").innerHTML = json.message)
        .catch(err => console.log(err));
}

//Kategoriák
window.addEventListener("load", function AllCat() {
    const url = 'http://localhost:5000/categories';
    const menu = document.getElementById("categoriesID");
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token,
        }
    })

    .then((response) => response.json())
    .then(json => {
        lista5.innerHTML = "<tr><th>Kategóriák</th></tr>";
        json.forEach(cs => {
            lista5.innerHTML += "<option value=" + cs.ID + ">" + cs.denomination + "</option> "
            });
          })
    .catch (err => console.log(err));
}
)