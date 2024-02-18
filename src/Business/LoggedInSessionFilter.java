package Business;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LoggedInSessionFilter", urlPatterns = {"/createContractServlet", "/insertUpdateServlet", "/orderDetailsServlet", "/orderPublishingServlet", "/orderShipmentUpdatesServlet", "/visualizeOrdersServlet", "/createContract.jsp", "/orderDetail.jsp", "/orderAssociated.jsp", "/orderSubmitting.jsp", "/updateOrder.jsp"})
public class LoggedInSessionFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }




    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest newRequest = (HttpServletRequest) request;

        HttpSession session = newRequest.getSession(false);
        HttpServletResponse resp= (HttpServletResponse) response;
        if(session == null){
            RequestDispatcher dispatcher = request.getRequestDispatcher("./.auth/login/google");
            dispatcher.forward(request, response);
            return;
        }else if(session.getAttribute("Account") == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("./.auth/login/google");
            dispatcher.forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }
}
