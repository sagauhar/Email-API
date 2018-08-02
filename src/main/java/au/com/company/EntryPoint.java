package au.com.company;

import com.netflix.config.DynamicPropertyFactory;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.protocol.http.server.*;
import org.apache.commons.lang.StringUtils;
import rx.Observable;

import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static au.com.company.Constants.API_CLASSES;
import static au.com.company.Constants.FAILURE_MESSAGE;
import static au.com.company.Constants.SERVER_PORT;

public class EntryPoint {
    private static final Map<String, Method> ROUTES = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        final int port = DynamicPropertyFactory.getInstance().getIntProperty(SERVER_PORT, 8080).getValue();

        registerRoutes();

        new HttpServerBuilder<>(port,
                (RequestHandler<ByteBuf, ByteBuf>) (req, res) ->
                        Observable.just(req)
                                .flatMap(request -> {
                                    Method method = ROUTES.get(req.getUri());
                                    if (method != null) {
                                        try {
                                            return (Observable<? extends Void>) method.invoke(method.getDeclaringClass().newInstance(), request, res);
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
     */
    private static void registerRoutes() {
        String classes = DynamicPropertyFactory.getInstance().getStringProperty(API_CLASSES, StringUtils.EMPTY).getValue();
        Arrays.stream(classes.split(";"))
                .map(className -> {
                    try {
                        return Optional.of(EntryPoint.class.getClassLoader().loadClass(className));
                    } catch (ClassNotFoundException ignored) {}

                    return Optional.empty();
                })
                .filter(Optional::isPresent)
                .forEach(optional -> buildRoutesForClass((Class) optional.get()));
    }

    /**
     * This will build the paths by using the JAX RS Path annotation
     * @param clazz Class which needs to be checked for annotations
     */
    private static void buildRoutesForClass(Class clazz) {
        Annotation classAnnotation = clazz.getDeclaredAnnotation(Path.class);
        String basePath = classAnnotation != null ? ((Path) classAnnotation).value() : StringUtils.EMPTY;
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    Annotation methodAnnotation = method.getDeclaredAnnotation(Path.class);
                    if (methodAnnotation != null) {
                        ROUTES.put(basePath + ((Path) methodAnnotation).value(), method);
                    }
                });
    }
}
