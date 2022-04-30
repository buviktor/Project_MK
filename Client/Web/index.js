// // Regisztráció
// document.getElementById("gomb").onclick = function (e) {
//     e.preventDefault();
//     const url = 'http://localhost:5000/SignUp';
//     fetch(url, {
//         method: 'POST',
//         headers: {
//             'Content-type': 'application/json;charset=utf-8'
//         },
//         body: JSON.stringify({
//             "fname": document.getElementById("fnev").value,
//             "felszo": document.getElementById("fjelszo").value
//         })
//     })
//         .then((response) => response.json())
//         .then(json => document.getElementById("uzenet").innerHTML = json.message)
//         .catch(err => console.log(err));
// }

// Bejelentkezés
document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    let ok = false
    const url = 'http://localhost:5000/user/login';
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            "fnev": document.getElementById("fnev").value,
            "fjelszo": document.getElementById("fjelszo").value
        })
    })
        .then((response) => {
            ok = response.ok
            return response.json()    
        })
        .then((json) => {
            sessionStorage.token = json.token
            document.getElementById("uzenet").innerHTML = json.message
            if (ok) document.location = "login.html"  //Ez lesz az az oldal amin majd megjelennek a kimutatások.
        })    
        .then((json) => {
            function AllPost(e)
        })
        .catch(err =>console.log(err))
}

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
