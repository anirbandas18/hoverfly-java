package io.specto.hoverfly.webserver;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static io.specto.hoverfly.webserver.WebServerUtils.findUnusedPort;

public class CaptureModeTestWebServer extends AbstractHandler {

    private static Server server;

    public static URI run() throws Exception {
        final int port = findUnusedPort();
        server = new Server(port);
        server.setHandler(new CaptureModeTestWebServer());
        server.start();
        return UriComponentsBuilder.fromUriString(String.format("http://localhost:%s", port)).build().toUri();
    }

    public static void terminate() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to terminate CaptureModeTestWebServer", e);
            }
        }
    }

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");

        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);

        // Must not use system line separator in Hello World string as
        // it would not match the expected files using linux line separator.
        if(request.getPathInfo().equals("/other")) {
            // Write back response
            response.getWriter().print("<h1>Hello Other World</h1>\n");
        }
        else {
            // Write back response
            response.getWriter().print("<h1>Hello World</h1>\n");
        }

        // Inform jetty that this request has now been handled
        baseRequest.setHandled(true);
    }
}
