// Lekérdezés
function Allpost() {
    const url = 'http://localhost:5000/user/all/1' /*+ sessionStorage.id*/;
    const token = 'Bearer: ' + sessionStorage.token
    const lista= document.getElementById("lista");

    fetch(url, { 
        method: "GET",
        headers: {'Authorization': token}
      })
        .then(response => {ok = response.ok; return response.json()})
            
        .then(json => {lista.innerHTML = json})
        /*.then((json) => {
            if (ok) {
                lista.innerHTML = "";
                json.forEach(f => { lista.innerHTML = f
                    /*+= "<tr>" +"<td>" 
                        + f.amount + "</td>" 
                        + "<td>" + f.denomination 
                        + "</td>" + "<td>" + f.date + "</td>" + "</tr>"  */
        /*})}})*/
        
        .catch (err => console.log(err));
}

Allpost();

