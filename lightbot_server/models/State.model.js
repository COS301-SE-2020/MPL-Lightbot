const mongoose = require('mongoose')

const StateSchema = new mongoose.Schema({
  id: String,
  number: {
    type: Number,
    required: true,
  },
  systemState: {
    type: String,
    required: true,
  },
  controller: {
    type: String,
    required: true,
  },
  controllerState: {
    type: String,
    required: true,
  },
})
module.exports = mongoose.model('state', StateSchema, "Traffic_metrics")
