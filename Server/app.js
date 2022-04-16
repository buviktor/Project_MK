require('dotenv').config()
const jwt= require('jsonwebtoken')
const express=require("express");
const app=express();
app.use(express.json());
const cors = require("cors");
app.use(cors())
const bcrypt = require("bcrypt");