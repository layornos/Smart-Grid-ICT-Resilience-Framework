package smartgrid.simcontrol.interfaces;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridoutput.ScenarioResult;
import smartgridtopo.Scenario;


	//TODO JavaDoc
/**
 * Interface for the Attacker Simulation
 * 
 */
public interface IAttackerSimulation {
    
	
	
	
	//TODO JavaDoc
	/**
	 * Runs the Atacker Simulation
	 * 
	 * @param smartGridTopo
	 * @param impactAnalysisOutput
	 * @return
	 */
	ScenarioResult run(Scenario smartGridTopo, ScenarioResult impactAnalysisOutput);
	
	
	/**
	 * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol 
	 * UI to this Method to build the desired AttackerSimulation ("Factory Method")
	 * 
	 * 
	 * 
	 * 
	 * @param config behavior for the Attacker
	 * @return true if Init was successful
	 * @throws CoreException If ILaunchConfiguration.getAttribute fails
	 */
	ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException;
}
