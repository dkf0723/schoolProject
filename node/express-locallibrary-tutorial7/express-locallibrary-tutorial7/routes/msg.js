var express = require('express');
var router = express.Router();
var db = require('../db');

var checkLogin = function(req, res, next){
  if(req.session.user){
    next();
  }else{
    res.redirect('/login');
  }
}

var permissionCheck = (req, res, next) =>{
  if(req.session.user){
    var user = req.session.user;
    var msgID = req.params.id;
    db.get('select id from message where id = ? and author=? ',
    [msgID, user.id],(err, row)=>{
      if(err) res.send('取得檢查資料錯誤!' + err.message);
      else if(row)
        next();
      else  
        res.send(`目前帳號: ${user.email} 無權限存取`);
    });
  }else{
    res.redirect('/login');
  }
}


/* GET home page. */
router.get('/all',checkLogin,function(req, res, next){
  var data = []; 
  var loginUser=req.session.user.email;
  db.all(`select m.id,title, content, author, lastupdatetime, email 
  from message m, users u
  where author=u.id`,function(err, rows){
    if(err){
    console.log('出錯！' + err.message);
    res.send('出錯！' + err.message);
    return;
    }
    // rows.forEach(row=>{
    //   data.push({
    //     id: row.id,
    //     title: row.title,
    //     content: row.content,
    //     lastupdatetime: row.lastupdatetime
    //   });
    // });
    res.render('messagelist', {data: rows,loginUser:loginUser, loginUserid:req.session.user.id});
  });
});

//新增
router.get('/create',checkLogin,function(req, res, next) {
  res.render('messagelist', { data: msg ,loginUser:req.session.user.email});
});
router.post('/create',checkLogin,function(req, res, next) {
  let title = req.body.title;
  let content = req.body.content;
  let now = new Date().toLocaleString();
  let userid=req.session.user.id;
  db.run('insert into message(title, content, author, lastupdatetime) values(?,?,?,?)',
        [title, content, userid, now], 
      (err)=>{
        if(err){
          console.log('有錯誤！！！' + err.message);
          res.send('程式寫錯了！！' + err.message);
          return;
        } 
        console.log(`成功新增資料`);
        res.redirect('/msg/all');
  });
});

//編輯
router.use('/edit/:id',permissionCheck);
router.get('/edit/:id', function(req, res, next){
  var id = req.params.id;
  var user=req.session.user;
  if(user){
    db.get('select id,title, content, lastupdatetime from message where id=?', id ,function(err, row){
        if(err){
          console.log('出錯！' + err.message);
          res.send('出錯！' + err.message);
          return;
        }
        if(row){
          console.log('title:' + row.title)
          res.render('edit', {data:row,loginUser:user.email});
        }else{
          res.send(`沒有找到ID=${id}的文章!`);
        }
    });
  }else{
    res.redirect('/login');
  }
});

router.post('/edit/:id', function(req, res, next){
  var id = req.body.id;
  let title = req.body.title;
  let content = req.body.content;
  let now = new Date().toLocaleString();

  db.run("update message set title=?, content=?, lastupdatetime=? where id=?", [title, content, now, id], (err)=>{
    if(err){
      console.log(err.message);
      res.send(err.message);
    }
    res.redirect('/msg/all');
  });
});

//刪除
router.get('/delete/:id', function(req, res, next){
  var id = req.params.id;
  db.run('delete from message where id=?', id ,(err)=>{
    if(err){
      console.log('出錯！' + err.message);
      res.send('出錯！' + err.message);
    }
    res.redirect('/msg/all');
  });
});

module.exports = router;
