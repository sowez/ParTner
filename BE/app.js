var express = require('express');
var app = express();

const port = process.env.PORT || 3000;

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use('/', require('./router'));

app.listen(port, function () {
  console.log('Example app listening on port 3000!');
});