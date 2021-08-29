package votingsystem;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class Election {
  private final ElectionFacts electionFacts;

  private Election(ElectionFacts electionFacts) {
    this.electionFacts = electionFacts;
  }

  /** Tabulates and returns the winners of the election. */
  public ImmutableSet<String> tabulate() {
    Round round = Round.firstRound(electionFacts);
    while (!round.isFinal()) {
      round = round.nextRound();
    }
    return ImmutableSet.copyOf(round.winners());
  }

  public static Election create(
      int numSeats,
      ImmutableMap<String, Ranking> candidatesAndRankings,
      ImmutableMap<String, Long> votes) {
    return new Election(new ElectionFacts(numSeats, candidatesAndRankings, votes));
  }
}