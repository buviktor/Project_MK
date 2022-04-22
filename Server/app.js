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
//Bejelentkezés
app.post('/users/login', (req, res) => {
    const q ="select name from persons WHERE name=?"
    const k ="select password from persons where name=?"
    //const hash= bcrypt.hashSync(req.body.jelszo, 10)
    pool.query(q,[req.body.fnev],
        function(error, results){         
            //return res.status(401).send(results)
            if(results[0] && !error){
                //return res.status(401).send(results) 
                pool.query(k,[req.body.fnev],
                    function(error,results1){               
                        //return res.send(results1[0].password)
                        if (!bcrypt.compareSync(req.body.jelszo,results1[0].password) && !error)
                        return res.status(401).send({ message: "Hibás jelszó!" }) 
                        /*
                        const token = jwt.sign(req.body.fnev, process.env.TOKEN_SECRET, { expiresIn: 3600 })
                        res.json({ token: token, message: "Sikeres bejelentkezés." })
                         */
                        return res.send("bejelentkezve") //ez nem kell majd
                    })
            }else{
                return res.status(401).send({ message: "Nincs ilyen nevű felhasználó!" })
                //res.send(error);
            }
       })}   
    )