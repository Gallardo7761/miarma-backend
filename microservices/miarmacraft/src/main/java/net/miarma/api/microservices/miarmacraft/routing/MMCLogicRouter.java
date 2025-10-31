package net.miarma.api.microservices.miarmacraft.routing;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.microservices.miarmacraft.handlers.PlayerLogicHandler;
import net.miarma.api.microservices.miarmacraft.routing.middlewares.MMCAuthGuard;
import net.miarma.api.microservices.miarmacraft.services.PlayerService;

public class MMCLogicRouter {
	public static void mount(Router router, Vertx vertx, Pool pool) {
		PlayerLogicHandler hPlayerLogic = new PlayerLogicHandler(vertx);
		PlayerService playerService = new PlayerService(pool);
		MMCAuthGuard authGuard = new MMCAuthGuard(playerService);
		
		router.route().handler(BodyHandler.create());

		router.post(MMCEndpoints.LOGIN).handler(hPlayerLogic::login);
		router.get(MMCEndpoints.PLAYER_STATUS).handler(authGuard.check()).handler(hPlayerLogic::getStatus);
		router.put(MMCEndpoints.PLAYER_STATUS).handler(authGuard.check()).handler(hPlayerLogic::updateStatus);
		router.get(MMCEndpoints.PLAYER_ROLE).handler(authGuard.check()).handler(hPlayerLogic::getRole);
		router.put(MMCEndpoints.PLAYER_ROLE).handler(authGuard.check()).handler(hPlayerLogic::updateRole);
		router.get(MMCEndpoints.PLAYER_AVATAR).handler(authGuard.check()).handler(hPlayerLogic::getAvatar);
		router.put(MMCEndpoints.PLAYER_AVATAR).handler(authGuard.check()).handler(hPlayerLogic::updateAvatar);
		router.get(MMCEndpoints.PLAYER_INFO).handler(authGuard.check()).handler(hPlayerLogic::getInfo);
		router.get(MMCEndpoints.PLAYER_EXISTS).handler(authGuard.check()).handler(hPlayerLogic::playerExists);
	}
}
