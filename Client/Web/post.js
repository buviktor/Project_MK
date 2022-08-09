// ----- Kategoriák -----
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
function Post() {
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
            lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.date + "</td><td>" + cs.denomination + "</td>"
            document.getElementById("amount").setAttribute ('value', cs.amount);
            document.getElementById("regAt").setAttribute ('value', cs.date);
            document.getElementById("menu").selectedIndex = (cs.categoriesID);
        });   
    })
    .catch(err => console.log(err))
}
Post()

// ----Post módosítása
document.getElementById("gomb").onclick = function (e) {
    e.preventDefault();

    if(document.getElementById("amount").value =="" && document.getElementById("regAt").value == "" && document.getElementById("menu").value == "") {
        alert("Kérjük minden mezőt töltsön ki!")
    } else {
    const url = 'http://localhost:5000/user/post' + "/" + sessionStorage.id + "/" +  sessionStorage.regid;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': token,
            "Content-type": "application/json;charset=utf-8"
        },
        body: JSON.stringify({
            "amount" : document.getElementById("amount").value,
            "regAt" : document.getElementById("regAt").value,
            "categori" : document.getElementById("menu").value 
        })
    })

    .then((response) => response.json())
    .then(json => {
        Post()
        document.getElementById("uzenet").innerHTML = json.message
    })
    .catch(err => console.log(err))
}}

// ----Post törlés
document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();

    if (confirm("Biztosan törölni szeretné?") == true) {    

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
    } else {
  }
} 

