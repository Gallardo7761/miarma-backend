package net.miarma.api.microservices.miarmacraft.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import net.miarma.api.backlib.ConfigManager;
import net.miarma.api.backlib.Constants;
import net.miarma.api.backlib.LogAccumulator;
import net.miarma.api.backlib.util.DeploymentUtil;

public class MMCMainVerticle extends AbstractVerticle {
	private ConfigManager configManager;
	
	@Override
	public void start(Promise<Void> startPromise) {
		try {
			this.configManager = ConfigManager.getInstance(); 
			deployVerticles();
			startPromise.complete();
		} catch (Exception e) {
            Constants.LOGGER.error(DeploymentUtil.failMessage(MMCMainVerticle.class, e));
			startPromise.fail(e);
		}
	}
	
	private void deployVerticles() {
		vertx.deployVerticle(new MMCDataVerticle(), result -> {
			if (result.succeeded()) {
				String message = String.join("\n\r  ",
						DeploymentUtil.successMessage(MMCDataVerticle.class),
						DeploymentUtil.apiUrlMessage(
								configManager.getHost(),
								configManager.getIntProperty("mmc.data.port")
						)
				);
				LogAccumulator.add(message);
			} else {
				LogAccumulator.add(DeploymentUtil.failMessage(MMCDataVerticle.class, result.cause()));
			}
		});
		
		vertx.deployVerticle(new MMCLogicVerticle(), result -> {
			if (result.succeeded()) {
				String message = String.join("\n\r  ",
						DeploymentUtil.successMessage(MMCLogicVerticle.class),
						DeploymentUtil.apiUrlMessage(
								configManager.getHost(),
								configManager.getIntProperty("mmc.logic.port")
						)
				);
				LogAccumulator.add(message);
			} else {
				LogAccumulator.add(DeploymentUtil.failMessage(MMCLogicVerticle.class, result.cause()));
			}
		});
	}

}
