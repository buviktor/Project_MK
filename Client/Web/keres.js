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
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" id="dated" disabled>';
    } 
    else {
        document.getElementById("days").innerHTML = "";
        document.getElementById("days").innerHTML = '<input class="form-select "type="number" value="1" min="1" max="31" id="dated" placeholder="Válasszon napot!">'
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
    const lista = document.getElementById("lista");
    const lista2 = document.getElementById("lista2");
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
        if (ok && json[1] != null) {
        lista2.innerHTML = "<tr><th>Összegzés</th><th>Bevétel</th><th>Kiadás</th></tr>";
        json.forEach(a => {
          if(a!=json[1] && a.sum) 
          lista2.innerHTML += "<tr><td>" + a.sum + "</td><td>" + a.income + "</td>"
              + "</td><td>" + a.outlay + "</td></tr>"
            });
        } 
      if (ok && json[0] != null) {
        lista.innerHTML = "<tr><th>Összeg</th><th>Kategória</th><th>Dátum</th><th></th></tr>";
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
              + "</td><td>" + cs.date + "</td><td><button class='btn btn-primary button' id='"+ cs.ID + "' onClick='reply_click(this.id),Post()' >...</button></td></tr>"
            });
            document.getElementById("uzenet").innerHTML = ""
        } else {
            document.getElementById("uzenet").innerHTML = "Nincs ilyen adat!"
            lista2.innerHTML = "<tr><th>Összegzés</th><th>Bevétel</th><th>Kiadás</th></tr>";
            lista.innerHTML = "<tr><th>Összeg</th><th>Kategória</th><th>Dátum</th><th></th></tr>";
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

function Post() {
    document.location = "post.html"
    sessionStorage.regid = ID;
}