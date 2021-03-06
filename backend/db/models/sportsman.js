const { mongoose } = require('../mongo')

const sportsmanSchema = new mongoose.Schema({
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

    token: {
        type: String,
        default: ""
    },
    
    trainer_bookmark:[{ type: mongoose.Schema.Types.ObjectId, ref: 'trainer' }],

    call_history: [{ type: Number, ref: 'call_history' }],

    training_history: [{ type: Number, ref: 'training_history' }],
})



module.exports = mongoose.model('sportsman', sportsmanSchema)
