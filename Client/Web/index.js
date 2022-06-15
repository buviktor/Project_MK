// Bejelentkezés
document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();

    document.getElementById("gomb1").onclick = function (a) {
        if(document.getElementById(text.id).value =="")
            return alert
    }

    let ok = false
    const url = 'http://localhost:5000/login';
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            "uname": document.getElementById("uname").value,
            "upassword": document.getElementById("upassword").value
        })
    })
        .then((response) => {
            ok = response.ok
            return response.json()    
        })
        .then((json) => {
            sessionStorage.token = json.token
            sessionStorage.id = json.id
            document.getElementById("uzenet").innerHTML = json.message
            if (ok) document.location = "user.html"  //Ez lesz az az oldal amin majd megjelennek a kimutatások.
        })    
        .catch(err =>console.log(err))
}

