/**
 * Created by cyricc on 7/20/2016.
 */
"use strict";

let TEMPINTERVAL = 20;

$(document).ready(main);

function main() {
    let loc = window.location;
    let connection = new WebSocket('ws://' + loc.host + '/temp');
    let temp = $(".temp");
    let timer = setInterval(function() {}, 1000);
    let temptimer = $('temp-timer');
    let counter = parseInt(temptimer.text);

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
        temptimer.text(counter);
        counter = TEMPINTERVAL;
    };

    connection.onclose = function (event) {
        clearInterval(timer);
        console.log('Server closed connection: ' + event.reason);
        setTimeout(main, 30000);
    }
}

function initTimer(seconds) {
    let counter = seconds;
    let temptimer = $('#temp-timer');
    let timerTask = setInterval(function() {
        counter--;
        temptimer.text(counter);
        if (counter <= 0) {
            clearInterval(timerTask);
        }
    }, 1000);
    return timerTask;
}
