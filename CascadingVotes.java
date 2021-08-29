package votingsystem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;

public record CascadingVotes(
    ImmutableMap<String, VotesFromSources> votes) {

  public VotesFromSources getCascadingVotesForCandidate(String candidate) {
    return votes.getOrDefault(candidate, VotesFromSources.empty());
  }

  public static CascadingVotes of(ImmutableList<CascadingVote> votes) {
    HashMap<String, VotesFromSources> builder = new HashMap<>();
    votes.forEach(vote -> {
      builder.putIfAbsent(vote.to, VotesFromSources.empty());
      builder.put(
          vote.to,
          builder.get(vote.to).withAddedVotes(VotesFromSource.of(vote.from, vote.votes)));
    });
    return new CascadingVotes(ImmutableMap.copyOf(builder));
  }

  public static class CascadingVote {

    private String to;
    private String from;
    private double votes;

    public CascadingVote fromCandidate(String ranker) {
      from = ranker;
      return this;
    }

    public CascadingVote toCandidate(String candidate) {
      to = candidate;
      return this;
    }

    public CascadingVote ofAmount(double votes) {
      this.votes = votes;
      return this;
    }
  }
}
