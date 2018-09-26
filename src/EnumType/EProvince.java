package EnumType;

public enum EProvince
{
	Beijing(11), Tianjin(12), Hebei(13), Shan_1xi(14), Neimenggu(15), Liaoning(21), Jilin(22), Heilongjiang(23), Shanghai(31), Jiangsu(32), Zhejiang(33), Anhui(
					34), Fujian(35), Jiangxi(36), Shandong(37), Henan(41), Hubei(42), Hunan(43), Guangdong(44), Guangxi(45), Hainan(46), Chongqing(50), Sichuan(
									51), Guizhou(52), Yunnan(53), Xizang(54), Shan_3xi(61), Gansu(62), Qinghai(63), Ningxia(64), Xinjiang(65), Other(0);
	private int value = 0;

	private EProvince(int value)
	{
		this.value = value;
	}

	public static EProvince valueOf(int value)
	{
		switch (value)
		{
			case 0:
				return Other;
			case 11:
				return Beijing;
			case 12:
				return Tianjin;
			case 13:
				return Hebei;
			case 14:
				return Shan_1xi;
			case 15:
				return Neimenggu;
			case 21:
				return Liaoning;
			case 22:
				return Jilin;
			case 23:
				return Heilongjiang;
			case 31:
				return Shanghai;
			case 32:
				return Jiangsu;
			case 33:
				return Zhejiang;
			case 34:
				return Anhui;
			case 35:
				return Fujian;
			case 36:
				return Jiangxi;
			case 37:
				return Shandong;
			case 41:
				return Henan;
			case 42:
				return Hubei;
			case 43:
				return Hunan;
			case 44:
				return Guangdong;
			case 45:
				return Guangxi;
			case 46:
				return Hainan;
			case 50:
				return Chongqing;
			case 51:
				return Sichuan;
			case 52:
				return Guizhou;
			case 53:
				return Yunnan;
			case 54:
				return Xizang;
			case 61:
				return Shan_3xi;
			case 62:
				return Gansu;
			case 63:
				return Qinghai;
			case 64:
				return Ningxia;
			case 65:
				return Xinjiang;
			default:
				return null;
		}
	}

	public int value( )
	{
		return this.value;
	}
}
