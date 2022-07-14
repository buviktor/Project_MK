document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    let ok = false
    const url = 'http://localhost:5000/login'; 
    let uname = document.getElementById("uname").value;
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            "uname" : uname,
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
            if (ok){
                if (uname == "Admin"){
                 document.location = "adminhome.html" 
                } else {
                    document.location = "user.html" 
                }
            } 
             
        })    
        .catch(err =>console.log(err))
}

