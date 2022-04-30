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

const Adminfiok=process.env.ADMIN

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
                            return res.status(201).send({ message: "Sikeres regisztráció" })
                        }else{
                            return res.status(500).send({ message: error })
                        }}
            )}
                
        })
    })
    //Bejelentkezés--->fnev && fjelszo
    app.post('/user/login', (req, res) => {
        const q ="select name,password from persons WHERE name=?"
        var token=""
        pool.query(q,[req.body.fnev],
        function(error,results){
            
            if(results[0] && !error){
               if(bcrypt.compareSync(req.body.fjelszo,results[0].password) && !error){
                    token = jwt.sign({username:req.body.fnev, password:results[0].password,id:results[0].ID}, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                    return res.status(200).json({ token: token, message: "Sikeres bejelentkezés."})}
                else
                    return res.status(400).send({ message: "Hibás jelszó!" }) }
            else
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


//felhasználó posztjainak listázása(védett)
app.get('/Users/AllPost', authenticateToken, (req, res) => {
    var q = String("select registers.amount,date_format(registers.dates,'%Y-%m-%d') as date,categories.denomination from registers join persons on persons.ID=registers.personsID join categories on categories.ID=registers.categoriesID where persons.name=? and persons.password=? ")
    const osszeg=String(" and registers.amount='"+req.body.osszeg+"' ")
    const kategoria=String(" and categories.denomination='"+req.body.kategoria+"'")
//date_format(registers.dates,'%Y-%M-%d') - hónap neveinek kiirása
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
    const datum=String("and registers.dates like '"+date1+"'")

    if(req.body.year!=00)
    q+=datum
    if(req.body.osszeg!=00)
        q+=osszeg
    if(req.body.kategoria!=00)
        q+=kategoria
    pool.query(q,[req.user.username,req.user.password],
        function(error, results){
            if(!error)
            return res.status(200).send(results)
            else
            return res.status(500).send({message: error})
                                        
    })
    })



//új poszt hozzáadása
app.post('/Post/New', authenticateToken,(req,res)=>{
    const q ="Insert into registers (registers.personsID, registers.amount, registers.dates, registers.categoriesID) values (?, ?, ?, ?); "
    pool.query(q,[req.user.id, req.body.amount, req.body.dates, req.body.categoriesID],
        function(error){
            if(!error)
               return res.status(201).send({message: "Sikeres hozzáadás"})
            else
            return res.status(500).send({message: error})
        })
})


//felhasználó törlése
app.post('/User/Delete', authenticateToken,(req,res)=>{
    const q ="delete from persons where persons.ID=?"
    const kesz=false
    pool.query(q,[req.body.fiok],
        function(error){
            if(!error && kesz!=true)
                return res.status(200).send({message:"Felhasználó sikeresen törölve"})
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