window.addEventListener("load", function Adatok() {
    const url = 'http://localhost:5000/user/person/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("adatok");
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

document.getElementById("gomb4").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/user/person'+ sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'PUT',
        headers: {
            'Authorization': token
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
