package au.com.company;

import au.com.company.handlers.EmailHandler;
import au.com.company.handlers.HttpHandler;
import com.netflix.config.DynamicPropertyFactory;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.protocol.http.server.*;
import rx.Observable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static au.com.company.Constants.FAILURE_MESSAGE;
import static au.com.company.Constants.SERVER_PORT;

public class EntryPoint {
    private static final Map<String, Class<? extends HttpHandler>> ROUTES = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        final int port = DynamicPropertyFactory.getInstance().getIntProperty(SERVER_PORT, 8080).getValue();

        registerRoutes();

        new HttpServerBuilder<>(port,
                (RequestHandler<ByteBuf, ByteBuf>) (req, res) ->
                        Observable.just(req)
                                .flatMap(request -> {
                                    Class<? extends HttpHandler> handler = ROUTES.get(req.getUri());
                                    if (handler != null) {
                                        try {
                                            return handler.newInstance().handle(request, res);
                                        } catch (Exception exp) {
                                            res.writeString(FAILURE_MESSAGE);
                                        }
                                    }

                                    res.setStatus(HttpResponseStatus.NOT_FOUND);
                                    return res.close();
                                }), true)
                .enableWireLogging(LogLevel.TRACE)
                .build()
                .startAndWait();
    }

    /**
     * This function can be used to add service endpoints and routes linked to it.
     * The assumption is that each class will implement one endpoint.
     */
    private static void registerRoutes() {
        ROUTES.put("/email/send", EmailHandler.class);
    }
}
