package votingsystem;

import com.google.common.collect.ImmutableSet;

/** Represents one phase of vote tabulation. */
public abstract class Round {

  protected final ElectionFacts electionFacts;
  protected final RoundState state;

  protected Round(
      ElectionFacts electionFacts,
      RoundState state) {
    this.electionFacts = electionFacts;
    this.state = state;
  }

  /** Whether the vote tabulation is complete as of this round. */
  public boolean isFinal() {
    return winners().size() == electionFacts.numSeats();
  }

  /** Creates a {@link Round} representing the first round of tabulation. */
  public static Round firstRound(ElectionFacts electionFacts) {
    return new AwardRound(electionFacts, RoundState.seed(electionFacts));
  }

  public abstract Round nextRound();

  /** Returns the candidates that have already secured seats by this round. */
  public ImmutableSet<String> winners() {
    return state.winners();
  }
}