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
                + "</td><td>" + cs.date + "</td><td><button class='btn btn-primary button' id='"+ cs.ID + "' onClick='reply_click(this.id),Post()' >...</button></td></tr>"
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
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" value="01" min="1" max="31" id="dated" onchange="if(parseInt(this.value,10)<10)this.value="0"+this.value;" placeholder="V??lasszon napot!">'
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
      lista.innerHTML = "<tr><th>??sszeg</th><th>Kategoria</th><th>D??tum</th><th></th></tr>";
      if (ok && json[0] != null) {
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
              + "</td><td>" + cs.date + "</td><td><button class='btn btn-primary button' id='"+ cs.ID + "' onClick='reply_click(this.id)' >...</button></td></tr>"
            });
        } else {
            document.getElementById("uzenet").innerHTML = "Nincs ilyen adat!"
        }
    })
        .catch (err => console.log(err));
}

//Kategori??k
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

//M??dos??tand?? poszt megjelen??t??se

function Post() {
    document.location = "post.html"
    sessionStorage.regid = ID;
}




