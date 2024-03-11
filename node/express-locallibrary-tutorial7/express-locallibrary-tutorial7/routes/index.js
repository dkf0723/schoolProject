var express = require('express');
var router = express.Router();
var db = require('../db');

var db = require('../db');
var bcrypt = require('bcrypt');
var saltRounds = 5;

/* GET home page. */
router.get('/', function (req, res, next) {
  res.redirect('/login')
});
router.get('/login', function (req, res, next) {
  res.render('login', { account: "", password: "", errorMessage: "" })
});

router.post('/login',function(req, res, next){
  var account=req.body.account;
  var password=req.body.password;
  if (account == "") {
    res.render('login', { account: account, password: password, errorMessage: "請輸入帳號" })
  }
  if (password == "") {
    res.render('login', { account: account, password: password, errorMessage: "請輸入密碼" })
  }
  db.get('select id, email, password, EducationalSystem, grade,createdtime from users where email =?', account,(err, user)=>{
    if(err){
      console.log('存取資料庫失敗!' + err.message);
      res.render('login', {errorMessage: '存取資料庫失敗!' +err.message});
    }
    if(user){
      bcrypt.compare(password, user.password, (err,result)=>{
        if(result){
          req.session.user = user;
          res.redirect('/msg/all');
        }
        else{
          res.render('login',{ account: account, password: password, errorMessage: "登入失敗，密碼錯誤" });
        }
      });
    }else{
      res.status(401);
      res.render('login',{ account: account, password: password, errorMessage: "登入失敗，找不到使用者" });
    }
  });
});

module.exports = router;
