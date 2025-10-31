package net.miarma.api.microservices.huertosdecine.routing;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.Constants.CineUserRole;
import net.miarma.api.backlib.http.ApiResponse;
import net.miarma.api.backlib.http.ApiStatus;
import net.miarma.api.backlib.security.SusPather;
import net.miarma.api.microservices.huertosdecine.handlers.MovieDataHandler;
import net.miarma.api.microservices.huertosdecine.handlers.ViewerDataHandler;
import net.miarma.api.microservices.huertosdecine.routing.middlewares.CineAuthGuard;
import net.miarma.api.microservices.huertosdecine.services.ViewerService;

public class CineDataRouter {
    public static void mount(Router router, Vertx vertx, Pool pool) {
        MovieDataHandler hMovieData = new MovieDataHandler(pool);
        ViewerDataHandler hViewerData = new ViewerDataHandler(pool);
        ViewerService viewerService = new ViewerService(pool);
        CineAuthGuard authGuard = new CineAuthGuard(viewerService);

        router.route().handler(BodyHandler.create());

        router.get(CineEndpoints.MOVIES).handler(authGuard.check()).handler(hMovieData::getAll);
        router.get(CineEndpoints.MOVIE).handler(authGuard.check()).handler(hMovieData::getById);
        router.post(CineEndpoints.MOVIES).handler(authGuard.check(CineUserRole.ADMIN)).handler(hMovieData::create);
        router.put(CineEndpoints.MOVIE).handler(authGuard.check(CineUserRole.ADMIN)).handler(hMovieData::update);
        router.delete(CineEndpoints.MOVIE).handler(authGuard.check(CineUserRole.ADMIN)).handler(hMovieData::delete);

        router.get(CineEndpoints.VIEWERS).handler(authGuard.check(CineUserRole.ADMIN)).handler(hViewerData::getAll);
        router.get(CineEndpoints.VIEWER).handler(authGuard.check(CineUserRole.ADMIN)).handler(hViewerData::getById);
        router.post(CineEndpoints.VIEWERS).handler(authGuard.check(CineUserRole.ADMIN)).handler(hViewerData::create);
        router.put(CineEndpoints.VIEWER).handler(authGuard.check(CineUserRole.ADMIN)).handler(hViewerData::update);
        router.delete(CineEndpoints.VIEWER).handler(authGuard.check(CineUserRole.ADMIN)).handler(hViewerData::delete);
        router.post(CineEndpoints.VIEWER_METADATA).handler(authGuard.check(CineUserRole.ADMIN)).handler(hViewerData::createMetadata);
    }
}
