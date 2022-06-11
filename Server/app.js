//MODULES-MODULOK
require('dotenv').config()
const jwt= require('jsonwebtoken')
const express=require("express");
const app=express();
app.use(express.json());
const cors = require("cors");
app.use(cors())
const bcrypt = require("bcrypt");
const mysql= require('mysql');
const { json } = require('express');

//SERVER SATRTING-SZERVER INDITÁS
app.listen(5000,()=>console.log("Szerver elinditva az 5000-es porton"));

//DATABASE-ADATBÁZIS
var pool=mysql.createPool({
    host:process.env.HOST,
    port:process.env.PORT,
    user:process.env.USER,
    password:process.env.PASSWORD,
    database:process.env.DATABASE
});

//ADMIN USER-ADMIN FIÓK
const AdminNev=process.env.ADMINNAME
const AdminJelszo=process.env.ADMINPASSWORD

 //TOKEN CHECK-TOKEN ELLENÖRZÉS
 function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization']
    const token = authHeader && authHeader.split(' ')[1]
    if (!token) 
    return res.status(204).send({message: "Azonosítás szükséges!"})
    jwt.verify(token, process.env.TOKEN_SECRET, (err, user) => {
        if (err) 
        return res.status(403).send({message: "Nincs jogosultsága!"})
        req.user = user
        next()
    })
}

//CALCULATION-SZÁMITÁS
function calculation(data){
    var sum=0
    var income=0
    var outlay=0
    for (let i = 0; i < data.length; i++){
        if(data[i].amount>0)
            income+=data[i].amount
        else if(data[i].amount<0)
            outlay+=Math. abs(data[i].amount)
        sum+=data[i].amount
    }
    return {sum:sum, income:income , outlay:outlay}
}

////////////////////////////////////////////////////////USERS/ADMIN////////////////////////////////////////////////////////////////////////////////////////////

///SIGNUP-PAGE///
app.route("/signup")

    //REGISTRATION-REGISZTRÁCIÓ
    .post(function(req,res){
        const q ="select name from persons WHERE name=?"
        const q1 ="insert into persons(name,password,email,postcode,country,county,city) values(?,?,?,?,?,?,?)"
        pool.query(q,[req.body.uname],
            function(error,results){
                if(results[0] || AdminNev==req.body.uname && !error)
                    return res.status(400).send({message: "Van már ilyen nevű felhasználó!!"})
                else if(error)
                    return res.status(400).send(error)
                else{
                    const hash=bcrypt.hashSync(req.body.upassword,10)
                    pool.query(q1, [req.body.uname,hash,req.body.email,req.body.postcode,req.body.country,req.body.county, req.body.city],
                        function(error){
                            if(!error){
                                return res.status(201).send({ message: "Sikeres regisztráció"})
                            }else{
                                return res.status(500).send({ message: error })
                            }}
                )}               
            })
        })

//////LOGIN-PAGE//////
app.route("/login")

    //LOGIN-BEJELENTKEZÉS
    .post(function(req, res){
        const q ="select name,password,ID from persons WHERE name=?"
        var token=""
        pool.query(q,[req.body.uname],
        function(error,results){
            if(results[0] && !error){
               if(bcrypt.compareSync(req.body.upassword,results[0].password)){
                       token = jwt.sign({username:req.body.uname, password:results[0].password,id:results[0].ID}, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                            const k ="update persons set persons.active=CURDATE() where name=?"      
                            pool.query(k,[req.body.uname],
                            function(error){
                            if(error)
                                return res.status(400).send(error)})                          
                            return res.status(200).json({ token: token, id:results[0].ID,message: "Sikeres bejelentkezés."})}  
                else     
                      return res.status(400).send({ message: "Hibás jelszó!" })}        
            else
                if(req.body.uname==AdminNev){
                    if(bcrypt.compareSync(req.body.upassword,AdminJelszo)){
                        token = jwt.sign({username:AdminNev, password:AdminJelszo}, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                        return res.status(200).json({ token: token,message: "Sikeres bejelentkezés."})}   
                    else
                        return res.status(400).send({ message: "Hibás jelszó!" }) }
                return res.status(400).send({ message: "Hibás felhasználónév!" }) 
                })
            })

/////USERHOME-PAGE///POSTMODIFICATION-PAGE///ADMINHOME-PAGE/////
app.route("/categories")

    //ALL CATEGORIES-MINDEN KATEGORIA
    .get(authenticateToken, (req,res)=>{
        const q="select * from categories"    
        pool.query(q,function(error,results){
            if(!results[0])
                return res.status(400).send({message:"Nincs ilyen adat!"})
            else if(!error)
                    return res.status(200).send(results)
            else
                return res.status(500).send({message:error})
        })
    })
                
////////////////////////////////////////////////////////////////////////USERS///////////////////////////////////////////////////////////////////////////////////

/////USERHOME-PAGE/////
app.route("/user/all/:id")

    //USER POSTS(+CATEGORIES)-FELHASZNÁLÓ POSZTJAI (+KATEGORIÁK)
    .get(authenticateToken, (req, res) => {
        const q ="Select registers.ID, registers.amount,categories.denomination,date_format(registers.regAt,'%Y-%m-%d') as date from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where persons.name=? AND year(regAt)=year(now()) and month(regAt)=month(now());"
            pool.query(q,[req.user.username],
                function(error, posts){
                    if(!error){
                            if(!error && req.user.id!=req.params.id)
                                return res.status(400).send({message: "Hibás felhasználó!"})
                            else if(!error && posts[0]){
                                posts.splice(0, 0, calculation(posts))
                                return res.status(200).send(posts)
                                }
                            else if(!error && !posts[0])
                                return res.status(200).send({message:"Még nincs bevitt adat!"})
                            else
                                return res.status(400).send({error})
                        }
                    else   
                        return res.status(400).send({error})    
            })
    })

    //NEW POST-ÚJ POSZT 
    .post(authenticateToken,(req,res)=>{
        const q ="Insert into registers (registers.personsID, registers.amount, registers.regAt, registers.categoriesID) values (?, ?, ?, ?);"
            if(req.user.id==req.params.id){
                pool.query(q,[req.user.id, req.body.amount, req.body.dates, req.body.categoriesID],
                    function(error){
                        if(!error)
                        return res.status(201).send({message: "Sikeres hozzáadás"})
                        else
                        return res.status(500).send({message: "Hozzáadás sikertelen"})
                    })
            }else
                return res.status(400).send({message: "Hibás felhasználó!"})
    })

/////USERPOSTS-PAGE/////
app.route("/user/posts/:id/:year/:month/:day/:categories/:cost/:order/:desc")

    //SELECT POST-POSZT KIVÁLASZTÁSA
    .get(authenticateToken, (req, res) => {
        var q = String("select registers.ID, registers.amount,date_format(registers.regAt,'%Y-%m-%d') as date,categories.denomination from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where persons.name=? ")

        //date_format(registers.dates,'%Y-%M-%d') - hónapok neveinek kiirása
        if(req.params.year!=00){
            if(req.params.month!=00){
                if(req.params.day!=00)
                    var date1=String(req.params.year+"-"+req.params.month+"-"+req.params.day)
                else
                    var date1=String(req.params.year+"-"+req.params.month+"-__%")              
            }else
                    var date1=String(req.params.year+"-__%-__%")
        q+=String("and registers.regAt like '"+date1+"'")
        }
        
        if(req.params.categories!=00)
            q+=String(" and categories.ID='"+req.params.categories+"' ")

        if(req.params.cost==01)
            q+=String(" and registers.amount like '-%' ")
        else if(req.params.cost==02)
            q+=String(" and registers.amount not like '-%' ")
        else if(req.params.cost!=00)
                q+=String(" and registers.amount='"+req.params.cost+"' ")
        
        if(req.params.order=="categori")
            q+=String(" order by categories.denomination ")
        else if(req.params.order=="amount")
            q+=String(" order by registers.amount ")
        else if(req.params.order=="date")
            q+=String(" order by registers.regAt ")
        
        if(req.params.desc==1)
            q+=String(" desc")
      

            pool.query(q,[req.user.username],
            function(error, results){
                
                if(!error && results[0]){
                    results.splice(0, 0, calculation(results))
                    return res.status(200).send(results)
                    }

                else if(!error && req.user.id==req.params.id && !results[0])
                    return res.status(200).send({message: "Nincs ilyen adat!"})
                else if(!error && req.user.id!=req.params.id)
                    return res.status(400).send({message: "Hibás felhasználó!"})
                else
                    return res.status(500).send({message: error})                                   
            })
        })

/////POSTMODIFICATION-PAGE/////   
app.route("/user/post/:id/:regid")

    //ONE POST-EGY POSZT
    .get(authenticateToken, (req,res)=>{
        const q="select registers.amount, date_format(registers.regAt,'%Y-%m-%d') as date, registers.categoriesID from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where name=? and registers.ID = ?"    
        pool.query(q,[req.user.username, req.params.regid], function(error,results){
            if(!error && req.user.id!=req.params.id)
                    return res.status(400).send({message: "Hibás felhasználó!"})
            else if(!results[0])
                return res.status(400).send({message:"Nincs ilyen adat!"})
            else if(!error)
                    return res.status(200).send(results)
            else
                return res.status(500).send({message:error})
        })
    })

    //POST MODIFICATION-POSZT MODOSTITÁSA
    .put(authenticateToken, (req,res)=>{
        const q = "update registers, categories set registers.regAt=?, registers.amount=?,registers.categoriesID=? where registers.ID=?;"
        if(req.user.id==req.params.id){
            pool.query(q, [req.body.regAt , req.body.amount,req.body.categori, req.params.regid],
                function(error){
                    if(!error)
                        return res.status(200).send({message: "Modositás sikeres"})
                    else
                        return res.status(500).send({message:error})
                })
            }else
            return res.status(400).send({message: "Hibás felhasználó!"})
    })

    //DELETE POST-POSZT TÖRLÉSE
    .delete(authenticateToken, (req,res)=>{
        const q= "delete from registers where registers.ID=?"
        if(req.user.id==req.params.id){
        pool.query(q,[req.params.regid],
            function(error){
                if(!error)
                    return res.status(200).send({message: "Eltávolitás sikeres!"})
                else
                    return res.status(500).send({message:error}) 
            })
        }else
        return res.status(400).send({message: "Hibás felhasználó!"})
    })

////////////////////////////////////////////////////////USERS/ADMIN////////////////////////////////////////////////////////////////////////////////////////////

/////PERSON-PAGE/////
app.route("/user/person/:id")

    //DELETE USER-FELHASZNÁLÓ TÖRLÉSE
    .delete(authenticateToken,(req,res)=>{
        const q ="delete from persons where persons.id=?"
        if(req.user.id==req.params.id || req.user.username==AdminNev){
            pool.query(q,[req.params.id],
                function(error){
                    if(!error)
                        return res.status(200).send({message:"Felhasználó sikeresen törölve"})
                    else
                    return res.status(400).send({message:"Felhasználó törlése sikertelen!"})
                })
        }else
            return res.status(400).send({message: "Hibás felhasználó!"})
    })
    ////////////////////////////////////////////////////////////////////////USERS//////////////////////////////////////////////////////////////////////

    //USER DATA-FEHASZNÁLÓI ADATOK
    .get(authenticateToken , (req,res)=>{
        const q = "select persons.email, persons.postcode, persons.country, persons.county, persons.city from persons where persons.ID=?;"
        pool.query(q, [req.params.id],
            function(error,results){
                if(!error && req.user.id!=req.params.id)
                    return res.status(400).send({message: "Hibás felhasználó!"})
                else if(!error)
                    return res.status(200).send(results)
                else
                    return res.status(500).send({message:error}) 
            })
    })

    //MODIFICATION USER ADTA - FELHASZNÁLÓK ADATAINAK MODÓSITÁSA
    .put(authenticateToken, (req,res)=>{
        var password=req.user.password
        const q ="update persons set persons.password=?, persons.email=?, persons.postcode=?, persons.country=?, persons.county=?, persons.city=? where persons.ID=?;"
        if(bcrypt.compareSync(req.body.password,req.user.password)){
            if(req.body.newpassword!=""){
                if(!bcrypt.compareSync(req.body.newpassword,req.user.password))
                    password=bcrypt.hashSync(req.body.newpassword,10)
                else 
                    return res.status(400).send({message: "Jelszó nem lehet ugyanaz!"})
            } 
            if(req.user.id==req.params.id){
                pool.query(q,[password, req.body.email, req.body.postcode, req.body.country, req.body.county, req.body.city, req.params.id],
                    function(error){
                        if(!error)
                            return res.status(200).send({message: "Modositás sikeres"})
                        else
                            return res.status(500).send({message:error}) 
                    })
            }else
                return res.status(400).send({message: "Hibás felhasználó!"})
        }else
            return res.status(400).send({message: "Hibás jelszó!"})
    }) 

////////////////////////////////////////////////////////////////////////ADMIN///////////////////////////////////////////////////////////////////////////////////

/////ADMINHOME-PAGE/////
app.route("/admin/stat/:what/:order")

    //STATISTICS-STATISZTIKA
    .get(authenticateToken , (req,res)=>{
        if(req.params.what=="postcode"){
            var select1="persons.country , persons.county, persons.city , persons.postcode"
            var group1="postcode"
        }
        else if(req.params.what=="city"){
            var select1="persons.country , persons.county, persons.city"
            var group1="city"
        }
        else if(req.params.what=="county"){
            var select1="persons.country , persons.county"
            var group1="county"
        }
        else if(req.params.what=="country"){
            var select1="persons.country"
            var group1="country"
        }
        else
            return res.status(400).send({message: "Hibás parancs!"})
        if(req.params.order==1)
            var order1=" desc;"
        else if(req.params.order==0)
            var order1=";"
        else
            return res.status(400).send({message: "Hibás parancs!"}) 
        const q = "select " +select1+" , count(persons."+ group1 +") as db  from persons group by "+ group1 +" order by db"+order1
            pool.query(q, function(error,results){
                if(!error && req.user.username==AdminNev)
                    return res.status(200).send(results)
                else if(error)
                    return res.status(500).send({message:error}) 
                else
                    return res.status(400).send({message: "Hibás felhasználó!"})
                })
    })

    //ADD NEW KATEGORI-ÚJ KATEGORIA HOZZÁADÁSA
    .post(authenticateToken, (req,res)=>{
        const q ="insert into categories (denomination) value (?);"
        if(req.user.username==AdminNev){
            pool.query(q,[req.body.categori],
            function(error,results){
                if(!error)
                return res.status(200).send({message: "Hozzáadás sikeres!"})
                else
                return res.status(500).send({message:error}) 
            })
        }else
            return res.status(400).send({message: "Hibás felhasználó!"})

    })
    //SET CATEGORI-KATEGORIA MODÓSITÁS
    .put(authenticateToken , (req,res)=>{
        const q="update categories set denomination=? where ID=?"
        if(req.user.username==AdminNev){
            pool.query(q,[req.body.new, req.body.id],
                function(error,results){
                    if(!error)
                        return res.status(200).send({message: "Módosítás sikeres!"})
                    else
                        return res.status(500).send({message:error}) 
                    })
        }else
            return res.status(400).send({message: "Hibás felhasználó!"})
    })

/////ACCOUNTS-PAGE/////
app.route("/admin/users/:active/:order/:desc")

    //USERS-FALHASZNÁLÓK
    .get(authenticateToken,(req,res)=>{
        
        if(req.params.active=="0")
            var where1="where datediff(CURDATE(), persons.active)>90"
        else if(req.params.active=="1")
            var where1="where datediff(CURDATE(), persons.active)<=90"
        else if(req.params.active=="2")
            var where1=""
        else
            return res.status(400).send({message: "Hibás parancs!"})

        if(req.params.order=="name")
            var order1="name"
        else if(req.params.order=="postcode")
            var order1="postcode"
        else if(req.params.order=="country")
            var order1="country"
        else if(req.params.order=="county")
            var order1="county"
        else if(req.params.order=="city")
            var order1="city"
        else
            return res.status(400).send({message: "Hibás parancs!"})
        
        if(req.params.desc=="0")
            var desc1=""
        else if(req.params.desc=="1")
            var desc1=" desc"
        else
            return res.status(400).send({message: "Hibás parancs!"})

        const q= "select name,email,postcode,country,county,city, date_format(persons.active,'%Y-%m-%d') as date from persons " + where1 +" order by persons." + order1 + desc1
            pool.query(q,
                function(error,results){
                    if(!error && req.user.username==AdminNev && results[0]){
                        var sum=results.length
                        return res.status(200).send({users:results, number:sum})
                    }else if(!error && req.user.username==AdminNev && !results[0]){
                        return res.status(200).send({message:"Nincs ilyen felhasználó"})
                    }else if(error)
                        return res.status(500).send({message:error})
                    else
                        return res.status(400).send({message: "Hibás felhasználó!"})
                })          
    })

    //DELETE ALL PASSIV USERS-MINDEN PASSZIV FELHASZNÁLÓ TÖRÉLÉSE
    .delete(authenticateToken , (req,res)=>{
        const q ="delete from persons where datediff(CURDATE(), persons.active)>90;"
        if(req.user.username==AdminNev){
            pool.query(q, function(error,results){
                if(!error)
                    return res.status(200).send({message: "Törlés sikeres!"})
                else
                    return res.status(500).send({message:error}) 
            })
        }else
            return res.status(400).send({message: "Hibás felhasználó!"})
    })

/*
hibakodok
200 -ok
201 -létrehozva
204 -feldolgozva, de üres/nincs visszaadott érték
400 -ügyfél hiba
403 -server megértette de megtagatta a kérést(nincs jogosultság)
500 -szerver hiba 
*/