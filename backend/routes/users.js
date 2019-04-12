var express = require('express');
var router = express.Router();

const userModel = require('../db/models/user')
const trainerModel = require('../db/models/trainer')
/* GET users listing. */
router.get('/', function (req, res, next) {
  res.send('respond with a resource');
});

router.post('/signup', function (req, res, next) {
  console.log(req.body);
  if (req.body.type == "user") {

    userModel.find({ $or: [{ id: req.body.id }] }, function (err, existUser) {
      // trainer 까지 id 중복체크
      if (existUser.length == 0) {

        var user = new userModel({
          id: req.body.id,
          pw: req.body.pw,
          sex: req.body.sex,
          name: req.body.name,
        });

        user.save(function (err) {
          if (err) return console.log(err);
          console.log('user information saved!')
        })

        req.body.id = 'saved'
        res.send(req.body)

      } else {
        req.body.id = 'exist'
        res.send(req.body)
      }
    });

  } else if (req.body.type == "trainer") {
    
    trainerModel.find({ $or: [{ id: req.body.id }] }, function (err, existTrainer) {

      if (existTrainer.length == 0) {

        var trainer = new trainerModel({
          id: req.body.id,
          pw: req.body.pw,
          sex: req.body.sex,
          name: req.body.name,
          training_type : req.body.training_type,
        });

        trainer.save(function (err) {
          if (err) return console.log(err);
          console.log('user information saved!')
        })

        req.body.id = 'saved'
        res.send(req.body)

      } else {
        req.body.id = 'exist'
        res.send(req.body)
      }
    });

  }

});

module.exports = router;
