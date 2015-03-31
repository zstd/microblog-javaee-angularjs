package com.example.zstd.microblog.servlet.rest;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PostsServletPrettyTimeTest {

    private static final Logger LOG = Logger.getLogger(UsersServlet.class.getName());

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private Date current;

    @Before
    public void setUp() throws Exception {
        current = FORMAT.parse("04/04/2015 09:09:09");
    }

    private String input;
    private String expected;

    public PostsServletPrettyTimeTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testGetPrettyTime() throws Exception {
        String formatted = PostsServlet.getPrettyTime(current,FORMAT.parse(input));
        LOG.info(String.format("If now is '%s' and message time is '%s' then message was created '%s'",
                FORMAT.format(current), input,formatted));
        assertEquals(expected,formatted);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                new Object[]{"04/04/2015 09:09:06","today, 3 second(s) ago"},
                new Object[]{"04/04/2015 09:07:06","today, 2 minute(s) ago"},
                new Object[]{"04/04/2015 08:07:06","today, 1 hour(s) ago"},
                new Object[]{"03/04/2015 08:07:06","03/04/2015 08:07"},
        });
    }
}