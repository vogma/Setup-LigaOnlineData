package de.vogma.ssp.ligaonline.entitys;

public class Goal
{

	private int goalID;
	private int toreHeim;
	private int toreGast;
	private int minute;
	private String schuetze;
	private int playerID;
	private int matchID;
	private boolean goalPenalty;

	public boolean isGoalPenalty()
	{
		return goalPenalty;
	}

	public void setGoalPenalty(boolean goalPenalty)
	{
		this.goalPenalty = goalPenalty;
	}

	public int getGoalID()
	{
		return goalID;
	}

	public void setGoalID(int goalID)
	{
		this.goalID = goalID;
	}

	public int getToreHeim()
	{
		return toreHeim;
	}

	public void setToreHeim(int toreHeim)
	{
		this.toreHeim = toreHeim;
	}

	public int getToreGast()
	{
		return toreGast;
	}

	public void setToreGast(int toreGast)
	{
		this.toreGast = toreGast;
	}

	public int getMinute()
	{
		return minute;
	}

	public void setMinute(int minute)
	{
		this.minute = minute;
	}

	public String getSchuetze()
	{
		return schuetze;
	}

	public void setSchuetze(String schuetze)
	{
		this.schuetze = schuetze;
	}

	public int getPlayerID()
	{
		return playerID;
	}

	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}

	public int getMatchID()
	{
		return matchID;
	}

	public void setMatchID(int matchID)
	{
		this.matchID = matchID;
	}

	@Override
	public String toString()
	{
		return getSchuetze() + " " + getMatchID() + "  " + getMinute() + " " + getToreGast() + " " + getToreHeim();
	}
}