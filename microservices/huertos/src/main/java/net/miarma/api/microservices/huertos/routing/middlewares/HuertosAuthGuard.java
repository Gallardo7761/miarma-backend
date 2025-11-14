package net.miarma.api.microservices.huertos.routing.middlewares;

import java.util.function.Consumer;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.Constants.HuertosUserRole;
import net.miarma.api.backlib.http.ApiStatus;
import net.miarma.api.backlib.middlewares.AbstractAuthGuard;
import net.miarma.api.backlib.security.JWTManager;
import net.miarma.api.backlib.util.JsonUtil;
import net.miarma.api.microservices.huertos.entities.MemberEntity;
import net.miarma.api.microservices.huertos.services.MemberService;

public class HuertosAuthGuard extends AbstractAuthGuard<MemberEntity, HuertosUserRole> {

    private final MemberService memberService;

    public HuertosAuthGuard(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    protected HuertosUserRole parseRole(String roleStr) {
        return HuertosUserRole.valueOf(roleStr.toUpperCase());
    }

    @Override
    protected void getUserEntity(int userId, RoutingContext ctx, Consumer<MemberEntity> callback) {
        memberService.getById(userId).onComplete(ar -> {
            if (ar.succeeded()) callback.accept(ar.result());
            else callback.accept(null);
        });
    }

    @Override
    protected boolean hasPermission(MemberEntity member, HuertosUserRole userRole, HuertosUserRole... allowedRoles) {
        if (member == null) return false;
        if (member.getRole() == HuertosUserRole.ADMIN || member.getRole() == HuertosUserRole.DEV) return true;
        for (HuertosUserRole role : allowedRoles) {
            if (member.getRole() == role) return true;
        }
        return false;
<<<<<<< HEAD
    }

    @Override
    public Handler<RoutingContext> check(HuertosUserRole... allowedRoles) {
        return ctx -> {
            String token = extractToken(ctx);
            if (token == null || !JWTManager.getInstance().isValid(token)) {
                JsonUtil.sendJson(ctx, ApiStatus.UNAUTHORIZED, "Invalid or missing token");
                return;
            }

            int userId = JWTManager.getInstance().extractUserId(token);

            getUserEntity(userId, ctx, member -> {
                if (member == null) {
                    JsonUtil.sendJson(ctx, ApiStatus.UNAUTHORIZED, "User not found");
                    return;
                }

                HuertosUserRole role = HuertosUserRole.USER;
                if (member.getRole() != null) {
                    role = member.getRole();
                }

                ctx.put("userId", userId);
                ctx.put("role", role);
                ctx.put("userEntity", member);

                if (allowedRoles.length == 0 || isRoleAllowed(role, allowedRoles)) {
                    ctx.next();
                } else {
                    JsonUtil.sendJson(ctx, ApiStatus.FORBIDDEN, "Forbidden");
                }
            });
        };
=======
>>>>>>> refs/remotes/origin/dev
    }

}
