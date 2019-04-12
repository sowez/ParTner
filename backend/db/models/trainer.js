const { mongoose } = require('../mongo')

const trainerSchema = new mongoose.Schema({
    id: {
        type: String,
        required: true,
        unique: true
    },
    pw: {
        type: String,
        required: true
    },
    sex: {
        type: String,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    training_type: {
        type: [String,]
    },
    star_rate: {
        type: Number
    },
    state: {
        type: String
    }
    ,
    self_instruction: {
        type: String
    },
    profileImg: {
        type: String
    },

    call_history: {

    }
})



module.exports = mongoose.model('trainer', trainerSchema)
