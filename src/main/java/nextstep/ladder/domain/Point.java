package nextstep.ladder.domain;

import java.util.Objects;

public final class Point {

  private final boolean point;

  public Point(boolean point) {
    this.point = point;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Point)) {
      return false;
    }
    Point point1 = (Point) o;
    return point == point1.point;
  }

  @Override
  public int hashCode() {
    return Objects.hash(point);
  }
}
