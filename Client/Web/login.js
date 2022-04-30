// Lekérdezés
window.addEventListener("load", function Allpost(){
    const url = 'http://localhost:5000/Users/AllPost';
    // const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
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
        .catch(err => console.log(err));
})



// Lekérdezés
window.addEventListener("load", function Allpost1(){
    const url = 'http://localhost:5000/Users/AllPost';
     const token = 'Bearer: ' + sessionStorage.token
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
        if (ok) {
            json.forEach(p => {
                lista.innerHTML += "<td>" + p.amount + " " + p.date + "</td>"
            })
        } else {
            document.getElementById("uzenet").innerHTML = json.message
        }
    })
        .catch(err => console.log(err));
})
