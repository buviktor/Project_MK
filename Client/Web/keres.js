let ID = "";

window.addEventListener("load", function Allpost() {
    const url = 'http://localhost:5000/user/all/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })
    .then((response) => response.json())
    .then(json => 
        json.forEach(cs => {
            if(cs!=json[0]) 
            lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
                + "</td><td>" + cs.date + "</td><td><button class='btn btn-primary button' id='"+ cs.ID + "' onClick='reply_click(this.id)' >...</button></td></tr>"
              }),
              )
    .catch (err => console.log(err))
})

function reply_click(clicked_id)
  {
      ID = clicked_id;
  }


/////USERPOSTS-PAGE/////

document.getElementById("gomb2").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/user/posts' + "/" + sessionStorage.id + "/" + document.getElementById("datesy").value + "/" + document.getElementById("datesm").value + "/" + document.getElementById("dated").value + "/" + document.getElementById("categoriesID").value + "/" + document.getElementById("cost").value + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
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
      lista.innerHTML = "<tr><th>Összeg</th><th>Kategoria</th><th>Dátum</th><th></th></tr>";
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
              + "</td><td>" + cs.date + "</td><td><button id='"+ cs.ID + "'></button></td></tr>"
            });
        })
        .catch (err => console.log(err));
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
        json.forEach(cs => {
            menu.innerHTML += "<option value=" + cs.ID + ">" + cs.denomination + "</option> "
            });
          })
    .catch (err => console.log(err))
})

//Módosítandó poszt megjelenítése

ID.onclick = function (f) {
    e.preventDefault();
    const url = 'http://localhost:5000/user/post/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': token,
        }
    })
        .then((response) => response.json())
        .then((json) => {
            document.getElementById("uzenet").innerHTML = json.message
            document.location = "post.html"
        })
        .catch(err => console.log(err));

}

