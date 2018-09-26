package Input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.csvreader.CsvWriter;

import DisAndEntity.EnterpJointDis;
import DisAndEntity.HHJointDis;
import DisAndEntity.HouseHold;
import DisAndEntity.IndJointDis;
import DisAndEntity.Individual;
import EnumType.EAgeInterval;
import EnumType.EEnterScale;
import EnumType.EEnterType;
import EnumType.EGender;
import EnumType.EProvince;
import EnumType.EResidenceType;
import EnumType.EhhType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class InputDistributions
{
	// population:
	// Gender*Reside Province*Residence Type*HH Type
	private Map<IndJointDis, Integer> genProvResTypeHHTypeIndCon;
	// Gender*Reside Province*Residence Type*Age Interval
	private Map<IndJointDis, Integer> genProvResTypeAgeIndCon;
	// household:
	// Reside Province*Residence Type*HH Type
	private Map<HHJointDis, Integer> provResTypeHHTypeHHCon;
	// Reside Province*Residence Type*Member Number (family)
	private Map<HHJointDis, Integer> provResTypeMemNumHHCon;
	// Reside Province*Residence Type*Elder Number (family)
	private Map<HHJointDis, Integer> provResTypeElderTypeHHCon;
	// enterprise:
	// Reside Province*Enterprise Type
	private Map<EnterpJointDis, Integer> provEnterTypeEnCon;
	// Enterprise Type*Enterprise Scale
	private Map<EnterpJointDis, Integer> enterTypeScaleEnCon;
	// sample:
	private Map<Integer, HouseHold> sampleHH;
	private Map<HHJointDis, Integer> sampleHHDis;
	// sample distribution:
	private Map<IndJointDis, Integer> sampleIndDis;

	public InputDistributions( )
	{
		genProvResTypeHHTypeIndCon = new HashMap<IndJointDis, Integer>( );
		genProvResTypeAgeIndCon = new HashMap<IndJointDis, Integer>( );
		provResTypeHHTypeHHCon = new HashMap<HHJointDis, Integer>( );
		provResTypeMemNumHHCon = new HashMap<HHJointDis, Integer>( );
		provResTypeElderTypeHHCon = new HashMap<HHJointDis, Integer>( );
		provEnterTypeEnCon = new HashMap<EnterpJointDis, Integer>( );
		enterTypeScaleEnCon = new HashMap<EnterpJointDis, Integer>( );
		sampleHH = new HashMap<Integer, HouseHold>( );
		sampleIndDis = new HashMap<IndJointDis, Integer>( );
		sampleHHDis = new HashMap<HHJointDis, Integer>( );
	}

	public void InitControls(String inputPath)
	{
		try
		{
			InitInd_ProvResTypeHHType(inputPath + "/t0101a.xls", EResidenceType.city);
			InitInd_ProvResTypeHHType(inputPath + "/t0101b.xls", EResidenceType.town);
			InitInd_ProvResTypeHHType(inputPath + "/t0101c.xls", EResidenceType.rural);
			InitInd_ProvResTypeAge(inputPath + "/t0107a.xls", EResidenceType.city);
			InitInd_ProvResTypeAge(inputPath + "/t0107b.xls", EResidenceType.town);
			InitInd_ProvResTypeAge(inputPath + "/t0107c.xls", EResidenceType.rural);
			InitHH_provResTypeHHType(inputPath + "/t0101a.xls", EResidenceType.city);
			InitHH_provResTypeHHType(inputPath + "/t0101b.xls", EResidenceType.town);
			InitHH_provResTypeHHType(inputPath + "/t0101c.xls", EResidenceType.rural);
			InitHH_provResTypeMemNum(inputPath + "/t0110a.xls", EResidenceType.city);
			InitHH_provResTypeMemNum(inputPath + "/t0110b.xls", EResidenceType.town);
			InitHH_provResTypeMemNum(inputPath + "/t0110c.xls", EResidenceType.rural);
			InitHH_provResTypeElderType(inputPath + "/t0504a.xls", EResidenceType.city);
			InitHH_provResTypeElderType(inputPath + "/t0504b.xls", EResidenceType.town);
			InitHH_provResTypeElderType(inputPath + "/t0504c.xls", EResidenceType.rural);
			InitEnterp_provEnterType(inputPath + "/a1a03c.xls");
			InitEnterp_enterTypeScale(inputPath + "/a1a02c.xls");
		} catch (BiffException | IOException e)
		{
			e.printStackTrace( );
		}
	}

	public void InitSamples(String inputPath)
	{
		try
		{
			ReadSample(inputPath + "/sample.csv");
			System.out.println("ReadSample complete");
			ComputeHHSampleDis( );
			System.out.println("ComputeHHSampleDis complete");
			ComputePopSampleDis( );
			System.out.println("ComputePopSampleDis complete");
		} catch (IOException e)
		{
			e.printStackTrace( );
		}
	}

	public void WritePopTensors( ) throws IOException
	{
		LinkedHashSet<EGender> genderSet = new LinkedHashSet<EGender>( );
		genderSet.add(EGender.Male);
		genderSet.add(EGender.Female);
		LinkedHashSet<EProvince> resideProvSet = new LinkedHashSet<EProvince>( );
		for (int i = 11; i <= 65; i++)
		{
			EProvince prov = EProvince.valueOf(i);
			if (prov != null)
				resideProvSet.add(prov);
		}
		LinkedHashSet<EResidenceType> resideTypeSet = new LinkedHashSet<EResidenceType>( );
		resideTypeSet.add(EResidenceType.city);
		resideTypeSet.add(EResidenceType.town);
		resideTypeSet.add(EResidenceType.rural);
		LinkedHashSet<EAgeInterval> ageInterSet = new LinkedHashSet<EAgeInterval>( );
		for (int i = 0; i <= 20; i++)
		{
			EAgeInterval ageInter = EAgeInterval.valueOf(i);
			if (ageInter != null)
				ageInterSet.add(ageInter);
		}
		LinkedHashSet<EhhType> hhTypeSet = new LinkedHashSet<EhhType>( );
		hhTypeSet.add(EhhType.family);
		hhTypeSet.add(EhhType.collectiveHH);
		File fileName = new File("Ind_GenProvResTypeHHType.txt");
		if (fileName.exists( ))
			fileName.delete( );
		fileName.createNewFile( );
		FileOutputStream outStream = new FileOutputStream(fileName);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
		out.write("tensor\n");
		out.write("4\n");
		out.write(genderSet.size( ) + " " + resideProvSet.size( ) + " " + resideTypeSet.size( ) + " " + hhTypeSet.size( ) + " \n");
		int writeSum = 0;
		for (EhhType hhType : hhTypeSet)
		{
			for (EResidenceType resideType : resideTypeSet)
			{
				for (EProvince resideProv : resideProvSet)
				{
					for (EGender gender : genderSet)
					{
						for (IndJointDis indDis : genProvResTypeHHTypeIndCon.keySet( ))
						{
							if (indDis.getGender( ) == gender && indDis.getResideProvin( ) == resideProv && indDis.getResidenceType( ) == resideType
											&& indDis.getHhType( ) == hhType)
							{
								int indNum = genProvResTypeHHTypeIndCon.get(indDis);
								out.write(String.valueOf(indNum) + "\n");
								writeSum += indNum;
								break;
							}
						}
					}
				}
			}
		}
		out.flush( );
		out.close( );
		outStream.close( );
		System.out.println("Ind_GenProvResTypeHHType: " + writeSum);
		fileName = new File("Ind_GenProvResTypeAge.txt");
		if (fileName.exists( ))
			fileName.delete( );
		fileName.createNewFile( );
		outStream = new FileOutputStream(fileName);
		out = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
		out.write("tensor\n");
		out.write("4\n");
		out.write(genderSet.size( ) + " " + resideProvSet.size( ) + " " + resideTypeSet.size( ) + " " + ageInterSet.size( ) + " \n");
		writeSum = 0;
		for (EAgeInterval ageInter : ageInterSet)
		{
			for (EResidenceType resideType : resideTypeSet)
			{
				for (EProvince resideProv : resideProvSet)
				{
					for (EGender gender : genderSet)
					{
						for (IndJointDis indDis : genProvResTypeAgeIndCon.keySet( ))
						{
							if (indDis.getGender( ) == gender && indDis.getResideProvin( ) == resideProv && indDis.getResidenceType( ) == resideType
											&& indDis.getAge( ) == ageInter)
							{
								int indNum = genProvResTypeAgeIndCon.get(indDis);
								out.write(String.valueOf(indNum) + "\n");
								writeSum += indNum;
								break;
							}
						}
					}
				}
			}
		}
		out.flush( );
		out.close( );
		outStream.close( );
		System.out.println("Ind_GenProvResTypeAge: " + writeSum);
		fileName = new File("Ind_Sample.txt");
		if (fileName.exists( ))
			fileName.delete( );
		fileName.createNewFile( );
		outStream = new FileOutputStream(fileName);
		out = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
		out.write("tensor\n");
		out.write("5\n");
		out.write(genderSet.size( ) + " " + resideProvSet.size( ) + " " + resideTypeSet.size( ) + " " + ageInterSet.size( ) + " " + hhTypeSet.size( ) + " \n");
		writeSum = 0;
		for (EhhType hhType : hhTypeSet)
		{
			for (EAgeInterval ageInter : ageInterSet)
			{
				for (EResidenceType resideType : resideTypeSet)
				{
					for (EProvince resideProv : resideProvSet)
					{
						for (EGender gender : genderSet)
						{
							boolean hasDis = false;
							for (IndJointDis indDis : sampleIndDis.keySet( ))
							{
								if (indDis.getGender( ) == gender && indDis.getResideProvin( ) == resideProv && indDis.getResidenceType( ) == resideType
												&& indDis.getAge( ) == ageInter && indDis.getHhType( ) == hhType)
								{
									int indNum = sampleIndDis.get(indDis);
									out.write(String.valueOf(indNum) + "\n");
									writeSum += indNum;
									hasDis = true;
									break;
								}
							}
							if (!hasDis)
							{
								out.write("0\n");
							}
						}
					}
				}
			}
		}
		out.flush( );
		out.close( );
		outStream.close( );
		System.out.println("Ind_Sample: " + writeSum);
	}

	public void WriteHHJointDis( ) throws IOException
	{
		LinkedHashSet<EProvince> resideProvSet = new LinkedHashSet<EProvince>( );
		for (int i = 11; i <= 65; i++)
		{
			EProvince prov = EProvince.valueOf(i);
			if (prov != null)
				resideProvSet.add(prov);
		}
		LinkedHashSet<EResidenceType> resideTypeSet = new LinkedHashSet<EResidenceType>( );
		resideTypeSet.add(EResidenceType.city);
		resideTypeSet.add(EResidenceType.town);
		resideTypeSet.add(EResidenceType.rural);
		LinkedHashSet<Integer> memNumSet = new LinkedHashSet<Integer>( );
		for (int i = 1; i <= 10; i++)
		{
			memNumSet.add(i);
		}
		CsvWriter writer = new CsvWriter("FamilyJointDis.csv");
		String[ ] famHead = { "Province", "ResidenceType", "MemberNum", "HHNum" };
		writer.writeRecord(famHead);
		int totalNum = 0;
		for (EProvince resideProv : resideProvSet)
		{
			for (EResidenceType resideType : resideTypeSet)
			{
				for (Integer memNum : memNumSet)
				{
					int hhNum = 0;
					for (HHJointDis hhJointDis : provResTypeMemNumHHCon.keySet( ))
					{
						if (hhJointDis.getResideProvin( ) == resideProv && hhJointDis.getResideType( ) == resideType && hhJointDis.getMemberNum( ) == memNum)
						{
							hhNum += provResTypeMemNumHHCon.get(hhJointDis);
						}
					}
					String[ ] items = { String.valueOf(resideProv.value( )), String.valueOf(resideType.value( )), String.valueOf(memNum),
									String.valueOf(hhNum) };
					writer.writeRecord(items);
					totalNum += hhNum;
				}
			}
		}
		writer.close( );
		System.out.println("total family number: " + totalNum);
		writer = new CsvWriter("ColHHJointDis.csv");
		String[ ] colHHHead = { "Province", "ResidenceType", "HHNum" };
		writer.writeRecord(colHHHead);
		totalNum = 0;
		for (EProvince resideProv : resideProvSet)
		{
			for (EResidenceType resideType : resideTypeSet)
			{
				int hhNum = 0;
				for (HHJointDis hhJointDis : provResTypeHHTypeHHCon.keySet( ))
				{
					if (hhJointDis.getResideProvin( ) == resideProv && hhJointDis.getResideType( ) == resideType
									&& hhJointDis.getHouseHoldType( ) == EhhType.collectiveHH)
					{
						hhNum += provResTypeHHTypeHHCon.get(hhJointDis);
					}
				}
				String[ ] items = { String.valueOf(resideProv.value( )), String.valueOf(resideType.value( )), String.valueOf(hhNum) };
				writer.writeRecord(items);
				totalNum += hhNum;
			}
		}
		writer.close( );
		System.out.println("total collective household number: " + totalNum);
	}

	public void ComputeEnterJointDis( ) throws IOException
	{
		LinkedHashSet<EProvince> resideProvSet = new LinkedHashSet<EProvince>( );
		for (int i = 11; i <= 65; i++)
		{
			EProvince prov = EProvince.valueOf(i);
			if (prov != null)
				resideProvSet.add(prov);
		}
		LinkedHashSet<EEnterType> enterTypeSet = new LinkedHashSet<EEnterType>( );
		enterTypeSet.add(EEnterType.corporation);
		enterTypeSet.add(EEnterType.industrialUnit);
		LinkedHashSet<EEnterScale> enterScaleSet = new LinkedHashSet<EEnterScale>( );
		for (int i = 0; i <= 9; i++)
		{
			EEnterScale enterScale = EEnterScale.valueOf(i);
			if (enterScale != null)
				enterScaleSet.add(enterScale);
		}
		Map<EnterpJointDis, Integer> enterJointDis = new HashMap<EnterpJointDis, Integer>( );
		for (EnterpJointDis provTypeDis : provEnterTypeEnCon.keySet( ))
		{
			EProvince provin = provTypeDis.getProvin( );
			EEnterType enterType = provTypeDis.getEnterType( );
			int employeeNum = provTypeDis.getEmployedNum( );
			int enterpNum = provEnterTypeEnCon.get(provTypeDis);
			Map<EEnterScale, Integer> scaleNumMap = new HashMap<EEnterScale, Integer>( );
			Map<EEnterScale, Integer> employeeNumMap = new HashMap<EEnterScale, Integer>( );
			int sumNum = 0;
			int employeeSum = 0;
			for (EnterpJointDis typeScaleDis : enterTypeScaleEnCon.keySet( ))
			{
				if (typeScaleDis.getEnterType( ) == enterType)
				{
					int scaleNum = enterTypeScaleEnCon.get(typeScaleDis);
					scaleNumMap.put(typeScaleDis.getEnterScale( ), scaleNum);
					employeeNumMap.put(typeScaleDis.getEnterScale( ), typeScaleDis.getEmployedNum( ));
					sumNum += scaleNum;
					employeeSum += typeScaleDis.getEmployedNum( );
				}
			}
			for (EEnterScale enterpScale : scaleNumMap.keySet( ))
			{
				int newEmployeeNum = (int) ((float) employeeNumMap.get(enterpScale) / employeeSum * employeeNum);
				EnterpJointDis newEnterpDis = new EnterpJointDis(provin, enterType, enterpScale, newEmployeeNum);
				int newEnterpNum = 0;
				if (sumNum > 0)
				{
					newEnterpNum = (int) ((double) (scaleNumMap.get(enterpScale)) / sumNum * enterpNum);
				}
				enterJointDis.put(newEnterpDis, newEnterpNum);
			}
		}
		CsvWriter writer = new CsvWriter("EnterpJointDis.csv");
		String[ ] contents = { "Province", "EnterpType", "EnterpScale", "EmployedNum", "EnterpNum" };
		writer.writeRecord(contents);
		int totalEmployeeNum = 0;
		int totalEnterNum = 0;
		for (EProvince province : resideProvSet)
		{
			for (EEnterType enterType : enterTypeSet)
			{
				for (EEnterScale enterScale : enterScaleSet)
				{
					int employeeNum = 0;
					int enterNum = 0;
					for (EnterpJointDis enterpDis : enterJointDis.keySet( ))
					{
						if (enterpDis.getProvin( ) == province && enterpDis.getEnterType( ) == enterType && enterpDis.getEnterScale( ) == enterScale)
						{
							employeeNum += enterpDis.getEmployedNum( );
							enterNum += enterJointDis.get(enterpDis);
						}
					}
					String[ ] items = { String.valueOf(province.value( )), String.valueOf(enterType.value( )), String.valueOf(enterScale.value( )),
									String.valueOf(employeeNum), String.valueOf(enterNum) };
					writer.writeRecord(items);
					totalEmployeeNum += employeeNum;
					totalEnterNum += enterNum;
				}
			}
		}
		writer.close( );
		System.out.println("total employee number: " + totalEmployeeNum);
		System.out.println("total enterprise number: " + totalEnterNum);
	}

	private void InitInd_ProvResTypeHHType(String fileName, EResidenceType resideType) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		// jxl提供的Workbook类
		Workbook wb = Workbook.getWorkbook(is);
		// Excel的页签数量
		Sheet sheet = wb.getSheet(0);
		// sheet.getRows()返回该页的总行数
		for (int i = 2; i < sheet.getRows( ); i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );// 居住省份
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				break;
			EProvince prov = AttributeCheck.CheckProvince(cellinfo);
			cellinfo = sheet.getCell(9, i).getContents( );// 家庭户男性人数
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int familyMaleNum = Integer.parseInt(cellinfo);
			IndJointDis newFamMaleDis = new IndJointDis(EGender.Male, prov, resideType, EAgeInterval.ZeroToFour, EhhType.family);
			genProvResTypeHHTypeIndCon.put(newFamMaleDis, familyMaleNum);
			cellinfo = sheet.getCell(10, i).getContents( );// 家庭户女性人数
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int familyFemaleNum = Integer.parseInt(cellinfo);
			IndJointDis newFamFemaleDis = new IndJointDis(EGender.Female, prov, resideType, EAgeInterval.ZeroToFour, EhhType.family);
			genProvResTypeHHTypeIndCon.put(newFamFemaleDis, familyFemaleNum);
			cellinfo = sheet.getCell(13, i).getContents( );// 集体户男性人数
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int collecMaleNum = Integer.parseInt(cellinfo);
			IndJointDis newCollectMaleDis = new IndJointDis(EGender.Male, prov, resideType, EAgeInterval.ZeroToFour, EhhType.collectiveHH);
			genProvResTypeHHTypeIndCon.put(newCollectMaleDis, collecMaleNum);
			cellinfo = sheet.getCell(14, i).getContents( );// 集体户女性人数
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int collecFemaleNum = Integer.parseInt(cellinfo);
			IndJointDis newCollectFemaleDis = new IndJointDis(EGender.Female, prov, resideType, EAgeInterval.ZeroToFour, EhhType.collectiveHH);
			genProvResTypeHHTypeIndCon.put(newCollectFemaleDis, collecFemaleNum);
		}
		is.close( );
	}

	private void InitInd_ProvResTypeAge(String fileName, EResidenceType resideType) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sheet = wb.getSheet(0);
		for (int i = 2; i < sheet.getRows( ); i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				break;
			EProvince resideProv = AttributeCheck.CheckProvince(cellinfo);
			for (int j = 3; j < sheet.getColumns( ); j++)
			{
				cellinfo = sheet.getCell(3 * j - 2, 0).getContents( );
				cellinfo = cellinfo.trim( );
				if (cellinfo == "")
					break;
				EAgeInterval ageInter = EAgeInterval.valueOf(j - 3);
				cellinfo = sheet.getCell(3 * j - 1, i).getContents( );
				cellinfo = cellinfo.trim( );
				if (cellinfo == "")
					cellinfo = "0";
				int maleNum = Integer.parseInt(cellinfo);
				if (ageInter == EAgeInterval.ZeroToFour)
				{
					cellinfo = sheet.getCell(5, i).getContents( );
					cellinfo = cellinfo.trim( );
					if (cellinfo == "")
						cellinfo = "0";
					int zeorMaleNum = Integer.parseInt(cellinfo);
					maleNum += zeorMaleNum;
				}
				IndJointDis newMaleDis = new IndJointDis(EGender.Male, resideProv, resideType, ageInter, EhhType.collectiveHH);
				genProvResTypeAgeIndCon.put(newMaleDis, maleNum);
				cellinfo = sheet.getCell(3 * j, i).getContents( );
				cellinfo = cellinfo.trim( );
				if (cellinfo == "")
					cellinfo = "0";
				int femaleNum = Integer.parseInt(cellinfo);
				if (ageInter == EAgeInterval.ZeroToFour)
				{
					cellinfo = sheet.getCell(6, i).getContents( );
					cellinfo = cellinfo.trim( );
					if (cellinfo == "")
						cellinfo = "0";
					int zeorFemaleNum = Integer.parseInt(cellinfo);
					femaleNum += zeorFemaleNum;
				}
				IndJointDis newFemaleDis = new IndJointDis(EGender.Female, resideProv, resideType, ageInter, EhhType.collectiveHH);
				genProvResTypeAgeIndCon.put(newFemaleDis, femaleNum);
			}
		}
		is.close( );
	}

	private void InitHH_provResTypeHHType(String fileName, EResidenceType resideType) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sheet = wb.getSheet(0);
		for (int i = 2; i < sheet.getRows( ); i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				break;
			EProvince prov = AttributeCheck.CheckProvince(cellinfo);
			cellinfo = sheet.getCell(2, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int familyHHNum = Integer.parseInt(cellinfo);
			HHJointDis familyHH = new HHJointDis(EhhType.family, prov, resideType, 0, 0);
			provResTypeHHTypeHHCon.put(familyHH, familyHHNum);
			cellinfo = sheet.getCell(3, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int collectHHNum = Integer.parseInt(cellinfo);
			HHJointDis collectHH = new HHJointDis(EhhType.collectiveHH, prov, resideType, 0, 0);
			provResTypeHHTypeHHCon.put(collectHH, collectHHNum);
		}
		is.close( );
	}

	private void InitHH_provResTypeMemNum(String fileName, EResidenceType resideType) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sheet = wb.getSheet(0);
		for (int i = 2; i < sheet.getRows( ); i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				break;
			EProvince prov = AttributeCheck.CheckProvince(cellinfo);
			for (int j = 2; j < sheet.getColumns( ); j++)
			{
				cellinfo = sheet.getCell(j, i).getContents( );
				cellinfo = cellinfo.trim( );
				if (cellinfo == "")
					break;
				int familyHHNum = Integer.parseInt(cellinfo);
				HHJointDis familyHH = new HHJointDis(EhhType.family, prov, resideType, j - 1, 0);
				provResTypeMemNumHHCon.put(familyHH, familyHHNum);
			}
		}
		is.close( );
	}

	private void InitHH_provResTypeElderType(String fileName, EResidenceType resideType) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sheet = wb.getSheet(0);
		for (int i = 2; i < sheet.getRows( ); i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				break;
			EProvince prov = AttributeCheck.CheckProvince(cellinfo);
			cellinfo = sheet.getCell(2, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int oneElderNum = Integer.parseInt(cellinfo);
			HHJointDis hhWithOneElder = new HHJointDis(EhhType.family, prov, resideType, 0, 1);
			provResTypeElderTypeHHCon.put(hhWithOneElder, oneElderNum);
			cellinfo = sheet.getCell(6, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int twoElderNum = Integer.parseInt(cellinfo);
			HHJointDis hhWithTwoElder = new HHJointDis(EhhType.family, prov, resideType, 0, 2);
			provResTypeElderTypeHHCon.put(hhWithTwoElder, twoElderNum);
			cellinfo = sheet.getCell(10, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int threeElderNum = Integer.parseInt(cellinfo);
			HHJointDis hhWithThreeElder = new HHJointDis(EhhType.family, prov, resideType, 0, 3);
			provResTypeElderTypeHHCon.put(hhWithThreeElder, threeElderNum);
		}
		is.close( );
	}

	private void InitEnterp_provEnterType(String fileName) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sheet = wb.getSheet(0);
		for (int i = 2; i < sheet.getRows( ); i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );
			cellinfo = cellinfo.trim( );
			cellinfo = cellinfo.replace(" ", "");
			if (cellinfo == "")
				break;
			EProvince prov = AttributeCheck.CheckProvince(cellinfo);
			cellinfo = sheet.getCell(1, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int corporationNum = Integer.parseInt(cellinfo);
			cellinfo = sheet.getCell(4, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int corpEmployedNum = new Double(Double.parseDouble(cellinfo) * 10000).intValue( );
			EnterpJointDis corporation = new EnterpJointDis(prov, EEnterType.corporation, EEnterScale.TenThousandAndAbove, corpEmployedNum);
			provEnterTypeEnCon.put(corporation, corporationNum);
			cellinfo = sheet.getCell(6, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int industrialUnitNum = Integer.parseInt(cellinfo);
			cellinfo = sheet.getCell(8, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int industEmployedNum = new Double(Double.parseDouble(cellinfo) * 10000).intValue( );
			EnterpJointDis industrialUnit = new EnterpJointDis(prov, EEnterType.industrialUnit, EEnterScale.TenThousandAndAbove, industEmployedNum);
			provEnterTypeEnCon.put(industrialUnit, industrialUnitNum);
		}
		is.close( );
	}

	private void InitEnterp_enterTypeScale(String fileName) throws BiffException, IOException
	{
		String cellinfo = "";
		File inputFile = new File(fileName);
		InputStream is = new FileInputStream(inputFile.getAbsolutePath( ));
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sheet = wb.getSheet(0);
		for (int i = 9; i <= 18; i++)
		{
			cellinfo = sheet.getCell(0, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				break;
			EEnterScale enterpScale = AttributeCheck.CheckEnterScale(cellinfo);
			cellinfo = sheet.getCell(1, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int corpNum = Integer.parseInt(cellinfo);
			cellinfo = sheet.getCell(4, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int corpEmployedNum = new Double(Double.parseDouble(cellinfo) * 10000).intValue( );
			EnterpJointDis corporation = new EnterpJointDis(EProvince.Other, EEnterType.corporation, enterpScale, corpEmployedNum);
			enterTypeScaleEnCon.put(corporation, corpNum);
			cellinfo = sheet.getCell(6, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int industrialUnitNum = Integer.parseInt(cellinfo);
			cellinfo = sheet.getCell(8, i).getContents( );
			cellinfo = cellinfo.trim( );
			if (cellinfo == "")
				cellinfo = "0";
			int industEmployedNum = new Double(Double.parseDouble(cellinfo) * 10000).intValue( );
			EnterpJointDis industrialUnit = new EnterpJointDis(EProvince.Other, EEnterType.industrialUnit, enterpScale, industEmployedNum);
			enterTypeScaleEnCon.put(industrialUnit, industrialUnitNum);
		}
		is.close( );
	}

	private void ReadSample(String fileName) throws IOException
	{
		// read disaggregate sample to initialize sampleHH
	}

	// compute HH distribution
	private void ComputeHHSampleDis( )
	{
		for (HouseHold parHH : sampleHH.values( ))
		{
			EProvince resideProvin = parHH.getResideProvin( );
			EResidenceType resideType = parHH.getResideType( );
			EhhType hhType = parHH.getHouseHoldType( );
			int memNum = parHH.getMemberList( ).size( );
			HHJointDis hhKind = null;
			for (HHJointDis hhDis : sampleHHDis.keySet( ))
			{
				if (hhDis.getResideProvin( ) == resideProvin && hhDis.getResideType( ) == resideType && hhDis.getHouseHoldType( ) == hhType)
				{
					if (hhType == EhhType.family)
					{
						if (hhDis.getMemberNum( ) == memNum)
						{
							hhKind = hhDis;
							break;
						}
					} else
					{
						hhKind = hhDis;
						break;
					}
				}
			}
			if (hhKind != null)
			{
				sampleHHDis.put(hhKind, sampleHHDis.get(hhKind) + 1);
			} else
			{
				hhKind = new HHJointDis(hhType, resideProvin, resideType, memNum, 0);
				sampleHHDis.put(hhKind, 1);
			}
		}
	}

	// compute Ind distribution
	private void ComputePopSampleDis( )
	{
		for (HouseHold parHH : sampleHH.values( ))
		{
			EhhType hhType = parHH.getHouseHoldType( );
			Individual hhHead = parHH.getHhHead( );
			EGender headGender = hhHead.getGender( );
			EProvince headProvin = hhHead.getProvin( );
			EResidenceType headResideType = hhHead.getResidenceType( );
			EAgeInterval headAge = hhHead.getAgeInter( );
			IndJointDis indType = null;
			for (IndJointDis indDis : sampleIndDis.keySet( ))
			{
				if (indDis.getGender( ) == headGender && indDis.getResideProvin( ) == headProvin && indDis.getResidenceType( ) == headResideType
								&& indDis.getAge( ) == headAge && indDis.getHhType( ) == hhType)
				{
					indType = indDis;
					break;
				}
			}
			if (indType != null)
			{
				sampleIndDis.put(indType, sampleIndDis.get(indType) + 1);
			} else
			{
				indType = new IndJointDis(headGender, headProvin, headResideType, headAge, hhType);
				sampleIndDis.put(indType, 1);
			}
			// 统计成员
			for (Individual member : parHH.getMemberList( ))
			{
				EGender gender = member.getGender( );
				EProvince resideProvin = member.getProvin( );
				EResidenceType residenceType = member.getResidenceType( );
				EAgeInterval ageInter = member.getAgeInter( );
				indType = null;
				for (IndJointDis indDis : sampleIndDis.keySet( ))
				{
					if (indDis.getGender( ) == gender && indDis.getResideProvin( ) == resideProvin && indDis.getResidenceType( ) == residenceType
									&& indDis.getAge( ) == ageInter && indDis.getHhType( ) == hhType)
					{
						indType = indDis;
						break;
					}
				}
				if (indType != null)
				{
					sampleIndDis.put(indType, sampleIndDis.get(indType) + 1);
				} else
				{
					indType = new IndJointDis(gender, resideProvin, residenceType, ageInter, hhType);
					sampleIndDis.put(indType, 1);
				}
			}
		}
	}

	public Map<IndJointDis, Integer> getGenProvResTypeHHTypeIndCon( )
	{
		return genProvResTypeHHTypeIndCon;
	}

	public Map<IndJointDis, Integer> getGenProvResTypeAgeIndCon( )
	{
		return genProvResTypeAgeIndCon;
	}

	public Map<HHJointDis, Integer> getProvResTypeHHTypeHHCon( )
	{
		return provResTypeHHTypeHHCon;
	}

	public Map<HHJointDis, Integer> getProvResTypeMemNumHHCon( )
	{
		return provResTypeMemNumHHCon;
	}

	public Map<HHJointDis, Integer> getProvResTypeElderTypeHHCon( )
	{
		return provResTypeElderTypeHHCon;
	}

	public Map<EnterpJointDis, Integer> getProvEnterTypeEnCon( )
	{
		return provEnterTypeEnCon;
	}

	public Map<EnterpJointDis, Integer> getEnterTypeScaleEnCon( )
	{
		return enterTypeScaleEnCon;
	}

	public Map<Integer, HouseHold> getSampleHH( )
	{
		return sampleHH;
	}

	public Map<HHJointDis, Integer> getSampleHHDis( )
	{
		return sampleHHDis;
	}
}
