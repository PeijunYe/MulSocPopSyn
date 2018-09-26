package DisAndEntity;

import EnumType.EProvince;
import EnumType.EResidenceType;
import EnumType.EhhType;

public class HHJointDis
{
	private EhhType houseHoldType;
	private EProvince resideProvin;
	private EResidenceType resideType;
	private int memberNum;
	private int elderNum;
	private double colHHMemNum;// for collective HH
	private double colHHChildNum;// for collective HH
	private double colHHElderNum;// for collective HH

	public HHJointDis(EhhType houseHoldType, EProvince resideProvin, EResidenceType resideType, int memberNum, int elderNum)
	{
		this.houseHoldType = houseHoldType;
		this.resideProvin = resideProvin;
		this.resideType = resideType;
		this.memberNum = memberNum;
		this.elderNum = elderNum;
		this.colHHMemNum = 0;
		this.colHHChildNum = 0;
		this.colHHElderNum = 0;
	}

	public void setHouseHoldType(EhhType houseHoldType)
	{
		this.houseHoldType = houseHoldType;
	}

	public void setResideProvin(EProvince resideProvin)
	{
		this.resideProvin = resideProvin;
	}

	public void setResideType(EResidenceType resideType)
	{
		this.resideType = resideType;
	}

	public void setMemberNum(int memberNum)
	{
		this.memberNum = memberNum;
	}

	public void setElderNum(int elderNum)
	{
		this.elderNum = elderNum;
	}

	public void setColHHMemNum(double colHHMemNum)
	{
		this.colHHMemNum = colHHMemNum;
	}

	public void setColHHChildNum(double colHHChildNum)
	{
		this.colHHChildNum = colHHChildNum;
	}

	public void setColHHElderNum(double colHHElderNum)
	{
		this.colHHElderNum = colHHElderNum;
	}

	public EhhType getHouseHoldType( )
	{
		return houseHoldType;
	}

	public EProvince getResideProvin( )
	{
		return resideProvin;
	}

	public EResidenceType getResideType( )
	{
		return resideType;
	}

	public int getMemberNum( )
	{
		return memberNum;
	}

	public int getElderNum( )
	{
		return elderNum;
	}

	public double getColHHMemNum( )
	{
		return colHHMemNum;
	}

	public double getColHHChildNum( )
	{
		return colHHChildNum;
	}

	public double getColHHElderNum( )
	{
		return colHHElderNum;
	}
}
