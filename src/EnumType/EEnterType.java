package EnumType;

public enum EEnterType
{
	corporation(0), industrialUnit(1), none(2);
	private int value = 0;

	private EEnterType(int value)
	{
		this.value = value;
	}

	public static EEnterType valueOf(int value)
	{
		switch (value)
		{
			case 0:
				return corporation;
			case 1:
				return industrialUnit;
			case 2:
				return none;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
