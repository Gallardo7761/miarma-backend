package net.miarma.api.microservices.miarmacraft.routing.middlewares;

import java.util.function.Consumer;

import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.Constants.MMCUserRole;
import net.miarma.api.backlib.middlewares.AbstractAuthGuard;
import net.miarma.api.microservices.miarmacraft.entities.PlayerEntity;
import net.miarma.api.microservices.miarmacraft.services.PlayerService;

public class MMCAuthGuard extends AbstractAuthGuard<PlayerEntity, MMCUserRole> {
	private final PlayerService playerService;
	
	public MMCAuthGuard(PlayerService playerService) {
        this.playerService = playerService;
    }
	
	@Override
    protected MMCUserRole parseRole(String roleStr) {
        return MMCUserRole.valueOf(roleStr.toUpperCase());
    }

    @Override
    protected void getUserEntity(int userId, RoutingContext ctx, Consumer<PlayerEntity> callback) {
    	playerService.getById(userId).onComplete(ar -> {
            if (ar.succeeded()) callback.accept(ar.result());
            else callback.accept(null);
        });
    }

    @Override
    protected boolean hasPermission(PlayerEntity user, MMCUserRole role) {
        return user.getRole() == MMCUserRole.ADMIN;
    }

}

