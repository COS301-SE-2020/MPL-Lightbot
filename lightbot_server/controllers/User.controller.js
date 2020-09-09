const asyncHandler = require('express-async-handler')
const { ErrorResponse } = require('../utils/Error.util')
const { BadRequest } = require('../utils/Error.util')
const { SuccessResponse } = require('../utils/Success.util')
const User = require('../models/User.model')
const bcrypt = require('bcryptjs')
const { Forbidden, Unauthorized } = require('../utils/Error.util')

module.exports = {
  registerUser: asyncHandler(async (req, res, next) => {
    const { User_name, User_surname, User_email, User_password } = req.body
    let existing
    try {
      existing = await User.findOne({ User_email: User_email })
    }
    catch (err) {
      return next(
        new ErrorResponse('Something went wrong could not register user.', err),
        console.log("registerUser 1 Something went wrong could not register user."),
        console.log(err)
      )
    }
    if (existing) {
      return next(
        new BadRequest('User already exists please sign in.'),
        console.log("registerUser 2 User already exists please sign in")
      )
    }

    const createdUser = new User({
      User_name,
      User_surname,
      User_email,
      User_password,
      ForumPosts: []
    })

    try {
      await createdUser.save()
    }
    catch (err) {
      return next(
        new ErrorResponse('Something went wrong could not save/register user.', err),
        console.log("registerUser 3 Something went wrong could not save/register user"),
        console.log(err)
      )
    }

    let token;
    try {
      token = createdUser.getJWT()
      console.log("bearer: " + token)
    }
    catch (err) {
      return next(
        new ErrorResponse('Something went wrong could not getJWT()', err),
        console.log("registerUser 4 Something went wrong could not getJWT()"),
        console.log(err)
      )
    }

    res.json(new SuccessResponse("User registration successful.", await User.findOne({ User_email: req.body.User_email }).select("-User_password")))
  }),

  loginUser: asyncHandler(async (req, res, next) => {
    const { User_email, User_password } = req.body
    let existing
    try {
      existing = await User.findOne({ User_email: User_email })
    }
    catch (err) {
      return next(
        new ErrorResponse('Something went wrong could not findOne user.', err),
        console.log("loginUser 1 Something went wrong could not findOne user"),
        console.log(err)
      )
    }
    // console.log("loginUser 1 existing")
    if (!existing) {
      return next(
        new BadRequest('User does not Exist.'),
        console.log("loginUser 2 User does not Exist"),
        console.log(err)
      )
    }
    // console.log("loginUser 2 existing")

    let isValidPassword = false
    try {
      isValidPassword = await existing.MatchPassword(existing, User_password)
    }
    catch (err) {
      return next(
        new ErrorResponse('Something went wrong could not login user.', err),
        console.log("loginUser 3 Something went wrong could not MatchPassword"),
        console.log(err)
      )
    }
    // console.log("loginUser 3 isValidPassword")

    if (!isValidPassword) {
      return next(
        new Unauthorized("Invalid Credentials"),
        console.log("loginUser 4 Invalid Credentials")
      )
    }
    // console.log("loginUser 4 isValidPassword")

    // console.log("User login successful")
    res.json(new SuccessResponse("User login successful.", { bearer: existing.getJWT(User_email) }))
  }),

  logoutUser: asyncHandler(async (req, res, next) => {
    res.json(new SuccessResponse("Successfully signed out user.", "Redirect sign in."))
  }),

  loggedIn: asyncHandler(async (req, res, next) => {
    // res.json(new SuccessResponse("Successfully signed out user.","Redirect sign in."))
    const Data = await User.findOne({ User_email: req.body.User_email }).select("-User_password"); //.select("-User_password")
    res.json(Data);
  }),

  updateUserDetails: asyncHandler(async (req, res, next) => {
    // res.json(new SuccessResponse("Successfully updated user details.","New user data"))
    const filter = { User_email: req.body.User_email };
    const update = {
      User_name: req.body.User_name,
      User_surname: req.body.User_surname
    };

    console.log(filter, update);

    // let doc = await User.findOne({User_email : req.body.User_email});

    // Document changed in MongoDB, but not in Mongoose
    await User.updateOne(filter, update);

    // await doc.save();
    res.json(new SuccessResponse("Successfully updated user details.", await User.findOne({ User_email: req.body.User_email }).select("-User_password"))) //
  }),

  updateUserPass: asyncHandler(async (req, res, next) => {
    res.json(new SuccessResponse("Successfully updated user password.", "Miscellaneous"))
  }),

  recoverUserPass: asyncHandler(async (req, res, next) => {
    res.json(new SuccessResponse("If an account associated with this address exists, an email will be sent to it.", "Miscellaneous"))
  }),

  resetUserPass: asyncHandler(async (req, res, next) => {
    res.json(new SuccessResponse("Successfully reset user password.", req.params.passresetid))
  }),

  deleteUser: asyncHandler(async (req, res, next) => {
    res.json(new SuccessResponse("Successfully removed user.", "Redirect to sign in."))
  }),

  returnUsers: asyncHandler(async (req, res, next) => {
    let users
    try {
      users = await User.find({}, '-User_password')
    } catch (err) {
      return next(
        //new ErrorResponse('Fetching users failed.', err)
      )
    }
    res.json(new SuccessResponse("Successfully removed user.", users.map(user => user.toObject({ getters: true }))))
  }),
}
