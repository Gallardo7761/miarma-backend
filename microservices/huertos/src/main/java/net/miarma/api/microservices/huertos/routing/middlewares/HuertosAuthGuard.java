package net.miarma.api.microservices.huertos.routing.middlewares;

import java.util.function.Consumer;

import io.vertx.ext.web.RoutingContext;
import net.miarma.api.backlib.Constants.HuertosUserRole;
import net.miarma.api.backlib.middlewares.AbstractAuthGuard;
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
    protected boolean hasPermission(MemberEntity user, HuertosUserRole role) {
        return user.getRole() == HuertosUserRole.ADMIN;
    }
}
