//Kategoriák
window.addEventListener("load", function AllCat() {
    const url = 'http://localhost:5000/categories';
    const menu = document.getElementById("menu");
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token,
        }
    })


.then((response) => response.json())
    .then(json => {
        json.forEach(cs => {
            menu.innerHTML += "<option value=" + cs.ID + ">" + cs.denomination + "</option> "
            });
          })
    .catch (err => console.log(err))
})

// ----Módosítandó post kiírása

window.addEventListener("load", function Post() {
    const url = 'http://localhost:5000/user/post' + "/" + sessionStorage.id + "/" +  sessionStorage.regid;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista");
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

        .then((response) => response.json())
        .then(json => {
            lista.innerHTML = "<tr><th>Összeg</th><th>Dátum</th><th>Kategória</th></tr>";
            json.forEach(cs => {
                lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.date + "</td><td>" + cs.categoriesID + "</td>"
                    });   
})

        .catch(err => console.log(err))
})

// ----Post módosítása

document.getElementById("gomb").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/user/post' + "/" + sessionStorage.id + "/" +  sessionStorage.regid;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': token,
            // "Content-type": "application/json;charset=utf-8"
        },
        body: {
            "amount" : document.getElementById("amount").value,
            "regAt" : document.getElementById("regAt").value,
            "categori" : document.getElementById("menu").value 
        }
    })

        .then((response) => response.json())
        .then(json => document.getElementById("uzenet").innerHTML = json.message)
        .catch(err => console.log(err))
}

// ----Post törlés

document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();

    const url = 'http://localhost:5000/user/post' + "/" + sessionStorage.id + "/" +  sessionStorage.regid;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': token
        }
        
    })
        .then((response) => response.json())
        .then((json) => {
            document.getElementById("uzenet").innerHTML = json.message
            setTimeout(function a() {
                document.location = "keres.html"
        }, 1500)
        })
        .catch(err => console.log(err))
}

