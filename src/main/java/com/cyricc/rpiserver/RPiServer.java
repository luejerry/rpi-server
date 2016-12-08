package com.cyricc.rpiserver;

import static spark.Spark.*;
import static j2html.TagCreator.*;

/**
 * Created by luej on 7/1/16.
 */
public class RPiServer {

    private static void init() {
        port(80);
        staticFiles.externalLocation(System.getProperty("user.dir"));
    }

    private static void setRoutes() {
        final WettyChecker checker = new WettyChecker();
        final Uptime uptime = new Uptime();
        webSocket("/temp", BroadcastHandler.class);
        redirect.get("/rpi-wetty", "https://cyricc.duckdns.org:3000");
        get("/", (request, response) -> {
            Status status = checker.checkWetty();
            return html().with(
                    head().with(
                            title("VS-RPI3 web access"),
                            link().withHref("web/main.css").withType("text/css").withRel("stylesheet")
                    ),
                    body().with(
                            h1("Raspberry Pi webserver"),
                            h2("VS-RPI3 access"),
                            ul().with(
                                    li().with(
                                            div().withClass("status " + status.toString()).with(
                                                    text("[" + status.toString() + "] ")
                                            ),
                                            a("Web SSH").withHref("/rpi-wetty")
                                    ),
                                    li().with(
                                            div().withClass("status " + status.toString()).with(
                                                    text("[" + TempSensor.getStatus() + "] ")
                                            ),
                                            text("Room temperature: " +
                                                    TempSensor.getTemp() + " \u00B0C (" +
                                                    TempSensor.oGetDelay()
                                                            .map(Object::toString)
                                                            .orElse("Unknown") + " seconds to next update)"
                                            )
                                    )
                            )
                    ),
                    div().withClass("temp").with(
                            text("placeholder")
                    ),
                    h2("CORE2 access"),
                    footer().with(
                            hr(),
                            em().with(text(uptime.toString()))
                    ),
                    script().withSrc("https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"),
                    script().withSrc("web/main.js")
            ).render();
        });
    }

    public static void main(String[] args) {
        init();
        setRoutes();
        TempSensor.startScheduler();
    }
}
