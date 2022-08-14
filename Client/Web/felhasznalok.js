document.getElementById("gomb").onclick = function Keres(e) {
    e.preventDefault();
    const url = 'http://localhost:5000/admin/users' + "/" + document.getElementById("active").value + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista")
    const össz = document.getElementById("össz")
    const delet = document.getElementById("delet")
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
        if(json[0]!=null){
        lista.innerHTML = "<tr><th>Név</th><th>E-mail</th><th>Ország</th><th>Megye</th><th>Város</th><th>Irányítószám</th><th>Dátum</th><th></th></tr>";
        json.forEach(cs => {
            if(cs!=json[0]) {
                lista.innerHTML += "<tr><td>" + cs.name + "</td><td>" + cs.email + "</td><td>" + cs.country + "</td><td>" + cs.county + "</td>" +
                "<td>" + cs.city + "</td><td>" + cs.postcode + "</td><td>" + cs.date + "</td><td><button class='btn btn-danger' id='"+ cs.id + "' onClick='reply_click(this.id)'>X</button></td></tr>"
            }
            else if(cs==json[0]){
                if(document.getElementById("active").value == "0"){
                    össz.innerHTML = "<h4>Passzív felhasználók száma: " + cs.number + "</h4>",
                    delet.innerHTML = "<button id='delete' class='button btn btn-danger' onclick='Töröl()'>Passzív felhasználók törlése</button>"
                }else if(document.getElementById("active").value == "1"){
                    össz.innerHTML = "<h4>Aktív felhasználók száma: " + cs.number + "</h4>"
                }else if(document.getElementById("active").value == "2"){
                    össz.innerHTML = "<h4>Felhasználók száma: " + cs.number + "</h4>"
                }
            } 
        });
    }else{
        össz.innerHTML = "<h4>Nincs felhasználó!</h4>"
    }
        })
        .catch (err => console.log(err));
}

// ----- Felhasználó ID -----
function reply_click(clicked_id){
    ID = clicked_id;
}

// ---- Felhasználó törlése ----
function Töröl() {

    if (confirm("Biztosan törölni szeretné?") == true) {
        const url = 'http://localhost:5000/admin/users' + "/" + document.getElementById("active").value + "/" + document.getElementById("order").value + "/" + document.getElementById("desc").value;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': token
        },
    })
    
        .then((response) => response.json())
        .then((json) => {
            document.getElementById("uzenet").innerHTML = json.message
            setTimeout(function a() {
                location.reload()
            }, 1500)
    })
    .catch(err => console.log(err));
     
}else{

}
}