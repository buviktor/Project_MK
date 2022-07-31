//---- Adatok módosítása ----

window.addEventListener("load", function Adatok() {
    const url = 'http://localhost:5000/user/person/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    const adatok = document.getElementById("adatok");
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })
        .then((response) => response.json())
        .then(json => {
            adatok.innerHTML = "<tr><th>Email</th><th>Irányítószám</th><th>Ország</th><th>Megye</th><th>Város</h></tr>";
            json.forEach(cs => {
                adatok.innerHTML += "<tr><td>" + cs.email + "</td><td>" + cs.postcode + "</td>"
                    + "</td><td>" + cs.country + "</td><td>" + cs.county + "</td><td>" + cs.city + "</td></tr>";
                }); 
        })
        .catch(err => console.log(err))
})

// ---- Adatok módosítása ----

document.getElementById("gomb4").onclick = function (e) {
    e.preventDefault();

    if (document.getElementById("email").value === "" && document.getElementById("postcode").value === "" && document.getElementById("postcode").value === "" && 
        document.getElementById("country").value === "" && document.getElementById("city").value === "" && document.getElementById("password").value === "")
    {
        alert("Kérjük minden mezőt töltsön ki!")
    } else {
    const url = 'http://localhost:5000/user/person' + "/" + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': token,
            "Content-type": "application/json;charset=utf-8"
        },
        body: JSON.stringify({
            "newpassword": document.getElementById("newpassword").value,
            "email": document.getElementById("email").value,
            "postcode": document.getElementById("postcode").value,
            "country": document.getElementById("country").value,
            "county": document.getElementById("county").value,
            "city": document.getElementById("city").value,
            "password": document.getElementById("password").value
        })
    })
    .then((response) => response.json())
    .then(json => document.getElementById("uzenet").innerHTML = json.message)
    .catch(err => console.log(err));
}
}


let mutat = false;

window.addEventListener("load", function() {
    document.getElementById("madatok").style.display ="none"
});

document.getElementById("gomb5").onclick = function Mutat(){
    if(mutat == false) {
        document.getElementById("madatok").style.display = "block";
        mutat = true;
    }
    else{
        document.getElementById("madatok").style.display = "none";
        mutat = false;
    }
}

// ---- Felhasználó törlése ----

document.getElementById("gomb6").onclick = function (e) {
    e.preventDefault();

    if (confirm("Biztosan törölni szeretné?") == true) {

    const url = 'http://localhost:5000/user/person/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': token,
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({

        })
    })
        .then((response) => response.json())
        .then((json) => {
            document.getElementById("uzenet").innerHTML = json.message
            setTimeout(function a() {
                document.location = "index.html"
        }, 1500)
    })
    .catch(err => console.log(err));
}else {
}
}

