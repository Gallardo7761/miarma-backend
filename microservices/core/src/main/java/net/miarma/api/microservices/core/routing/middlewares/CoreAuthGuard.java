package net.miarma.api.microservices.core.routing.middlewares;

import java.util.function.Consumer;

import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.Constants.CoreUserRole;
import net.miarma.api.backlib.middlewares.AbstractAuthGuard;
import net.miarma.api.backlib.core.entities.UserEntity;
import net.miarma.api.backlib.core.services.UserService;

public class CoreAuthGuard extends AbstractAuthGuard<UserEntity, CoreUserRole> {
	private final UserService userService;
	
	public CoreAuthGuard(UserService userService) {
		this.userService = userService;
	}
	
	@Override
    protected CoreUserRole parseRole(String roleStr) {
        return CoreUserRole.valueOf(roleStr.toUpperCase());
    }

    @Override
    protected void getUserEntity(int userId, RoutingContext ctx, Consumer<UserEntity> callback) {
        userService.getById(userId).onComplete(ar -> {
            if (ar.succeeded()) callback.accept(ar.result());
            else callback.accept(null);
        });
    }

    @Override
    protected boolean hasPermission(UserEntity user, CoreUserRole role) {
        return user.getGlobal_role() == CoreUserRole.ADMIN;
    }
}
