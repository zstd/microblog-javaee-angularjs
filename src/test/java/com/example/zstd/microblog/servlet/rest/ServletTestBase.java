package com.example.zstd.microblog.servlet.rest;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import org.junit.Before;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
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
        doAnswer(invocation -> {
            responseStatusCode = (Integer) invocation.getArguments()[0];
            return null;
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
        when(request.getParameter(anyString())).thenAnswer(invocation -> {
            String paramValue[] = map.get(invocation.getArguments()[0]);
            return paramValue != null ? paramValue[0] : null;
        });
    }

    protected void givenRequestContainsPayload(final InputStream inputStream) throws IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new InputStreamReader(inputStream)));
    }


    protected void givenRequestContainsUserWithRoles(final String name, final String...roles) {
        when(request.getUserPrincipal()).thenReturn(() -> name);
        when(request.isUserInRole(anyString()))
                .thenAnswer(invocation -> Arrays.asList(roles).contains(invocation.getArguments()[0]));
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