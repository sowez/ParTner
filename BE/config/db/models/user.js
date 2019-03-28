const { mongoose } = require('../mongo')

const userSchema = new mongoose.Schema({
    id:{
        type: String,
        required: true,
        unique: true
    },
    pw:{
        type: String,
        required: true
    },
    sex:{
        type: String,
        required: true
    },
    name:{
        type: String,
        required: true
    },
    trainer_bookmark :{
        
    },
    call_history :{

    },
    training_history :{

    }
})



module.exports = mongoose.model('user', userSchema)
