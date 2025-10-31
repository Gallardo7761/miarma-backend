package net.miarma.api.microservices.huertosdecine.routing.middlewares;

import java.util.function.Consumer;

import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.Constants.CineUserRole;
import net.miarma.api.backlib.middlewares.AbstractAuthGuard;
import net.miarma.api.microservices.huertosdecine.entities.ViewerEntity;
import net.miarma.api.microservices.huertosdecine.services.ViewerService;

public class CineAuthGuard extends AbstractAuthGuard<ViewerEntity, CineUserRole> {
	private final ViewerService viewerService;

    public CineAuthGuard(ViewerService viewerService) {
        this.viewerService = viewerService;
    }

    @Override
    protected CineUserRole parseRole(String roleStr) {
        return CineUserRole.valueOf(roleStr.toUpperCase());
    }

    @Override
    protected void getUserEntity(int userId, RoutingContext ctx, Consumer<ViewerEntity> callback) {
        viewerService.getById(userId).onComplete(ar -> {
            if (ar.succeeded()) callback.accept(ar.result());
            else callback.accept(null);
        });
    }

    @Override
    protected boolean hasPermission(ViewerEntity user, CineUserRole role) {
        return user.getRole() == CineUserRole.ADMIN;
    }
}
