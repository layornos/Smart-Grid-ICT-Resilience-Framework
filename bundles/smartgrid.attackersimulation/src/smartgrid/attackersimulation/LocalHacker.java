package smartgrid.attackersimulation;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.attackersimulation.strategies.BFSStrategy;
import smartgrid.attackersimulation.strategies.DFSStrategy;
import smartgrid.attackersimulation.strategies.FullyMeshedStrategy;
import smartgrid.attackersimulation.strategies.SingleStepAttackStrategies;
import smartgrid.helper.ScenarioModelHelper;
import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.HackingType;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * This Class represents an local Hacker.
 *
 * A Local Hacker operates from one distinct Node and can only hack Node that have a logical
 * Connection to his "root" Node
 *
 * @author Christian
 */
public class LocalHacker implements IAttackerSimulation {

    private static final Logger LOG = Logger.getLogger(LocalHacker.class);

    private SingleStepAttackStrategies hackingTypes;
    private String rootNodeID; // IDs stay the same over the whole Analysis
    private On rootNodeState; // Reference Changes between runs!
    private int hackingSpeed;
    private boolean ignoreLogicalConnections;
    private boolean initDone = false;
    private ScenarioResult scenarioResult;

    /**
     * Default constructor is needed by the OSGi framework to be able to use the extension point
     */
    public LocalHacker() {
    }

    @Override
    public boolean enableHackingSpeed() {
        return true;
    }

    @Override
    public boolean enableLogicalConnections() {
        return true;
    }

    @Override
    public boolean enableRootNode() {
        return true;
    }

    @Override
    public String getName() {
        return "Local Hacker";
    }

    @Override
    public void init(final ILaunchConfiguration config) throws CoreException {

        this.hackingSpeed = Integer
                .parseInt(config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));

        this.ignoreLogicalConnections = Boolean
                .valueOf(config.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FALSE));
        this.rootNodeID = config.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID);
        final var hackingTypes = HackingType
                .valueOf(config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.DEFAULT_HACKING_STYLE));
        switch (hackingTypes) {
        case BFS_HACKING:
            this.hackingTypes = new BFSStrategy(this.ignoreLogicalConnections, this.hackingSpeed);
            break;
        case DFS_HACKING:
            this.hackingTypes = new DFSStrategy(this.ignoreLogicalConnections, this.hackingSpeed);
            break;
        case FULLY_MESHED_HACKING:
            this.hackingTypes = new FullyMeshedStrategy(this.hackingSpeed);
            this.ignoreLogicalConnections = false;
            break;
        default:
            assert false;
            break;
        }
        this.initDone = true;
    }

    /**
     * setting the Root node
     */
    private void rootNodeUpdate() {

        if (this.rootNodeID.equalsIgnoreCase(Constants.NO_ROOT_NODE_ID)) {
            LOG.info("No root node specified.");
            this.rootNodeID = ScenarioModelHelper.selectRandomRoot(this.ignoreLogicalConnections, this.scenarioResult);
        }
        this.rootNodeState = ScenarioModelHelper.findEntityOnStateFromID(this.rootNodeID, this.scenarioResult); // update
                                                                                                                // state
        this.rootNodeState.setIsHacked(true);
    }

    /**
     * @see smartgrid.simcontrol.interfaces.IAttackerSimulation#run(smartgridtopo .Scenario,
     *      smartgridoutput.ScenarioResult)
     */
    @Override
    public ScenarioResult run(final SmartGridTopology smartGridTopo, final ScenarioResult scenarioResult) {

        if (!this.initDone) {
            throw new IllegalStateException("LocalHacker not initialization. Run init()");
        }

        this.scenarioResult = scenarioResult;

        this.rootNodeUpdate();

        if (this.rootNodeState != null) {
            this.hackingTypes.hackNextNode(this.rootNodeState);
        }
        this.scenarioResult.setScenario(smartGridTopo);

        LOG.debug("Hacking done");
        return this.scenarioResult;
    }

}