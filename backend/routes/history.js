var express = require('express');
var router = express.Router();

const trainHistModel = require('../db/models/training_history');
const callHistModel = require('../db/models/call_history');

// GET Training Histories
router.get('training/:id/:year/:month',function(req, res, next){
    var s_d = req.params.year + '-' + req.params.month + '-01 00:00';
    var e_d = req.params.year + '-' + req.params.month + '-31 23:59';
    trainHistModel.find({id : req.params.id, 
        start_time : {$gte: s_d,$lte: e_d}} , function(err, histories){
            if(err){
                return res.status(500).send({error: 'databasefailure'});
            }
            console.log(histories);
            res.send(histories);
        }
    );
});

// GET Call Histories
router.get('call/:id/:year/:month',function(req, res, next){
    var s_d = req.params.year + '-' + req.params.month + '-01 00:00';
    var e_d = req.params.year + '-' + req.params.month + '-31 23:59';
    callHistModel.find({id : req.params.id, 
        start_time : {$gte: s_d,$lte: e_d}} , function(err, histories){
            if(err){
                return res.status(500).send({error: 'databasefailure'});
            }
            console.log(histories);
            res.send(histories);
        }
    );
});

// POST Training History
router.post('/create', function(req, res, next){
    var history = new trainHistModel({
        id: req.body.id,
        start_time: req.body.start_time,
        ex_count: req.body.ex_count,
        ex_type: req.body.ex_type
    });
    history.save(function(err){
        if (err) return console.log(err);
        console.log('training history saved!')
    });

    req.body.result = 'saved'
    res.send(req.body)
});

module.exports = router;