package net.miarma.api.microservices.core.routing;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.Constants.CoreUserRole;
import net.miarma.api.backlib.core.handlers.FileDataHandler;
import net.miarma.api.backlib.core.handlers.UserDataHandler;
import net.miarma.api.backlib.core.services.UserService;
import net.miarma.api.microservices.core.routing.middlewares.CoreAuthGuard;

public class CoreDataRouter {
	public static void mount(Router router, Vertx vertx, Pool pool) {
		UserDataHandler hUserData = new UserDataHandler(pool);
		FileDataHandler hFileData = new FileDataHandler(pool);
		UserService userService = new UserService(pool);
		CoreAuthGuard authGuard = new CoreAuthGuard(userService);
		
		router.route().handler(BodyHandler.create());

		router.get(CoreEndpoints.USERS).handler(authGuard.check(CoreUserRole.ADMIN)).handler(hUserData::getAll);
		router.get(CoreEndpoints.USER).handler(authGuard.check(CoreUserRole.ADMIN)).handler(hUserData::getById);
		router.post(CoreEndpoints.USERS).handler(hUserData::create);
		router.put(CoreEndpoints.USER).handler(authGuard.check(CoreUserRole.ADMIN)).handler(hUserData::update);
		router.delete(CoreEndpoints.USER).handler(authGuard.check(CoreUserRole.ADMIN)).handler(hUserData::delete);
		
		router.get(CoreEndpoints.FILES).handler(authGuard.check()).handler(hFileData::getAll);
		router.get(CoreEndpoints.FILE).handler(authGuard.check()).handler(hFileData::getById);
		router.post(CoreEndpoints.FILE_UPLOAD).handler(authGuard.check()).handler(hFileData::create);
		router.put(CoreEndpoints.FILE).handler(authGuard.check()).handler(hFileData::update);
		router.delete(CoreEndpoints.FILE).handler(authGuard.check()).handler(hFileData::delete);
		
	}
}
