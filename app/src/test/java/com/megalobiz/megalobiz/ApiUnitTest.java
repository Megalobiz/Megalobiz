package com.megalobiz.megalobiz;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by KeitelRobespierre on 9/2/2016.
 */
public class ApiUnitTest {

    @Test
    public void hostIsCorrect() throws Exception {
        assertEquals("https://www.megalobiz-staging.com", MegalobizApi.HOST);
    }
}
