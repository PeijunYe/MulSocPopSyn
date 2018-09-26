package Input;

import EnumType.EAgeInterval;
import EnumType.EEnterScale;
import EnumType.EProvince;

public class AttributeCheck
{
	public static EAgeInterval CheckAgeInterval(Integer age)
	{
		if (age < 5)
			return EAgeInterval.ZeroToFour;
		else if (age < 10)
			return EAgeInterval.FiveToNine;
		else if (age < 15)
			return EAgeInterval.TenToFourteen;
		else if (age < 20)
			return EAgeInterval.FifteenToNineteen;
		else if (age < 25)
			return EAgeInterval.TwentyToTwentyFour;
		else if (age < 30)
			return EAgeInterval.TwentyFiveToTwentyNine;
		else if (age < 35)
			return EAgeInterval.ThirtyToThirtyFour;
		else if (age < 40)
			return EAgeInterval.ThirtyFiveToThirtyNine;
		else if (age < 45)
			return EAgeInterval.FortyToFortyFour;
		else if (age < 50)
			return EAgeInterval.FortyFiveToFortyNine;
		else if (age < 55)
			return EAgeInterval.FiftyToFiftyFour;
		else if (age < 60)
			return EAgeInterval.FiftyFiveToFiftyNine;
		else if (age < 65)
			return EAgeInterval.SixtyToSixtyFour;
		else if (age < 70)
			return EAgeInterval.SixtyFiveToSixtyNine;
		else if (age < 75)
			return EAgeInterval.SeventyToSeventyFour;
		else if (age < 80)
			return EAgeInterval.SeventyFiveToSeventyNine;
		else if (age < 85)
			return EAgeInterval.EightyToEightyFour;
		else if (age < 90)
			return EAgeInterval.EightyFiveToEightyNine;
		else if (age < 95)
			return EAgeInterval.NinetyToNinetyFour;
		else if (age < 100)
			return EAgeInterval.NinetyFiveToNinetyNine;
		else
			return EAgeInterval.HundredAndAbove;
	}

	public static EAgeInterval CheckAgeInterval(String ageString)
	{
		EAgeInterval ageInter = null;
		switch (ageString)
		{
			case "ZeroToFour":
				ageInter = EAgeInterval.ZeroToFour;
				break;
			case "FiveToNine":
				ageInter = EAgeInterval.FiveToNine;
				break;
			case "TenToFourteen":
				ageInter = EAgeInterval.TenToFourteen;
				break;
			case "FifteenToNineteen":
				ageInter = EAgeInterval.FifteenToNineteen;
				break;
			case "TwentyToTwentyFour":
				ageInter = EAgeInterval.TwentyToTwentyFour;
				break;
			case "TwentyFiveToTwentyNine":
				ageInter = EAgeInterval.TwentyFiveToTwentyNine;
				break;
			case "ThirtyToThirtyFour":
				ageInter = EAgeInterval.ThirtyToThirtyFour;
				break;
			case "ThirtyFiveToThirtyNine":
				ageInter = EAgeInterval.ThirtyFiveToThirtyNine;
				break;
			case "FortyToFortyFour":
				ageInter = EAgeInterval.FortyToFortyFour;
				break;
			case "FortyFiveToFortyNine":
				ageInter = EAgeInterval.FortyFiveToFortyNine;
				break;
			case "FiftyToFiftyFour":
				ageInter = EAgeInterval.FiftyToFiftyFour;
				break;
			case "FiftyFiveToFiftyNine":
				ageInter = EAgeInterval.FiftyFiveToFiftyNine;
				break;
			case "SixtyToSixtyFour":
				ageInter = EAgeInterval.SixtyToSixtyFour;
				break;
			case "SixtyFiveToSixtyNine":
				ageInter = EAgeInterval.SixtyFiveToSixtyNine;
				break;
			case "SeventyToSeventyFour":
				ageInter = EAgeInterval.SeventyToSeventyFour;
				break;
			case "SeventyFiveToSeventyNine":
				ageInter = EAgeInterval.SeventyFiveToSeventyNine;
				break;
			case "EightyToEightyFour":
				ageInter = EAgeInterval.EightyToEightyFour;
				break;
			case "EightyFiveToEightyNine":
				ageInter = EAgeInterval.EightyFiveToEightyNine;
				break;
			case "NinetyToNinetyFour":
				ageInter = EAgeInterval.NinetyToNinetyFour;
				break;
			case "NinetyFiveToNinetyNine":
				ageInter = EAgeInterval.NinetyFiveToNinetyNine;
				break;
			case "HundredAndAbove":
				ageInter = EAgeInterval.HundredAndAbove;
		}
		return ageInter;
	}

	public static EProvince CheckProvince(String provName)
	{
		EProvince result = EProvince.Other;
		switch (provName)
		{
			case "北京市":
				result = EProvince.Beijing;
				break;
			case "北京":
				result = EProvince.Beijing;
				break;
			case "Beijing":
				result = EProvince.Beijing;
				break;
			case "天津市":
				result = EProvince.Tianjin;
				break;
			case "天津":
				result = EProvince.Tianjin;
				break;
			case "Tianjin":
				result = EProvince.Tianjin;
				break;
			case "河北省":
				result = EProvince.Hebei;
				break;
			case "河北":
				result = EProvince.Hebei;
				break;
			case "Hebei":
				result = EProvince.Hebei;
				break;
			case "山西省":
				result = EProvince.Shan_1xi;
				break;
			case "山西":
				result = EProvince.Shan_1xi;
				break;
			case "Shan_1xi":
				result = EProvince.Shan_1xi;
				break;
			case "内蒙古自治区":
				result = EProvince.Neimenggu;
				break;
			case "内蒙古":
				result = EProvince.Neimenggu;
				break;
			case "Neimenggu":
				result = EProvince.Neimenggu;
				break;
			case "辽宁省":
				result = EProvince.Liaoning;
				break;
			case "辽宁":
				result = EProvince.Liaoning;
				break;
			case "Liaoning":
				result = EProvince.Liaoning;
				break;
			case "吉林省":
				result = EProvince.Jilin;
				break;
			case "吉林":
				result = EProvince.Jilin;
				break;
			case "Jilin":
				result = EProvince.Jilin;
				break;
			case "黑龙江省":
				result = EProvince.Heilongjiang;
				break;
			case "黑龙江":
				result = EProvince.Heilongjiang;
				break;
			case "Heilongjiang":
				result = EProvince.Heilongjiang;
				break;
			case "上海市":
				result = EProvince.Shanghai;
				break;
			case "上海":
				result = EProvince.Shanghai;
				break;
			case "Shanghai":
				result = EProvince.Shanghai;
				break;
			case "江苏省":
				result = EProvince.Jiangsu;
				break;
			case "江苏":
				result = EProvince.Jiangsu;
				break;
			case "Jiangsu":
				result = EProvince.Jiangsu;
				break;
			case "浙江省":
				result = EProvince.Zhejiang;
				break;
			case "浙江":
				result = EProvince.Zhejiang;
				break;
			case "Zhejiang":
				result = EProvince.Zhejiang;
				break;
			case "安徽省":
				result = EProvince.Anhui;
				break;
			case "安徽":
				result = EProvince.Anhui;
				break;
			case "Anhui":
				result = EProvince.Anhui;
				break;
			case "福建省":
				result = EProvince.Fujian;
				break;
			case "福建":
				result = EProvince.Fujian;
				break;
			case "Fujian":
				result = EProvince.Fujian;
				break;
			case "江西省":
				result = EProvince.Jiangxi;
				break;
			case "江西":
				result = EProvince.Jiangxi;
				break;
			case "Jiangxi":
				result = EProvince.Jiangxi;
				break;
			case "山东省":
				result = EProvince.Shandong;
				break;
			case "山东":
				result = EProvince.Shandong;
				break;
			case "Shandong":
				result = EProvince.Shandong;
				break;
			case "河南省":
				result = EProvince.Henan;
				break;
			case "河南":
				result = EProvince.Henan;
				break;
			case "Henan":
				result = EProvince.Henan;
				break;
			case "湖北省":
				result = EProvince.Hubei;
				break;
			case "湖北":
				result = EProvince.Hubei;
				break;
			case "Hubei":
				result = EProvince.Hubei;
				break;
			case "湖南省":
				result = EProvince.Hunan;
				break;
			case "湖南":
				result = EProvince.Hunan;
				break;
			case "Hunan":
				result = EProvince.Hunan;
				break;
			case "广东省":
				result = EProvince.Guangdong;
				break;
			case "广东":
				result = EProvince.Guangdong;
				break;
			case "Guangdong":
				result = EProvince.Guangdong;
				break;
			case "广西壮族自治区":
				result = EProvince.Guangxi;
				break;
			case "广西":
				result = EProvince.Guangxi;
				break;
			case "Guangxi":
				result = EProvince.Guangxi;
				break;
			case "海南省":
				result = EProvince.Hainan;
				break;
			case "海南":
				result = EProvince.Hainan;
				break;
			case "Hainan":
				result = EProvince.Hainan;
				break;
			case "重庆市":
				result = EProvince.Chongqing;
				break;
			case "重庆":
				result = EProvince.Chongqing;
				break;
			case "Chongqing":
				result = EProvince.Chongqing;
				break;
			case "四川省":
				result = EProvince.Sichuan;
				break;
			case "四川":
				result = EProvince.Sichuan;
				break;
			case "Sichuan":
				result = EProvince.Sichuan;
				break;
			case "贵州省":
				result = EProvince.Guizhou;
				break;
			case "贵州":
				result = EProvince.Guizhou;
				break;
			case "Guizhou":
				result = EProvince.Guizhou;
				break;
			case "云南省":
				result = EProvince.Yunnan;
				break;
			case "云南":
				result = EProvince.Yunnan;
				break;
			case "Yunnan":
				result = EProvince.Yunnan;
				break;
			case "西藏自治区":
				result = EProvince.Xizang;
				break;
			case "西藏":
				result = EProvince.Xizang;
				break;
			case "Xizang":
				result = EProvince.Xizang;
				break;
			case "陕西省":
				result = EProvince.Shan_3xi;
				break;
			case "陕西":
				result = EProvince.Shan_3xi;
				break;
			case "Shan_3xi":
				result = EProvince.Shan_3xi;
				break;
			case "甘肃省":
				result = EProvince.Gansu;
				break;
			case "甘肃":
				result = EProvince.Gansu;
				break;
			case "Gansu":
				result = EProvince.Gansu;
				break;
			case "青海省":
				result = EProvince.Qinghai;
				break;
			case "青海":
				result = EProvince.Qinghai;
				break;
			case "Qinghai":
				result = EProvince.Qinghai;
				break;
			case "宁夏回族自治区":
				result = EProvince.Ningxia;
				break;
			case "宁夏":
				result = EProvince.Ningxia;
				break;
			case "Ningxia":
				result = EProvince.Ningxia;
				break;
			case "新疆维吾尔自治区":
				result = EProvince.Xinjiang;
				break;
			case "新疆":
				result = EProvince.Xinjiang;
				break;
			case "Xinjiang":
				result = EProvince.Xinjiang;
				break;
		}
		return result;
	}

	public static double CheckEmployeeNum(String enterScale, Boolean isCorpor)
	{
		double result = 0;
		if (isCorpor)
		{
			switch (enterScale)
			{
				case "7人及以下":
					result = 3.8827;
					break;
				case "8-19人":
					result = 11.7333;
					break;
				case "20-49人":
					result = 30.4587;
					break;
				case "50-99人":
					result = 68.3476;
					break;
				case "100-299人":
					result = 161.4023;
					break;
				case "300-499人":
					result = 378.1169;
					break;
				case "500-999人":
					result = 680.8361;
					break;
				case "1000-4999人":
					result = 1856.1031;
					break;
				case "5000-9999人":
					result = 6715.5820;
					break;
				case "10000人及以上":
					result = 21403.4413;
					break;
			}
		} else
		{
			switch (enterScale)
			{
				case "7人及以下":
					result = 3.6212;
					break;
				case "8-19人":
					result = 11.7075;
					break;
				case "20-49人":
					result = 30.2665;
					break;
				case "50-99人":
					result = 68.1119;
					break;
				case "100-299人":
					result = 160.7403;
					break;
				case "300-499人":
					result = 377.9293;
					break;
				case "500-999人":
					result = 680.8243;
					break;
				case "1000-4999人":
					result = 1815.8148;
					break;
				case "5000-9999人":
					result = 6603.5891;
					break;
				case "10000人及以上":
					result = 18292.3077;
					break;
			}
		}
		return result;
	}

	public static double CheckEmployeeNum(EEnterScale enterScale, Boolean isCorpor)
	{
		double result = 0;
		if (isCorpor)
		{
			switch (enterScale)
			{
				case SevenAndBelow:
					result = 3.8827;
					break;
				case EightToNineteen:
					result = 11.7333;
					break;
				case TweentyToFortyNine:
					result = 30.4587;
					break;
				case FiftyToNintyNine:
					result = 68.3476;
					break;
				case OneHundredTo299:
					result = 161.4023;
					break;
				case ThreeHundredTo499:
					result = 378.1169;
					break;
				case FiveHundredTo999:
					result = 680.8361;
					break;
				case OneThousandTo4999:
					result = 1856.1031;
					break;
				case FiveThousandTo9999:
					result = 6715.5820;
					break;
				case TenThousandAndAbove:
					result = 21403.4413;
					break;
			}
		} else
		{
			switch (enterScale)
			{
				case SevenAndBelow:
					result = 3.6212;
					break;
				case EightToNineteen:
					result = 11.7075;
					break;
				case TweentyToFortyNine:
					result = 30.2665;
					break;
				case FiftyToNintyNine:
					result = 68.1119;
					break;
				case OneHundredTo299:
					result = 160.7403;
					break;
				case ThreeHundredTo499:
					result = 377.9293;
					break;
				case FiveHundredTo999:
					result = 680.8243;
					break;
				case OneThousandTo4999:
					result = 1815.8148;
					break;
				case FiveThousandTo9999:
					result = 6603.5891;
					break;
				case TenThousandAndAbove:
					result = 18292.3077;
					break;
			}
		}
		return result;
	}

	public static EEnterScale CheckEnterScale(String enterScale)
	{
		EEnterScale result = EEnterScale.TenThousandAndAbove;
		switch (enterScale)
		{
			case "7人及以下":
				result = EEnterScale.SevenAndBelow;
				break;
			case "8-19人":
				result = EEnterScale.EightToNineteen;
				break;
			case "20-49人":
				result = EEnterScale.TweentyToFortyNine;
				break;
			case "50-99人":
				result = EEnterScale.FiftyToNintyNine;
				break;
			case "100-299人":
				result = EEnterScale.OneHundredTo299;
				break;
			case "300-499人":
				result = EEnterScale.ThreeHundredTo499;
				break;
			case "500-999人":
				result = EEnterScale.FiveHundredTo999;
				break;
			case "1000-4999人":
				result = EEnterScale.OneThousandTo4999;
				break;
			case "5000-9999人":
				result = EEnterScale.FiveThousandTo9999;
				break;
			case "10000人及以上":
				result = EEnterScale.TenThousandAndAbove;
				break;
		}
		return result;
	}

	public static EAgeInterval CategorizeAgeInterval(EAgeInterval ageInter)
	{
		switch (ageInter)
		{
			case ZeroToFour:
			case FiveToNine:
			case TenToFourteen:
			case FifteenToNineteen:
				return EAgeInterval.FifteenToNineteen;
			case SixtyFiveToSixtyNine:
			case SeventyToSeventyFour:
			case SeventyFiveToSeventyNine:
			case EightyToEightyFour:
			case EightyFiveToEightyNine:
			case NinetyToNinetyFour:
			case NinetyFiveToNinetyNine:
			case HundredAndAbove:
				return EAgeInterval.SixtyFiveToSixtyNine;
			default:
				return EAgeInterval.FortyToFortyFour;
		}
	}

	public static EAgeInterval CategorizeAgeInterval(int age)
	{
		// if (age < 20)
		// return EAgeInterval.FifteenToNineteen;
		// else if (age < 65)
		// return EAgeInterval.FortyToFortyFour;
		// else
		// return EAgeInterval.EightyFiveToEightyNine;
		if (age < 5)
			return EAgeInterval.ZeroToFour;
		else if (age < 10)
			return EAgeInterval.FiveToNine;
		else if (age < 15)
			return EAgeInterval.TenToFourteen;
		else if (age < 20)
			return EAgeInterval.FifteenToNineteen;
		else if (age < 25)
			return EAgeInterval.TwentyToTwentyFour;
		else if (age < 30)
			return EAgeInterval.TwentyFiveToTwentyNine;
		else if (age < 35)
			return EAgeInterval.ThirtyToThirtyFour;
		else if (age < 40)
			return EAgeInterval.ThirtyFiveToThirtyNine;
		else if (age < 45)
			return EAgeInterval.FortyToFortyFour;
		else if (age < 50)
			return EAgeInterval.FortyFiveToFortyNine;
		else if (age < 55)
			return EAgeInterval.FiftyToFiftyFour;
		else if (age < 60)
			return EAgeInterval.FiftyFiveToFiftyNine;
		else if (age < 65)
			return EAgeInterval.SixtyToSixtyFour;
		else if (age < 70)
			return EAgeInterval.SixtyFiveToSixtyNine;
		else if (age < 75)
			return EAgeInterval.SeventyToSeventyFour;
		else if (age < 80)
			return EAgeInterval.SeventyFiveToSeventyNine;
		else if (age < 85)
			return EAgeInterval.EightyToEightyFour;
		else if (age < 90)
			return EAgeInterval.EightyFiveToEightyNine;
		else if (age < 95)
			return EAgeInterval.NinetyToNinetyFour;
		else if (age < 100)
			return EAgeInterval.NinetyFiveToNinetyNine;
		else
			return EAgeInterval.HundredAndAbove;
	}
}
