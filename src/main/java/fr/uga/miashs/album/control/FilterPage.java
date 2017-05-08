package fr.uga.miashs.album.control;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.enterprise.context.NonexistentConversationException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@WebFilter("/*")
public class FilterPage implements Filter {

    private class ConversationAwareRequestWrapper extends HttpServletRequestWrapper {

        public ConversationAwareRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            if ("cid".equals(name)) {
                return null;
            }
            return super.getParameter(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> map = super.getParameterMap();
            map.remove("cid");
            return map;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(getParameterMap().keySet());
        }

    }

    public String[] filteredPages;
    @Inject
    private AppUserSession appUserSession;
    @Inject PictureService pictureService;
    @Inject AlbumController albumController;

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

    boolean alreadyCommited = false;
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    String requestedUri = request.getRequestURI()
        .substring(request.getContextPath().length()+1);

    // @TODO
    // Filter paramet cid, and call AlbumController.endConversation()
    // Maybe, find a solution for always remove parameter cid. It would be the better
    if (request.getParameter("cid")!=null && !request.getParameter("cid").equals("")) {
        System.out.println("[Warning] conversation not properly closed");
    }

    // Filter 403 and login
    if (requestedUri.equals(Pages.error_403))
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    else {
        for (String s : Pages.FILTEREDPAGE_CONNECTED) {
            if (s.equals(requestedUri)) {
                if(appUserSession == null || appUserSession.getConnectedUser() == null){
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    request.getRequestDispatcher(Pages.login).forward(request, response);
                    alreadyCommited=true;
                }
            }
        }
    }

    // Filter ghost folder « pictures/ »
    if (requestedUri.startsWith("pictures/")){
            if ( appUserSession != null
                    && appUserSession.getConnectedUser() != null){
                String pictureStringId = requestedUri.substring(requestedUri.indexOf('/')+1);
                try {
                    Long pictureId = Long.valueOf(pictureStringId);
                    Picture picture = pictureService.read(pictureId);
                    if (picture != null) {
                        if ( picture.getAlbum().getOwner()
                                .equals(appUserSession.getConnectedUser())
                                || picture.getAlbum().getSharedWith().contains(
                                    appUserSession.getConnectedUser())) {
                            response.reset();
                            if (picture.getContentType()!=""
                                    || picture.getContentType()!=null)
                                response.setContentType(picture.getContentType());
                            BufferedOutputStream outputStream = new BufferedOutputStream(
                                    response.getOutputStream());
                            outputStream.write(picture.getFile());
                            outputStream.close();
                            // facesContext.responseComplete();
                            alreadyCommited = true;
                        }
                    }
                } catch(NumberFormatException | ServiceException e) {
                }
            }
            else {
                // request.getRequestDispatcher(Pages.login).forward(request, response);
                // ==> Doesn't work because HttpServletResponse#sendError() send
                // IllegalStateException (see documentation)
                // if you want write a custome page, write a html page
                // and send it with :
                // outputStream.write(Files.readAllBytes(Paths.get(
                //             request.getSession().getServletContext().
                //             getRealPath("/"+Pages.error_403_picture))));
                // You can also customize error page with js
                // (test windows.location.href)
            }
    }

    // try catch for when we type http://cid=3
    // http://www.programcreek.com/java-api-examples/index.php?source_dir=primefaces-starter-master/primefaces-webapp/src/main/java/com/mycompany/lifecycle/ConversationExceptionFilter.java
    // See also
    // http://www.programcreek.com/java-api-examples/index.php?api=javax.enterprise.context.NonexistentConversationException
    // Works only in Production mode (configure it in web.xml)
    // Line same thing with NonexistentConversationException
    // Maybe try http://www.programcreek.com/java-api-examples/index.php?source_dir=seam-booking-ogm-master/src/main/java/org/jboss/seam/examples/booking/exceptioncontrol/ConversationExceptionHandler.java with Jboss
    if (!alreadyCommited)
        try {
                chain.doFilter(request, response);
            } catch (ServletException e) {
                if (e.getCause() instanceof NonexistentConversationException) {
                    HttpServletRequestWrapper wrapper = new ConversationAwareRequestWrapper(request);
                    String context = request.getContextPath();
                    String viewId = request.getRequestURI();
                    String path = viewId.replaceFirst(context, "");
                    request.getRequestDispatcher(path).forward(wrapper, res);
                }
                else {
                    throw e;
                }
            }
    }

}

// vim: sw=4 ts=4 et:
