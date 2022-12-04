package grammar;

import java.util.List;
import java.util.Objects;

public class Production {
    private String leftSide;
    private List<String> rightSide;

    public Production(String leftSide, List<String> rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(String leftSide) {
        this.leftSide = leftSide;
    }

    public List<String> getRightSide() {
        return rightSide;
    }

    public void setRightSide(List<String> rightSide) {
        this.rightSide = rightSide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(leftSide, that.leftSide) && Objects.equals(rightSide, that.rightSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftSide, rightSide);
    }

    @Override
    public String toString() {
        return leftSide + "->" + rightSide;
    }
}
