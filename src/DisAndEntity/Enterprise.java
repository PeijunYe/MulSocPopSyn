package DisAndEntity;

import java.util.ArrayList;
import java.util.List;

import EnumType.EEnterScale;
import EnumType.EEnterType;
import EnumType.EProvince;

public class Enterprise
{
	private int enterID;// 0 means the unemployed person
	private EProvince provin;
	private EEnterType enterType;
	private EEnterScale enterScale;
	private int employeeNum;
	private List<Individual> memberList;

	public Enterprise(EProvince provin, EEnterType enterType, EEnterScale enterScale, int employeeNum)
	{
		enterID = -1;
		this.provin = provin;
		this.enterType = enterType;
		this.enterScale = enterScale;
		this.employeeNum = employeeNum;
		this.memberList = new ArrayList<Individual>( );
	}

	public int getEnterID( )
	{
		return enterID;
	}

	public void setEnterID(int enterID)
	{
		this.enterID = enterID;
	}

	public EProvince getProvin( )
	{
		return provin;
	}

	public void setProvin(EProvince provin)
	{
		this.provin = provin;
	}

	public EEnterType getEnterType( )
	{
		return enterType;
	}

	public void setEnterType(EEnterType enterType)
	{
		this.enterType = enterType;
	}

	public EEnterScale getEnterScale( )
	{
		return enterScale;
	}

	public void setEnterScale(EEnterScale enterScale)
	{
		this.enterScale = enterScale;
	}

	public int getEmployeeNum( )
	{
		return employeeNum;
	}

	public void setEmployeeNum(int employeeNum)
	{
		this.employeeNum = employeeNum;
	}

	public List<Individual> getMemberList( )
	{
		return memberList;
	}

	public void setMemberList(List<Individual> memberList)
	{
		this.memberList = memberList;
	}
}
