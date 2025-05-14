package ProgaConcu;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Standings
{
    public static void sort(List<Team> teams)
    {
        Collections.sort(teams, new Comparator<Team>()
        {
            @Override
            public int compare(Team t1, Team t2)
            {
                if (t2.getPoints() != t1.getPoints())
                {
                    return Integer.compare(t2.getPoints(), t1.getPoints());
                }
                if (t2.getGoalDifference() != t1.getGoalDifference())
                {
                    return Integer.compare(t2.getGoalDifference(), t1.getGoalDifference());
                }
                return Integer.compare(t2.getGoalsFor(), t1.getGoalsFor());
            }
        });
    }

    public static void display(List<Team> teams)
    {
        System.out.printf("%-20s %3s %3s %3s %3s %3s %3s %3s %4s%n",
            "Team", "PTS", "MP", "W", "D", "L", "GF", "GA", "GD");

        for (Team t : teams)
        {
            System.out.printf("%-20s %3d %3d %3d %3d %3d %3d %3d %4d%n",
                t.getName(), t.getPoints(), t.getMatchesPlayed(), t.getWins(),
                t.getDraws(), t.getLosses(), t.getGoalsFor(),
                t.getGoalsAgainst(), t.getGoalDifference());
        }
    }
}