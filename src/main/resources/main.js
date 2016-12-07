/**
 * Created by cyricc on 7/20/2016.
 */
$(document).load(main);

function main() {
    var loc = window.location;
    var connection = new WebSocket('ws://' + loc.host + '/temp');
    var temp = $(".temp");


    connection.onopen = function () {
        connection.send("ACK");
    };

    connection.onerror = function (error) {
        console.log('Websocket error ' + error);
    };

    connection.onmessage = function (msg) {
        // temp.innerHTML = msg.data;
        temp.text(msg.data);
    };
}
