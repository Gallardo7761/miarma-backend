package net.miarma.api.backlib.middlewares;

import java.util.function.Consumer;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.http.ApiStatus;
import net.miarma.api.backlib.interfaces.IUserRole;
import net.miarma.api.backlib.security.JWTManager;
import net.miarma.api.backlib.util.JsonUtil;

/**
 * Base para AuthGuards de microservicios.
 * Maneja extracción de JWT y verificación básica.
 * Los microservicios solo implementan getUserEntity y hasPermission.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractAuthGuard<U, R extends Enum<R> & IUserRole> {
    
    protected abstract R parseRole(String roleStr);
    protected abstract void getUserEntity(int userId, RoutingContext ctx, Consumer<U> callback);
    protected abstract boolean hasPermission(U user, R role);

    public Handler<RoutingContext> check(R... allowedRoles) {
        return ctx -> {
            String token = extractToken(ctx);
            if (token == null || !JWTManager.getInstance().isValid(token)) {
                JsonUtil.sendJson(ctx, ApiStatus.UNAUTHORIZED, "Invalid or missing token");
                return;
            }

            int userId = JWTManager.getInstance().extractUserId(token);

            getUserEntity(userId, ctx, entity -> {
                if (entity == null) {
                    JsonUtil.sendJson(ctx, ApiStatus.UNAUTHORIZED, "User not found");
                    return;
                }

                R userRole = extractRoleFromEntity(entity);

                if (allowedRoles.length == 0 || hasPermission(entity, userRole, allowedRoles)) {
                    ctx.put("userEntity", entity);
                    ctx.next();
                } else {
                    JsonUtil.sendJson(ctx, ApiStatus.FORBIDDEN, "Forbidden");
                }
            });
        };
    }
    
<<<<<<< HEAD
    protected boolean isRoleAllowed(R role, R... allowedRoles) {
        for (R allowed : allowedRoles) {
            if (role == allowed) return true;
=======
    protected R extractRoleFromEntity(U user) {
        try {
            return (R) user.getClass().getMethod("getRole").invoke(user);
        } catch (Exception e) {
            return null;
>>>>>>> refs/remotes/origin/dev
        }
    }

    protected String extractToken(RoutingContext ctx) {
        String authHeader = ctx.request().getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
