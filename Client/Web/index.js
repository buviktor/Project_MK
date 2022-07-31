document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    let ok = false
    let uname = document.getElementById("uname").value;

    if(uname == "") {
        alert("Kérem adja meg a felhasználó nevet és a jelszót!")
    } else {
    const url = 'http://localhost:5000/login'; 
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
                setTimeout(function a(){
                if (uname == "Admin"){
                 document.location = "adminhome.html" 
                } else {
                    document.location = "user.html" 
                }
            },1000)
            } 
             
        })    
        .catch(err =>console.log(err))
}
}

const tabEl = document.getElementById('myTab')
tabEl.addEventListener('shown.bs.tab', event => {
  event.target // newly activated tab
  event.relatedTarget // previous active tab
})
