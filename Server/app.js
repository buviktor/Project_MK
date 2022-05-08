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

app.listen(5000,()=>console.log("Szerver elinditva az 5000-es porton"));

var pool=mysql.createPool({
    host:process.env.HOST,
    port:process.env.PORT,
    user:process.env.USER,
    password:process.env.PASSWORD,
    database:process.env.DATABASE
});

const AdminNev=process.env.ADMINNAME
const AdminJelszo=process.env.ADMINPASSWORD

//regisztráció---.fnev && fjelszo
app.post('/SignUp', (req,res)=>{
    const q ="select name from persons WHERE name=?"
    const k ="insert into persons(name,password,email,postcode,country,county,city) values(?,?,?,?,?,?,?)"
    pool.query(q,[req.body.fnev],
        function(error,results){
            if(results[0] || error || AdminNev==req.body.fnev)
            return res.status(400).send({message: "Van már ilyen nevű felhasználó!!"})
            else{
                const hash=bcrypt.hashSync(req.body.fjelszo,10)
                pool.query(k, [req.body.fnev,hash,req.body.email,req.body.postcode,req.body.country,req.body.county, req.body.city],
                    function(error){
                        if(!error){
                            return res.status(201).send({ message: "Sikeres regisztráció"})
                        }else{
                            return res.status(500).send({ message: error })
                        }}
            )}
                
        })
    })
    //Bejelentkezés--->fnev && fjelszo
    app.post('/user/login', (req, res) => {
        const q ="select name,password,ID from persons WHERE name=?"
        var token=""
        pool.query(q,[req.body.fnev],
        function(error,results){
            if(results[0] && !error){
               if(bcrypt.compareSync(req.body.fjelszo,results[0].password)){
                       token = jwt.sign({username:req.body.fnev, password:results[0].password,id:results[0].ID}, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                            const k ="update persons set persons.active=CURDATE() where name=?"      
                            pool.query(k,[req.body.fnev],
                            function(error){
                            if(error)
                                return res.status(400).send(error)})                          
                            return res.status(200).json({ token: token, message: "Sikeres bejelentkezés."})}  
                else     
                      return res.status(400).send({ message: "Hibás jelszó!" })}        
            else
                if(req.body.fnev==AdminNev){
                    if(bcrypt.compareSync(req.body.fjelszo,AdminJelszo)){
                        token = jwt.sign({username:AdminNev, password:AdminJelszo}, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                        return res.status(200).json({ token: token, message: "Sikeres bejelentkezés."})}   
                    else
                        return res.status(400).send({ message: "Hibás jelszó!" }) }
                return res.status(400).send({ message: "Hibás felhasználónév!" }) 
                })
            })
                

 //token ellenörzése
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


//felhasználó posztjainak egyéni kilistázása (védett)
app.post('/Users/AllPost', authenticateToken, (req, res) => {
    var q = String("select registers.ID, registers.amount,date_format(registers.regAt,'%Y-%m-%d') as date,categories.denomination from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where persons.name=? ")
    const osszeg=String(" and registers.amount='"+req.body.osszeg+"' ")
    const kategoria=String(" and categories.denomination='"+req.body.kategoria+"'")
//date_format(registers.dates,'%Y-%M-%d') - hónapok neveinek kiirása
        if(req.body.year!=00){
            if(req.body.month!=00){
                if(req.body.day!=00)
                    var date1=String(req.body.year+"-"+req.body.month+"-"+req.body.day)
                else
                    var date1=String(req.body.year+"-"+req.body.month+"-__%")
                }
            else
                var date1=String(req.body.year+"-__%-__%")
        }
    const datum=String("and registers.regAt like '"+date1+"'")

    if(req.body.year!=00)
    q+=datum
    if(req.body.osszeg!=00)
        q+=osszeg
    if(req.body.kategoria!=00)
        q+=kategoria
    pool.query(q,[req.user.username],
        function(error, results){
            if(!error)
            return res.status(200).send(results)
            else
            return res.status(500).send({message: error})
                                        
    })
    })


//felhasználó összes posztjának listázása(védett)
app.get('/Users/AllPost', authenticateToken, (req, res) => {
    var q ="Select registers.ID, registers.amount,categories.denomination,date_format(registers.regAt,'%Y-%m-%d') as date from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where persons.name=?"
    pool.query(q,[req.user.username],
        function(error, results){
            if(!error)
            return res.status(200).send(results)
            else
            return res.status(500).send({message: error})
                                        
    })
})


//új poszt hozzáadása meglévó kategoriához 
app.post('/Post/New', authenticateToken,(req,res)=>{
    const q ="Insert into registers (registers.personsID, registers.amount, registers.regAt, registers.categoriesID) values (?, ?, ?, ?); "
    pool.query(q,[req.user.id, req.body.amount, req.body.dates, req.body.categoriesID],
        function(error){
            if(!error)
               return res.status(201).send({message: "Sikeres hozzáadás"})
            else
            return res.status(500).send({message: "Hozzáadás sikertelen"})
        })
})

//Minden kategoria kiirása
app.get('/Categories',(req,res)=>{
    const q="select * from categories;"
    pool.query(q,function(error,results){
        if(!error && results[0])
            return res.status(200).send(results)
        else
            return res.status(400).send({error})
    })
})


//felhasználó törlése
app.post('/Delete', authenticateToken,(req,res)=>{
    const q ="delete from persons where persons.name=?"
    pool.query(q,[req.body.fiok],
        function(error){
            if(!error)
                return res.status(200).send({message:"Felhasználó sikeresen törölve"})
            else
            return res.status(400).send({message:"Felhasználó törlése sikertelen!"})
        })
    
})

//Felhasználók kilistázása
app.get('/Admin/Users', authenticateToken,(req,res)=>{
    const q= "select name,email,postcode,country,county,city, date_format(persons.active,'%Y-%m-%d') as date from persons"
    if(req.user.username==AdminNev && req.user.password==AdminJelszo)
    pool.query(q,
        function(error,results){
            if(!error)
                return res.status(200).send(results)
            else
                return res.status(500).send({message:error})
        })
})
//datediff(CURDATE(), active) as passiv

//egy poszt megjelenitése modositáshoz
app.post('/SetData',authenticateToken, (req,res)=>{
    const q="select registers.amount, date_format(registers.regAt,'%Y-%m-%d') as date, registers.categoriesID from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where name=? and registers.ID = ?"
     
    pool.query(q,[req.user.username, req.body.id ], function(error,results){
        if(!error)
                return res.status(200).send(results)
        else
            return res.status(500).send({message:error})
    })
})

//adatok modositása(poszt)
app.put('/SetData/Update', authenticateToken, (req,res)=>{
    const q = "update registers set registers.regAt=?, registers.amount=? where registers.ID=?;"
    pool.query(q, [req.body.regAt , req.body.amount, req.body.id],
        function(error){
            if(!error)
                return res.status(200).send({message: "Modositás sikeres"})
            else
                return res.status(500).send({message:error})
        
        })
})
//poszt kategoriájának modositása
app.put('/SetData/Categories', authenticateToken, (req,res)=>{
    const q= "update registers set registers.categoriesID=? where registers.ID=?;"
    pool.query(q, [req.body.catID, req.body.id],
        function(error){
            if(!error)
                return res.status(200).send({message: "Modositás sikeres"})
            else
                return res.status(500).send({message:error}) 
        })
})
//poszt törlése
app.post('/Delete/Post', authenticateToken, (req,res)=>{
    const q= "delete from registers where registers.ID=?"
    pool.query(q,[req.body.id],
        function(error){
            if(!error)
                return res.status(200).send({message: "Eltávolitás sikeres!"})
            else
                return res.status(500).send({message:error}) 
        })
})
//Felhasználó személyes datakok kiirása(modositáshoz)
app.get('/Setting', authenticateToken , (req,res)=>{
    const q = "select persons.email, persons.postcode, persons.country, persons.county, persons.city from persons where persons.ID=?;"
    pool.query(q, [req.user.id],
        function(error,results){
            if(!error)
                return res.status(200).send(results)
            else
                return res.status(500).send({message:error}) 
        })
})
//személyes adatok modositása
app.put('/Setting/Update', authenticateToken, (req,res)=>{
    const q ="update persons set persons.email=?, persons.postcode=?, persons.country=?, persons.county=?, persons.city=? where persons.ID=?;"
    pool.query(q,[req.body.email, req.body.postcode, req.body.country, req.body.county, req.body.city, req.user.id],
        function(error){
            if(!error)
                return res.status(200).send({message: "Modositás sikeres"})
            else
                return res.status(500).send({message:error}) 
        })
})
//Admin statisztika
app.get('/Admin/Login', authenticateToken , (req,res)=>{
    const q = "select persons.country , persons.county, persons.city , persons.postcode, count(persons.postcode) as db  from persons group by city order by country, county,city, postcode;"
    pool.query(q, function(error,results){
        if(!error)
        return res.status(200).send(results)
    else
        return res.status(500).send({message:error}) 
    })
})

//Aktiv/passziv felhasználók kiirása
app.get('/Admin/Active' , authenticateToken , (req,res)=>{
    const q = "select count(name) as active from persons GROUP by datediff(CURDATE(), persons.active)>90;"
    pool.query(q,function(error,results){
        if(!error)
        return res.status(200).send(results)
    else
        return res.status(500).send({message:error}) 
    })
})
//Kategoria hozzáadása
app.post('/Admin/Add', authenticateToken, (req,res)=>{
    const q ="insert into categories (denomination) value (?);"
    pool.query(q,[req.body.categori],
    function(error,results){
        if(!error)
        return res.status(200).send({message: "Hozzáadás sikeres!"})
        else
        return res.status(500).send({message:error}) 
    })
})
//Kategoria modositása
app.put('/Admin/Set', authenticateToken , (req,res)=>{
    const q="update categories set denomination=? where denomination=?"
    pool.query(q,[req.body.uj, req.body.regi],
        function(error,results){
        if(!error)
        return res.status(200).send({message: "Módosítás sikeres!"})
        else
        return res.status(500).send({message:error}) 
        })
})

//Jelszó módositása bcrypt.compareSync(req.body.regijelszo,req.user.password)
app.put('/Update/Password', authenticateToken, (req,res)=>{
    const q="update persons set password=? where name=?"
    const hash=bcrypt.hashSync(req.body.fjelszo,10)
    if(bcrypt.compareSync(req.body.regijelszo,req.user.password)){
        if(!bcrypt.compareSync(req.body.fjelszo,req.user.password)){ 
            pool.query(q,[hash, req.user.username],
                function(error, results){
                    if(!error)
                    return res.status(200).send({message: "Módosítás sikeres!"})
                    else
                    return res.status(400).send({message:error}) })
                }else
        return res.status(400).send({message: "Jelszó nem lehet ugyanaz!"})
            }
    else
        return res.status(400).send({message: "Hibás jelszó!"})     
})
//Összes passziv felhasználó törlsése
app.get('/Admin/Delete/Passiv' , authenticateToken , (req,res)=>{
    const q ="delete from persons where datediff(CURDATE(), persons.active)>90;"
    pool.query(q, function(error,results){
        if(!error)
                return res.status(200).send({message: "Törlés sikeres!"})
                else
                return res.status(500).send({message:error}) 
    })
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