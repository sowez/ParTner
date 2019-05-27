var express = require('express');
var router = express.Router();

const userModel = require('../db/models/sportsman')
const trainerModel = require('../db/models/trainer')

/* ./trainer test */
router.get('/', function (req, res, next) {
  res.send('.trainers/');
});

/* GET trainers list. */
router.get('/list/:name', function (req, res, next) {

  if(req.params.name == "all") {
    if(req.query.sex == "all") {
      if (req.query.traintype == "all") {
        // name:all, sex:all, traintype:all
        trainerModel.find({ }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })
      }
      else {
        // name:all, sex:all, traintype:x
        trainerModel.find({ training_type: req.query.traintype }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })
      }
    }
    else {
      if (req.query.traintype == "all") {
        // name:all, sex:x, traintype:all
        trainerModel.find({ sex: req.query.sex }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })
      }
      else {
        // name:all, sex:x, traintype:x
        trainerModel.find({ $and:[{ sex: req.query.sex },{ training_type: req.query.traintype }] }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })  
      }
    }
  }
  else {
    if(req.query.sex == "all") {
      if (req.query.traintype == "all") {
        // name:x, sex:all, traintype:all
        trainerModel.find({ name: {$regex:req.params.name} }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })
      }
      else {
        // name:x, sex:all, traintype:x
        trainerModel.find({ $and:[{ name: {$regex:req.params.name} },{ training_type: req.query.traintype }] }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        }) 
      }
    }
    else {
      if (req.query.traintype == "all") {
        // name:x, sex:x, traintype:all
        trainerModel.find({ $and:[{ name: {$regex:req.params.name} }, { sex: req.query.sex }] }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })
      }
      else {
        // name:all, sex:x, traintype:x
        trainerModel.find({ $and:[{ name: {$regex:req.params.name} }, { sex: req.query.sex }, { training_type: req.query.traintype }] }, function(err, trainers) {
          if(err){
            return res.status(500).send({error: 'databasefailure'});
          }
          console.log(trainers);
          res.json(trainers);
        })
      }
    }
  }
});

/* 트레이너 프로필 가져오기 */
router.get('/profile', function (req, res, next) {
  trainerModel.findOne({ id: req.query.id }, function(err, trainer) {
    if(err){
      return res.status(500).send({error: 'databasefailure'});
    }
    console.log(trainer);
    res.json(trainer);
  })
});

/* 프로필 수정 */
router.get('/profile/edit', function (req, res, next) {
  trainerModel.findOneAndUpdate({ id: req.body.id }, function(err, trainer) {
    if(err){
      return res.status(500).send({error: 'databasefailure'});
    }
    console.log(trainer);
    res.json(trainer);
  })
 });

 /* 프로필 수정 */
router.post('/profile/edit', function (req, res, next) {
  trainerModel.findOneAndUpdate({ id: req.body.id },{name: req.body.name, self_introduction: req.body.self_introduction, sex: req.body.sex, training_type: req.body.training_type}, function(err, trainer) {
    console.log(req.body.id);
    console.log(req.body.name);
    console.log(req.body.self_introduction);
    console.log(req.body.sex);
    console.log(req.body.training_type);
 
    if(err){
      return res.status(500).send({error: 'databasefailure'});
    }
    console.log(trainer);
    res.json({"editresult":"success"});
  });
 });
 

module.exports = router;