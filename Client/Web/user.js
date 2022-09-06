// ----- Összegzés és az utolsó havi adatok listázása -----
function Allpost() {
    const url = 'http://localhost:5000/user/all/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista");
    const lista2 = document.getElementById("lista2");
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
        if (json[0] != null){
        lista2.innerHTML = "<tr><th>Összegzés</th><th>Bevétel</th><th>Kiadás</th></tr>";
        lista.innerHTML = "<tr><th>Összeg</th><th>Kategoria</th><th>Dátum</th></tr>";
        }
        json.forEach(a => {
          if(a!=json[1] && a.sum) 
          lista2.innerHTML += "<tr><td>" + a.sum + "</td><td>" + a.income + "</td>"
              + "</td><td>" + a.outlay + "</td></tr>"
            });
        json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
              + "</td><td>" + cs.date + "</td></tr>"
            });
          }) 
    .catch (err => console.log(err))
    .finally(document.getElementById("form").reset())
}
Allpost()

// ----- Új poszt hozzáadása -----
document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    if(document.getElementById("dates").value == "")
    alert("Kérem töltse ki a dátum mezőt!")
    else if(document.getElementById("amount").value == "0")
    alert("Az összeg nem lehet nulla!")
    else if(document.getElementById("amount").value == "")
    alert("Kérem töltse ki az összeg mezőt!")
    else if(document.getElementById("categoriesID").value == "0")
    alert("Kérem töltse ki a kategória mezőt!")

    else if(Date.parse(document.getElementById("dates").value) > Date.now() )
    alert("Dátum megadása csak a mai dátumig engedélyezett!")

    else{
    const url = 'http://localhost:5000/user/all/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': token,
            "Content-type": "application/json;charset=utf-8"
        },
        body: JSON.stringify({
            "amount": document.getElementById("amount").value,
            "dates": document.getElementById("dates").value,
            "categoriesID": document.getElementById("categoriesID").value,
           
        })
    })
        .then((response) => response.json())
        .then(json => {
            document.getElementById("uzenet").innerHTML = json.message
            Allpost()
        })
        .catch(err => console.log(err));
}}

// ----- Kategoriák -----
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
        json.forEach(cs => {
            menu.innerHTML += "<option value=" + cs.ID + ">" + cs.denomination + "</option> "
        });
    })
    .catch (err => console.log(err));
})


