package TensorPopSyn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.csvreader.CsvWriter;

import DisAndEntity.EnterpJointDis;
import DisAndEntity.Enterprise;
import DisAndEntity.HHJointDis;
import DisAndEntity.HouseHold;
import DisAndEntity.IndJointDis;
import DisAndEntity.Individual;
import EnumType.EGender;
import EnumType.EProvince;
import EnumType.EResidenceType;

public class ProvincePopAssign extends Thread
{
	private Map<IndJointDis, Integer> syntheticPopulation;
	private Map<HHJointDis, Integer> syntheticFamily;
	private Map<HHJointDis, Integer> syntheticColHH;
	private Map<EnterpJointDis, Integer> syntheticEnterprise;
	private Random rand;
	private EProvince resideProv;
	private String runTimeFileName;

	public ProvincePopAssign(Map<IndJointDis, Integer> syntheticPopulation, Map<HHJointDis, Integer> syntheticFamily, Map<HHJointDis, Integer> syntheticColHH,
					Map<EnterpJointDis, Integer> syntheticEnterprise, EProvince resideProv, String runTimeFileName)
	{
		super( );
		this.syntheticPopulation = syntheticPopulation;
		this.syntheticFamily = syntheticFamily;
		this.syntheticColHH = syntheticColHH;
		this.syntheticEnterprise = syntheticEnterprise;
		this.resideProv = resideProv;
		this.runTimeFileName = runTimeFileName;
		rand = new Random( );
	}

	@Override
	public void run( )
	{
		try
		{
			long startTime = System.currentTimeMillis( );
			PopAssign("Population", "Household", "Enterprise");
			long endTime = System.currentTimeMillis( );
			long seconds = (long) (((double) (endTime - startTime)) / 1000);
			File runTimeFile = new File(runTimeFileName);
			FileWriter fw = new FileWriter(runTimeFile, true);
			PrintWriter pw = new PrintWriter(fw);
			String line = resideProv.toString( ) + "(" + resideProv.value( ) + "):" + seconds + " s";
			pw.println(line);// append the run time file
			pw.flush( );
			fw.flush( );
			pw.close( );
			fw.close( );
			System.out.println(line);
		} catch (IOException e)
		{
			e.printStackTrace( );
		}
	}

	private void PopAssign(String popFileDir, String hhFileDir, String enterpFileDir) throws IOException
	{
		// Households:
		ArrayList<HouseHold> cityFamilyList = new ArrayList<HouseHold>( );// family
		ArrayList<HouseHold> townFamilyList = new ArrayList<HouseHold>( );
		ArrayList<HouseHold> ruralFamilyList = new ArrayList<HouseHold>( );
		ArrayList<HouseHold> cityColHHList = new ArrayList<HouseHold>( );// collectiveHH
		ArrayList<HouseHold> townColHHList = new ArrayList<HouseHold>( );
		ArrayList<HouseHold> ruralColHHList = new ArrayList<HouseHold>( );
		File hhFile = new File(hhFileDir + "/SynHH_" + resideProv.value( ) + ".csv");
		if (hhFile.exists( ))
			hhFile.delete( );
		OutputStreamWriter hhOutStream = new OutputStreamWriter(new FileOutputStream(hhFile), "utf-8");
		BufferedWriter hhBufferWriter = new BufferedWriter(hhOutStream);
		CsvWriter hhWriter = new CsvWriter(hhBufferWriter, ',');
		String[ ] hhWriteLine = { "HouseholdID", "ResideProvince", "ResidenceType", "HouseholdType", "MemberNum", "ElderNum" };
		hhWriter.writeRecord(hhWriteLine);
		// family distribution
		Map<HHJointDis, Integer> hhFamDisMap = new HashMap<HHJointDis, Integer>( );
		for (HHJointDis hhDis : syntheticFamily.keySet( ))
		{
			if (hhDis.getResideProvin( ) == resideProv)
				hhFamDisMap.put(hhDis, syntheticFamily.get(hhDis));
		}
		// colHH distribution
		Map<HHJointDis, Integer> hhColHHDisMap = new HashMap<HHJointDis, Integer>( );
		for (HHJointDis hhDis : syntheticColHH.keySet( ))
		{
			if (hhDis.getResideProvin( ) == resideProv)
				hhColHHDisMap.put(hhDis, syntheticColHH.get(hhDis));
		}
		int householdID = 1;
		// generate family candidates
		for (Iterator<Map.Entry<HHJointDis, Integer>> iterator = hhFamDisMap.entrySet( ).iterator( ); iterator.hasNext( );)
		{
			HHJointDis hhDis = iterator.next( ).getKey( );
			HouseHold newFamily = new HouseHold(hhDis.getHouseHoldType( ), hhDis.getResideProvin( ), hhDis.getResideType( ));
			newFamily.setHouseHoldID(householdID);
			newFamily.setMemberNum(hhDis.getMemberNum( ));
			newFamily.setElderNum(hhDis.getElderNum( ));
			householdID++;
			if (newFamily.getResideType( ) == EResidenceType.city)
				cityFamilyList.add(newFamily);
			else if (newFamily.getResideType( ) == EResidenceType.town)
				townFamilyList.add(newFamily);
			else
				ruralFamilyList.add(newFamily);
			int restNum = hhFamDisMap.get(hhDis) - 1;
			if (restNum <= 0)
				iterator.remove( );
			else
				hhFamDisMap.put(hhDis, restNum);
		}
		// generate all colHH candidates
		for (Iterator<Map.Entry<HHJointDis, Integer>> iterator = hhColHHDisMap.entrySet( ).iterator( ); iterator.hasNext( );)
		{
			HHJointDis hhDis = iterator.next( ).getKey( );
			int hhNum = hhColHHDisMap.get(hhDis);
			for (int i = 0; i < hhNum; i++)
			{
				HouseHold newColHH = new HouseHold(hhDis.getHouseHoldType( ), hhDis.getResideProvin( ), hhDis.getResideType( ));
				newColHH.setHouseHoldID(householdID);
				householdID++;
				if (newColHH.getResideType( ) == EResidenceType.city)
					cityColHHList.add(newColHH);
				else if (newColHH.getResideType( ) == EResidenceType.town)
					townColHHList.add(newColHH);
				else
					ruralColHHList.add(newColHH);
			}
			iterator.remove( );
		}
		// Enterprises:
		// enterprise list
		ArrayList<Enterprise> enterpriseList = new ArrayList<Enterprise>( );
		// enterprise distribution
		Map<EnterpJointDis, Integer> enterpDisMap = new HashMap<EnterpJointDis, Integer>( );
		File enterFile = new File(enterpFileDir + "/SynEnter_" + resideProv.value( ) + ".csv");
		if (enterFile.exists( ))
			enterFile.delete( );
		OutputStreamWriter enterOutStream = new OutputStreamWriter(new FileOutputStream(enterFile), "utf-8");
		BufferedWriter enterBufferWriter = new BufferedWriter(enterOutStream);
		CsvWriter enterpWriter = new CsvWriter(enterBufferWriter, ',');
		String[ ] enterWriteLine = { "EnterpriseID", "ResideProvince", "EnterpriseType", "EnterpriseScale", "EmployeeNum" };
		enterpWriter.writeRecord(enterWriteLine);
		for (EnterpJointDis enterpDis : syntheticEnterprise.keySet( ))
		{
			if (enterpDis.getProvin( ) == resideProv)
			{
				int enterNum = syntheticEnterprise.get(enterpDis);
				EnterpJointDis newEnterDis = new EnterpJointDis(enterpDis.getProvin( ), enterpDis.getEnterType( ), enterpDis.getEnterScale( ),
								(int) Math.round((float) (enterpDis.getEmployedNum( )) / enterNum));
				enterpDisMap.put(newEnterDis, enterNum);
			}
		}
		int enterID = 1;
		// generate enterprise candidates
		for (Iterator<Map.Entry<EnterpJointDis, Integer>> iterator = enterpDisMap.entrySet( ).iterator( ); iterator.hasNext( );)
		{
			EnterpJointDis enterpDis = iterator.next( ).getKey( );
			Enterprise newEnter = new Enterprise(enterpDis.getProvin( ), enterpDis.getEnterType( ), enterpDis.getEnterScale( ), enterpDis.getEmployedNum( ));
			newEnter.setEnterID(enterID);
			enterID++;
			enterpriseList.add(newEnter);
			int restNum = enterpDisMap.get(enterpDis) - 1;
			if (restNum <= 0)
				iterator.remove( );
			else
				enterpDisMap.put(enterpDis, restNum);
		}
		// Population:
		ArrayList<IndJointDis> indDisList = new ArrayList<IndJointDis>( );
		Map<IndJointDis, Integer> indDisMap = new HashMap<IndJointDis, Integer>( );
		for (IndJointDis indDis : syntheticPopulation.keySet( ))
		{
			if (indDis.getResideProvin( ) == resideProv)
			{
				indDisMap.put(indDis, syntheticPopulation.get(indDis));
				indDisList.add(indDis);
			}
		}
		String[ ] popWriteLine = { "IndividualID", "Gender", "ResideProvince", "ResidenceType", "AgeInterval", "HouseholdType", "HouseholdID", "EnterpriseID" };
		File cityPopFile = new File(popFileDir + "/SynPop_" + resideProv.value( ) + "_city.csv");
		if (cityPopFile.exists( ))
			cityPopFile.delete( );
		OutputStreamWriter cityPopOutStream = new OutputStreamWriter(new FileOutputStream(cityPopFile), "utf-8");
		BufferedWriter cityPopBufferWriter = new BufferedWriter(cityPopOutStream);
		CsvWriter cityPopWriter = new CsvWriter(cityPopBufferWriter, ',');
		cityPopWriter.writeRecord(popWriteLine);
		File townPopFile = new File(popFileDir + "/SynPop_" + resideProv.value( ) + "_town.csv");
		if (townPopFile.exists( ))
			townPopFile.delete( );
		OutputStreamWriter townPopOutStream = new OutputStreamWriter(new FileOutputStream(townPopFile), "utf-8");
		BufferedWriter townPopBufferWriter = new BufferedWriter(townPopOutStream);
		CsvWriter townPopWriter = new CsvWriter(townPopBufferWriter, ',');
		townPopWriter.writeRecord(popWriteLine);
		File ruralPopFile = new File(popFileDir + "/SynPop_" + resideProv.value( ) + "_rural.csv");
		if (ruralPopFile.exists( ))
			ruralPopFile.delete( );
		OutputStreamWriter ruralPopOutStream = new OutputStreamWriter(new FileOutputStream(ruralPopFile), "utf-8");
		BufferedWriter ruralPopBufferWriter = new BufferedWriter(ruralPopOutStream);
		CsvWriter ruralPopWriter = new CsvWriter(ruralPopBufferWriter, ',');
		ruralPopWriter.writeRecord(popWriteLine);
		int agentID = 1;
		// 1:elder, 2:male, 3:female, 4:children
		// <indType(1,2,3,4),<FamIndex>>:
		Map<Integer, HashSet<Integer>> cityPopCand = new HashMap<Integer, HashSet<Integer>>( );
		Map<Integer, HashSet<Integer>> townPopCand = new HashMap<Integer, HashSet<Integer>>( );
		Map<Integer, HashSet<Integer>> ruralPopCand = new HashMap<Integer, HashSet<Integer>>( );
		for (int j = 1; j <= 4; j++)
		{
			cityPopCand.put(j, new HashSet<Integer>( ));
			townPopCand.put(j, new HashSet<Integer>( ));
			ruralPopCand.put(j, new HashSet<Integer>( ));
		}
		while (!indDisMap.isEmpty( ))
		{
			// compute eligible individual type
			if (cityFamilyList.size( ) > 0)
				ComputeEligiblePop(cityPopCand, cityFamilyList);
			if (townFamilyList.size( ) > 0)
				ComputeEligiblePop(townPopCand, townFamilyList);
			if (ruralFamilyList.size( ) > 0)
				ComputeEligiblePop(ruralPopCand, ruralFamilyList);
			// randomly generate an individual
			ArrayList<Float> indDisDrawProb = new ArrayList<Float>( );
			float indSum = 0;
			for (int i = 0; i < indDisList.size( ); i++)
			{
				IndJointDis indDis = indDisList.get(i);
				// impose heuristics for eligible individual
				if (cityFamilyList.size( ) > 0)
				{
					boolean isCityElder = indDis.getResidenceType( ) == EResidenceType.city && indDis.getAge( ).value( ) >= 13;
					if (isCityElder && cityPopCand.get(1).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isCityMale = indDis.getResidenceType( ) == EResidenceType.city && indDis.getGender( ) == EGender.Male
									&& indDis.getAge( ).value( ) >= 5 && indDis.getAge( ).value( ) < 13;
					if (isCityMale && cityPopCand.get(2).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isCityFemale = indDis.getResidenceType( ) == EResidenceType.city && indDis.getGender( ) == EGender.Female
									&& indDis.getAge( ).value( ) >= 5 && indDis.getAge( ).value( ) < 13;
					if (isCityFemale && cityPopCand.get(3).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isCityChild = indDis.getResidenceType( ) == EResidenceType.city && indDis.getAge( ).value( ) < 5;
					if (isCityChild && cityPopCand.get(4).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
				}
				if (townFamilyList.size( ) > 0)
				{
					boolean isTownElder = indDis.getResidenceType( ) == EResidenceType.town && indDis.getAge( ).value( ) >= 13;
					if (isTownElder && townPopCand.get(1).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isTownMale = indDis.getResidenceType( ) == EResidenceType.town && indDis.getGender( ) == EGender.Male
									&& indDis.getAge( ).value( ) >= 5 && indDis.getAge( ).value( ) < 13;
					if (isTownMale && townPopCand.get(2).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isTownFemale = indDis.getResidenceType( ) == EResidenceType.town && indDis.getGender( ) == EGender.Female
									&& indDis.getAge( ).value( ) >= 5 && indDis.getAge( ).value( ) < 13;
					if (isTownFemale && townPopCand.get(3).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isTownChild = indDis.getResidenceType( ) == EResidenceType.town && indDis.getAge( ).value( ) < 5;
					if (isTownChild && townPopCand.get(4).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
				}
				if (ruralFamilyList.size( ) > 0)
				{
					boolean isRuralElder = indDis.getResidenceType( ) == EResidenceType.rural && indDis.getAge( ).value( ) >= 13;
					if (isRuralElder && ruralPopCand.get(1).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isRuralMale = indDis.getResidenceType( ) == EResidenceType.rural && indDis.getGender( ) == EGender.Male
									&& indDis.getAge( ).value( ) >= 5 && indDis.getAge( ).value( ) < 13;
					if (isRuralMale && ruralPopCand.get(2).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isRuralFemale = indDis.getResidenceType( ) == EResidenceType.rural && indDis.getGender( ) == EGender.Female
									&& indDis.getAge( ).value( ) >= 5 && indDis.getAge( ).value( ) < 13;
					if (isRuralFemale && ruralPopCand.get(3).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
					boolean isRuralChild = indDis.getResidenceType( ) == EResidenceType.rural && indDis.getAge( ).value( ) < 5;
					if (isRuralChild && ruralPopCand.get(4).isEmpty( ))
					{
						indDisDrawProb.add((float) 0.0);
						continue;
					}
				}
				int indNum = 0;
				for (IndJointDis parIndDis : indDisMap.keySet( ))
				{
					if (parIndDis.getGender( ) == indDis.getGender( ) && parIndDis.getResideProvin( ) == indDis.getResideProvin( )
									&& parIndDis.getResidenceType( ) == indDis.getResidenceType( ) && parIndDis.getAge( ) == indDis.getAge( )
									&& parIndDis.getHhType( ) == indDis.getHhType( ))
					{
						indNum += indDisMap.get(parIndDis);
					}
				}
				indDisDrawProb.add(Float.valueOf(indNum));
				indSum += indNum;
			}
			if (indSum <= 0)
			{
				System.out.println("No qualified person for family candidates: city fam = " + cityFamilyList.size( ) + "; town fam = " + townFamilyList.size( )
								+ "; rural fam = " + ruralFamilyList.size( ));
				// when the program reaches here, it means there is no qualified
				// person for family candidates
				// clear family lists
				for (int i = 0; i < cityFamilyList.size( ); i++)
					WriteFamily(hhWriter, cityFamilyList.get(i));
				for (int i = 0; i < townFamilyList.size( ); i++)
					WriteFamily(hhWriter, townFamilyList.get(i));
				for (int i = 0; i < ruralFamilyList.size( ); i++)
					WriteFamily(hhWriter, ruralFamilyList.get(i));
				cityFamilyList.clear( );
				townFamilyList.clear( );
				ruralFamilyList.clear( );
				cityPopCand.clear( );
				townPopCand.clear( );
				ruralPopCand.clear( );
				continue;
			}
			if (indDisDrawProb.size( ) > 1)
			{
				float sumProb = 0;
				for (int i = 0; i < indDisDrawProb.size( ) - 1; i++)
				{
					float assignProb = indDisDrawProb.get(i) / indSum;
					indDisDrawProb.set(i, assignProb);
					sumProb += assignProb;
				}
				indDisDrawProb.set(indDisDrawProb.size( ) - 1, 1 - sumProb);
			} else if (indDisDrawProb.size( ) == 1)
			{
				indDisDrawProb.set(0, (float) 1);
			} else
			{
				System.out.println("indDisDrawProb.size( ) = " + indDisDrawProb.size( ));
			}
			Individual agent = null;
			float seed = rand.nextFloat( );
			indSum = 0;
			int removeIndex = -1;
			for (int i = 0; i < indDisDrawProb.size( ); i++)
			{
				indSum += indDisDrawProb.get(i);
				if (seed <= indSum)
				{
					IndJointDis indDis = indDisList.get(i);
					agent = new Individual(indDis.getGender( ), indDis.getResideProvin( ), indDis.getResidenceType( ), indDis.getAge( ));
					agent.setAgentID(agentID);
					agentID++;
					int restNum = 0;
					if (indDisMap.containsKey(indDis))
						restNum = indDisMap.get(indDis) - 1;
					if (restNum <= 0)
					{
						indDisMap.remove(indDis);
						removeIndex = i;
					} else
						indDisMap.put(indDis, restNum);
					break;
				}
			}
			if (removeIndex != -1)
				indDisList.remove(removeIndex);
			if (agent == null)
			{
				System.out.println("agent == null!! seed = " + seed + "; indSum = " + indSum);
				continue;
			}
			// assign to household:
			if (agent.getResidenceType( ) == EResidenceType.city)
			{
				if (!cityFamilyList.isEmpty( ))
				{
					// compute assign probability
					ArrayList<Float> assignProb = new ArrayList<Float>( );
					float famSum = 0;
					for (int j = 0; j < cityFamilyList.size( ); j++)
					{
						HouseHold parFam = cityFamilyList.get(j);
						// heuristics
						if (!cityPopCand.get(1).contains(j) && agent.getAgeInter( ).value( ) >= 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!cityPopCand.get(2).contains(j) && agent.getGender( ) == EGender.Male && agent.getAgeInter( ).value( ) >= 5
										&& agent.getAgeInter( ).value( ) < 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!cityPopCand.get(3).contains(j) && agent.getGender( ) == EGender.Female && agent.getAgeInter( ).value( ) >= 5
										&& agent.getAgeInter( ).value( ) < 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!cityPopCand.get(4).contains(j) && agent.getAgeInter( ).value( ) < 5)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						int indNumFromHH = 1;
						for (HHJointDis hhDis : hhFamDisMap.keySet( ))
						{
							if (hhDis.getResideProvin( ) == parFam.getResideProvin( ) && hhDis.getResideType( ) == parFam.getResideType( )
											&& hhDis.getMemberNum( ) == parFam.getMemberNum( ) && hhDis.getElderNum( ) == parFam.getElderNum( ))
							{
								if (agent.getAgeInter( ).value( ) >= 13)
									indNumFromHH += hhDis.getElderNum( ) * hhFamDisMap.get(hhDis);
								else
									indNumFromHH += (hhDis.getMemberNum( ) - hhDis.getElderNum( )) * hhFamDisMap.get(hhDis);
							}
						}
						assignProb.add(Float.valueOf(indNumFromHH));
						famSum += indNumFromHH;
					}
					if (famSum <= 0)
					{
						// for test
						System.out.println("city famSum <= 0");
					}
					// probability normalization
					if (assignProb.size( ) > 1)
					{
						float probSum = 0;
						for (int j = 0; j < assignProb.size( ) - 1; j++)
						{
							float prob = assignProb.get(j) / famSum;
							assignProb.set(j, prob);
							probSum += prob;
						}
						assignProb.set(assignProb.size( ) - 1, 1 - probSum);
					} else if (assignProb.size( ) == 1)
					{
						assignProb.set(0, (float) 1);
					} else
					{
						System.out.println("assignProb.size( ) <= 0");
					}
					seed = rand.nextFloat( );
					famSum = 0;
					for (int j = 0; j < assignProb.size( ); j++)
					{
						famSum += assignProb.get(j);
						if (seed <= famSum)
						{
							HouseHold family = cityFamilyList.get(j);
							agent.setHh(family);
							family.getMemberIDList( ).add(agent.getAgentID( ));
							family.getMemberList( ).add(agent);
							if (family.getMemberList( ).size( ) == family.getMemberNum( ))
							{
								// write hh file
								WriteFamily(hhWriter, family);
								boolean removeResult = cityFamilyList.remove(family);
								if (!removeResult)
									System.out.println("cityFamilyList.remove");
								// supplement particular household
								for (Iterator<Map.Entry<HHJointDis, Integer>> iter = hhFamDisMap.entrySet( ).iterator( ); iter.hasNext( );)
								{
									HHJointDis hhDis = iter.next( ).getKey( );
									if (hhDis.getResideProvin( ) == family.getResideProvin( ) && hhDis.getResideType( ) == family.getResideType( )
													&& hhDis.getMemberNum( ) == family.getMemberNum( ) && hhDis.getElderNum( ) == family.getElderNum( ))
									{
										HouseHold newFamily = new HouseHold(hhDis.getHouseHoldType( ), hhDis.getResideProvin( ), hhDis.getResideType( ));
										newFamily.setHouseHoldID(householdID);
										newFamily.setMemberNum(hhDis.getMemberNum( ));
										newFamily.setElderNum(hhDis.getElderNum( ));
										householdID++;
										cityFamilyList.add(newFamily);
										int restNum = hhFamDisMap.get(hhDis) - 1;
										if (restNum <= 0)
											iter.remove( );
										else
											hhFamDisMap.put(hhDis, restNum);
										break;
									}
								}
							}
							break;
						}
					}
				} else
				{
					AssignColHH(cityColHHList, agent);
				}
			} else if (agent.getResidenceType( ) == EResidenceType.town)
			{
				if (!townFamilyList.isEmpty( ))
				{
					// compute assign probability
					ArrayList<Float> assignProb = new ArrayList<Float>( );
					float famSum = 0;
					for (int j = 0; j < townFamilyList.size( ); j++)
					{
						HouseHold parFam = townFamilyList.get(j);
						// heuristics
						if (!townPopCand.get(1).contains(j) && agent.getAgeInter( ).value( ) >= 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!townPopCand.get(2).contains(j) && agent.getGender( ) == EGender.Male && agent.getAgeInter( ).value( ) >= 5
										&& agent.getAgeInter( ).value( ) < 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!townPopCand.get(3).contains(j) && agent.getGender( ) == EGender.Female && agent.getAgeInter( ).value( ) >= 5
										&& agent.getAgeInter( ).value( ) < 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!townPopCand.get(4).contains(j) && agent.getAgeInter( ).value( ) < 5)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						int indNumFromHH = 1;
						for (HHJointDis hhDis : hhFamDisMap.keySet( ))
						{
							if (hhDis.getResideProvin( ) == parFam.getResideProvin( ) && hhDis.getResideType( ) == parFam.getResideType( )
											&& hhDis.getMemberNum( ) == parFam.getMemberNum( ) && hhDis.getElderNum( ) == parFam.getElderNum( ))
							{
								if (agent.getAgeInter( ).value( ) >= 13)
									indNumFromHH += hhDis.getElderNum( ) * hhFamDisMap.get(hhDis);
								else
									indNumFromHH += (hhDis.getMemberNum( ) - hhDis.getElderNum( )) * hhFamDisMap.get(hhDis);
							}
						}
						assignProb.add(Float.valueOf(indNumFromHH));
						famSum += indNumFromHH;
					}
					if (famSum <= 0)
					{
						// for test:
						System.out.println("town famSum <= 0");
					}
					// probability normalization
					if (assignProb.size( ) > 1)
					{
						float probSum = 0;
						for (int j = 0; j < assignProb.size( ) - 1; j++)
						{
							float prob = assignProb.get(j) / famSum;
							assignProb.set(j, prob);
							probSum += prob;
						}
						assignProb.set(assignProb.size( ) - 1, 1 - probSum);
					} else if (assignProb.size( ) == 1)
					{
						assignProb.set(0, (float) 1);
					} else
					{
						System.out.println("assignProb.size( ) <= 0");
					}
					seed = rand.nextFloat( );
					famSum = 0;
					for (int j = 0; j < assignProb.size( ); j++)
					{
						famSum += assignProb.get(j);
						if (seed <= famSum)
						{
							HouseHold family = townFamilyList.get(j);
							agent.setHh(family);
							family.getMemberIDList( ).add(agent.getAgentID( ));
							family.getMemberList( ).add(agent);
							if (family.getMemberList( ).size( ) == family.getMemberNum( ))
							{
								// write hh file
								WriteFamily(hhWriter, family);
								boolean removeResult = townFamilyList.remove(family);
								if (!removeResult)
									System.out.println("townFamilyList.remove");
								// supplement particular household
								for (Iterator<Map.Entry<HHJointDis, Integer>> iter = hhFamDisMap.entrySet( ).iterator( ); iter.hasNext( );)
								{
									HHJointDis hhDis = iter.next( ).getKey( );
									if (hhDis.getResideProvin( ) == family.getResideProvin( ) && hhDis.getResideType( ) == family.getResideType( )
													&& hhDis.getMemberNum( ) == family.getMemberNum( ) && hhDis.getElderNum( ) == family.getElderNum( ))
									{
										HouseHold newFamily = new HouseHold(hhDis.getHouseHoldType( ), hhDis.getResideProvin( ), hhDis.getResideType( ));
										newFamily.setHouseHoldID(householdID);
										newFamily.setMemberNum(hhDis.getMemberNum( ));
										newFamily.setElderNum(hhDis.getElderNum( ));
										householdID++;
										townFamilyList.add(newFamily);
										int restNum = hhFamDisMap.get(hhDis) - 1;
										if (restNum <= 0)
											iter.remove( );
										else
											hhFamDisMap.put(hhDis, restNum);
										break;
									}
								}
							}
							break;
						}
					}
				} else
				{
					AssignColHH(townColHHList, agent);
				}
			} else
			{
				if (!ruralFamilyList.isEmpty( ))
				{
					// compute assign probability
					ArrayList<Float> assignProb = new ArrayList<Float>( );
					float famSum = 0;
					for (int j = 0; j < ruralFamilyList.size( ); j++)
					{
						HouseHold parFam = ruralFamilyList.get(j);
						// heuristics
						if (!ruralPopCand.get(1).contains(j) && agent.getAgeInter( ).value( ) >= 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!ruralPopCand.get(2).contains(j) && agent.getGender( ) == EGender.Male && agent.getAgeInter( ).value( ) >= 5
										&& agent.getAgeInter( ).value( ) < 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!ruralPopCand.get(3).contains(j) && agent.getGender( ) == EGender.Female && agent.getAgeInter( ).value( ) >= 5
										&& agent.getAgeInter( ).value( ) < 13)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						if (!ruralPopCand.get(4).contains(j) && agent.getAgeInter( ).value( ) < 5)
						{
							assignProb.add((float) 0.0);
							continue;
						}
						int indNumFromHH = 1;
						for (HHJointDis hhDis : hhFamDisMap.keySet( ))
						{
							if (hhDis.getResideProvin( ) == parFam.getResideProvin( ) && hhDis.getResideType( ) == parFam.getResideType( )
											&& hhDis.getMemberNum( ) == parFam.getMemberNum( ) && hhDis.getElderNum( ) == parFam.getElderNum( ))
							{
								if (agent.getAgeInter( ).value( ) >= 13)
									indNumFromHH += hhDis.getElderNum( ) * hhFamDisMap.get(hhDis);
								else
									indNumFromHH += (hhDis.getMemberNum( ) - hhDis.getElderNum( )) * hhFamDisMap.get(hhDis);
							}
						}
						assignProb.add(Float.valueOf(indNumFromHH));
						famSum += indNumFromHH;
					}
					if (famSum <= 0)
					{
						// for test:
						System.out.println("rural famSum <= 0");
					}
					// probability normalization
					if (assignProb.size( ) > 1)
					{
						float probSum = 0;
						for (int j = 0; j < assignProb.size( ) - 1; j++)
						{
							float prob = assignProb.get(j) / famSum;
							assignProb.set(j, prob);
							probSum += prob;
						}
						assignProb.set(assignProb.size( ) - 1, 1 - probSum);
					} else if (assignProb.size( ) == 1)
					{
						assignProb.set(0, (float) 1);
					} else
					{
						System.out.println("assignProb.size( ) <= 0");
					}
					seed = rand.nextFloat( );
					famSum = 0;
					for (int j = 0; j < assignProb.size( ); j++)
					{
						famSum += assignProb.get(j);
						if (seed <= famSum)
						{
							HouseHold family = ruralFamilyList.get(j);
							agent.setHh(family);
							family.getMemberIDList( ).add(agent.getAgentID( ));
							family.getMemberList( ).add(agent);
							if (family.getMemberList( ).size( ) == family.getMemberNum( ))
							{
								// write hh file
								WriteFamily(hhWriter, family);
								boolean removeResult = ruralFamilyList.remove(family);
								if (!removeResult)
									System.out.println("ruralFamilyList.remove");
								// supplement particular household
								for (Iterator<Map.Entry<HHJointDis, Integer>> iter = hhFamDisMap.entrySet( ).iterator( ); iter.hasNext( );)
								{
									HHJointDis hhDis = iter.next( ).getKey( );
									if (hhDis.getResideProvin( ) == family.getResideProvin( ) && hhDis.getResideType( ) == family.getResideType( )
													&& hhDis.getMemberNum( ) == family.getMemberNum( ) && hhDis.getElderNum( ) == family.getElderNum( ))
									{
										HouseHold newFamily = new HouseHold(hhDis.getHouseHoldType( ), hhDis.getResideProvin( ), hhDis.getResideType( ));
										newFamily.setHouseHoldID(householdID);
										newFamily.setMemberNum(hhDis.getMemberNum( ));
										newFamily.setElderNum(hhDis.getElderNum( ));
										householdID++;
										ruralFamilyList.add(newFamily);
										int restNum = hhFamDisMap.get(hhDis) - 1;
										if (restNum <= 0)
											iter.remove( );
										else
											hhFamDisMap.put(hhDis, restNum);
										break;
									}
								}
							}
							break;
						}
					}
				} else
				{
					AssignColHH(ruralColHHList, agent);
				}
			}
			// assign to enterprise
			switch (agent.getAgeInter( ))
			{
				case ZeroToFour:
				case FiveToNine:
				case TenToFourteen:
				case FifteenToNineteen:
				case SixtyFiveToSixtyNine:
				case SeventyToSeventyFour:
				case SeventyFiveToSeventyNine:
				case EightyToEightyFour:
				case EightyFiveToEightyNine:
				case NinetyToNinetyFour:
				case NinetyFiveToNinetyNine:
				case HundredAndAbove:
					agent.setEnterp(null);
					break;
				case TwentyToTwentyFour:
				case TwentyFiveToTwentyNine:
				case ThirtyToThirtyFour:
				case ThirtyFiveToThirtyNine:
				case FortyToFortyFour:
				case FortyFiveToFortyNine:
				case FiftyToFiftyFour:
				case FiftyFiveToFiftyNine:
				case SixtyToSixtyFour:
					if (!enterpriseList.isEmpty( ))
					{
						// compute assign probability
						ArrayList<Float> assignProb = new ArrayList<Float>( );
						float enterSum = 0;
						for (int j = 0; j < enterpriseList.size( ); j++)
						{
							Enterprise enterp = enterpriseList.get(j);
							int enterNum = 1;
							for (EnterpJointDis enterDis : enterpDisMap.keySet( ))
							{
								if (enterDis.getProvin( ) == enterp.getProvin( ) && enterDis.getEnterType( ) == enterp.getEnterType( )
												&& enterDis.getEnterScale( ) == enterp.getEnterScale( ))
									enterNum += enterpDisMap.get(enterDis);
							}
							assignProb.add(Float.valueOf(enterNum));
							enterSum += enterNum;
						}
						// probability normalization
						for (int j = 0; j < assignProb.size( ); j++)
							assignProb.set(j, assignProb.get(j) / enterSum);
						seed = rand.nextFloat( );
						enterSum = 0;
						for (int j = 0; j < assignProb.size( ); j++)
						{
							enterSum += assignProb.get(j);
							if (seed <= enterSum)
							{
								Enterprise enterp = enterpriseList.get(j);
								agent.setEnterp(enterp);
								enterp.getMemberList( ).add(agent);
								if (enterp.getMemberList( ).size( ) == enterp.getEmployeeNum( ))
								{
									// write enterprise file
									WriteEnterprise(enterpWriter, enterp);
									boolean removeResult = enterpriseList.remove(enterp);
									if (!removeResult)
										System.out.println("enterpriseList.remove");
									// supplement particular enterprise
									for (Iterator<Map.Entry<EnterpJointDis, Integer>> iter = enterpDisMap.entrySet( ).iterator( ); iter.hasNext( );)
									{
										EnterpJointDis enterDis = iter.next( ).getKey( );
										if (enterDis.getProvin( ) == enterp.getProvin( ) && enterDis.getEnterType( ) == enterp.getEnterType( )
														&& enterDis.getEnterScale( ) == enterp.getEnterScale( ))
										{
											Enterprise newEnterp = new Enterprise(enterDis.getProvin( ), enterDis.getEnterType( ), enterDis.getEnterScale( ),
															enterDis.getEmployedNum( ));
											newEnterp.setEnterID(enterID);
											enterID++;
											enterpriseList.add(newEnterp);
											int restNum = enterpDisMap.get(enterDis) - 1;
											if (restNum <= 0)
												iter.remove( );
											else
												enterpDisMap.put(enterDis, restNum);
											break;
										}
									}
								}
								break;
							}
						}
					} else
						agent.setEnterp(null);
				default:
					break;
			}
			// write individual record to file
			if (agent.getResidenceType( ) == EResidenceType.city)
			{
				WriteIndividual(cityPopWriter, agent);
			} else if (agent.getResidenceType( ) == EResidenceType.town)
			{
				WriteIndividual(townPopWriter, agent);
			} else
			{
				WriteIndividual(ruralPopWriter, agent);
			}
			if (agentID % 5000000 == 0)
			{
				// re-open hhCsvWriter
				hhWriter.close( );
				hhBufferWriter.close( );
				hhOutStream.close( );
				hhOutStream = new OutputStreamWriter(new FileOutputStream(hhFile, true), "utf-8");
				hhBufferWriter = new BufferedWriter(hhOutStream);
				hhWriter = new CsvWriter(hhBufferWriter, ',');
				// re-open enterCsvWriter
				enterpWriter.close( );
				enterBufferWriter.close( );
				enterOutStream.close( );
				enterOutStream = new OutputStreamWriter(new FileOutputStream(enterFile, true), "utf-8");
				enterBufferWriter = new BufferedWriter(enterOutStream);
				enterpWriter = new CsvWriter(enterBufferWriter, ',');
				// re-open popCsvWriter
				cityPopWriter.close( );
				cityPopBufferWriter.close( );
				cityPopOutStream.close( );
				cityPopOutStream = new OutputStreamWriter(new FileOutputStream(cityPopFile, true), "utf-8");
				cityPopBufferWriter = new BufferedWriter(cityPopOutStream);
				cityPopWriter = new CsvWriter(cityPopBufferWriter, ',');
				townPopWriter.close( );
				townPopBufferWriter.close( );
				townPopOutStream.close( );
				townPopOutStream = new OutputStreamWriter(new FileOutputStream(townPopFile, true), "utf-8");
				townPopBufferWriter = new BufferedWriter(townPopOutStream);
				townPopWriter = new CsvWriter(townPopBufferWriter, ',');
				ruralPopWriter.close( );
				ruralPopBufferWriter.close( );
				ruralPopOutStream.close( );
				ruralPopOutStream = new OutputStreamWriter(new FileOutputStream(ruralPopFile, true), "utf-8");
				ruralPopBufferWriter = new BufferedWriter(ruralPopOutStream);
				ruralPopWriter = new CsvWriter(ruralPopBufferWriter, ',');
			}
			if (agentID % 100000 == 0)
				System.out.println(resideProv + " Pop Syn: " + agentID + "---HH Rest: "
								+ (cityFamilyList.size( ) + townFamilyList.size( ) + ruralFamilyList.size( )) + "---Enter Rest: " + enterpriseList.size( ));
		}
		cityPopWriter.close( );
		cityPopBufferWriter.close( );
		cityPopOutStream.close( );
		townPopWriter.close( );
		townPopBufferWriter.close( );
		townPopOutStream.close( );
		ruralPopWriter.close( );
		ruralPopBufferWriter.close( );
		ruralPopOutStream.close( );
		enterpWriter.close( );
		enterBufferWriter.close( );
		enterOutStream.close( );
		// write collective household
		hhWriter.close( );
		hhBufferWriter.close( );
		hhOutStream.close( );
		hhOutStream = new OutputStreamWriter(new FileOutputStream(hhFile, true), "utf-8");
		hhBufferWriter = new BufferedWriter(hhOutStream);
		hhWriter = new CsvWriter(hhBufferWriter, ',');
		for (int j = 0; j < cityColHHList.size( ); j++)
		{
			HouseHold colHH = cityColHHList.get(j);
			WriteColHH(hhWriter, colHH);
		}
		for (int j = 0; j < townColHHList.size( ); j++)
		{
			HouseHold colHH = townColHHList.get(j);
			WriteColHH(hhWriter, colHH);
		}
		for (int j = 0; j < ruralColHHList.size( ); j++)
		{
			HouseHold colHH = ruralColHHList.get(j);
			WriteColHH(hhWriter, colHH);
		}
		if (!cityFamilyList.isEmpty( ))
		{
			int hhNum = 0;
			for (HHJointDis hhDis : hhFamDisMap.keySet( ))
			{
				if (hhDis.getResideType( ) == EResidenceType.city)
					hhNum += hhFamDisMap.get(hhDis);
			}
			hhNum += cityFamilyList.size( );
			System.out.println(resideProv + "cityFamilyList is not empty! Rest = " + hhNum);
		}
		if (!townFamilyList.isEmpty( ))
		{
			int hhNum = 0;
			for (HHJointDis hhDis : hhFamDisMap.keySet( ))
			{
				if (hhDis.getResideType( ) == EResidenceType.town)
					hhNum += hhFamDisMap.get(hhDis);
			}
			hhNum += townFamilyList.size( );
			System.out.println(resideProv + "townFamilyList is not empty! Rest = " + hhNum);
		}
		if (!ruralFamilyList.isEmpty( ))
		{
			int hhNum = 0;
			for (HHJointDis hhDis : hhFamDisMap.keySet( ))
			{
				if (hhDis.getResideType( ) == EResidenceType.rural)
					hhNum += hhFamDisMap.get(hhDis);
			}
			hhNum += ruralFamilyList.size( );
			System.out.println(resideProv + "ruralFamilyList is not empty! Rest = " + hhNum);
		}
		if (!enterpriseList.isEmpty( ))
		{
			int enterNum = 0;
			for (Integer parNum : enterpDisMap.values( ))
			{
				enterNum += parNum;
			}
			enterNum += enterpriseList.size( );
			System.out.println(resideProv + "enterpriseList is not empty! Rest = " + enterNum);
		}
		hhWriter.close( );
		hhBufferWriter.close( );
		hhOutStream.close( );
	}

	private void AssignColHH(ArrayList<HouseHold> colHHList, Individual agent)
	{
		boolean allAssigned = true;
		for (int j = 0; j < colHHList.size( ); j++)
		{
			HouseHold colHH = colHHList.get(j);
			if (colHH.getMemberIDList( ).isEmpty( ))
			{
				agent.setHh(colHH);
				colHH.getMemberIDList( ).add(agent.getAgentID( ));
				allAssigned = false;
				break;
			}
		}
		if (allAssigned)
		{
			int index = rand.nextInt(colHHList.size( ));
			HouseHold colHH = colHHList.get(index);
			agent.setHh(colHH);
			colHH.getMemberIDList( ).add(agent.getAgentID( ));
		}
	}

	private void WriteIndividual(CsvWriter popWriter, Individual agent) throws IOException
	{
		String[ ] popWriteLine = new String[8];
		popWriteLine[0] = String.format(agent.getProvin( ).value( ) + "%08d", agent.getAgentID( ));
		popWriteLine[1] = String.valueOf(agent.getGender( ).value( ));
		popWriteLine[2] = String.valueOf(agent.getProvin( ).value( ));
		popWriteLine[3] = String.valueOf(agent.getResidenceType( ).value( ));
		popWriteLine[4] = String.valueOf(agent.getAgeInter( ).value( ));
		popWriteLine[5] = String.valueOf(agent.getHh( ).getHouseHoldType( ).value( ));
		popWriteLine[6] = String.valueOf(agent.getHh( ).getHouseHoldID( ));
		popWriteLine[7] = String.valueOf(agent.getEnterp( ) == null ? -1 : agent.getEnterp( ).getEnterID( ));
		popWriter.writeRecord(popWriteLine);
	}

	private void WriteEnterprise(CsvWriter enterpWriter, Enterprise enterp) throws IOException
	{
		String[ ] enterWriteLine = new String[5];
		enterWriteLine[0] = String.format(enterp.getProvin( ).value( ) + "%08d", enterp.getEnterID( ));
		enterWriteLine[1] = String.valueOf(enterp.getProvin( ).value( ));
		enterWriteLine[2] = String.valueOf(enterp.getEnterType( ).value( ));
		enterWriteLine[3] = String.valueOf(enterp.getEnterScale( ).value( ));
		enterWriteLine[4] = String.valueOf(enterp.getMemberList( ).size( ));
		enterpWriter.writeRecord(enterWriteLine);
	}

	private void WriteFamily(CsvWriter hhWriter, HouseHold family) throws IOException
	{
		String[ ] hhWriteLine = new String[6];
		hhWriteLine[0] = String.format(family.getResideProvin( ).value( ) + "%08d", family.getHouseHoldID( ));
		hhWriteLine[1] = String.valueOf(family.getResideProvin( ).value( ));
		hhWriteLine[2] = String.valueOf(family.getResideType( ).value( ));
		hhWriteLine[3] = String.valueOf(family.getHouseHoldType( ).value( ));
		// hhWriteLine[4] = String.valueOf(family.getMemberNum( ));
		int memNum = 0;
		int elderNum = 0;
		for (Individual mem : family.getMemberList( ))
		{
			memNum++;
			if (mem.getAgeInter( ).value( ) >= 13)
				elderNum++;
		}
		hhWriteLine[4] = String.valueOf(memNum);
		hhWriteLine[5] = String.valueOf(elderNum);
		hhWriter.writeRecord(hhWriteLine);
	}

	private void WriteColHH(CsvWriter hhWriter, HouseHold colHH) throws IOException
	{
		String[ ] hhWriteLine = new String[6];
		hhWriteLine[0] = String.format(colHH.getResideProvin( ).value( ) + "%08d", colHH.getHouseHoldID( ));
		hhWriteLine[1] = String.valueOf(colHH.getResideProvin( ).value( ));
		hhWriteLine[2] = String.valueOf(colHH.getResideType( ).value( ));
		hhWriteLine[3] = String.valueOf(colHH.getHouseHoldType( ).value( ));
		hhWriteLine[4] = String.valueOf(colHH.getMemberIDList( ).size( ));
		hhWriteLine[5] = String.valueOf(-1);
		hhWriter.writeRecord(hhWriteLine);
	}

	private void ComputeEligiblePop(Map<Integer, HashSet<Integer>> popCand, ArrayList<HouseHold> familyList)
	{
		popCand.get(1).clear( );// elder
		popCand.get(2).clear( );// male
		popCand.get(3).clear( );// female
		popCand.get(4).clear( );// children
		for (int i = 0; i < familyList.size( ); i++)
		{
			HouseHold hhCandidate = familyList.get(i);
			int elderNum = 0;
			int maleNum = 0;
			int femaleNum = 0;
			int chidrenNum = 0;
			for (Individual member : hhCandidate.getMemberList( ))
			{
				if (member.getAgeInter( ).value( ) >= 13)
				{
					elderNum++;
				} else if (member.getAgeInter( ).value( ) >= 5)
				{
					if (member.getGender( ) == EGender.Male)
						maleNum++;
					else
						femaleNum++;
				} else
				{
					chidrenNum++;
				}
			}
			int restElderNum = hhCandidate.getElderNum( ) - elderNum;
			if (restElderNum > 0)
				popCand.get(1).add(i);
			else if (restElderNum < 0)
				System.out.println("Check heuristics : restElderNum < 0");
			int adultRoom = hhCandidate.getMemberNum( ) - hhCandidate.getElderNum( ) - chidrenNum - maleNum - femaleNum;
			if (adultRoom > 0)
			{
				if (maleNum == 0)
				{
					if (femaleNum == 0)
					{
						// maleNum = 0 && femaleNum = 0
						popCand.get(2).add(i);
						popCand.get(3).add(i);
					} else
					{
						// maleNum = 0 && femaleNum > 0
						popCand.get(2).add(i);
					}
				} else
				{
					if (femaleNum == 0)
					{
						// maleNum > 0 && femaleNum = 0
						popCand.get(3).add(i);
					} else
					{
						// maleNum > 0 && femaleNum > 0
						popCand.get(4).add(i);
					}
				}
			} else if (adultRoom < 0)
				System.out.println("Check heuristics : adultRoom < 0");
		}
	}
}
