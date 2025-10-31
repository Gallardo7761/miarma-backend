package net.miarma.api.microservices.mpaste.routing;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.http.ApiResponse;
import net.miarma.api.backlib.http.ApiStatus;
import net.miarma.api.backlib.security.SusPather;
import net.miarma.api.microservices.mpaste.handlers.PasteLogicHandler;

public class MPasteLogicRouter {
	public static void mount(Router router, Vertx vertx, Pool pool) {
		PasteLogicHandler hPasteLogic = new PasteLogicHandler(vertx);
		
		router.route().handler(BodyHandler.create());

		router.get(MPasteEndpoints.PASTE_BY_KEY).handler(hPasteLogic::getByKey);
	}
}
