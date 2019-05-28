var express = require('express');
var router = express.Router();

const sportsmanModel = require('../db/models/sportsman')
const trainerModel = require('../db/models/trainer')

/* ./sportsman test */
router.get('/', function (req, res, next) {
  res.send('.sportsmans/');
});

router.get('/bookmark/:id', function (req, res, next) {
  console.log(req.params);
  var trainers = new Array();
  var sportsmanID = req.params.id;
  sportsmanModel.findOne({ id: sportsmanID }).populate('trainer_bookmark').exec((err, data) => {
    if (err) {
      console.log(err);
      res.json({ "result": "fail" });
    }
    console.log(data.trainer_bookmark);
    res.send(data.trainer_bookmark);
  });
});




router.post('/bookmark/update', function (req, res, next) {
  console.log(req.body);
  var sportsmanID = req.body.sportsmanID;
  var trainerID = req.body.trainerID;

  sportsmanModel.findOne({ id: sportsmanID }, function (err, sportsman) {
    if (err) {
      console.log(err)
      res.json({ "result": "fail" });
    }
    console.log(sportsman)
    trainerModel.findOne({ id: trainerID }, function (err, trainer) {
      sportsman.trainer_bookmark.push(trainer);
      sportsman.save(function (err) {
        if (err) return console.log(err);
        console.log('user information saved!')
        res.json({ "result": "success" });
      });
    });
  });

});

router.post('/bookmark/delete', function (req, res, next) {
  console.log(req.body);
  sportsmanID = req.body.sportsmanID;
  trainerID = req.body.trainerID;

  sportsmanModel.findOne({ id: sportsmanID }, function (err, sportsman) {
    if (err) {
      console.log(err)
      res.json({ "result": "fail" });
    }
    console.log(sportsman)
    trainerModel.findOne({ id: trainerID }, function (err, trainer) {
      sportsman.trainer_bookmark.pull(trainer);
      sportsman.save(function (err) {
        if (err) return console.log(err);
        console.log('user information saved!')
        res.json({ "result": "success" });
      });
    });
  });
});

module.exports = router;