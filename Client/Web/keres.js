let ID = "";
let cost = document.getElementById("cost").value;
let dated;

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
    if  (this.checked){
        document.getElementById("days").innerHTML = "";
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" disabled>';
    } 
    else {
        document.getElementById("days").innerHTML = "";
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" value="01" min="1" max="31" id="dated" placeholder="Válasszon napot!">'
    }

}
    
document.getElementById("gomb2").onclick = function (e) {
    e.preventDefault();
    if  (document.getElementById("cost").value == 3){
        cost = document.getElementById("szam").value;
    }

    // összes napok figyelése és helyes formátumú érték küldése
    if (!document.getElementById("napok").checked) {
        dated = document.getElementById("dated").value;
        if(parseInt(dated)<10) dated = "0" + dated;
    }else{
        dated = document.getElementById("napok").value;
    }

    const url = 'http://localhost:5000/user/posts' + "/" + sessionStorage.id + "/" + document.getElementById("datesy").value + "/" + document.getElementById("datesm").value + "/" + dated + "/" + document.getElementById("categoriesID").value + "/" + cost + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
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
              + "</td><td>" + cs.date + "</td><td><button class='btn btn-primary button' id='"+ cs.ID + "' onClick='reply_click(this.id),Post()' >...</button></td></tr>"
            });
            document.getElementById("uzenet").innerHTML = ""
        } else {
            document.getElementById("uzenet").innerHTML = "Nincs ilyen adat!"
        }
    })
        .catch (err => console.log(err));
}

function reply_click(clicked_id)
  {
      ID = clicked_id;
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

function Post() {
    document.location = "post.html"
    sessionStorage.regid = ID;
}




