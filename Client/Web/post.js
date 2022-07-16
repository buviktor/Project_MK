window.addEventListener("load", function Post() {
    const url = 'http://localhost:5000/user/post/' + "/" + sessionStorage.id + "/" +  sessionStorage.regid;
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
            lista.innerHTML = "<tr><th>Összeg</th><th>Dátum</th><th>Kategória</th></tr>";
            json.forEach(cs => {
                lista.innerHTML += "<tr><td>" + cs.amount + "</td><td>" + cs.date + "</td>"
                    + "</td><td>" + cs.categoriesID + "</td></tr>";
                  });   
})
        .catch(err => console.log(err))
})