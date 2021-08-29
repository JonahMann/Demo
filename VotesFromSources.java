package votingsystem;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.collect.ImmutableList;
import java.util.function.Function;
import java.util.stream.Stream;

public record VotesFromSources(
    ImmutableList<VotesFromSource> votesFromSources) {

  public VotesFromSources withAddedVotes(VotesFromSources newVotes) {
    return new VotesFromSources(
        ImmutableList.<VotesFromSource>builder()
            .addAll(votesFromSources)
            .addAll(newVotes.votesFromSources)
            .build());
  }

  public VotesFromSources withAddedVotes(VotesFromSource votesFromSource) {
    return withAddedVotes(VotesFromSources.of(votesFromSource));
  }

  public double getTotal() {
    return stream().mapToDouble(VotesFromSource::getNumVotes).sum();
  }

  public static VotesFromSources of(VotesFromSource votesFromSource) {
    return VotesFromSources.of(ImmutableList.of(votesFromSource));
  }

  public static VotesFromSources of(ImmutableList<VotesFromSource> votes) {
    return new VotesFromSources(votes);
  }

  public static VotesFromSources empty() {
    return new VotesFromSources(ImmutableList.of());
  }

  public Stream<VotesFromSource> stream() {
    return votesFromSources.stream();
  }

  public VotesFromSources mapVotes(Function<Double, Double> mapper) {
    return VotesFromSources.of(
        stream()
            .map(original ->
                VotesFromSource.of(original.getRanker(), mapper.apply(original.getNumVotes())))
        .collect(toImmutableList()));
  }
}