package net.miarma.api.microservices.huertosdecine.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import net.miarma.api.backlib.ConfigManager;
import net.miarma.api.backlib.Constants;
import net.miarma.api.backlib.LogAccumulator;
import net.miarma.api.backlib.util.DeploymentUtil;

public class CineMainVerticle extends AbstractVerticle {
    private ConfigManager configManager;

    @Override
    public void start(Promise<Void> startPromise) {
        try {
            this.configManager = ConfigManager.getInstance();
            deployVerticles();
            startPromise.complete();
        } catch (Exception e) {
            Constants.LOGGER.error(DeploymentUtil.failMessage(CineMainVerticle.class, e));
            startPromise.fail(e);
        }
    }

    private void deployVerticles() {
        vertx.deployVerticle(new CineDataVerticle(), result -> {
            if (result.succeeded()) {
                String message = String.join("\n\r  ",
                        DeploymentUtil.successMessage(CineDataVerticle.class),
                        DeploymentUtil.apiUrlMessage(
                                configManager.getHost(),
                                configManager.getIntProperty("cine.data.port")
                        )
                );
                LogAccumulator.add(message);
            } else {
                LogAccumulator.add(DeploymentUtil.failMessage(CineDataVerticle.class, result.cause()));
            }
        });

        vertx.deployVerticle(new CineLogicVerticle(), result -> {
            if (result.succeeded()) {
                String message = String.join("\n\r  ",
                        DeploymentUtil.successMessage(CineLogicVerticle.class),
                        DeploymentUtil.apiUrlMessage(
                                configManager.getHost(),
                                configManager.getIntProperty("cine.logic.port")
                        )
                );
                LogAccumulator.add(message);
            } else {
                LogAccumulator.add(DeploymentUtil.failMessage(CineLogicVerticle.class, result.cause()));
            }
        });
    }
}
