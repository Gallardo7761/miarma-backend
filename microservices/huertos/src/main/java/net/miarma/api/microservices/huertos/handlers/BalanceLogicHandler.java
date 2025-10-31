package net.miarma.api.microservices.huertos.handlers;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.Constants;
import net.miarma.api.backlib.http.ApiStatus;
import net.miarma.api.backlib.util.EventBusUtil;
import net.miarma.api.backlib.util.JsonUtil;

public class BalanceLogicHandler {
	private final Vertx vertx;
	
	public BalanceLogicHandler(Vertx vertx) {
		this.vertx = vertx;
	}
	
	public void getBalanceWithTotals(RoutingContext ctx) {
		JsonObject request = new JsonObject().put("action", "getBalanceWithTotals");
		vertx.eventBus().request(Constants.HUERTOS_EVENT_BUS, request, ar -> {
			if (ar.succeeded()) JsonUtil.sendJson(ctx, ApiStatus.OK, ar.result().body());
			else EventBusUtil.handleReplyError(ctx, ar.cause(), "Balance not found");
		});
	}
}
