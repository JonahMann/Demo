package votingsystem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Main {

  private static void singleSeatExample() {
    int numSeats = 1;
    ImmutableMap<String, Ranking> candidatesAndRankings =
        ImmutableMap.<String, Ranking>builder()
            .put("Sanders", new Ranking(
                ImmutableList.of("Sanders", "Warren", "Yang", "Harris", "Biden", "Buttigieg",
                    "Klobuchar")))
            .put("Warren", new Ranking(
                ImmutableList.of("Warren", "Sanders", "Harris", "Biden", "Yang", "Buttigieg",
                    "Klobuchar")))
            .put("Biden", new Ranking(
                ImmutableList.of("Biden", "Harris", "Klobuchar", "Warren", "Buttigieg", "Sanders",
                    "Yang")))
            .put("Buttigieg", new Ranking(
                ImmutableList.of("Buttigieg", "Klobuchar", "Biden", "Harris", "Yang", "Warren",
                    "Sanders")))
            .put("Yang", new Ranking(
                ImmutableList.of("Yang", "Sanders", "Warren", "Buttigieg", "Biden", "Klobuchar",
                    "Harris")))
            .put("Harris", new Ranking(
                ImmutableList.of("Harris", "Biden", "Buttigieg", "Klobuchar", "Warren", "Sanders",
                    "Yang")))
            .put("Klobuchar", new Ranking(
                ImmutableList.of("Klobuchar", "Biden", "Buttigieg", "Harris", "Yang", "Sanders",
                    "Warren")))
            .build();
    ImmutableMap<String, Long> votes =
        ImmutableMap.<String, Long>builder()
            .put("Sanders", 30L)
            .put("Warren", 15L)
            .put("Biden", 15L)
            .put("Buttigieg", 15L)
            .put("Yang", 10L)
            .put("Harris", 10L)
            .put("Klobuchar", 5L)
            .build();

    Election election = Election.create(numSeats, candidatesAndRankings, votes);
    System.out.println("Winners are " + election.tabulate());
  }

  private static void multiSeatExample() {
    int numSeats = 4;
    ImmutableMap<String, Ranking> candidatesAndRankings =
        ImmutableMap.<String, Ranking>builder()
            .put("DS1", new Ranking(ImmutableList.of("DS1", "DS2", "DS3", "DS4", "MD2", "MD3", "MD1", "MD4", "R4", "R3", "R2", "R1")))
            .put("DS2", new Ranking(ImmutableList.of("DS2", "DS1", "DS3", "DS4", "MD2", "MD3", "MD1", "MD4", "R4", "R3", "R2", "R1")))
            .put("DS3", new Ranking(ImmutableList.of("DS3", "DS1", "DS2", "DS4", "MD2", "MD3", "MD1", "R4", "MD4", "R3", "R2", "R1")))
            .put("DS4", new Ranking(ImmutableList.of("DS4", "DS1", "DS2", "DS3", "MD2", "MD3", "MD1", "MD4", "R4", "R3", "R2", "R1")))

            .put("MD1", new Ranking(ImmutableList.of("MD1", "MD2", "MD3", "MD4", "DS2", "DS1", "R4", "DS3", "DS4", "R3", "R2", "R1")))
            .put("MD2", new Ranking(ImmutableList.of("MD2", "MD1", "MD3", "MD4", "DS2", "DS1", "R4", "DS3", "DS4", "R3", "R2", "R1")))
            .put("MD3", new Ranking(ImmutableList.of("MD3", "MD1", "MD2", "MD4", "DS2", "DS1", "R4", "DS3", "DS4", "R3", "R2", "R1")))
            .put("MD4", new Ranking(ImmutableList.of("MD4", "MD1", "MD2", "MD3", "DS2", "DS1", "R4", "DS3", "DS4", "R3", "R2", "R1")))

            .put("R1", new Ranking(ImmutableList.of("R1", "R2", "R3", "R4", "MD4", "MD1", "MD3", "DS3", "MD2", "DS4", "DS2", "DS1")))
            .put("R2", new Ranking(ImmutableList.of("R2", "R1", "R3", "R4", "MD4", "MD1", "MD3", "DS3", "MD2", "DS4", "DS2", "DS1")))
            .put("R3", new Ranking(ImmutableList.of("R3", "R1", "R2", "R4", "MD4", "MD1", "MD3", "DS3", "MD2", "DS4", "DS2", "DS1")))
            .put("R4", new Ranking(ImmutableList.of("R4", "R1", "R2", "R3", "MD4", "MD1", "MD3", "DS3", "MD2", "DS4", "DS2", "DS1")))

            .build();
    ImmutableMap<String, Long> votes =
        ImmutableMap.<String, Long>builder()
            .put("DS1", 18L)
            .put("DS2", 4L)
            .put("DS3", 2L)
            .put("DS4", 1L)
            .put("MD1", 31L)
            .put("MD2", 8L)
            .put("MD3", 4L)
            .put("MD4", 2L)
            .put("R1", 23L)
            .put("R2", 4L)
            .put("R3", 2L)
            .put("R4", 1L)
            .build();

    Election election = Election.create(numSeats, candidatesAndRankings, votes);
    System.out.println("Winners are " + election.tabulate());
  }

  public static void main(String[] args) {
    singleSeatExample();
    multiSeatExample();
  }

}
