package DisAndEntity;

import java.util.ArrayList;
import java.util.List;

import EnumType.EProvince;
import EnumType.EResidenceType;
import EnumType.EhhType;

public class HouseHold
{
	private int houseHoldID;
	private EhhType houseHoldType;
	private EProvince resideProvin;
	private EResidenceType resideType;
	private Individual hhHead;
	private List<Individual> memberList;
	private List<Integer> memberIDList;// member ID, for limited memory
	private int memberNum;
	private int elderNum;

	public HouseHold(EhhType houseHoldType, EProvince resideProvin, EResidenceType resideType)
	{
		this.houseHoldType = houseHoldType;
		this.resideProvin = resideProvin;
		this.resideType = resideType;
		this.hhHead = null;
		this.memberIDList = new ArrayList<Integer>( );
		memberList = new ArrayList<Individual>( );
		this.memberNum = 0;
		this.elderNum = 0;
		this.houseHoldID = 0;
	}

	public int getHouseHoldID( )
	{
		return houseHoldID;
	}

	public void setHouseHoldID(int houseHoldID)
	{
		this.houseHoldID = houseHoldID;
	}

	public EhhType getHouseHoldType( )
	{
		return houseHoldType;
	}

	public void setHouseHoldType(EhhType houseHoldType)
	{
		this.houseHoldType = houseHoldType;
	}

	public EProvince getResideProvin( )
	{
		return resideProvin;
	}

	public void setResideProvin(EProvince resideProvin)
	{
		this.resideProvin = resideProvin;
	}

	public EResidenceType getResideType( )
	{
		return resideType;
	}

	public void setResideType(EResidenceType resideType)
	{
		this.resideType = resideType;
	}

	public Individual getHhHead( )
	{
		return hhHead;
	}

	public void setHhHead(Individual hhHead)
	{
		this.hhHead = hhHead;
	}

	public int getMemberNum( )
	{
		return memberNum;
	}

	public void setMemberNum(int memNum)
	{
		this.memberNum = memNum;
	}

	public List<Integer> getMemberIDList( )
	{
		return memberIDList;
	}

	public void setMemberIDList(List<Integer> memberIDList)
	{
		this.memberIDList = memberIDList;
	}

	public List<Individual> getMemberList( )
	{
		return memberList;
	}

	public void setMemberList(List<Individual> memberList)
	{
		this.memberList = memberList;
	}

	public int getElderNum( )
	{
		return elderNum;
	}

	public void setElderNum(int elderNum)
	{
		this.elderNum = elderNum;
	}
}
