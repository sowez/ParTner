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
  // else (req.params.name == "all" && req.query.sex == "all") {
  //   trainerModel.find({ $or:[{name: {$regex:req.params.name}}, {sex: req.query.sex},{training_type: req.query.traintype}] }, function(err, trainers) {
  //     if(err){
  //       return res.status(500).send({error: 'databasefailure'});
  //     }
  //     console.log(trainers);
  //     res.json(trainers);
  //   })  
  // }


});

// GET Trainer's name
router.get('/name/:id', function(req, res, next){
  trainerModel.findOne({id:req.params.id}, {name: true}, function(err, name) {
    if(err){
      return res.status(500).send({error: 'databasefailure'});
    }
    console.log(name);
    res.json(name);
  })
});


module.exports = router;