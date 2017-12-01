module.exports = checkRequest

const RateLimiter = require('limiter').RateLimiter;

// Allow 150 requests per hour (the Twitter search limit). Also understands 
// 'second', 'minute', 'day', or a number of milliseconds 
const limiter = new RateLimiter(150, 'hour', true) //fire CB immediately


function checkRequest(response, cb){
    //console.log('limiter!!!')
    limiter.removeTokens(1, function(err, remainingRequest){
        if(err) console.log(err.message())
        if(remainingRequest < 1){
            response.writeHead(429, {'Content-Type': 'text/plain;charset=UTF-8'});
            response.end('429 Too Many Request - your IP is being rate limited');
        }else{
            //callMyMessageSendingFunction()
            cb()
        }
        console.log(remainingRequest)
    });

}