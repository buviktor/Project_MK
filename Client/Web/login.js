// Lekérdezés
function Allpost(e) {
    e.preventDefault();
    const url = 'http://localhost:5000/AllPost';
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        },
        body: JSON.stringify({
            "osszeg": "00",
            "kategoria": "00",
            "year": "00",
            "month": "00",
            "day": "00"
        })
    })
    .then((response) => {
        ok = response.ok
        return response.json()
    })
    .then(json => {
        if (ok) {
            lista.innerHTML = "";
            json.forEach(p => {
                lista.innerHTML += "<td>" + p.amount + " " + p.date + "</td>"
            })
        } else {
            document.getElementById("uzenet").innerHTML = json.message
        }
    }
    )
        .catch(err => console.log(err));
}
