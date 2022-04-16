document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    let ok = false
    const url = 'http://localhost:5000/index';
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            "fnev": document.getElementById("fnev").value,
            "jelszo": document.getElementById("jelszo").value
        })
    })
        .then((response) => {
            ok = response.ok
            return response.json()    
        })
        .then((response) => {
            sessionStorage.token = json.token
            if (ok) document.location = "login" //Ez lesz az az oldal amin majd megjelennek a kimutatások.
        })
        .catch(err =>console.log(err));
}