var express = require('express');
var router = express.Router();

const userModel = require('../db/models/user')
const trainerModel = require('../db/models/trainer')

/* ./trainer test */
router.get('/', function (req, res, next) {
  res.send('.trainers/');
});

/* GET trainers list. */
router.get('/list/:name', function (req, res, next) {

  if(req.params.name == "all" && req.query.sex == "all" && req.query.traintype == "all") {
    trainerModel.find({ }, function(err, trainers) {
      if(err){
        return res.status(500).send({error: 'databasefailure'});
      }
      console.log(trainers);
      res.json(trainers);
    })
  }

  else {
    trainerModel.find({ $or:[{name: {$regex:req.params.name}}, {sex: req.query.sex},{training_type: req.query.traintype}] }, function(err, trainers) {
      if(err){
        return res.status(500).send({error: 'databasefailure'});
      }
      console.log(trainers);
      res.json(trainers);
    })  
  }

});



module.exports = router;