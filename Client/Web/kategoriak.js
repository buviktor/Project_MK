let mutat = false;

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
        ID = this.sessionStorage.id
        menu.innerHTML = "<tr><th>Kategóriák</th></tr>";
        json.forEach(cs => {
            menu.innerHTML += "<tr><td>"+ cs.denomination + "</td><td><button class='btn btn-primary button' id='"+ cs.ID + "' onClick='reply_click(this.id),Mutat()' >...</button></td></tr>"
            });
          }
          )
    .catch (err => console.log(err));
}
)

function reply_click(clicked_id)
  {
      ID = clicked_id;
  }

window.addEventListener("load", function() {
    document.getElementById("kat").style.display ="none"
});

function Mutat() {
    if(mutat == false) {
        document.getElementById("kat").style.display = "block";
        mutat = true;
    }
    else{
        document.getElementById("kat").style.display = "none";
        mutat = false;
    }
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

document.getElementById("gomb2").onclick = function (e) {
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
            "new": document.getElementById("newcategori").value,
            "id": ID
        })
    })
        .then((response) => response.json())
        .then(json => document.getElementById("uzenet").innerHTML = json.message)
        .catch(err => console.log(err));
}