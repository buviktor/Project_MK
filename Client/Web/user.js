window.addEventListener("load", function Allpost() {
    const url = 'http://localhost:5000/user/all/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    const lista = document.getElementById("lista");
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': token
        }
    })

    .then((response) => response.json())
    .then(json => {
      lista.innerHTML = "<tr><th>Összeg</th><th>Kategoria</th><th>Dátum</th></tr>";
      json.forEach(cs => {
          if(cs!=json[0]) 
          lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.denomination + "</td>"
              + "</td><td>" + cs.date + "</td>"+"</tr>"
            });
          })



        .catch (err => console.log(err));
})

//Új poszt hozzáadása
document.getElementById("gomb1").onclick = function (e) {
    e.preventDefault();
    const url = 'http://localhost:5000/all/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': token
        },
        body: JSON.stringify({
            "amount": document.getElementById("amount").value,
            "dates": document.getElementById("dates").value,
            "categoriesID": document.getElementById("categoriesID").value,
           
        })
    })
        .then((response) => response.json())
        .then(json => document.getElementById("uzenet").innerHTML = json.message)
        .catch(err => console.log(err));
}