var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const bodyParser = require('body-parser')

require('./db/mongo')

//Router 정의
const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const trainerRouter = require('./routes/trainers');
const historyRouter = require('./routes/history');
const sportsmanRouter = require('./routes/sportsmans')

const app = express();


app.use((req, res, next) =>{
  res.header("Access-Control-Allow-Origin", "*")
  res.header('Access-Control-Allow-Methods', 'GET, PUT, POST, DELETE, OPTIONS');
  res.header("Access-Control-Allow-Headers", "X-Requested-With, Content-Type")
  next()
})

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use("/db",express.static(path.join(__dirname, 'db')));

app.use(bodyParser.urlencoded({extended: true}))
app.use(bodyParser.json())


//Route 정의
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/trainers', trainerRouter);
app.use('/history',historyRouter);
app.use('/sportsmans', sportsmanRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
