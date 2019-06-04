var express = require('express');
var router = express.Router();

const trainHistModel = require('../db/models/training_history');
const callHistModel = require('../db/models/call_history');

// ./history test
router.get('/', function(req, res, next){
    res.send('./history/');
})

// find all Training Histories
router.get('/')

// GET Training Histories
router.get('/training/:id/:year/:month',function(req, res, next){
    var s_d = req.params.year + '-' + req.params.month + '-01 00:00';
    var e_d = req.params.year + '-' + req.params.month + '-31 23:59';
    console.log(s_d);
    console.log(e_d);
    trainHistModel.find({id : req.params.id, 
        start_time : {$gte: s_d,$lte: e_d}
    }, {start_time:true, ex_count:true, ex_type:true} , function(err, histories){
            if(err){
                return res.status(500).send({error: 'databasefailure'});
            }
            console.log(histories);
            res.send(histories);
        }
    );
});

// GET Call Histories
router.get('/call/:id/:year/:month',function(req, res, next){
    var s_d = req.params.year + '-' + req.params.month + '-01 00:00';
    var e_d = req.params.year + '-' + req.params.month + '-31 23:59';
    callHistModel.find({id : req.params.id, 
        start_time : {$gte: s_d,$lte: e_d}
    }, {trainer_id:true, start_time:true, call_duration:true}, function(err, histories){
            if(err){
                return res.status(500).send({error: 'databasefailure'});
            }
            console.log(histories);
            res.send(histories);
        }
    );
});

// POST Training History
router.post('/trainhistory/create', function(req, res, next){
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

    res.json({'result': 'saved'})
});

// POST Call History
router.post('/callhistory/create', function(req, res, next) {
    var callhistory  = new callHistModel({
        trainer_id: req.body.trainer_id,
        user_id: req.body.user_id,
        start_time: req.body.start_time,
        end_time: req.body.end_time,
        call_duration: req.body.call_duration
    });
    callhistory.save(function(err) {
        if(err) return console.log(err);
        console.log('call history saved');
        res.json({ "result": "success" });
    });
});

module.exports = router;