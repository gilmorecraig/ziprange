package com.williamssonoma.ziprange;

import java.util.regex.Pattern;

/**
 * US 5-digit zip code. E.g., 00123
 *
 * @author Craig Gilmore
 */
public class ZipCode implements Comparable<ZipCode> {
    /**
     * US 5-digit zip code pattern.
     */
    private static final Pattern ZIP_RE = Pattern.compile("^\\d{5}$");

    /**
     * Lowest zip code "00000".
     */
    private static final ZipCode MIN = new ZipCode(0);

    /**
     * Highest zip code "99999".
     */
    private static final ZipCode MAX = new ZipCode(99999);

    private final int code;

    private ZipCode(int code) {
        this.code = code;
    }

    /**
     * Create a 5-digit US zip code.
     *
     * @param code Zip code
     * @throws IllegalArgumentException If not exactly 5-digits
     */
    public static ZipCode valueOf(String code) {
        if (!ZIP_RE.matcher(code).find()) {
            throw new IllegalArgumentException(
                    "Zip code must be exactly five digits. Unable to parse \"" + code + "\"");
        }

        return ZipCode.valueOf(Integer.parseInt(code));
    }

    /**
     * Create a 5-digit US zip code.
     *
     * @param code Zip code value
     * @return Zip code
     * @throws IllegalArgumentException If code value is less than {@link #MIN} or greater than {@link #MAX}
     */
    private static ZipCode valueOf(int code) {
        if (code < MIN.code || code > MAX.code) {
            throw new IllegalArgumentException(
                    "Zip code must be between " + MIN.code + " and . Unable to parse \"" + code + "\"");
        }
        else if (code == MAX.code) {
            return MAX;
        }
        else if (code == MIN.code) {
            return MIN;
        }

        return new ZipCode(code);
    }

    /**
     * @return Previous zip code in integer order or {@link #MIN}
     */
    public ZipCode previous() {
        return this == MIN ? MIN : ZipCode.valueOf(this.code - 1);
    }

    /**
     * @return Next zip code in integer order or {@link #MAX}
     */
    public ZipCode next() {
        return this == MAX ? MAX : ZipCode.valueOf(this.code + 1);
    }

    /**
     * @param code Zip code
     * @return Lowest of {@code this) and given zip code
     * @throws NullPointerException If argument is {@code null}
     */
    public ZipCode min(ZipCode code) {
        return this.compareTo(code) < 0 ? this : code;

    }

    /**
     * @param code Zip code
     * @return Highest of {@code this) and given zip code
     * @throws NullPointerException If argument is {@code null}
     */
    public ZipCode max(ZipCode code) {
        return this.compareTo(code) > 0 ? this : code;

    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ZipCode o) {
        return this.code - o.code;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.code;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ZipCode other = (ZipCode) o;

        return this.code == other.code;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // pad left zeros
        return String.format("%05d", this.code);
    }
}
