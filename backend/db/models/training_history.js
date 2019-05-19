const { mongoose } = require('../mongo');

const training_historySchema = new mongoose.Schema({
    start_time:{
        type:Date,
        required:true
    },
    end_time:{
        type:Date,
        required:true
    },
    ex_count:{
        type:Number,
        required:true
    },
    ex_type:{
        type:Number,
        required:true
    }
});

module.exports = mongoose.model('training_history', training_historySchema);
