package com.williamssonoma.ziprange;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class ZipCodeTest {
    @Test(expected = IllegalArgumentException.class)
    public void testValueOf0() {
        ZipCode.valueOf("zippy-dee-doo-dah");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf1() {
        ZipCode.valueOf("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf2() {
        ZipCode.valueOf("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf3() {
        ZipCode.valueOf("000001");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf4() {
        ZipCode.valueOf("9900000");
    }

    public void testValueOf5() {
        ZipCode.valueOf("00000");
        ZipCode.valueOf("99999");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf6() {
        ZipCode.valueOf(-1);
    }

    public void testValueOf7() {
        ZipCode.valueOf(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf8() {
        ZipCode.valueOf(9900000);
    }

    public void testValueOf9() {
        ZipCode.valueOf(99999);
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

    @Test
    public void testEquals0() {
        ZipCode a = ZipCode.valueOf("00001");

        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void testEquals1() {
        ZipCode a = ZipCode.valueOf("00001");

        Assert.assertFalse(a.equals(null));
    }

    @Test
    public void testEquals2() {
        ZipCode a = ZipCode.valueOf("00001");

        Assert.assertFalse(a.equals(BigDecimal.ONE));
    }

    @Test
    public void testEquals3() {
        ZipCode a = ZipCode.valueOf("00001");
        ZipCode b = ZipCode.valueOf("00001");

        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void testEquals4() {
        ZipCode a = ZipCode.valueOf("00001");
        ZipCode b = ZipCode.valueOf("00010");

        Assert.assertFalse(a.equals(b));
    }
}
