var express = require('express');
var router = express.Router();

const userModel = require('../db/models/user')
const trainerModel = require('../db/models/trainer')
/* GET users listing. */
router.get('/', function (req, res, next) {
  res.send('respond with a resource');
});

router.post('/upload/image', function(req, res, next){
  console.log(req);
});

/* 회원가입 */
router.post('/signup', function (req, res, next) {
  console.log(req.body);

  userModel.find({ $or: [{ id: req.body.id }] }, function (err, existUser) {

    trainerModel.find({ $or: [{ id: req.body.id }] }, function (err, existTrainer) {

      if (existUser.length == 0 && existTrainer.length == 0) {
        
        switch (req.body.type) {
          case "user": {
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

      } else {
        req.body.result = 'exist'
        res.send(req.body)
      }

    });
  });
});

module.exports = router;
