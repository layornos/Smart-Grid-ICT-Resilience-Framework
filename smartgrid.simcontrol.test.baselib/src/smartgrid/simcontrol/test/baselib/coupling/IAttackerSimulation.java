package smartgrid.simcontrol.test.baselib.coupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

public interface IAttackerSimulation extends ISimulationComponent {

    public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioResult impactAnalysisOutput);

    /**
     * If using ExtensionPoints and so 0-parameter Constructor pass the config from Simcontrol UI to
     * this Method to build the desired AttackerSimulation ("Factory Method")
     *
     * @param config
     *            behavior for the Attacker
     * @return true if Init was successful
     * @throws CoreException
     *             If ILaunchConfiguration.getAttribute fails
     */
    public void init(ILaunchConfiguration config) throws CoreException;

    /**
     * Should return true if hacking speed should be considered during the attack simulation
     *
     * @return true if hacking speed should be considered
     */
    public boolean enableHackingSpeed();

    /**
     * Should return true if root node id should be considered during the attack simulation
     *
     * @return true if root node should be considered
     */
    public boolean enableRootNode();

    /**
     * Should return true if logical connections may or may not be considered during the attack
     * simulation
     *
     * @return true if logical connections should be considered
     */
    public boolean enableLogicalConnections();
}