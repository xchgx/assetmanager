package com.xchgx.cloud.sso8.assetmanager.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class A0VisitLogFilterTest {
    private A0VisitLogFilter a0VisitLogFilter = new A0VisitLogFilter();

    @Test
    void testHandleUrl() {
        String url = "http://xchgx.vicp.net/application/list";
        String expected = "/application/list";
        String actual = a0VisitLogFilter.handleUrl(url);
        assertEquals(expected, actual);
    }

    @Test
    void testHandleOs() {
        String header = "aabccddwef a sdf   Windows  apple ";
        String expected = "Windows";
        String actual = a0VisitLogFilter.handleOs(header);
        assertEquals(expected, actual);
    }
}