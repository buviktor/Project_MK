// Regisztráció
document.getElementById("gomb").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/SignUp';
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            "fname": document.getElementById("fnev").value,
            "felszo": document.getElementById("fjelszo").value
        })
    })
        .then((response) => response.json())
        .then(json => document.getElementById("uzenet").innerHTML = json.message)
        .catch(err => console.log(err));
}