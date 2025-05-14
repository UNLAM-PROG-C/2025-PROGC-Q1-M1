package ProgaConcu;

public class Team
{
    private String name;
    private int points;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;

    public Team(String name)
    {
        this.name = name;
        this.points = 0;
        this.matchesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.goalDifference = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public int getMatchesPlayed()
    {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed)
    {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWins()
    {
        return wins;
    }

    public void setWins(int wins)
    {
        this.wins = wins;
    }

    public int getLosses()
    {
        return losses;
    }

    public void setLosses(int losses)
    {
        this.losses = losses;
    }

    public int getDraws()
    {
        return draws;
    }

    public void setDraws(int draws)
    {
        this.draws = draws;
    }

    public int getGoalsFor()
    {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor)
    {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst()
    {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst)
    {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference()
    {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference)
    {
        this.goalDifference = goalDifference;
    }
}