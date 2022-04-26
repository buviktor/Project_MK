require('dotenv').config()
const jwt= require('jsonwebtoken')
const express=require("express");
const app=express();
app.use(express.json());
const cors = require("cors");
app.use(cors())
const bcrypt = require("bcrypt");
const mysql= require('mysql');

app.listen(5000,()=>console.log("Szerver elinditva az 5000-es porton"));

var pool=mysql.createPool({
    host:process.env.HOST,
    port:process.env.PORT,
    user:process.env.USER,
    password:process.env.PASSWORD,
    database:process.env.DATABASE
});


//regisztráció---.fnev && fjelszo
app.post('/SignUp', (req,res)=>{
    const q ="select name from persons WHERE name=?"
    const k ="insert into persons(name,password) values(?,?)"
    pool.query(q,[req.body.fnev],
        function(error,results){
            if(results[0] || error)
            return res.status(400).send({message: "Van már ilyen nevű felhasználó!!"})
            else{
                const hash=bcrypt.hashSync(req.body.fjelszo,10)
                pool.query(k, [req.body.fnev,hash],
                    function(error){
                        if(!error){
                            return res.send({ message: "Sikeres regisztráció" })
                        }else{
                            return res.send({ message: error })
                        }}
            )}
                
        })
    })

//Bejelentkezés--->fnev && fjelszo
app.post('/user/login', (req, res) => {
    const q ="select name from persons WHERE name=?"
    const k ="select password, ID from persons where name=?"
    pool.query(q,[req.body.fnev],
        function(error, results){         
            if(results[0] && !error){
                pool.query(k,[req.body.fnev],
                    function(error,results1){               
                        if (!bcrypt.compareSync(req.body.fjelszo,results1[0].password) && !error)
                        return res.status(401).send({ message: "Hibás jelszó!" }) 
                        const token = jwt.sign({username:req.body.fnev, password:results1[0].password,id:results1[0].ID}, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                        //nem lehet beállitani a payload ot ha a fnev string , objektummá kell alakitani!!!
                        return res.json({ token: token, message: "Sikeres bejelentkezés."})
                    })
            }else{
                return res.status(401).send({ message: "Nincs ilyen nevű felhasználó!" })
           }
       })
    })

//token ellenörzése
function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization']
    const token = authHeader && authHeader.split(' ')[1]
    if (!token) 
    return res.status(401).send({message: "Azonosítás szükséges!"})
    jwt.verify(token, process.env.TOKEN_SECRET, (err, user) => {
        if (err) 
        return res.status(403).send({message: "Nincs jogosultsága!"})
        req.user = user
        next()
    })
}

//új poszt hozzáadása
app.post('/Post/New', authenticateToken,(req,res)=>{
    const q ="Insert into registers (registers.personsID, registers.amount, registers.dates, registers.categoriesID) values (?, ?, ?, ?); "
    pool.query(q,[req.user.id, req.body.amount, req.body.dates, req.body.categoriesID],
        function(error){
            if(!error)
               return res.send("Sikeres hozzáadás")
            else
            return res.send(error)
        })
})

//felhasználó posztjainak listázása(védett)
app.get('/Users/AllPost', authenticateToken, (req, res) => {
    const q = "select registers.amount, registers.personsID,registers.dates, categories.denomination from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where persons.name=? and persons.password=?"
    pool.query(q,[req.user.username,req.user.password],
        function(error, results){
            if(!error)
               return res.send(results)
            else
            return res.send(error)
    
})
})

//felhasználó törlése
app.post('/User/Delete', authenticateToken,(req,res)=>{
    const q ="delete from persons where persons.ID=?"
    const kesz=false
    pool.query(q,[req.user.id],
        function(error){
            if(!error && kesz!=true)
                return res.status(200).send({message:"Felhasználó sikeresen törölve"})
            else
            return res.send(error)
        })
})
