package EnumType;

public enum EhhType
{
	family(1), collectiveHH(2);
	private int value = 0;

	private EhhType(int value)
	{
		this.value = value;
	}

	public static EhhType valueOf(int value)
	{
		switch (value)
		{
			case 1:
				return family;
			case 2:
				return collectiveHH;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
