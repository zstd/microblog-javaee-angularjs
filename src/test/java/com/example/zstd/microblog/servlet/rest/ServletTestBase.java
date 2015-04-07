package com.example.zstd.microblog.servlet.rest;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ServletTestBase {

    private static final Logger LOG = Logger.getLogger(ServletTestBase.class.getName());

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected StringWriter stringWriter = new StringWriter();
    protected int responseStatusCode = 0;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                responseStatusCode = (Integer) invocation.getArguments()[0];
                return null;
            }
        }).when(response).setStatus(anyInt());

        doSetUp();
    }

    /**
     * Child classes may override to add some specific setup.
     */
    protected void doSetUp() {

    }

    protected void givenRequestContainsParameters(final Map<String,String[]> map) {
        when(request.getParameterMap()).thenReturn(map);
        when(request.getParameter(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String paramValue[] = map.get(invocation.getArguments()[0]);
                return paramValue != null ? paramValue[0] : null;
            }
        });
    }


    protected void givenRequestContainsUserWithRoles(final String name, final String...roles) {
        when(request.getUserPrincipal()).thenReturn(new Principal() {
            @Override
            public String getName() {
                return name;
            }
        });
        when(request.isUserInRole(anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return Arrays.asList(roles).contains(invocation.getArguments()[0]);
            }
        });
    }

    protected String fileContentAsString(String pathToFile) {
        Preconditions.checkNotNull(pathToFile);
        String result = null;
        try {
            URL url = this.getClass().getResource(pathToFile);
            Preconditions.checkNotNull(url,"Resource not found: " + pathToFile);
            String fileStr = this.getClass().getResource(pathToFile).getFile();
            LOG.log(Level.CONFIG,"Loading resource from file: " + fileStr);
            result = Files.toString(new File(fileStr), Charset.forName("UTF-8"));
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return result;
    }


}