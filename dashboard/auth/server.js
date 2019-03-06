var http = require('http');
var fs = require('fs');
var cssHeaders = {'Content-Type': 'text/css'};


function onRequest(request, response){
    response.writeHead(200, {'Content-Type' : 'text/html'});
    response.write('hello qwerty');
    
    fs.readFile('./index.html', null, function(error,data){
        if(error){
            response.writeHead(404);
            response.write('file not found');
        }
        else{
            response.write(data);
        }
        response.end;
    });

}
http.createServer(onRequest).listen(8000);
 