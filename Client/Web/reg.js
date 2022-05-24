// Regisztráció
document.getElementById("gomb").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/signup';
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            "uname": document.getElementById("uname").value,
            "upassword": document.getElementById("upassword").value,
            "email": document.getElementById("email").value,
            "postcode": document.getElementById("postcode").value,
            "country": document.getElementById("country").value,
            "county": document.getElementById("county").value,
            "city": document.getElementById("city").value
        })
    })
        .then((response) => response.json())
        .then(json => document.getElementById("uzenet").innerHTML = json.message)
        .catch(err => console.log(err));
}