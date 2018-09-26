package TensorPopSyn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import com.csvreader.CsvReader;

import DisAndEntity.EnterpJointDis;
import DisAndEntity.HHJointDis;
import DisAndEntity.IndJointDis;
import EnumType.EAgeInterval;
import EnumType.EEnterScale;
import EnumType.EEnterType;
import EnumType.EGender;
import EnumType.EProvince;
import EnumType.EResidenceType;
import EnumType.EhhType;

public class PopulationAssignment
{
	private Map<IndJointDis, Integer> syntheticPopulation;
	private Map<HHJointDis, Integer> syntheticFamily;
	private Map<HHJointDis, Integer> syntheticColHH;
	private Map<EnterpJointDis, Integer> syntheticEnterprise;
	// property sets
	private LinkedHashSet<EGender> genderSet;
	private LinkedHashSet<EProvince> resideProvSet;
	private LinkedHashSet<EResidenceType> resideTypeSet;
	private LinkedHashSet<EAgeInterval> ageInterSet;
	private LinkedHashSet<EhhType> hhTypeSet;

	public PopulationAssignment( )
	{
		syntheticPopulation = new HashMap<IndJointDis, Integer>( );
		syntheticFamily = new HashMap<HHJointDis, Integer>( );
		syntheticColHH = new HashMap<HHJointDis, Integer>( );
		syntheticEnterprise = new HashMap<EnterpJointDis, Integer>( );
		genderSet = new LinkedHashSet<EGender>( );
		genderSet.add(EGender.Male);
		genderSet.add(EGender.Female);
		resideProvSet = new LinkedHashSet<EProvince>( );
		for (int i = 11; i <= 65; i++)
		{
			EProvince prov = EProvince.valueOf(i);
			if (prov != null)
				resideProvSet.add(prov);
		}
		resideTypeSet = new LinkedHashSet<EResidenceType>( );
		resideTypeSet.add(EResidenceType.city);
		resideTypeSet.add(EResidenceType.town);
		resideTypeSet.add(EResidenceType.rural);
		ageInterSet = new LinkedHashSet<EAgeInterval>( );
		for (int i = 0; i <= 20; i++)
		{
			EAgeInterval ageInter = EAgeInterval.valueOf(i);
			if (ageInter != null)
				ageInterSet.add(ageInter);
		}
		hhTypeSet = new LinkedHashSet<EhhType>( );
		hhTypeSet.add(EhhType.family);
		hhTypeSet.add(EhhType.collectiveHH);
	}

	public void Assignment( )
	{
		try
		{
			ReadSynPop("SynPopJointDis.csv");
			ReadSynFamily("SynFamilyDis.csv");
			ReadSynColHH("SynColHHDis.csv");
			ReadSynEnterprise("EnterpJointDis.csv");
			for (Iterator<Map.Entry<IndJointDis, Integer>> iterator = syntheticPopulation.entrySet( ).iterator( ); iterator.hasNext( );)
			{
				Map.Entry<IndJointDis, Integer> item = iterator.next( );
				if (item.getValue( ) == 0)
					iterator.remove( );
			}
			for (Iterator<Map.Entry<HHJointDis, Integer>> iterator = syntheticFamily.entrySet( ).iterator( ); iterator.hasNext( );)
			{
				Map.Entry<HHJointDis, Integer> item = iterator.next( );
				if (item.getValue( ) == 0)
					iterator.remove( );
			}
			for (Iterator<Map.Entry<HHJointDis, Integer>> iterator = syntheticColHH.entrySet( ).iterator( ); iterator.hasNext( );)
			{
				Map.Entry<HHJointDis, Integer> item = iterator.next( );
				if (item.getValue( ) == 0)
					iterator.remove( );
			}
			for (Iterator<Map.Entry<EnterpJointDis, Integer>> iterator = syntheticEnterprise.entrySet( ).iterator( ); iterator.hasNext( );)
			{
				Map.Entry<EnterpJointDis, Integer> item = iterator.next( );
				if (item.getValue( ) == 0)
					iterator.remove( );
			}
			File popDir = new File("Population");
			if (!popDir.exists( ) && !popDir.isDirectory( ))
				popDir.mkdirs( );
			File hhDir = new File("Household");
			if (!hhDir.exists( ) && !hhDir.isDirectory( ))
				hhDir.mkdirs( );
			File enterDir = new File("Enterprise");
			if (!enterDir.exists( ) && !enterDir.isDirectory( ))
				enterDir.mkdirs( );
			// multiple thread. Each thread corresponds to a province
			ProvincePopAssign[ ] assignThreads = new ProvincePopAssign[resideProvSet.size( )];
			int threadID = 0;
			for (EProvince resideProv : resideProvSet)
			{
				assignThreads[threadID] = new ProvincePopAssign(syntheticPopulation, syntheticFamily, syntheticColHH, syntheticEnterprise, resideProv,
								"RunTime.txt");
				assignThreads[threadID].start( );
				threadID++;
			}
			for (int i = 0; i < assignThreads.length; i++)
			{
				assignThreads[i].join( );
			}
		} catch (IOException | InterruptedException e)
		{
			e.printStackTrace( );
		}
	}

	private void ReadSynPop(String fileName) throws IOException
	{
		File inFile = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		CsvReader creader = new CsvReader(reader, ',');
		String line = "";
		if (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
		}
		while (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
			String[ ] items = line.split(",");
			EGender gender = EGender.valueOf(Integer.parseInt(items[0]));
			EProvince resideProv = EProvince.valueOf(Integer.parseInt(items[1]));
			EResidenceType residenceType = EResidenceType.valueOf(Integer.parseInt(items[2]));
			EAgeInterval ageInter = EAgeInterval.valueOf(Integer.parseInt(items[3]));
			EhhType hhType = EhhType.valueOf(Integer.parseInt(items[4]));
			int indNum = Math.abs(Integer.parseInt(items[5]));
			IndJointDis indDis = new IndJointDis(gender, resideProv, residenceType, ageInter, hhType);
			syntheticPopulation.put(indDis, indNum);
		}
		creader.close( );
	}

	private void ReadSynFamily(String fileName) throws IOException
	{
		File inFile = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		CsvReader creader = new CsvReader(reader, ',');
		String line = "";
		if (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
		}
		while (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
			String[ ] items = line.split(",");
			EProvince resideProv = EProvince.valueOf(Integer.parseInt(items[1]));
			EResidenceType residenceType = EResidenceType.valueOf(Integer.parseInt(items[2]));
			int memNum = Integer.parseInt(items[3]);
			int elderNum = Integer.parseInt(items[4]);
			int hhNum = Integer.parseInt(items[5]);
			HHJointDis hhDis = new HHJointDis(EhhType.family, resideProv, residenceType, memNum, elderNum);
			syntheticFamily.put(hhDis, hhNum);
		}
		creader.close( );
	}

	private void ReadSynColHH(String fileName) throws IOException
	{
		File inFile = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		CsvReader creader = new CsvReader(reader, ',');
		String line = "";
		if (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
		}
		while (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
			String[ ] items = line.split(",");
			EProvince resideProv = EProvince.valueOf(Integer.parseInt(items[1]));
			EResidenceType residenceType = EResidenceType.valueOf(Integer.parseInt(items[2]));
			int hhNum = Integer.parseInt(items[5]);
			HHJointDis hhDis = new HHJointDis(EhhType.collectiveHH, resideProv, residenceType, -1, -1);
			syntheticColHH.put(hhDis, hhNum);
		}
		creader.close( );
	}

	private void ReadSynEnterprise(String fileName) throws IOException
	{
		File inFile = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		CsvReader creader = new CsvReader(reader, ',');
		String line = "";
		if (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
		}
		while (creader.readRecord( ))
		{
			line = creader.getRawRecord( );
			String[ ] items = line.split(",");
			EProvince resideProv = EProvince.valueOf(Integer.parseInt(items[0]));
			EEnterType enterType = EEnterType.valueOf(Integer.parseInt(items[1]));
			EEnterScale enterScale = EEnterScale.valueOf(Integer.parseInt(items[2]));
			int employeeNum = Integer.parseInt(items[3]);
			int enterpNum = Integer.parseInt(items[4]);
			EnterpJointDis enterpDis = new EnterpJointDis(resideProv, enterType, enterScale, employeeNum);
			syntheticEnterprise.put(enterpDis, enterpNum);
		}
		creader.close( );
	}
}
