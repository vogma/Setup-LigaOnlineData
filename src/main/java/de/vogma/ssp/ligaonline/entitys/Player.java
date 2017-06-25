/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.vogma.ssp.ligaonline.entitys;

/**
 *
 * @author Marco
 */
public class Player
{
	private String name;
	private int teamID;

	public int getTeamID()
	{
		return teamID;
	}

	public void setTeamID(int teamID)
	{
		this.teamID = teamID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Player()
	{
	}

	public Player(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "Name: " + getName()+" ID: "+getTeamID();
	}

}
