package fr.uga.miashs.album.control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fr.uga.miashs.album.util.Pages;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/faces/*")
public class LoginFilter implements Filter {

	
	public String[] filteredPages;
    
	/**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		filteredPages = new String[] {
				Pages.add_album,
				Pages.list_album
		};
	}
	
	
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String requestedUri = ((HttpServletRequest) request).getRequestURI().substring(((HttpServletRequest) request).getContextPath().length()+1);
		for (String s : filteredPages) {
			if (s.equals(requestedUri)) {
				 HttpSession session = ((HttpServletRequest) request).getSession(false);
				 if (session == null || 
						 ((AppUserSession) session.getAttribute("appUserSession")) == null ||
								 ((AppUserSession) session.getAttribute("appUserSession")).getConnectedUser()==null) {
					 request.getRequestDispatcher(Pages.login).forward(request, response);
				 }
			}
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}



}
