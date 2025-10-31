package net.miarma.api.microservices.huertos.routing;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.Constants.HuertosUserRole;
import net.miarma.api.backlib.util.RouterUtil;
import net.miarma.api.microservices.huertos.handlers.AnnouncementDataHandler;
import net.miarma.api.microservices.huertos.handlers.BalanceDataHandler;
import net.miarma.api.microservices.huertos.handlers.ExpenseDataHandler;
import net.miarma.api.microservices.huertos.handlers.IncomeDataHandler;
import net.miarma.api.microservices.huertos.handlers.MemberDataHandler;
import net.miarma.api.microservices.huertos.handlers.PreUserDataHandler;
import net.miarma.api.microservices.huertos.handlers.RequestDataHandler;
import net.miarma.api.microservices.huertos.routing.middlewares.HuertosAuthGuard;
import net.miarma.api.microservices.huertos.services.MemberService;

public class HuertosDataRouter {
	
	public static void mount(Router router, Vertx vertx, Pool pool) {
		AnnouncementDataHandler hAnnounceData = new AnnouncementDataHandler(pool);
		BalanceDataHandler hBalanceData = new BalanceDataHandler(pool);
		ExpenseDataHandler hExpenseData = new ExpenseDataHandler(pool);
		IncomeDataHandler hIncomeData = new IncomeDataHandler(pool);
		MemberDataHandler hMemberData = new MemberDataHandler(pool);
		PreUserDataHandler hPreUserData = new PreUserDataHandler(pool);
		RequestDataHandler hRequestData = new RequestDataHandler(pool);
		MemberService memberService = new MemberService(pool);
		HuertosAuthGuard authGuard = new HuertosAuthGuard(memberService);		
 
		router.route().handler(BodyHandler.create());
		
		router.get(HuertosEndpoints.ANNOUNCES).handler(authGuard.check()).handler(hAnnounceData::getAll);
		router.get(HuertosEndpoints.ANNOUNCE).handler(authGuard.check()).handler(hAnnounceData::getById);
		router.post(HuertosEndpoints.ANNOUNCES).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hAnnounceData::create);
		router.put(HuertosEndpoints.ANNOUNCE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hAnnounceData::update);
		router.delete(HuertosEndpoints.ANNOUNCE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hAnnounceData::delete);
		
		router.get(HuertosEndpoints.BALANCE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hBalanceData::getBalance);
		router.post(HuertosEndpoints.BALANCE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hBalanceData::update);
		router.delete(HuertosEndpoints.BALANCE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hBalanceData::create);
		
		router.get(HuertosEndpoints.EXPENSES).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hExpenseData::getAll);
		router.get(HuertosEndpoints.EXPENSE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hExpenseData::getById);
		router.post(HuertosEndpoints.EXPENSES).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hExpenseData::create);
		router.put(HuertosEndpoints.EXPENSE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hExpenseData::update);
		router.delete(HuertosEndpoints.EXPENSE).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hExpenseData::delete);
		
		router.get(HuertosEndpoints.INCOMES).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hIncomeData::getAll);
		router.get(HuertosEndpoints.INCOME).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hIncomeData::getById);
		router.post(HuertosEndpoints.INCOMES).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hIncomeData::create);
		router.put(HuertosEndpoints.INCOME).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hIncomeData::update);
		router.delete(HuertosEndpoints.INCOME).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hIncomeData::delete);
		router.get(HuertosEndpoints.INCOMES_WITH_NAMES).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hIncomeData::getIncomesWithNames);
		
		router.get(HuertosEndpoints.MEMBERS).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hMemberData::getAll);
		router.get(HuertosEndpoints.MEMBER).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hMemberData::getById);
		router.post(HuertosEndpoints.MEMBERS).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hMemberData::create);
		router.put(HuertosEndpoints.MEMBER).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hMemberData::update);
		router.delete(HuertosEndpoints.MEMBER).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hMemberData::delete);
		
		router.get(HuertosEndpoints.PRE_USERS).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hPreUserData::getAll);
		router.get(HuertosEndpoints.PRE_USER).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hPreUserData::getById);
		router.post(HuertosEndpoints.PRE_USERS).handler(hPreUserData::create);
		router.put(HuertosEndpoints.PRE_USER).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hPreUserData::update);
		router.delete(HuertosEndpoints.PRE_USER).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hPreUserData::delete);
		
		router.get(HuertosEndpoints.REQUESTS).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hRequestData::getAll);
		router.get(HuertosEndpoints.REQUEST).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hRequestData::getById);
		router.post(HuertosEndpoints.REQUESTS).handler(hRequestData::create);
		router.put(HuertosEndpoints.REQUEST).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hRequestData::update);
		router.delete(HuertosEndpoints.REQUEST).handler(authGuard.check(HuertosUserRole.ADMIN, HuertosUserRole.DEV)).handler(hRequestData::delete);
		
	}
}
