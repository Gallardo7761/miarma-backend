package net.miarma.api.microservices.core.routing;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.Constants.CoreUserRole;
import net.miarma.api.backlib.core.handlers.FileLogicHandler;
import net.miarma.api.backlib.core.handlers.ScreenshotHandler;
import net.miarma.api.backlib.core.handlers.UserLogicHandler;
import net.miarma.api.backlib.core.services.UserService;
import net.miarma.api.microservices.core.routing.middlewares.CoreAuthGuard;

public class CoreLogicRouter {
	public static void mount(Router router, Vertx vertx, Pool pool) {
		UserLogicHandler hUserLogic = new UserLogicHandler(vertx);
		FileLogicHandler hFileLogic = new FileLogicHandler(vertx);
		ScreenshotHandler hScreenshot = new ScreenshotHandler(vertx);
        UserService userService = new UserService(pool);
        CoreAuthGuard authGuard = new CoreAuthGuard(userService);
        
        router.route().handler(BodyHandler.create());

		router.post(CoreEndpoints.LOGIN).handler(hUserLogic::login);
		router.get(CoreEndpoints.USER_INFO).handler(authGuard.check()).handler(hUserLogic::getInfo);
        router.post(CoreEndpoints.REGISTER).handler(hUserLogic::register);
        router.post(CoreEndpoints.CHANGE_PASSWORD).handler(authGuard.check()).handler(hUserLogic::changePassword);
        router.post(CoreEndpoints.LOGIN_VALID).handler(hUserLogic::loginValidate);
        router.get(CoreEndpoints.VALIDATE_TOKEN).handler(hUserLogic::validateToken);
        router.get(CoreEndpoints.REFRESH_TOKEN).handler(hUserLogic::refreshToken);
		
        router.get(CoreEndpoints.USER_EXISTS).handler(authGuard.check()).handler(hUserLogic::exists);
		router.get(CoreEndpoints.USER_STATUS).handler(authGuard.check()).handler(hUserLogic::getStatus);
		router.put(CoreEndpoints.USER_STATUS).handler(authGuard.check(CoreUserRole.ADMIN)).handler(hUserLogic::updateStatus);
		router.get(CoreEndpoints.USER_ROLE).handler(authGuard.check()).handler(hUserLogic::getRole);
		router.put(CoreEndpoints.USER_ROLE).handler(authGuard.check(CoreUserRole.ADMIN)).handler(hUserLogic::updateRole);
		router.get(CoreEndpoints.USER_AVATAR).handler(authGuard.check()).handler(hUserLogic::getAvatar);
		
		router.get(CoreEndpoints.FILE_DOWNLOAD).handler(authGuard.check()).handler(hFileLogic::downloadFile);
		router.get(CoreEndpoints.USER_FILES).handler(authGuard.check()).handler(hFileLogic::getUserFiles);
		
		router.get(CoreEndpoints.SCREENSHOT).handler(hScreenshot::getScreenshot);
	}
}
