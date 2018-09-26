package DisAndEntity;


import EnumType.EEnterScale;
import EnumType.EEnterType;
import EnumType.EProvince;

public class EnterpJointDis
{
	private EProvince provin;
	private EEnterType enterType;
	private EEnterScale enterScale;
	private int employedNum;

	public EnterpJointDis(EProvince provin, EEnterType enterType, EEnterScale enterScale, int employedNum)
	{
		this.provin = provin;
		this.enterType = enterType;
		this.enterScale = enterScale;
		this.employedNum = employedNum;
	}

	public void setProvin(EProvince provin)
	{
		this.provin = provin;
	}

	public void setEnterType(EEnterType enterType)
	{
		this.enterType = enterType;
	}

	public void setEnterScale(EEnterScale enterScale)
	{
		this.enterScale = enterScale;
	}

	public EProvince getProvin( )
	{
		return provin;
	}

	public EEnterType getEnterType( )
	{
		return enterType;
	}

	public EEnterScale getEnterScale( )
	{
		return enterScale;
	}

	public int getEmployedNum( )
	{
		return employedNum;
	}

	public void setEmployedNum(int employedNum)
	{
		this.employedNum = employedNum;
	}
}
