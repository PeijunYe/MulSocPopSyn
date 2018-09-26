import java.io.IOException;

import Input.InputDistributions;
import TensorPopSyn.PopulationAssignment;

public class Main
{
	public static void main(String[ ] args)
	{
		InputDistributions initializer = new InputDistributions( );
		initializer.InitControls("HouseholdData");
		initializer.InitSamples("HouseholdData");
		try
		{
			initializer.WritePopTensors( );
			initializer.WriteHHJointDis( );
			initializer.WriteHHJointDis( );
			initializer.ComputeEnterJointDis( );
		} catch (IOException e)
		{
			e.printStackTrace( );
		}
		// the above procedure writes constraints to files
		// tensor decomposition is conducted in Matlab
		// the achieved joint distribution is read in the following
		PopulationAssignment assembler = new PopulationAssignment( );
		assembler.Assignment( );
		System.out.println("done...");
	}
}
