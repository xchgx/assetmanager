package com.xchgx.cloud.sso8.assetmanager.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComputeTest {
    private Compute compute = new Compute();//被测试的类对象

    @Test
    void chengfa() {
        long expected = 1;
        long actual = compute.chengfa(1, 1);
        assertEquals(expected,actual,"1*1=1");

        expected = 4;
        actual = compute.chengfa(2, 2);
        assertEquals(expected,actual,"2*2=4");

        expected = 0;
        actual = compute.chengfa(0, 4);
        assertEquals(expected,actual,"0*4=0");
    }
}