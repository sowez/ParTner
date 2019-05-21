const { mongoose } = require('../mongo')

const call_historySchema = new mongoose.Schema({
    trainer_id:{
        type: String,
        required: true
    },
    user_id:{
        type: String,
        required: true
    },
    start_time:{
        type:Date,
        required:true
    },
    end_time:{
        type:Date,
        required:true
    },
    call_duration:{
        type:Number,
        required:true
    }
})



module.exports = mongoose.model('call_history', call_historySchema)
