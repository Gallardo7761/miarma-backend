 package net.miarma.api.microservices.huertos.routing;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.Pool;
import net.miarma.api.backlib.util.RouterUtil;
import net.miarma.api.microservices.huertos.handlers.BalanceLogicHandler;
import net.miarma.api.microservices.huertos.handlers.IncomeLogicHandler;
import net.miarma.api.microservices.huertos.handlers.MailHandler;
import net.miarma.api.microservices.huertos.handlers.MemberLogicHandler;
import net.miarma.api.microservices.huertos.handlers.RequestLogicHandler;
import net.miarma.api.microservices.huertos.routing.middlewares.HuertosAuthGuard;
import net.miarma.api.microservices.huertos.services.MemberService;

public class HuertosLogicRouter {
	public static void mount(Router router, Vertx vertx, Pool pool) {
		MemberLogicHandler hMemberLogic = new MemberLogicHandler(vertx);
		MemberService memberService = new MemberService(pool);
		IncomeLogicHandler hIncomeLogic = new IncomeLogicHandler(vertx);
		BalanceLogicHandler hBalanceLogic = new BalanceLogicHandler(vertx);
		RequestLogicHandler hRequestLogic = new RequestLogicHandler(vertx);
		MailHandler hMail = new MailHandler(vertx, pool);
		HuertosAuthGuard authGuard = new HuertosAuthGuard(memberService);
		
		router.route().handler(BodyHandler.create());
		
		router.post(HuertosEndpoints.LOGIN).handler(hMemberLogic::login);
		router.get(HuertosEndpoints.MEMBER_BY_NUMBER).handler(authGuard.check()).handler(hMemberLogic::getByMemberNumber);
		router.get(HuertosEndpoints.MEMBER_BY_PLOT).handler(authGuard.check()).handler(hMemberLogic::getByPlotNumber);
		router.get(HuertosEndpoints.MEMBER_BY_DNI).handler(authGuard.check()).handler(hMemberLogic::getByDni);
		router.get(HuertosEndpoints.MEMBER_PAYMENTS).handler(authGuard.check()).handler(hMemberLogic::getUserPayments);
		router.get(HuertosEndpoints.MEMBER_HAS_PAID).handler(authGuard.check()).handler(hMemberLogic::hasPaid);
		router.get(HuertosEndpoints.MEMBER_WAITLIST).handler(authGuard.check()).handler(hMemberLogic::getWaitlist);
		router.get(HuertosEndpoints.MEMBER_LIMITED_WAITLIST).handler(hMemberLogic::getLimitedWaitlist);
		router.get(HuertosEndpoints.LAST_MEMBER_NUMBER).handler(hMemberLogic::getLastMemberNumber);
		router.get(HuertosEndpoints.BALANCE_WITH_TOTALS).handler(authGuard.check()).handler(hBalanceLogic::getBalanceWithTotals);
		router.get(HuertosEndpoints.REQUESTS_WITH_PRE_USERS).handler(authGuard.check()).handler(hRequestLogic::getRequestsWithPreUsers);
		router.get(HuertosEndpoints.REQUEST_WITH_PRE_USER).handler(authGuard.check()).handler(hRequestLogic::getRequestWithPreUser);
		router.get(HuertosEndpoints.MEMBER_PROFILE).handler(hMemberLogic::getProfile);
		router.get(HuertosEndpoints.REQUEST_COUNT).handler(authGuard.check()).handler(hRequestLogic::getRequestCount);
		router.get(HuertosEndpoints.MY_INCOMES).handler(authGuard.check()).handler(hIncomeLogic::getMyIncomes);
		router.get(HuertosEndpoints.MY_REQUESTS).handler(authGuard.check()).handler(hRequestLogic::getMyRequests);
		router.put(HuertosEndpoints.ACCEPT_REQUEST).handler(authGuard.check()).handler(hRequestLogic::acceptRequest);
		router.put(HuertosEndpoints.REJECT_REQUEST).handler(authGuard.check()).handler(hRequestLogic::rejectRequest);
		router.put(HuertosEndpoints.CHANGE_MEMBER_STATUS).handler(authGuard.check()).handler(hMemberLogic::changeMemberStatus);
		router.put(HuertosEndpoints.CHANGE_MEMBER_TYPE).handler(authGuard.check()).handler(hMemberLogic::changeMemberType);
		router.get(HuertosEndpoints.MEMBER_HAS_COLLABORATOR).handler(authGuard.check()).handler(hMemberLogic::hasCollaborator);
		router.get(HuertosEndpoints.MEMBER_HAS_COLLABORATOR_REQUEST).handler(authGuard.check()).handler(hMemberLogic::hasCollaboratorRequest);
		router.get(HuertosEndpoints.MEMBER_HAS_GREENHOUSE).handler(authGuard.check()).handler(hMemberLogic::hasGreenHouse);
		router.get(HuertosEndpoints.MEMBER_HAS_GREENHOUSE_REQUEST).handler(authGuard.check()).handler(hMemberLogic::hasGreenHouseRequest);
		router.post(HuertosEndpoints.PRE_USER_VALIDATE).handler(hMemberLogic::validatePreUser);
		
		router.get(HuertosEndpoints.MAILS).handler(hMail::getFolder);
		router.get(HuertosEndpoints.MAIL).handler(hMail::getMail);
		router.post(HuertosEndpoints.SEND_MAIL).handler(hMail::sendMail);
		

	}
}
