package utils;

import java.util.Objects;

public class Pair<T1,T2> {
    private T1 firstElem;
    private T2 secondElem;

    public Pair(T1 firstElem, T2 secondElem) {
        this.firstElem = firstElem;
        this.secondElem = secondElem;
    }

    public T1 getFirstElem() {
        return firstElem;
    }

    public void setFirstElem(T1 firstElem) {
        this.firstElem = firstElem;
    }

    public T2 getSecondElem() {
        return secondElem;
    }

    public void setSecondElem(T2 secondElem) {
        this.secondElem = secondElem;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "firstElem=" + firstElem +
                ", secondElem=" + secondElem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(firstElem, pair.firstElem) && Objects.equals(secondElem, pair.secondElem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElem, secondElem);
    }
}
