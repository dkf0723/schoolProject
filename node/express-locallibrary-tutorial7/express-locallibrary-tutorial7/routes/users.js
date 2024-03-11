var express = require('express');
var router = express.Router();

var db = require('../db');
var bcrypt = require('bcrypt');
var saltRounds = 5;

/* GET users listing. */
// router.get('/', function(req, res, next) {
//   res.redirect('register');
// });

router.get('/register', function (req, res, next) {
  res.render('register', {name:"", pwd:"", pwd2:"", EducationalSystem:"", location:"", level:"",error:""});
});

router.post('/register', function (req, res, next) {
  let name = req.body.regusername;
  let pwd = req.body.regpassword;
  let pwd2 = req.body.regpassword2;
  let EducationalSystem = req.body.EducationalSystem;
  let location = req.body.location;
  let now = new Date().toLocaleString();
  if (name == "") {
    res.render('register', {name:name, pwd:pwd, pwd2:pwd2, EducationalSystem:EducationalSystem, location:location, error:"請輸入帳號"})
  }
  if (pwd == "") {
    res.render('register', {name:name, pwd:pwd, pwd2:pwd2, EducationalSystem:EducationalSystem, location:location, error:"請輸入密碼"})
  }
  if(pwd != pwd2){
    res.render('register', {name:name, pwd:pwd, pwd2:pwd2, EducationalSystem:EducationalSystem, location:location, error:"確認密碼欄位錯誤"})
  }
  if(pwd === pwd2){
    bcrypt.hash(pwd, saltRounds, function(err, hashpwd){
      db.run('insert into users(email, password, EducationalSystem,grade,createdtime) values(?,?,?,?,?)',
      [name, hashpwd,EducationalSystem,location,now],
      (err)=>{
        if(err){
        console.log('有錯誤!!!' + err.message);
        res.render('register', {error: '註冊失敗，錯誤:' + err.message});
        return;
      }
      console.log(`成功新增資料， ${this.lastID}`);
      res.render('login', { account: "", password: "", errorMessage: "" });
      });
    });
    // res.render('register', {name:name, pwd:pwd, pwd2:pwd2, EducationalSystem:EducationalSystem, location:location, error:""});
  }
});

module.exports = router;
