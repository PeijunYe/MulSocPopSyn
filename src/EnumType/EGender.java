package EnumType;

public enum EGender
{
	Male(1), Female(2);
	private int value = 0;

	private EGender(int value)
	{
		this.value = value;
	}

	public static EGender valueOf(int value)
	{
		switch (value)
		{
			case 1:
				return Male;
			case 2:
				return Female;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
