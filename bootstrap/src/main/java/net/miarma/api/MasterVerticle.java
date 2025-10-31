package net.miarma.api;

import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import net.miarma.api.backlib.Constants;
import net.miarma.api.backlib.LogAccumulator;
import net.miarma.api.backlib.util.DeploymentUtil;
import net.miarma.api.microservices.core.verticles.CoreMainVerticle;
import net.miarma.api.microservices.huertos.verticles.HuertosMainVerticle;
import net.miarma.api.microservices.huertosdecine.verticles.CineMainVerticle;
import net.miarma.api.microservices.miarmacraft.verticles.MMCMainVerticle;
import net.miarma.api.microservices.mpaste.verticles.MPasteMainVerticle;

public class MasterVerticle extends AbstractVerticle {	
	@Override
    public void start(Promise<Void> startPromise) {
		deploy()
		.onSuccess(v -> {
			vertx.setTimer(300, id -> {
				LogAccumulator.flushToLogger(Constants.LOGGER);
				startPromise.complete();
			});
		})
		.onFailure(startPromise::fail);
    }
	
	private Future<Void> deploy() {
		Promise<Void> promise = Promise.promise();

		Future<String> core = vertx.deployVerticle(new CoreMainVerticle())
				.onSuccess(id -> LogAccumulator.add(DeploymentUtil.successMessage(CoreMainVerticle.class)))
				.onFailure(err -> LogAccumulator.add(DeploymentUtil.failMessage(CoreMainVerticle.class, err)));

		Future<String> huertos = vertx.deployVerticle(new HuertosMainVerticle())
				.onSuccess(id -> LogAccumulator.add(DeploymentUtil.successMessage(HuertosMainVerticle.class)))
				.onFailure(err -> LogAccumulator.add(DeploymentUtil.failMessage(HuertosMainVerticle.class, err)));

		Future<String> mmc = vertx.deployVerticle(new MMCMainVerticle())
				.onSuccess(id -> LogAccumulator.add(DeploymentUtil.successMessage(MMCMainVerticle.class)))
				.onFailure(err -> LogAccumulator.add(DeploymentUtil.failMessage(MMCMainVerticle.class, err)));

		Future<String> cine = vertx.deployVerticle(new CineMainVerticle())
				.onSuccess(id -> LogAccumulator.add(DeploymentUtil.successMessage(CineMainVerticle.class)))
				.onFailure(err -> LogAccumulator.add(DeploymentUtil.failMessage(CineMainVerticle.class, err)));

		Future<String> mpaste = vertx.deployVerticle(new MPasteMainVerticle())
				.onSuccess(id -> LogAccumulator.add(DeploymentUtil.successMessage(MPasteMainVerticle.class)))
				.onFailure(err -> LogAccumulator.add(DeploymentUtil.failMessage(MPasteMainVerticle.class, err)));
		
		Future.all(core, huertos, mmc, cine, mpaste)
				.onSuccess(_ -> promise.complete())
				.onFailure(promise::fail);

		return promise.future();
	}


    @Override
    public void stop(Promise<Void> stopPromise) {
    	vertx.deploymentIDs().forEach(id -> vertx.undeploy(id));
		stopPromise.complete();
    }
}
