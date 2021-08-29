package votingsystem;

public class EliminationRound extends Round {

  protected EliminationRound(ElectionFacts electionFacts, RoundState state) {
    super(electionFacts, state);
  }

  @Override
  public Round nextRound() {
    return new EliminationReallocationRound(
        electionFacts,
        state.withNewStatus(state.getCandidatesWithMostLastChoiceVotes(), Status.ELIMINATED));
  }
}
