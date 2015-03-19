package com.example.zstd.microblog.servlet;

import com.example.zstd.microblog.dto.RegistrationData;
import com.example.zstd.microblog.dto.RegistrationDataBuilder;
import com.example.zstd.microblog.exception.RegistrationException;
import com.example.zstd.microblog.model.User;
import com.example.zstd.microblog.service.RegistrationService;
import com.example.zstd.microblog.utils.SomeUtils;
import com.google.common.base.Strings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Servlet processing new users registration. 
 */
@WebServlet(urlPatterns = {"register"})
public class RegistrationServlet extends HttpServlet{

    private static final Logger LOG = Logger.getLogger(RegistrationServlet.class.getName());

	private RegistrationService registrationService = new RegistrationService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doGet: request for register page, redirecting to welcome page");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.finest("doPost: " + request.getParameterMap());
        RegistrationData data = createRegistrationData(request);

        List<String> validationErrors = data.validate();
        if(validationErrors.isEmpty()) {
            try {
                User registered = registrationService.create(data);
                request.login(registered.getUsername(), registered.getPassword());
                request.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(request, response);
            } catch (RegistrationException e) {
                LOG.log(Level.SEVERE,"Failed to register ",e);
                throw new ServletException(e);
            }
        } else {
            LOG.log(Level.SEVERE,"Registration data validation failed. Validations: " + validationErrors);
            response.sendRedirect("index.jsp?error=some_validation_error");
        }
	}

    private RegistrationData createRegistrationData(HttpServletRequest request) {
        Map<String,String[]> map = request.getParameterMap();
        return RegistrationDataBuilder.aRegistrationData().
                withUsername(Strings.nullToEmpty(SomeUtils.getFirst("username", map)).trim()).
                withNickname(Strings.nullToEmpty(SomeUtils.getFirst("nickname", map)).trim()).
                withPassword(Strings.nullToEmpty(SomeUtils.getFirst("password", map)).trim()).
                withPasswordCheck(Strings.nullToEmpty(SomeUtils.getFirst("passwordCheck", map)).trim()).
                withDescription(Strings.nullToEmpty(SomeUtils.getFirst("description", map)).trim()).
                withPhotoUrl(Strings.nullToEmpty(SomeUtils.getFirst("photoUrl", map)).trim()).
                build();
    }

}
