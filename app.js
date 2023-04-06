const express = require('express');
const app = express();
const port = process.env.PORT || 80;


// packages loads
const axios = require('axios');
const bodyParser = require('body-parser');

//api_key
const api_key = "964498811c07e3642db2e5e0e7966a6d";
const finhubAPItoken = 'c94ij8qad3if4j50t1tg';

app.use(bodyParser.json());

//for cors policy
app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*"); // update to match the domain you will make the request from
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.get('/api/stockdetail/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/stock/profile2?symbol=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)

    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/latestprice/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/quote?symbol=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/news/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let toTime = new Date();
  let fromTime = new Date();
  fromTime.setDate(fromTime.getDate() - 7);

  let todate = toTime.toISOString().split('T')[0];
  let fromdate = fromTime.toISOString().split('T')[0];
  let url = `https://finnhub.io/api/v1/company-news?symbol=${tickerName}&from=${fromdate}&to=${todate}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/hisdata/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let toTime = new Date();
  let fromTime = new Date();
  fromTime.setFullYear(fromTime.getFullYear() - 2);

  let todate = toTime.toISOString().split('T')[0];
  let fromdate = fromTime.toISOString().split('T')[0];
  //console.log(todate);
  //console.log(fromdate);

  let fromUnixStamp = Math.floor(fromTime.getTime() / 1000);
  let toUnixStamp = Math.floor(toTime.getTime() / 1000);
  let fromtimeString = fromUnixStamp.toString();
  let totimeString = toUnixStamp.toString();
  let url = `https://finnhub.io/api/v1/stock/candle?symbol=${tickerName}&resolution=D&from=${fromtimeString}&to=${totimeString}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/hourdata/:tickerName/:fromtime/:totime', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let fromtime = req.params.fromtime;
  let totime = req.params.totime;
  let toTime = new Date();
    let toUnixStamp = Math.floor(toTime.getTime() / 1000);
    console.log(toTime);
    
    let fromTime= new Date((toUnixStamp-6*60*60)*1000)
    console.log(fromTime);
    let fromUnixStamp = Math.floor(fromTime.getTime() / 1000);
    
    
    let fromtimeString = fromUnixStamp.toString();
    let totimeString = toUnixStamp.toString();
    console.log(fromtimeString)
    console.log(totimeString)

  let url = `https://finnhub.io/api/v1/stock/candle?symbol=${tickerName}&resolution=5&from=${fromtime}&to=${totime}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})


app.get('/api/autocomplete/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/search?q=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/recommtrend/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/stock/recommendation?symbol=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/socialsentiment/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/stock/social-sentiment?symbol=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/companypeer/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/stock/peers?symbol=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})

app.get('/api/companyearning/:tickerName', async function (req, res) {
  var query_input = req.query.query_input;
  let tickerName = req.params.tickerName;
  let url = `https://finnhub.io/api/v1/stock/earnings?symbol=${tickerName}&token=${finhubAPItoken}`;
  axios.get(url).then(response => {
    //console.log(response.data)
    res.json(response.data);
  }).catch(error => {
    console.log(error);
  });
})










app.get('/', (req, res) => {
  res.send('Hello World! Yuhua')
})

app.listen(port, () => {
  console.log("Express API is running at port 3000");
});



