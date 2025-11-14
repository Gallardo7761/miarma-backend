package net.miarma.api.microservices.miarmacraft.routing;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.Constants.MMCUserRole;
import net.miarma.api.microservices.miarmacraft.handlers.ModDataHandler;
import net.miarma.api.microservices.miarmacraft.handlers.PlayerDataHandler;
import net.miarma.api.microservices.miarmacraft.routing.middlewares.MMCAuthGuard;
import net.miarma.api.microservices.miarmacraft.services.PlayerService;

public class MMCDataRouter {
	public static void mount(Router router, Vertx vertx, Pool pool) {
		ModDataHandler hModData = new ModDataHandler(pool);
		PlayerDataHandler hPlayerData = new PlayerDataHandler(pool);
		PlayerService playerService = new PlayerService(pool);
		MMCAuthGuard authGuard = new MMCAuthGuard(playerService);
		
		router.route().handler(BodyHandler.create());
		
		router.get(MMCEndpoints.MODS).handler(hModData::getAll);
		router.get(MMCEndpoints.MOD).handler(hModData::getById);
		router.post(MMCEndpoints.MODS).handler(BodyHandler.create().setBodyLimit(100 * 1024 * 1024)).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hModData::create); 
		router.put(MMCEndpoints.MOD).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hModData::update);
		router.delete(MMCEndpoints.MOD).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hModData::delete);
		
		router.get(MMCEndpoints.PLAYERS).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hPlayerData::getAll);
		router.post(MMCEndpoints.PLAYERS).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hPlayerData::create);
		router.put(MMCEndpoints.PLAYER).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hPlayerData::update);
		router.delete(MMCEndpoints.PLAYER).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hPlayerData::delete);
		router.get(MMCEndpoints.PLAYER).handler(authGuard.check(MMCUserRole.ADMIN)).handler(hPlayerData::getById);
	}
}
