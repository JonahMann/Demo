package votingsystem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import java.util.stream.Stream;
import votingsystem.CascadingVotes.CascadingVote;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

public record RoundState(
    ImmutableMap<String, Status> statuses,
    ImmutableList<CandidateRoundInfo> votes) {

  public static RoundState seed(ElectionFacts electionFacts) {
    return new RoundState(
        electionFacts.getAllCandidates().stream()
            .collect(toImmutableMap(name -> name, name -> Status.LIVE)),
        electionFacts.candidatesAndRankings().keySet().stream().map(
                candidate ->
                    new CandidateRoundInfo(
                        candidate,
                        VotesFromSources.of(
                            VotesFromSource.of(candidate,
                                electionFacts.getVotesForCandidate(candidate))),
                        VotesFromSources.of(
                            electionFacts.getAllCandidates().stream()
                                .filter(c -> electionFacts.getInitialLastChoiceForCandidate(c)
                                    .equals(candidate))
                                .map(ranker ->
                                    VotesFromSource.of(ranker,
                                        electionFacts.getVotesForCandidate(ranker)))
                                .collect(toImmutableList()))))
            .collect(toImmutableList()));
  }

  public RoundState withVotesAgainstRemoved() {
    return this.withModifiedVotes(
        candidate ->
            statuses.get(candidate.candidate()) == Status.LIVE
                ? candidate
                : candidate.withVotesAgainstRemoved());
  }

  public RoundState withVotesForRedistributed(CascadingVotes surpluses) {
    return this.withModifiedVotes(candidate -> candidate
        .withAddedVotesFor(surpluses.getCascadingVotesForCandidate(candidate.candidate())));
  }

  public RoundState withVotesAgainstRedistributed(CascadingVotes lastChoices) {
    return this.withModifiedVotes(candidate -> candidate
        .withAddedVotesAgainst(lastChoices.getCascadingVotesForCandidate(candidate.candidate())));
  }

  public RoundState withEliminatedVotesCleared() {
    return this.withModifiedVotes(candidate ->
        statuses.get(candidate.candidate()) == Status.ELIMINATED
            ? candidate.withVotesForRemoved().withVotesAgainstRemoved()
            : candidate);
  }

  public RoundState withEliminatedFirstChoiceVotesCascaded(ElectionFacts electionFacts) {
    return withVotesForRedistributed(
        CascadingVotes.of(
            votes.stream()
                .filter(c -> statuses.get(c.candidate()) == Status.ELIMINATED)
                .flatMap(c -> c.votesFor().stream()
                    .map(v ->
                        new CascadingVote()
                            .fromCandidate(v.getRanker())
                            .toCandidate(getNextLiveTopChoice(electionFacts, v.getRanker()))
                            .ofAmount(v.getNumVotes())))
                .collect(toImmutableList())));
  }

  public RoundState withEliminatedLastChoiceVotesCascaded(ElectionFacts electionFacts) {
    return withVotesAgainstRedistributed(
        CascadingVotes.of(
            votes.stream()
                .filter(c -> statuses.get(c.candidate()) == Status.ELIMINATED)
                .flatMap(c -> c.votesAgainst().stream()
                    .map(v ->
                        new CascadingVote()
                            .fromCandidate(v.getRanker())
                            .toCandidate(getNextLiveLastChoice(electionFacts, v.getRanker()))
                            .ofAmount(v.getNumVotes())))
                .collect(toImmutableList())));
  }

  public RoundState withModifiedVotes(
      Function<CandidateRoundInfo, CandidateRoundInfo> mapper) {
    return new RoundState(statuses, votes.stream().map(mapper).collect(toImmutableList()));
  }

  public String getNextLiveTopChoice(ElectionFacts electionFacts, String ranker) {
    return getNextLive(electionFacts.getRankings(ranker).stream());
  }

  public String getNextLiveLastChoice(ElectionFacts electionFacts, String ranker) {
    return getNextLive(electionFacts.getRankings(ranker).reverseStream());
  }

  private String getNextLive(Stream<String> candidates) {
    return candidates.filter(candidate -> statuses.get(candidate) == Status.LIVE).findFirst().get();
  }

  public ImmutableSet<String> winners() {
    return statuses.entrySet().stream()
        .filter(candidate -> candidate.getValue() == Status.WINNER).map(Map.Entry::getKey)
        .collect(toImmutableSet());
  }

  public Stream<CandidateRoundInfo> getCandidatesOverQuota(long quota) {
    return votes.stream().filter(v -> v.getTotalVotesFor() > quota);
  }

  private double getMostLastChoiceVotes() {
    return votes.stream().mapToDouble(CandidateRoundInfo::getTotalVotesAgainst).max().getAsDouble();
  }

  public RoundState withNewStatus(Set<String> candidates, Status status) {
    return new RoundState(
        statuses.entrySet().stream()
            .collect(toImmutableMap(
                Entry::getKey,
                s -> candidates.contains(s.getKey()) ? status : s.getValue())),
        votes);
  }

  public ImmutableSet<String> getCandidatesWithMostLastChoiceVotes() {
    double mostLastChoiceVotes = getMostLastChoiceVotes();
    return votes.stream()
        .filter(c -> c.getTotalVotesAgainst() == mostLastChoiceVotes)
        .map(CandidateRoundInfo::candidate)
        .collect(toImmutableSet());
  }
}
enum Status {
  LIVE, ELIMINATED, WINNER
}