package com.williamssonoma.ziprange;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class ZipRangeTest {
    /**
     * Absolute path to src/test/resources.
     */
    private static final Path TEST_RESOURCES_PATH =
            Paths.get("src","test","resources").toAbsolutePath();

    @Test
    public void testNew() {
        ZipCode low = ZipCode.valueOf("00123");
        ZipCode high = ZipCode.valueOf("01230");

        new ZipRange(low, high);
    }

    @Test
    public void testMergeable0() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00005"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00006"),
                ZipCode.valueOf("00010"));

        Assert.assertTrue(ZipRange.mergeable(a, b));

        Assert.assertTrue(ZipRange.mergeable(b, a));
    }

    @Test
    public void testMergeable1() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00005"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00005"),
                ZipCode.valueOf("00015"));

        Assert.assertTrue(ZipRange.mergeable(a, b));

        Assert.assertTrue(ZipRange.mergeable(b, a));
    }

    @Test
    public void testMergeable2() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00005"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00010"),
                ZipCode.valueOf("00015"));

        Assert.assertFalse(ZipRange.mergeable(a, b));

        Assert.assertFalse(ZipRange.mergeable(b, a));
    }

    @Test
    public void testEnclosing0() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00005"),
                ZipCode.valueOf("00020"));

        ZipRange c = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00020"));

        Assert.assertEquals(c, ZipRange.enclosing(a, b));
    }

    @Test
    public void testEnclosing1() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00020"),
                ZipCode.valueOf("00030"));

        ZipRange c = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00030"));

        Assert.assertEquals(c, ZipRange.enclosing(a, b));
    }

    @Test
    public void testConsolidate0() {
        Assert.assertEquals(
                Collections.emptySet(),
                ZipRange.consolidate(Collections.emptyList()));
    }

    @Test
    public void testConsolidate1() {
        // sequential ranges
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00011"),
                ZipCode.valueOf("00019"));

        ZipRange c = new ZipRange(
                ZipCode.valueOf("00020"),
                ZipCode.valueOf("00030"));

        // intersecting ranges
        ZipRange d = new ZipRange(
                ZipCode.valueOf("00040"),
                ZipCode.valueOf("00050"));

        ZipRange e = new ZipRange(
                ZipCode.valueOf("00045"),
                ZipCode.valueOf("00055"));

        // nested ranges
        ZipRange f = new ZipRange(
                ZipCode.valueOf("00070"),
                ZipCode.valueOf("00080"));

        ZipRange g = new ZipRange(
                ZipCode.valueOf("00060"),
                ZipCode.valueOf("00090"));

        // expected
        ZipRange h = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00030"));

        ZipRange i = new ZipRange(
                ZipCode.valueOf("00040"),
                ZipCode.valueOf("00055"));

        ZipRange j = new ZipRange(
                ZipCode.valueOf("00060"),
                ZipCode.valueOf("00090"));

        Assert.assertEquals(
                Stream.of(h, i, j).collect(Collectors.toSet()),
                ZipRange.consolidate(Arrays.asList(a, b, c, d, e, f, g)));

        Assert.assertEquals(
                Stream.of(h, i, j).collect(Collectors.toSet()),
                ZipRange.consolidate(Arrays.asList(g, f, e, d, c, b, a)));

        Assert.assertEquals(
                Stream.of(h, i, j).collect(Collectors.toSet()),
                ZipRange.consolidate(Arrays.asList(i, a, h, b, g, c, f, d, e)));
    }

    @Test
    public void testEquals0() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void testEquals1() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        Assert.assertFalse(a.equals(null));
    }

    @Test
    public void testEquals2() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        Assert.assertFalse(a.equals(BigDecimal.ONE));
    }

    @Test
    public void testEquals3() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void testEquals4() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00002"),
                ZipCode.valueOf("00010"));

        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testEquals5() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00010"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("00001"),
                ZipCode.valueOf("00020"));

        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testParseRanges() {
        ZipRange a = new ZipRange(
                ZipCode.valueOf("00000"),
                ZipCode.valueOf("99999"));

        ZipRange b = new ZipRange(
                ZipCode.valueOf("92345"),
                ZipCode.valueOf("93556"));

        ZipRange c = new ZipRange(
                ZipCode.valueOf("93456"),
                ZipCode.valueOf("99677"));

        ZipRange d = new ZipRange(
                ZipCode.valueOf("00000"),
                ZipCode.valueOf("00005"));

        ZipRange e = new ZipRange(
                ZipCode.valueOf("00000"),
                ZipCode.valueOf("00010"));

        List<ZipRange> expectedRanges = Arrays.asList(a, b, c, d, e);

        String[] args = {
                "[00000,99999]",
                "[0, 2]",// bad range, should be ignored
                TEST_RESOURCES_PATH + "/input.txt" };

        List<ZipRange> ranges = ZipRange.parseRanges(args);

        Assert.assertEquals(expectedRanges, ranges);
    }
}
