package ProgaConcu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class League
{

  private static final int NUMBER_OF_TEAMS = 20;
  private static final int FIXED_TEAM_INDEX = NUMBER_OF_TEAMS - 1;

  public static void main(String[] args)
  {
    String[] teamNames =
    {
        "Boca", "River", "San Lorenzo", "Ferro", "Huracan", "Velez", "Estudiantes", "Belgrano",
        "Lanus", "Talleres", "Dep Español", "San Martin(T)", "Mandiyu", "Rosario Central",
        "Independiente", "Racing Club", "Gimnasia LP", "Platense", "Argentinos", "Newells"
    };

    sequentialProcessing(teamNames);
    concurrentProcessing(teamNames);
  }

  public static Match[][] calculateFixture(List<Team> teams)
  {
    int numberOfTeams = teams.size();
    int numberOfRounds = numberOfTeams - 1;
    int matchesPerRound = numberOfTeams / 2;

    Match[][] fixture = new Match[numberOfRounds][matchesPerRound];

    for (int round = 0; round < numberOfRounds; round++)
    {
      for (int matchIndex = 0; matchIndex < matchesPerRound; matchIndex++)
      {
        int homeIndex = (round + matchIndex) % (numberOfTeams - 1);
        int awayIndex = (numberOfTeams - 1 - matchIndex + round) % (numberOfTeams - 1);

        if (matchIndex == 0)
        {
          awayIndex = FIXED_TEAM_INDEX;
        }

        Team home = teams.get(homeIndex);
        Team away = teams.get(awayIndex);

        // alternamos partidos visitante y de local
        if (round % 2 == 0 && matchIndex == 0)
        {
          fixture[round][matchIndex] = new Match(away, home);
        }
        else
        {
          fixture[round][matchIndex] = new Match(home, away);
        }
      }
    }

    return fixture;
  }

  public static void sequentialProcessing(String[] names)
  {
    List<Team> teams = new ArrayList<>();

    for (String name : names)
    {
      teams.add(new Team(name));
    }

    Match[][] fixture = calculateFixture(teams);

    long startTime = System.currentTimeMillis();

    for (Match[] round : fixture)
    {
      for (Match match : round)
      {
        match.simulate();
      }
      Standings.sort(teams);
    }

    Standings.display(teams);

    long endTime = System.currentTimeMillis();
    System.out.println("Sequential execution time: " + (endTime - startTime) / 1000 + " seconds");
  }

  public static void concurrentProcessing(String[] names)
  {
    List<Team> teams = new ArrayList<>();

    for (String name : names)
    {
      teams.add(new Team(name));
    }

    Match[][] fixture = calculateFixture(teams);

    long startTime = System.currentTimeMillis();

    int threadPoolSize = fixture[0].length;
    ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

    for (Match[] round : fixture)
    {
      CountDownLatch latch = new CountDownLatch(round.length);

      for (Match match : round)
      {
        executor.execute(() ->
        {
          match.simulate();
          latch.countDown();
        });
      }

      try
      {
        latch.await();
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
        System.err.println("Interrupted while waiting for match simulations.");
      }

      Standings.sort(teams);
    }

    executor.shutdown();

    Standings.display(teams);

    long endTime = System.currentTimeMillis();
    System.out.println("Concurrent execution time: " + (endTime - startTime) / 1000 + " seconds");
  }
}