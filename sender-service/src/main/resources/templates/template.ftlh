<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Spring Boot Email using FreeMarker</title>
</head>
<body>
<div style="margin-top: 10px">Greetings, ${user}</div>
<div>Your username is <b>${user}</b></div>
<div>Your receipt  
<p>${text}</p> ${amount}</div>
<br/>
<div> Have a nice day..!</div>
</body>
</html>