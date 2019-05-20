const { mongoose } = require('../mongo')

const call_historySchema = new mongoose.Schema({
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
    },
    trainer: {type : Number, ref: 'trainer'},
    user: {type : Number, ref: 'user'},
})



module.exports = mongoose.model('call_history', call_historySchema)
