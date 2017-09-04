http = require('http');
fs = require('fs');
server = http.createServer( function(req, res) {

    if (req.method == 'POST') {
        console.log("POST");
        var body = '';
        req.on('data', function (data) {
            body += data;
            // console.log("Partial body: " + body);
                    });
        req.on('end', function () {
            console.log("Body: " + body);
            var timeStamp = new Date().getTime();
            var fileName = timeStamp.toString()+".txt"
            console.log("Saving body to file: " + fileName);
            fs.writeFileSync(fileName, body,'utf8');
        });
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.end('post received');

    }
    else
    {
        console.log("GET");
        //var html = '<html><body><form method="post" action="http://localhost:3000">Name: <input type="text" name="name" /><input type="submit" value="Submit" /></form></body>';
        var html = fs.readFileSync('index.html');
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.end(html);
    }

});

port = 9000;
host = '127.0.0.1';
server.listen(port, host);
console.log('Listening at http://' + host + ':' + port);