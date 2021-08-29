package votingsystem;

public record CandidateRoundInfo(
    String candidate,
    VotesFromSources votesFor,
    VotesFromSources votesAgainst) {

  public double getTotalVotesFor() {
    return votesFor.getTotal();
  }

  public double getTotalVotesAgainst() {
    return votesAgainst.getTotal();
  }

  public CandidateRoundInfo withAddedVotesFor(VotesFromSources newVotesFor) {
    return new CandidateRoundInfo(candidate(), votesFor().withAddedVotes(newVotesFor),
        votesAgainst());
  }

  public CandidateRoundInfo withAddedVotesAgainst(VotesFromSources newVotesAgainst) {
    return new CandidateRoundInfo(candidate(), votesFor(),
        votesAgainst().withAddedVotes(newVotesAgainst));
  }

  public CandidateRoundInfo withVotesAgainstRemoved() {
    return new CandidateRoundInfo(candidate(), votesFor(), VotesFromSources.empty());
  }

  public CandidateRoundInfo withVotesForRemoved() {
    return new CandidateRoundInfo(candidate(), VotesFromSources.empty(), votesAgainst());
  }

  public CandidateRoundInfo withSurplusVotesForRemoved(long quota) {
    return new CandidateRoundInfo(
        candidate(),
        votesFor().mapVotes(numVotes -> numVotes * (quota / Math.max(quota, getTotalVotesFor()))),
        votesAgainst());
  }
}