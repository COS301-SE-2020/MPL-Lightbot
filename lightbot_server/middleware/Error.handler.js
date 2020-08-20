const { ErrorResponse } = require('../utils/Error.util')

module.exports = (err, req, res, next) => {
    let code = err.getCode()
    return res.status(code || 500).json({
        error : {
            status: code || 500,
            message: err.message || "Internal Server Error",
            // data: err.data
        }
    })
}

// const handleErrors = (err, req, res, next) => {
//    if (err instanceof ErrorResponse) {
// //     let code = err.getCode()
// //     return res.status(code).json({
// //       status:  code || 500,
// //       message: err.message,
// //       data: err.data
// //     });
//   }

//   return res.status(500).json({
//     // status: 500,
//     // message: err.message,
//     // message: err.data
//   });
// }


// module.exports = handleErrors;