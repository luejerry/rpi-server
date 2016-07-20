/**
 * Created by cyricc on 7/20/2016.
 */

var loc = window.location;
var connection = new WebSocket('ws://' + loc.host + '/temp');
var temp = document.querySelector(".temp");


connection.onopen = function () {
    connection.send("ACK");
};

connection.onerror = function (error) {
    console.log('Websocket error ' + error);
};

connection.onmessage = function (msg) {
    temp.innerHTML = msg.data;
};