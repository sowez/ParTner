var express = require('express');
var router = express.Router();

const userModel = require('../db/models/user')

/* GET home page. */
router.get('/', function(req, res, next) {
  userModel.find({id:"hi"}, function(err, posts){
    res.render('index', { title : posts })
  });
});

module.exports = router;
