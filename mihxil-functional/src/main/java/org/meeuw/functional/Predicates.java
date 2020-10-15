package org.meeuw.functional;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Provides several utility related to {@link Predicate}, {@link BiPredicate} and {@link TriPredicate} like versions of {@link #alwaysFalse} and {@link #alwaysTrue}, with nicer {@link #toString()}, and also
 * implementing {@link #hashCode()} and {@link #equals(Object)}.
 *
 * The point is that these kind of things won't happen if you use lambdas.
 *
 * So if they are important than you could use
 * <pre>
 * {@code
 *    Preditor<MyObject> predicate = Predicates.alwaysTrue();
 * }
 * </pre>
 * rather then:
 * <pre>
 *  {@code
 *     Preditor<MyObject> predicate = (o) -> true;
 *  }
 * </pre>
 *
 * @author Michiel Meeuwissen
 * @since 2.18
 */
public final class Predicates {

    private static final String FALSE = "FALSE";
    private static final String TRUE = "TRUE";

    private Predicates() {
    }

    public static <T> Predicate<T> always(boolean v, String s) {
        return new Always<>(v, s);
    }

    public static <T> Predicate<T> alwaysFalse() {
        return always(false, FALSE);
    }
    public static <T> Predicate<T> alwaysTrue() {
        return always(true, TRUE);
    }

    public static <T, U> BiPredicate<T, U> biAlways(boolean v, String s) {
        return new BiAlways<>(v, s);
    }

    public static <T, U> BiPredicate<T, U> biAlwaysFalse() {
        return biAlways(false, FALSE);
    }
    public static <T, U> BiPredicate<T, U> biAlwaysTrue() {
        return biAlways(true, TRUE);
    }

    public static <T, U, V> TriPredicate<T, U, V> triAlways(boolean val, String s) {
        return new TriAlways<>(val, s);
    }
    public static <T, U, V> TriPredicate<T, U, V> triAlwaysFalse() {
        return triAlways(false, FALSE);
    }
    public static <T, U, V> TriPredicate<T, U, V> triAlwaysTrue() {
        return triAlways(true, TRUE);
    }

    /**
     * Creates a new {@link TriPredicate} but implement it using a {@link BiPredicate}, simply completely ignoring the third argument
     */
    public static <T, U, V> TriPredicate<T, U, V> ignoreArg3(BiPredicate<T, U> biPredicate) {
        return new TriWrapper<BiPredicate<T, U>, T, U, V>(biPredicate, null, "ignore arg3") {
            @Override
            public boolean test(T t, U u, V v) {
                return wrapped.test(t, u);

            }
        };
    }
    /**
     * Creates a new {@link TriPredicate} but implement it using a {@link BiPredicate}, simply completely ignoring the second argument
     */
    public static <T, U, V> TriPredicate<T, U, V> ignoreArg2(BiPredicate<T, V> biPredicate) {
        return new TriWrapper<BiPredicate<T, V>, T, U, V>(biPredicate, null, "ignore arg2") {
            @Override
            public boolean test(T t, U u, V v) {
                return wrapped.test(t, v);

            }
        };
    }
    /**
     * Creates a new {@link TriPredicate} but implement it using a {@link BiPredicate}, simply completely ignoring the first argument
     */
    public static <T, U, V> TriPredicate<T, U, V> ignoreArg1(BiPredicate<U, V> biPredicate) {
        return new TriWrapper<BiPredicate<U, V>, T, U, V>(biPredicate, null, "ignore arg1") {
            @Override
            public boolean test(T t, U u, V v) {
                return wrapped.test(u, v);

            }
        };
    }

    /**
     * Creates a new {@link BiPredicate} but implement it using a {@link Predicate}, simply completely ignoring the second argument
     */
    public static <T, U> BiPredicate<T, U> ignoreArg2(Predicate<T> predicate) {
        return new BiWrapper<Predicate<T> , T, U>(predicate, null, "ignore arg2") {
            @Override
            public boolean test(T t, U u) {
                return wrapped.test(t);

            }
        };
    }
    /**
     * Creates a new {@link BiPredicate} but implement it using a {@link Predicate}, simply completely ignoring the first argument
     */
    public static <T, U> BiPredicate<T, U> ignoreArg1(Predicate<U> predicate) {
        return new BiWrapper<Predicate<U> , T, U>(predicate, null, "ignore arg1") {
            @Override
            public boolean test(T t, U u) {
                return wrapped.test(u);

            }
        };
    }


    /**
     * Morphs a given {@link BiPredicate} into a {@link Predicate}, which a certain given value for the first argument.
     *
     * See {@link TriPredicate#withArg1(Object)}
     */
    public static <U, V> Predicate<V> withArg1(BiPredicate<U, V> biPredicate, U u) {
        return new MonoWrapper<BiPredicate<U, V>, V>(biPredicate, u, "with arg1 " + u) {
            @Override
            public boolean test(V v) {
                return wrapped.test(u, v);
            }
        };
    }

    /**
     * Morphs a given {@link BiPredicate} into a {@link Predicate}, which a certain given value for the first argument.
     *
     * See {@link TriPredicate#withArg1(Object)}
     */
    public static <U, V> Predicate<U> withArg2(BiPredicate<U, V> biPredicate, V v) {
        return new MonoWrapper<BiPredicate<U, V>, U>(biPredicate, v, "with arg2 " + v) {
            @Override
            public boolean test(U u) {
                return  wrapped.test(u, v);
            }
        };
    }


    protected static abstract class AbstractAlways {
        protected final boolean val;
        private final String s;


        public AbstractAlways(boolean val, String s) {
            this.val = val;
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AbstractAlways always = (AbstractAlways) o;
            return val == always.val;
        }

        @Override
        public int hashCode() {
            return (val ? 1 : 0);
        }
    }

    protected static final class Always<T> extends AbstractAlways implements Predicate<T> {

        public Always(boolean val, String s) {
            super(val, s);
        }

        @Override
        public boolean test(T t) {
            return val;

        }
    }

    protected static final class BiAlways<T, U> extends AbstractAlways implements BiPredicate<T, U> {

        public BiAlways(boolean val, String s) {
            super(val, s);
        }
        @Override
        public boolean test(T t, U u) {
            return val;
        }
    }

    protected static final class TriAlways<T, U, V> extends AbstractAlways implements TriPredicate<T, U, V> {

        public TriAlways(boolean val, String s) {
            super(val, s);
        }

        @Override
        public boolean test(T t, U u, V v) {
            return val;
        }
    }

    protected static abstract  class TriWrapper<W, X, Y, Z> extends ValueWrapper<W> implements TriPredicate<X, Y, Z> {
        public TriWrapper(W wrapped, Object value, String why) {
            super(wrapped, value, why);
        }
    }
    protected static abstract  class BiWrapper<W, X, Y>  extends ValueWrapper<W> implements BiPredicate<X, Y> {
        public BiWrapper(W wrapped, Object value, String why) {
            super(wrapped, value, why);
        }
    }

    protected static abstract  class MonoWrapper<W, X>  extends ValueWrapper<W> implements Predicate<X> {
        public MonoWrapper(W wrapped, Object value, String why) {
            super(wrapped, value, why);
        }
    }



}