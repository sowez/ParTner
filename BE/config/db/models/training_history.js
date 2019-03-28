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
    trainer:{ref},
    user:{ref}
})



module.exports = mongoose.model('User', userSchema)
