package votingsystem;

public record VotesFromSource(String ranker, double votes) {

  public String getRanker() {
    return ranker;
  }

  public double getNumVotes() {
    return votes;
  }

  public static VotesFromSource of(String ranker, double votes) {
    return new VotesFromSource(ranker, votes);
  }

}
