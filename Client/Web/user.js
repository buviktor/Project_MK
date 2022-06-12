// Lekérdezés
window.addEventListener("load", function Allpost() {
    const url = 'http://localhost:5000/user/all/' + sessionStorage.id;
    const token = 'Bearer: ' + sessionStorage.token
    const lista= document.getElementById("lista");
    fetch(url, {
        method: 'GET'
    })
        .then((response) => response.json())

        lista.innerHTML = ""; 
        json.forEach(f => { lista.innerHTML += "<tr>" +"<td>" + f.amount + "</td>" + "<td>" + f.denomination + "</td>" + "<td>" + f.date + "</td>" + "</tr>"  

    })
        .catch (err => console.log(err));
})



