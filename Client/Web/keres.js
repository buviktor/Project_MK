let ID = "";
let cost = document.getElementById("cost").value;

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
document.getElementById("cost").onchange = function (a){
    a.preventDefault();
    if(document.getElementById("cost").value == 3){
        document.getElementById("egyeb").innerHTML = "";
        document.getElementById("egyeb").innerHTML = '<input id="szam" type="number" class="form-select">'
    } else {
        document.getElementById("egyeb").innerHTML = "";
        document.getElementById("egyeb").innerHTML = '<input id="szam" type="number" class="form-select" disabled>'
        cost = document.getElementById("cost").value;
    }
}
document.getElementById("napok").onchange = function (a){
    a.preventDefault();
    if  (document.getElementById("napok").value){
        document.getElementById("days").innerHTML = "";
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" disabled>'
    } 
    else if (!document.getElementById("napok").value) {
        document.getElementById("days").innerHTML = "";
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" value="01" min="1" max="31" id="dated" onchange="if(parseInt(this.value,10)<10)this.value="0"+this.value;" placeholder="Válasszon napot!">'
    }
}

document.getElementById("gomb2").onclick = function (e) {
    e.preventDefault();
    if  (document.getElementById("cost").value == 3){
        cost = document.getElementById("szam").value;
    }
    const url = 'http://localhost:5000/user/posts' + "/" + sessionStorage.id + "/" + document.getElementById("datesy").value + "/" + document.getElementById("datesm").value + "/" + document.getElementById("dated").value + "/" + document.getElementById("categoriesID").value + "/" + cost + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => {
        ok = response.ok
        return response.json()    
    })
    .then(json => {
      lista.innerHTML = "<tr><th>Összeg</th><th>Kategoria</th><th>Dátum</th><th></th></tr>";
      if (ok && json[0] != null) {
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
              + "</td><td>" + cs.date + "</td><td><button id='"+ cs.ID + "'></button></td></tr>"
            });
        } else {
            document.getElementById("uzenet").innerHTML = "Nincs ilyen adat!"
        }
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

