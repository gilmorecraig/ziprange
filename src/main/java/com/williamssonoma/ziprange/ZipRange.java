package com.williamssonoma.ziprange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents an inclusive range of US zip codes.
 *
 * @author Craig Gilmore
 */
public class ZipRange implements Comparable<ZipRange> {
    private static final Logger LOG = LogManager.getLogger(ZipRange.class);

    private final ZipCode low;
    private final ZipCode high;

    /**
     * Create a zip code range from the lowest given code to the highest given
     * code.
     *
     * @param a Zip code range to/from
     * @param b Zip code range to/from
     * @throws NullPointerException If either argument is {@code null}
     */
    public ZipRange(ZipCode a, ZipCode b) {
        this.low = a.min(b);
        this.high = a.max(b);
    }

    /**
     * <p>Determines if the given zip ranges may be merged together. That is, the
     * given ranges either intersect or one immediately follows the other.</p>
     *
     * <p>For example, [00000,12345] is followed by [12346,23456]</p>
     * <p>And, [00000,12345] intersects [00500,23456]</p>
     *
     * @param range
     * @return
     */
    public static boolean mergeable(ZipRange a, ZipRange b) {
        return !(a.low.compareTo(b.high.next()) > 0
             ||  a.high.compareTo(b.low.previous()) < 0);
    }

    /**
     * Calculates and returns the smallest zip code range enclosing
     * (containing) both of the given ranges.
     *
     * @param a Zip code range
     * @param b Zip code range
     * @return Enclosing zip code range
     */
    public static ZipRange enclosing(ZipRange a, ZipRange b) {
        ZipRange enclosed = new ZipRange(
                a.low.min(b.low),
                a.high.max(b.high));

        LOG.debug(enclosed + " <-- " + a + " + " + b);

        return enclosed;
    }

    /**
     * Combines all zip code ranges that are
     * {@link #mergeable(ZipRange, ZipRange) intersecting or adjacent}.
     *
     * @param ranges Zip ranges to consolidate
     * @return Set of zip code ranges representing the same set of zip codes as
     *         the given set
     */
    public static Set<ZipRange> consolidate(Iterable<ZipRange> ranges) {
        Set<ZipRange> consolidated = new TreeSet<>();

        /*
         * Methodology:
         *
         *   - Sort the zip code ranges by the integer representing the lowest
         *     zip code in each respective range.
         *   - In ascending order, merge ranges together while intersecting or
         *     immediately following (e.g. [10000, 12345] and [12346, 90000])
         *   - When the merged range is disjoint from the next item, it's added
         *     to the merged/consolidated set
         */

        Set<ZipRange> sorted = StreamSupport
                .stream(ranges.spliterator(), false)
                .collect(Collectors.toCollection(TreeSet::new));

        if (!sorted.isEmpty()) {
            ZipRange merged = null;

            // merge ranges together while intersecting or adjacent
            for (ZipRange r : sorted) {
                if (merged == null) {
                    merged = r;
                }
                // if next range intersecting or adjacent
                else if (mergeable(merged, r)) {
                    merged = enclosing(merged, r);
                }
                else {
                    // no further merging is possible
                    consolidated.add(merged);

                    merged = r;
                }
            }

            // no next item, done
            consolidated.add(merged);
        }

        if (LOG.isDebugEnabled()) {
            LOG.info("Consolidated ranges:\n" + printRanges(consolidated));
        }

        return consolidated;
    }

    @Override
    public int compareTo(ZipRange o) {
        int comparison = this.low.compareTo(o.low);

        return comparison == 0
             ? this.high.compareTo(o.high)
             : comparison;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.low, this.high);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ZipRange other = (ZipRange) o;

        return Objects.equals(this.low, other.low)
            && Objects.equals(this.high, other.high);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + this.low + "," + this.high + "]";
    }

    /**
     *  Regex to capture US zip codes pairs.
     *
     *  E.g. [32222,53333]
     *  group 1: 32222
     *  group 2: 53333
     */
    private static final Pattern ARGS_PATTERN = Pattern.compile("\\[\\s*(\\d{5})\\s*,\\s*(\\d{5})\\s*\\]");

    public static void main(String[] args) {
        System.out.println(printRanges(
                ZipRange.consolidate(parseRanges(args))));
    }

    /**
     * Parse zip code ranges from main argument.
     *
     * @param args Main arguments
     * @return Parsed zip code ranges
     */
    protected static List<ZipRange> parseRanges(String[] args) {
        List<ZipRange> ranges = Stream.of(args)
                .flatMap(ZipRange::parsePairs)
                .map(ZipRange::parseRange)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsed ranges:\n" + printRanges(ranges));
        }

        return ranges;
    }

    /**
     * Print zip code ranges as a space delimited list.
     *
     * @param ranges Zip code ranges
     * @return Space delimited list of zip code ranges
     */
    private static String printRanges(Collection<ZipRange> ranges) {
        return ranges.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    /**
     * Parse zip code pairs (e.g. [00012,95632]) from program arguments.
     *
     * @param arg Program argument
     * @return Stream of zip code pairs
     */
    private static Stream<String> parsePairs(String arg) {
        LOG.trace("Argument: " + arg);

        Path p = Paths.get(arg);

        Function<String, Stream<String>> split = l -> Stream.of(l.split("\\s+"));

        if (p.toFile().exists()) {
            LOG.debug("Found file at " + p);

            try (Stream<String> lines = Files.lines(p)) {
                // collecting stream before file stream is closed
                return lines.flatMap(split).collect(Collectors.toList()).stream();
            }
            catch (IOException e) {
                LOG.error("Unable to read " + arg, e);

                return Stream.empty();
            }
        }
        else {
            return split.apply(arg);
        }
    }

    /**
     * Parse zip codes from given pair and build a zip code range.
     *
     * @param pair Zip code pair (e.g. "[00012,95632]")
     * @return Zip code range
     */
    private static ZipRange parseRange(String pair) {
        LOG.trace("Zip code pair: " + pair);

        Matcher matcher = ARGS_PATTERN.matcher(pair);

        if (matcher.find()) {
            ZipCode a = ZipCode.valueOf(matcher.group(1));
            ZipCode b = ZipCode.valueOf(matcher.group(2));

            LOG.debug("parsed zip code: " + a);
            LOG.debug("parsed zip code: " + b);

            ZipRange range = new ZipRange(a, b);

            LOG.debug("parsed zip code range: " + range);

            return range;
        }
        else {
            LOG.warn("Unable to parse zip code range \"" + pair + "\"; Ignoring");
        }

        return null;
    }
}
