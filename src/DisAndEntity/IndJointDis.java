package DisAndEntity;

import EnumType.EAgeInterval;
import EnumType.EGender;
import EnumType.EProvince;
import EnumType.EResidenceType;
import EnumType.EhhType;

public class IndJointDis
{
	private EProvince resideProvin;
	private EResidenceType residenceType;
	private EAgeInterval age;
	private EhhType hhType;
	private EGender gender;

	public IndJointDis(EGender gender, EProvince resideProvin, EResidenceType residenceType, EAgeInterval age, EhhType hhType)
	{
		this.gender = gender;
		this.age = age;
		this.resideProvin = resideProvin;
		this.residenceType = residenceType;
		this.hhType = hhType;
	}

	public EProvince getResideProvin( )
	{
		return resideProvin;
	}

	public void setResideProvin(EProvince resideProvin)
	{
		this.resideProvin = resideProvin;
	}

	public void setResidenceType(EResidenceType residenceType)
	{
		this.residenceType = residenceType;
	}

	public void setAge(EAgeInterval age)
	{
		this.age = age;
	}

	public void setHhType(EhhType hhType)
	{
		this.hhType = hhType;
	}

	public void setGender(EGender gender)
	{
		this.gender = gender;
	}

	public EResidenceType getResidenceType( )
	{
		return residenceType;
	}

	public EAgeInterval getAge( )
	{
		return age;
	}

	public EhhType getHhType( )
	{
		return hhType;
	}

	public EGender getGender( )
	{
		return gender;
	}
}
