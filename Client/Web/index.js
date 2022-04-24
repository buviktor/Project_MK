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
            if (ok) document.location = "login"  //Ez lesz az az oldal amin majd megjelennek a kimutatások.
        })
        .catch(err =>console.log(err));
}