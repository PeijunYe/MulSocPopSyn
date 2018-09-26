package EnumType;

public enum EResidenceType
{
	city(1), town(2), rural(3);
	private int value = 0;

	private EResidenceType(int value)
	{
		this.value = value;
	}

	public static EResidenceType valueOf(int value)
	{
		switch (value)
		{
			case 1:
				return city;
			case 2:
				return town;
			case 3:
				return rural;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
