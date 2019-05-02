var express = require('express');
var router = express.Router();

const sportsmanModel = require('../db/models/sportsman')
const trainerModel = require('../db/models/trainer')


var formidable = require('formidable')
var fs = require('fs');

/* id 중복 체크 */
router.get('/signup/overlap/:type/:id', function (req, res, next) {
  console.log("signup 아이디 중복체크" + req.body);

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

    console.log(fields.upload);
    var trainer_id = fields.upload

    var db_location = "/../db/resources/images/trainer_profile/"

    fs.readFile(img_path, function (err, data) {

      var new_img_name = new Date().valueOf() + img_name;
      var newpath = __dirname + db_location + new_img_name

      fs.copyFile(img_path, newpath, function (err) {
        if (err) { console.error(err) }
        else {
          trainerModel.findOneAndUpdate({ id: trainer_id }, { profileImg: newpath }, function (err, trainer) {
            if (err) {
              console.error(err)
              res.json({ "result": "error" });
            } else {
              console.log("upload success")
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
  console.log("signup" + req.body);

  switch (req.body.type) {
    case "user": {
      var user = new sportsmanModel({
        id: req.body.id,
        pw: req.body.pw,
        sex: req.body.sex,
        name: req.body.name,
      });

      user.save(function (err) {
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

module.exports = router;
