var express = require('express');
var router = express.Router();

const sportsmanModel = require('../db/models/sportsman')
const trainerModel = require('../db/models/trainer')

/* ./trainer test */
router.get('/', function (req, res, next) {
  res.send('.trainers/');
});

/* GET trainers list. */
router.get('/list/:userId/:trainerName', function (req, res, next) {

  sportsmanModel.findOne({ id: req.params.userId }, function (err, sportsman) {
    if (req.params.trainerName == "all") {
      if (req.query.sex == "all") {
        if (req.query.traintype == "all") {
          // name:all, sex:all, traintype:all
          trainerModel.find({}, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
        else {
          // name:all, sex:all, traintype:x
          trainerModel.find({ training_type: req.query.traintype }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
      }
      else {
        if (req.query.traintype == "all") {
          // name:all, sex:x, traintype:all
          trainerModel.find({ sex: req.query.sex }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
        else {
          // name:all, sex:x, traintype:x
          trainerModel.find({ $and: [{ sex: req.query.sex }, { training_type: req.query.traintype }] }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
      }
    }
    else {
      if (req.query.sex == "all") {
        if (req.query.traintype == "all") {
          // name:x, sex:all, traintype:all
          trainerModel.find({ name: { $regex: req.params.trainerName } }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
        else {
          // name:x, sex:all, traintype:x
          trainerModel.find({ $and: [{ name: { $regex: req.params.trainerName } }, { training_type: req.query.traintype }] }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
      }
      else {
        if (req.query.traintype == "all") {
          // name:x, sex:x, traintype:all
          trainerModel.find({ $and: [{ name: { $regex: req.params.trainerName } }, { sex: req.query.sex }] }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
        else {
          // name:all, sex:x, traintype:x
          trainerModel.find({ $and: [{ name: { $regex: req.params.trainerName } }, { sex: req.query.sex }, { training_type: req.query.traintype }] }, function (err, trainers) {
            if (err) {
              return res.status(500).send({ error: 'databasefailure' });
            }
            console.log(trainers);
            res.json({ "bookmark" : sportsman.trainer_bookmark, "trainerProfiles" : trainers });
          })
        }
      }
    }
  });

});

/* 트레이너 프로필 가져오기 */
router.get('/profile', function (req, res, next) {
  trainerModel.findOne({ id: req.query.id }, function (err, trainer) {
    if (err) {
      return res.status(500).send({ error: 'databasefailure' });
    }
    console.log(trainer);
    res.json(trainer);
  })
});


/* 프로필 수정 */
router.post('/profile/edit', function (req, res, next) {
  trainerModel.findOneAndUpdate({ id: req.body.id }, { name: req.body.name, self_introduction: req.body.self_introduction, sex: req.body.sex, training_type: req.body.training_type }, function (err, trainer) {
    console.log(req.body.id);
    console.log(req.body.name);
    console.log(req.body.self_introduction);
    console.log(req.body.sex);
    console.log(req.body.training_type);

    if (err) {
      return res.status(500).send({ error: 'databasefailure' });
    }
    console.log(trainer);
    res.json({ "editresult": "success" });
  });
});

/* 별점 변경 */
router.post('/starrate', function (req, res, next) {
  trainerModel.findOneAndUpdate({ id: req.body.trainerID }, { $inc: { star_rate: req.body.star_rate, star_rate_num: 1 } }, function (err, trainer) {

    console.log(req.body.trainerID);
    console.log(req.body.star_rate);

    if (err) {
      return res.status(500).send({ error: 'databasefailure' });
    }
    console.log(trainer);
    res.json({ "starrate": "success" });
    
  });
});

/* quickblox id 등록 및 온오프라인 변경 */
router.post('/qb/id', function(req, res, next){
  trainerModel.findOneAndUpdate({id:req.body.id}, { state:"online" , qb_id:req.body.qb_id},function(err, trainer){
    if(err) console.log(err)
    res.json({"result":"success"});
  });
});

module.exports = router;