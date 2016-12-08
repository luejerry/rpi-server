/**
 * Created by cyricc on 7/20/2016.
 */
var TEMPINTERVAL = 20;

$(document).ready(main);

function main() {
    var loc = window.location;
    var connection = new WebSocket('ws://' + loc.host + '/temp');
    var temp = $(".temp");
    var timer = setInterval(function() {}, 1000);
    var counter = parseInt($('#temp-timer').text());

    connection.onopen = function () {
        connection.send("ACK");
    };

    connection.onerror = function (error) {
        console.log('Websocket error ' + error);
    };

    connection.onmessage = function (msg) {
        // temp.innerHTML = msg.data;
        temp.text(msg.data);
        clearInterval(timer);
        timer = initTimer(counter);
	$('#temp-timer').text(counter);
	counter = TEMPINTERVAL;
    };
}

function initTimer(seconds) {
    var counter = seconds;
    var timerTask = setInterval(function() {
        counter--;
        $('#temp-timer').text(counter);
        if (counter <= 0) {
            clearInterval(timerTask);
        }
    }, 1000);
    return timerTask;
}
