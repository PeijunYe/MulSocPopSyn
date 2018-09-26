package EnumType;

public enum EEnterScale
{
	SevenAndBelow(0), EightToNineteen(1), TweentyToFortyNine(2), FiftyToNintyNine(3), OneHundredTo299(4), ThreeHundredTo499(5), FiveHundredTo999(
					6), OneThousandTo4999(7), FiveThousandTo9999(8), TenThousandAndAbove(9);
	private int value = 0;

	private EEnterScale(int value)
	{
		this.value = value;
	}

	public static EEnterScale valueOf(int value)
	{
		switch (value)
		{
			case 0:
				return SevenAndBelow;
			case 1:
				return EightToNineteen;
			case 2:
				return TweentyToFortyNine;
			case 3:
				return FiftyToNintyNine;
			case 4:
				return OneHundredTo299;
			case 5:
				return ThreeHundredTo499;
			case 6:
				return FiveHundredTo999;
			case 7:
				return OneThousandTo4999;
			case 8:
				return FiveThousandTo9999;
			case 9:
				return TenThousandAndAbove;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
