package EnumType;

public enum EAgeInterval
{
	ZeroToFour(0), FiveToNine(1), TenToFourteen(2), FifteenToNineteen(3), TwentyToTwentyFour(4), TwentyFiveToTwentyNine(5), ThirtyToThirtyFour(
					6), ThirtyFiveToThirtyNine(7), FortyToFortyFour(8), FortyFiveToFortyNine(9), FiftyToFiftyFour(10), FiftyFiveToFiftyNine(
									11), SixtyToSixtyFour(12), SixtyFiveToSixtyNine(13), SeventyToSeventyFour(14), SeventyFiveToSeventyNine(
													15), EightyToEightyFour(16), EightyFiveToEightyNine(
																	17), NinetyToNinetyFour(18), NinetyFiveToNinetyNine(19), HundredAndAbove(20);
	private int value = 0;

	private EAgeInterval(int value)
	{
		this.value = value;
	}

	public static EAgeInterval valueOf(int value)
	{
		switch (value)
		{
			case 0:
				return ZeroToFour;
			case 1:
				return FiveToNine;
			case 2:
				return TenToFourteen;
			case 3:
				return FifteenToNineteen;
			case 4:
				return TwentyToTwentyFour;
			case 5:
				return TwentyFiveToTwentyNine;
			case 6:
				return ThirtyToThirtyFour;
			case 7:
				return ThirtyFiveToThirtyNine;
			case 8:
				return FortyToFortyFour;
			case 9:
				return FortyFiveToFortyNine;
			case 10:
				return FiftyToFiftyFour;
			case 11:
				return FiftyFiveToFiftyNine;
			case 12:
				return SixtyToSixtyFour;
			case 13:
				return SixtyFiveToSixtyNine;
			case 14:
				return SeventyToSeventyFour;
			case 15:
				return SeventyFiveToSeventyNine;
			case 16:
				return EightyToEightyFour;
			case 17:
				return EightyFiveToEightyNine;
			case 18:
				return NinetyToNinetyFour;
			case 19:
				return NinetyFiveToNinetyNine;
			case 20:
				return HundredAndAbove;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
