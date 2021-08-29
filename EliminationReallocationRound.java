package votingsystem;

public class EliminationReallocationRound extends Round {

  public EliminationReallocationRound(ElectionFacts electionFacts, RoundState state) {
    super(electionFacts, state);
  }

  @Override
  public Round nextRound() {
    return new AwardRound(
        electionFacts,
        state.withEliminatedLastChoiceVotesCascaded(electionFacts)
            .withEliminatedFirstChoiceVotesCascaded(electionFacts)
            .withEliminatedVotesCleared());
  }
}
