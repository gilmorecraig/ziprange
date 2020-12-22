package com.williamssonoma.ziprange;

import org.junit.Assert;
import org.junit.Test;

public class ZipCodeTest {
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfBad0() {
        ZipCode.valueOf("zippy-dee-doo-dah");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfBad1() {
        ZipCode.valueOf("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfBad2() {
        ZipCode.valueOf("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfBad3() {
        ZipCode.valueOf("000001");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfBad4() {
        ZipCode.valueOf("9900000");
    }

    public void testValueOfGood() {
        ZipCode.valueOf("00000");
        ZipCode.valueOf("99999");
    }

    @Test
    public void testPrevious0() {
        ZipCode a =  ZipCode.valueOf("00000");
        Assert.assertEquals(a, a.previous());
    }

    @Test
    public void testPrevious1() {
        Assert.assertEquals(
                ZipCode.valueOf("99997"),
                ZipCode.valueOf("99999").previous().previous());
    }

    @Test
    public void testNext0() {
        ZipCode a =  ZipCode.valueOf("99999");
        Assert.assertEquals(a, a.next());
    }

    @Test
    public void testNext1() {
        Assert.assertEquals(
                ZipCode.valueOf("00002"),
                ZipCode.valueOf("00000").next().next());
    }

    @Test
    public void testMin() {
        ZipCode a = ZipCode.valueOf("00000");
        Assert.assertEquals(a, a.min(ZipCode.valueOf("00001")));
    }

    @Test
    public void testMax() {
        ZipCode a = ZipCode.valueOf("99999");
        Assert.assertEquals(a, a.max(ZipCode.valueOf("99998")));
    }
}
