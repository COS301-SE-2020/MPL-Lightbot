const mongoose = require('mongoose')
const uV = require('mongoose-unique-validator')
const Bcrypt = require('bcryptjs')
const { ErrorResponse } = require('../utils/Error.util')
const jwt = require('njwt')

const UserSchema = new mongoose.Schema({
  User_name: {
    type: String,
    required: true,
  },
  User_surname: {
    type: String,
    required: true,
  },
  User_email: {
    type: String,
    required: true,
    unique: true,
  },
  User_password: {
    type: String,
    required: true,
    minlength: 8,
  },
  User_state: {
    type: Number,
    default: 1,
  },
  User_role: {
    type: Number,
    default: 1,
  },
  avatar: {
    type: String,
    default: 'avatar.png',
  },
  date: {
    type: Date,
    default: Date.now,
  },
  ForumPosts: [{ type: mongoose.Types.ObjectId, required: true, ref: 'Forum' }],
})

UserSchema.plugin(uV)

UserSchema.pre('save', async function (next) {
  if (!this.isModified('password')) {
    try {
      const salt = await Bcrypt.genSalt(12)
      this.User_password = await Bcrypt.hash(this.User_password, salt)
      this.id = undefined
    } catch (err) {
      return next(
        new ErrorResponse("User could not be Saved", err),
        console.log("Save 1"),
        console.log(err))
    }
  }
  next()
})

UserSchema.methods.MatchPassword = async (candidate,userpass) => {
    return await Bcrypt.compare( userpass,candidate.User_password)
}

UserSchema.methods.getJWT = (uemail) => {
  const claims = { iss: process.env.JWTiss, sub: process.env.JWTsub, aud:process.env.JWTaud, email:uemail}
  const token = jwt.create(claims, process.env.JWTKEY)
  token.setExpiration(new Date().getTime() + 60*1000)
  return token.compact()
}

module.exports = mongoose.model('User', UserSchema)
