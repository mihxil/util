package org.meeuw.functional;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * The next in succession of {@link java.util.function.Predicate} and {@link BiPredicate}.
 *
 * A predicate with three arguments
 *
 * @author Michiel Meeuwissen
 * @since 2.12.0
 */
@FunctionalInterface
public interface TriPredicate<T, U, V> {

    boolean test(T t, U u, V v);


    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U,? super V> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) && other.test(t, u, v);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    default TriPredicate<T, U, V> negate() {
        return (T t, U u, V v) -> !test(t, u, v);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) || other.test(t, u, v);
    }

    default BiPredicate<U, V> withArg1(T t) {
        return new Predicates.BiWrapper<TriPredicate<T, U, V>, U, V>(this, t, "with arg1 " + t) {
            @Override
            public boolean test(U u, V v) {
                return wrapped.test(t, u, v);
            }
        };
    }

    default BiPredicate<T, V> withArg2(U u) {
        return new Predicates.BiWrapper<TriPredicate<T, U, V>, T, V>(this, u, "with arg2 " + u) {
            @Override
            public boolean test(T t, V v) {
                return wrapped.test(t, u, v);
            }
        };
    }

    default BiPredicate<T, U> withArg3(V v) {
        return new Predicates.BiWrapper<TriPredicate<T, U, V>, T, U>(this, v, "with arg3 " + v) {
            @Override
            public boolean test(T t, U u) {
                return wrapped.test(t, u, v);
            }
        };
    }
}