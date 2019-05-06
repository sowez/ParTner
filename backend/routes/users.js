var express = require('express');
var router = express.Router();

const sportsmanModel = require('../db/models/sportsman')
const trainerModel = require('../db/models/trainer')

var formidable = require('formidable')
var fs = require('fs');
var jwt = require('jsonwebtoken');


/* id 중복 체크 */
router.get('/signup/overlap/:type/:id', function (req, res, next) {
  console.log("signup 아이디 중복체크");
  console.log(req.params)

  switch (req.params.type) {
    case 'sportsman': {
      sportsmanModel.find({ id: req.params.id }, function (err, existSportsman) {
        if (existSportsman.length == 0) {
          res.json({ "result": "none" })
        } else {
          res.json({ "result": "exist" })
        }
      });
      break;
    }
    case 'trainer': {
      trainerModel.find({ id: req.params.id }, function (err, existTrainer) {
        if (existTrainer.length == 0) {
          res.json({ "result": "none" })
        } else {
          res.json({ "result": "exist" })
        }
      });
      break;
    }
  }
});


/* 프로필 사진 저장 */
router.post('/upload/image', function (req, res, next) {

  var form = new formidable.IncomingForm()
  var img_name = ""
  var img_path = "";

  form.parse(req, function (err, fields, files) {

    img_name = files.image.name
    img_path = files.image.path

    console.log(fields.trainerId);
    var trainer_id = fields.trainerId;

    var db_location = "/../db/resources/images/trainer_profile/"

    fs.readFile(img_path, function (err, data) {

      var new_img_name = new Date().valueOf() + img_name;
      var newpath = __dirname + db_location + new_img_name


      fs.copyFile(img_path, newpath, function (err) {
        if (err) { console.error(err) }
        else {
          console.log(newpath)
          trainerModel.findOneAndUpdate({ id: trainer_id }, { profileImg: newpath }, function (err, trainer) {
            if (err) {
              console.log(err)
              res.json({ "result": "error" })
            } else {
              console.log(trainer)
              res.json({ "result": "success" })
            }
          });
        }
      });

    });

  });

});


/* 회원가입 */
router.post('/signup', function (req, res, next) {

  switch (req.body.type) {
    case "sportsman": {
      var sportsman = new sportsmanModel({
        id: req.body.id,
        pw: req.body.pw,
        sex: req.body.sex,
        name: req.body.name,
      });

      sportsman.save(function (err) {
        if (err) return console.log(err);
        console.log('user information saved!')
      })

      break;
    }

    case "trainer": {

      var trainer = new trainerModel({
        id: req.body.id,
        pw: req.body.pw,
        sex: req.body.sex,
        name: req.body.name,
        training_type: req.body.training_type,
        profileImg: null
      });

      trainer.save(function (err) {
        if (err) return console.log(err);
        console.log('user information saved!')
      })
      break;
    }
  }

  req.body.result = 'saved'
  res.send(req.body)

});

/* 로그인 */

router.post('/login', function (req, res, next) {
  console.log(req.body)

  var userId = req.body.id;
  var userPw = req.body.pw;

  var secret = 'soweZ_ParTner';
  var options = { expiresIn: 60 * 60 * 24 }

  sportsmanModel.findOne({ id: userId }, function (err, sportsman) {

    /* 운동인 로그인 */
    if (sportsman != null) {

      sportsmanExist = true;
      console.log(sportsman)

      if (userPw != sportsman.pw) {
        res.json({ "loginResult": "diffrent" })
      } else {

        var payload = {
          _id: sportsman._id,
          id: sportsman.id,
          type: "sportsman"
        };

        jwt.sign(payload, secret, options, function (err, token) {
          if (err) return console.log(err);
          res.json({
            loginResult: "login_success",
            token: token,
            id: sportsman.id,
            type: "sportsman"
          });
        });
      }
    } else {

      /*트레이너 로그인 */
      trainerModel.findOne({ id: userId }, function (err, trainer) {
        if (trainer == null) {
          res.json({ "loginResult": "fail" })
        } else {
          trainerExist = true;
          if (userPw != trainer.pw) {
            res.json({ "loginResult": "diffrent" })
          } else {
            var payload = {
              _id: trainer._id,
              id: trainer.id,
              type: "trainer"
            };

            jwt.sign(payload, secret, options, function (err, token) {
              if (err) return console.log(err);
              res.json({
                loginResult: "login_success",
                token: token,
                id: trainer.id,
                type: "trainer"
              });
            });
          }
        }
      });
    }
  });
})

router.post('/verify', function (req, res, next) {
  var token = req.body.token
  jwt.verify(token, 'soweZ_ParTner', function (err, decoded) {
    console.log(decoded)
    res.send(decoded)
  });
});

module.exports = router;
