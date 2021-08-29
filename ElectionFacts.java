package votingsystem;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public record ElectionFacts(
    int numSeats,
    ImmutableMap<String, Ranking> candidatesAndRankings,
    ImmutableMap<String, Long> votes) {

  public long getVotesForCandidate(String candidate) {
    return votes.get(candidate);
  }

  public ImmutableSet<String> getAllCandidates() {
    return votes.keySet();
  }

  public String getInitialLastChoiceForCandidate(String candidate) {
    return candidatesAndRankings.get(candidate).reverseStream().findFirst().get();
  }

  public long quota() {
    long totalVotes = votes.values().stream().mapToLong(i -> i).sum();
    return (totalVotes / (numSeats + 1));
  }

  public Ranking getRankings(String ranker) {
    return candidatesAndRankings.get(ranker);
  }
}
