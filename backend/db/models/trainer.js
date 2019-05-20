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
        type: Number,
        default: 0
    },
    state: {
        type: String,
        default: "offline"
    }
    ,
    self_introduction: {
        type: String,
        default: "Intro"
    },
    profileImg: {
        type: String,
        default: ""
    },

    call_history: [{ type : Number, ref: 'call_history' }],
    
})



module.exports = mongoose.model('trainer', trainerSchema)
