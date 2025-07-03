package ProgaConcu;

import java.util.concurrent.ThreadLocalRandom;

public class Match
{
    private Team homeTeam;
    private Team awayTeam;

    private static final int MAX_GOALS = 5;
    private static final int MIN_SLEEP_MS = 100;
    private static final int MAX_SLEEP_MS = 150;
    private static final int WIN_POINTS = 3;
    private static final int DRAW_POINTS = 1;

    public Match(Team homeTeam, Team awayTeam)
    {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Team getHomeTeam()
    {
        return homeTeam;
    }

    public Team getAwayTeam()
    {
        return awayTeam;
    }

    public void simulate()
    {
        long startTime = System.currentTimeMillis();

        int homeGoals = ThreadLocalRandom.current().nextInt(0, MAX_GOALS + 1);
        int awayGoals = ThreadLocalRandom.current().nextInt(0, MAX_GOALS + 1);

        homeTeam.setGoalsFor(homeTeam.getGoalsFor() + homeGoals);
        homeTeam.setGoalsAgainst(homeTeam.getGoalsAgainst() + awayGoals);

        awayTeam.setGoalsFor(awayTeam.getGoalsFor() + awayGoals);
        awayTeam.setGoalsAgainst(awayTeam.getGoalsAgainst() + homeGoals);

        homeTeam.setGoalDifference(homeTeam.getGoalsFor() - homeTeam.getGoalsAgainst());
        awayTeam.setGoalDifference(awayTeam.getGoalsFor() - awayTeam.getGoalsAgainst());

        homeTeam.setMatchesPlayed(homeTeam.getMatchesPlayed() + 1);
        awayTeam.setMatchesPlayed(awayTeam.getMatchesPlayed() + 1);

        if (homeGoals > awayGoals)
        {
            homeTeam.setWins(homeTeam.getWins() + 1);
            awayTeam.setLosses(awayTeam.getLosses() + 1);
            homeTeam.setPoints(homeTeam.getPoints() + WIN_POINTS);
        }
        else if (awayGoals > homeGoals)
        {
            awayTeam.setWins(awayTeam.getWins() + 1);
            homeTeam.setLosses(homeTeam.getLosses() + 1);
            awayTeam.setPoints(awayTeam.getPoints() + WIN_POINTS);
        }
        else
        {
            homeTeam.setDraws(homeTeam.getDraws() + 1);
            awayTeam.setDraws(awayTeam.getDraws() + 1);
            homeTeam.setPoints(homeTeam.getPoints() + DRAW_POINTS);
            awayTeam.setPoints(awayTeam.getPoints() + DRAW_POINTS);
        }

        long processingTime = System.currentTimeMillis() - startTime;
        long totalDuration = ThreadLocalRandom.current().nextLong(MIN_SLEEP_MS, MAX_SLEEP_MS + 1);
        long remainingTime = totalDuration - processingTime;

        if (remainingTime > 0)
        {
            try
            {
                Thread.sleep(remainingTime);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}