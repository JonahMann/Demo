package votingsystem;

import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;

public record Ranking(ImmutableList<String> rankings) {

  public Stream<String> stream() {
    return rankings.stream();
  }

  public Stream<String> reverseStream() {
    return rankings.reverse().stream();
  }
}
