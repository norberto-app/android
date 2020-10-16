const mongoose = require("mongoose");
const winston = require("winston");

module.exports = function () {
  
  mongoose
    .connect(
      "mongodb+srv://norberto:norberto@cluster0.5jjd3.mongodb.net/playground",
      {
        useUnifiedTopology: true,
        useNewUrlParser: true,
        useCreateIndex: true
      }
    )
    .then(() => {
      winston.info("Connected to mongo Db.");
    });
};
