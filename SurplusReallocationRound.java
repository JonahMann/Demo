package votingsystem;

import static com.google.common.collect.ImmutableList.toImmutableList;

import votingsystem.CascadingVotes.CascadingVote;

public class SurplusReallocationRound extends Round {

  public SurplusReallocationRound(ElectionFacts electionFacts, RoundState state) {
    super(electionFacts, state);
  }

  @Override
  public Round nextRound() {
    return new AwardRound(electionFacts, executeSurplusReallocationPhase());
  }

  public RoundState executeSurplusReallocationPhase() {
    CascadingVotes surpluses = calculateSurpluses();
    CascadingVotes lastChoices = calculateNextLastChoices();
    return state
        .withModifiedVotes(candidate -> candidate.withSurplusVotesForRemoved(electionFacts.quota()))
        .withVotesForRedistributed(surpluses)
        .withVotesAgainstRemoved()
        .withVotesAgainstRedistributed(lastChoices);
  }

  private CascadingVotes calculateSurpluses() {
    return CascadingVotes.of(
        state.getCandidatesOverQuota(electionFacts.quota())
            .flatMap(candidate ->
                candidate.votesFor().stream()
                    .map(source ->
                        new CascadingVote()
                            .fromCandidate(source.getRanker())
                            .toCandidate(
                                state.getNextLiveTopChoice(electionFacts, source.getRanker()))
                            .ofAmount(
                                source.getNumVotes()
                                    * (1 -
                                    (electionFacts.quota() / candidate.getTotalVotesFor())))))
            .collect(toImmutableList())
    );
  }

  private CascadingVotes calculateNextLastChoices() {
    return CascadingVotes.of(
        state.getCandidatesOverQuota(electionFacts.quota())
            .flatMap(candidate ->
                candidate.votesAgainst().stream()
                    .map(source ->
                        new CascadingVote()
                            .fromCandidate(source.getRanker())
                            .toCandidate(
                                state.getNextLiveLastChoice(electionFacts, source.getRanker()))
                            .ofAmount(source.getNumVotes())))
            .collect(toImmutableList()));
  }
}
