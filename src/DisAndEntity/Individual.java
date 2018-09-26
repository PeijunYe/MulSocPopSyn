package DisAndEntity;

import EnumType.EAgeInterval;
import EnumType.EGender;
import EnumType.EProvince;
import EnumType.EResidenceType;

public class Individual
{
	private int agentID;
	private EGender gender;
	private EAgeInterval ageInter;
	private EProvince provin;
	private EResidenceType residenceType;
	private HouseHold hh;
	private boolean isHHHead;
	private Enterprise enterp;// null means unemployed

	public Individual(EGender gender, EProvince provin, EResidenceType residenceType, EAgeInterval ageInter)
	{
		this.agentID = 0;
		this.gender = gender;
		this.ageInter = ageInter;
		this.provin = provin;
		this.residenceType = residenceType;
		isHHHead = false;
		hh = null;
		enterp = null;
	}

	public int getAgentID( )
	{
		return agentID;
	}

	public void setAgentID(int agentID)
	{
		this.agentID = agentID;
	}

	public EAgeInterval getAgeInter( )
	{
		return ageInter;
	}

	public void setAgeInter(EAgeInterval ageInter)
	{
		this.ageInter = ageInter;
	}

	public EProvince getProvin( )
	{
		return provin;
	}

	public void setProvin(EProvince provin)
	{
		this.provin = provin;
	}

	public EResidenceType getResidenceType( )
	{
		return residenceType;
	}

	public void setResidenceType(EResidenceType residenceType)
	{
		this.residenceType = residenceType;
	}

	public HouseHold getHh( )
	{
		return hh;
	}

	public void setHh(HouseHold hh)
	{
		this.hh = hh;
	}

	public Enterprise getEnterp( )
	{
		return enterp;
	}

	public void setEnterp(Enterprise enterp)
	{
		this.enterp = enterp;
	}

	public EGender getGender( )
	{
		return gender;
	}

	public boolean isHHHead( )
	{
		return isHHHead;
	}

	public void setHHHead(boolean isHHHead)
	{
		this.isHHHead = isHHHead;
	}
}
