package net.miarma.api.microservices.huertosdecine.routing;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.http.ApiResponse;
import net.miarma.api.backlib.http.ApiStatus;
import net.miarma.api.backlib.security.SusPather;
import net.miarma.api.microservices.huertosdecine.handlers.ViewerLogicHandler;
import net.miarma.api.microservices.huertosdecine.handlers.VoteLogicHandler;
import net.miarma.api.microservices.huertosdecine.routing.middlewares.CineAuthGuard;
import net.miarma.api.microservices.huertosdecine.services.ViewerService;

public class CineLogicRouter {
    public static void mount(Router router, Vertx vertx, Pool pool) {
        ViewerLogicHandler hViewerLogic = new ViewerLogicHandler(vertx);
        VoteLogicHandler hVoteLogic = new VoteLogicHandler(vertx);
        ViewerService viewerService = new ViewerService(pool);
        CineAuthGuard authGuard = new CineAuthGuard(viewerService);

        router.route().handler(BodyHandler.create());

        router.post(CineEndpoints.LOGIN).handler(hViewerLogic::login);
        router.get(CineEndpoints.VIEWER_VOTES_BY_MOVIE).handler(authGuard.check()).handler(hViewerLogic::getVotesOnMovieByUserId);
        router.get(CineEndpoints.MOVIE_VOTES).handler(authGuard.check()).handler(hVoteLogic::getVotes);
        router.post(CineEndpoints.MOVIE_VOTES).handler(authGuard.check()).handler(hVoteLogic::addVote);
        router.delete(CineEndpoints.MOVIE_VOTES).handler(authGuard.check()).handler(hVoteLogic::deleteVote);
        router.get(CineEndpoints.SELF_VOTES).handler(authGuard.check()).handler(hVoteLogic::getVoteSelf);
    }
}
