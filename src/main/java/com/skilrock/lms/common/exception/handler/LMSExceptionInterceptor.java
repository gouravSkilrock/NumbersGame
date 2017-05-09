package com.skilrock.lms.common.exception.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.MailSender;

public class LMSExceptionInterceptor extends ExceptionMappingInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void sendMail(String errMsg) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String errTime = "" + new Date().getTime();
		String local = request.getLocalAddr() + "/" + request.getLocalName();

		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		String remote = request.getRemoteAddr() + " UserName "
				+ userBean.getUserName() + " OrgName " + userBean.getOrgName();

		ServletContext sc = ServletActionContext.getServletContext();

		System.out.println(local + "-------*****************-----------"
				+ remote);
		sc.setAttribute("ERROR_TIME", errTime);
		List to = new ArrayList();
		to.add("lms.error@skilrock.com");
		MailSender ms = new MailSender("lms.error@skilrock.com", "skilrock",
				to, "ServerError at " + local + " " + errTime, remote + " "
						+ errMsg, "");
		ms.setDaemon(true);
		ms.start();
	}

	@Override
	protected void publishException(ActionInvocation ai, ExceptionHolder eh) {

		System.out.println("I am in my custom exception handler");
		try {
			// log Exception here
			System.out.println(eh.getExceptionStack());
			/*sendMail(eh.getExceptionStack());*/

		} catch (IOException e) {
			System.out.println("Excetion");
			e.printStackTrace();

		}

	}

}

// FOR Testing Purpose

/*
 * System.out.println(sc.getMajorVersion()+"Maj"+sc.getRealPath("/")+"Real Path
 * ser info--"+sc.getServerInfo());
 * System.out.println(sc.getServletContextName()+"Cont
 * "+ServletActionContext.getRequest()+"Req ser
 * info--"+ServletActionContext.getValueStack(ServletActionContext.getRequest()));
 * System.out.println(ServletActionContext.getRequest().getContextPath()+"ContPath
 * "+ServletActionContext.getRequest().getLocalAddr()+":"+ServletActionContext.getRequest().getLocalName()+"Name
 * Port--"+ServletActionContext.getRequest().getLocalPort());
 * System.out.println(ServletActionContext.getRequest().getRemoteAddr()+"ContPath
 * "+ServletActionContext.getRequest().getRemoteHost()+":"+ServletActionContext.getRequest().getRemoteUser()+"Name
 * Port--"+ServletActionContext.getRequest().getRemotePort());
 * 2MajD:\jboss-4.2.2.GA\server\default\.\deploy\LMSLinuxNew.war\Real Path ser
 * info--JBossWeb/2.0.1.GA Struts BlankCont
 * org.apache.struts2.dispatcher.StrutsRequestWrapper@6cd0f9Req ser
 * info--com.opensymphony.xwork2.util.OgnlValueStack@ba652 /LMSLinuxNewContPath
 * 192.168.123.109:GauravSkilName Port--8080 192.168.123.109ContPath
 * 192.168.123.109:nullName Port--4149
 */
