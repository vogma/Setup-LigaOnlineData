/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.vogma.ssp.ligaonline.entitys;

import java.util.List;

/**
 *
 * @author Marco Vogel
 */
public class Team
{

	private String teamName;
	private String teamIconUrl;
	private int teamID;
	private int stadionID;

	public int getStadionID()
	{
		return stadionID;
	}

	public void setStadionID(int stadionID)
	{
		this.stadionID = stadionID;
	}

	public String getTeamName()
	{
		return teamName;
	}

	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}

	public String getTeamIconUrl()
	{
		return teamIconUrl;
	}

	public void setTeamIconUrl(String teamIconUrl)
	{
		this.teamIconUrl = teamIconUrl;
	}

	public int getTeamID()
	{
		return teamID;
	}

	public void setTeamID(int teamID)
	{
		this.teamID = teamID;
	}

	@Override
	public String toString()
	{
		return "Teamname: " + getTeamName() + " TeamID: " + getTeamID() + " IconUrl: " + getTeamIconUrl() + " StadionID: "+ getStadionID();
	}

}
