package smartgrid.newsimcontrol.rmi.server;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import couplingToICT.ICTElement;
import couplingToICT.ISimulationControllerRemote;
import couplingToICT.ISimulationController;
import couplingToICT.PowerAssigned;
import couplingToICT.PowerSpecContainer;
import couplingToICT.SimcontrolException;
import couplingToICT.SimcontrolInitializationException;
import couplingToICT.SmartComponentStateContainer;
import couplingToICT.SmartGridTopoContainer;
import couplingToICT.initializer.AttackerSimulationsTypes;
import couplingToICT.initializer.HackingStyle;
import couplingToICT.initializer.InitializationMapKeys;
import couplingToICT.initializer.PowerSpecsModificationTypes;
import smartgrid.newsimcontrol.controller.ReactiveSimulationController;
import smartgrid.newsimcontrol.rmi.ActiveSimulationController;
import smartgrid.newsimcontrol.rmi.Startup;

/**
 * This class acts as RMI Server for the KRITIS simulation of the IKET. The
 * server is always registered when Eclipse starts by the early {@link Startup}
 * hook of the plugin. It also implements the RMI Interface of the simulation
 * coupling. The server delegates to the simulation coupling either in active or
 * reactive mode.
 * 
 * In <b>active mode</b>, the {@link ActiveSimulationController} has to be started via
 * the {@link SimcontrolLaunchConfigurationDelegate}. The simulation coupling and
 * the KRITIS simulation will synchronize and exchange data using the
 * {@link BlockingDataExchanger}.
 * 
 * In <b>reactive mode</b>, the {@link ReactiveSimulationController} will be
 * used. All control flow will originate from the KRITIS simulation, thus no
 * synchronization is required. However, all configuration must also be passed
 * in via the KRITIS simulation.
 * 
 * @author Mazen
 */
public class RmiServer implements ISimulationControllerRemote {

	private enum RmiServerState {
		NOT_INIT, ACTIVE, REACTIVE;
	}

	private static final Logger LOG = Logger.getLogger(RmiServer.class);
	private static final String ERROR_SERVER_NOT_INITIALIZED = "The SimControl RMI server is not initialized.";

	private static final String ERROR_SERVER_ALREADY_INITIALIZED = "The SimControl RMI server is already initialized.";

	private static RmiServer instance;

	public static synchronized void ensureRunning() {
		if (instance == null) {
			instance = new RmiServer();
			instance.startServer();
		}
	}

	public static synchronized void resetState() {
		if (instance != null) {
			instance.state = RmiServerState.NOT_INIT;
			LOG.info("The state of the RMI Server was reset to \"not initiated\".");
		}
	}

	public static synchronized void shutDown() {
		if (instance != null) {
			instance.shutDownServer();
			instance = null;
		}
	}

	private ReactiveSimulationController reactiveSimControl;

	/**
	 * The registry is used to bind and unbind the server (on start and shutdown).
	 */
	private Registry registry;

	/**
	 * This state of the server enforces a meaningful call sequence protocol.
	 */
	private RmiServerState state = RmiServerState.NOT_INIT;

	@Override
	public SmartComponentStateContainer getDysfunctSmartComponents() throws RemoteException, SimcontrolException {
		LOG.info("Dysfunctional smart components will be returned");

		if (state == RmiServerState.ACTIVE) {
			try {
				return BlockingDataExchanger.getSCSC();
			} catch (InterruptedException e) {
				throw new SimcontrolException(
						"Execution was manually interrupted while waiting for the smart component states.", e);
			}
		} else if (state == RmiServerState.REACTIVE) {
			return reactiveSimControl.getDysfunctionalcomponents();
		} else {
			LOG.error(ERROR_SERVER_NOT_INITIALIZED);
			throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
		}

	}

	@Override
	public PowerSpecContainer getModifiedPowerSpec(PowerSpecContainer powerSpecs, PowerAssigned SMPowerAssigned)
			throws RemoteException, SimcontrolException, InterruptedException {
		LOG.info("run was called remotely");
		PowerSpecContainer powerSpecContainer = null;

		if (state == RmiServerState.ACTIVE) {
			try {
				// buffer pA und pS
				BlockingDataExchanger.bufferPSAndPA(powerSpecs, SMPowerAssigned);

				// get Modified Power Demand
				powerSpecContainer = BlockingDataExchanger.getModifiedPowerSpecs();

			} catch (InterruptedException e) {
				throw e;
			} catch (SimcontrolException e) {
				resetState();
				BlockingDataExchanger.freeAll();
				throw new SimcontrolException(
						"There was an exception in SimControl. The RMI server has now been reset to 'uninitialized'.",
						e);
			}
		} else if (state == RmiServerState.REACTIVE) {
			// run
			reactiveSimControl.run(SMPowerAssigned);

			// Modify demand
			powerSpecContainer = powerSpecs;
			powerSpecContainer = reactiveSimControl.modifyPowerSpecContainer(powerSpecs);
		} else {
			LOG.error(ERROR_SERVER_NOT_INITIALIZED);
			throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
		}

		return powerSpecContainer;
	}

	@Override
	public void initWithoutConfiguration() {

		LOG.info("init active called remotely");
		if (state != RmiServerState.NOT_INIT) {
			LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
		}
		state = RmiServerState.ACTIVE;

		// TODO fix active mode

	}

	@Override
	public void initConfiguration(Map<InitializationMapKeys, String> initMap)
			throws RemoteException, SimcontrolInitializationException {

		LOG.info("init reactive called remotely");
		if (state != RmiServerState.NOT_INIT) {
			LOG.warn(ERROR_SERVER_ALREADY_INITIALIZED);
		}
		state = RmiServerState.REACTIVE;

		reactiveSimControl = new ReactiveSimulationController();

		// Values in the map
		String outputPath = null;
		String topoPath = "";
		String inputStatePath = "";
		String ignoreLogicalConnections = "false";
		String attackerType = AttackerSimulationsTypes.NO_ATTACK_SIMULATION.getDescription();
		String hackingStype = null;
		String hackingSpeed = "1";
		boolean generateTopo = false;
		int timeSteps = 1;
		String powerSpecsModificationType = PowerSpecsModificationTypes.NO_CHANGE_MODIFIER.getDescription();
		String rootNode = "";
		String impactAnalysis = "Graph Analyzer Impact Analysis";
		String timeProgresser = "No Operation";

		// createLaunchConfig
		final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		final ILaunchConfigurationType type = manager
				.getLaunchConfigurationType("smartgrid.simcontrol.test.SimcontrolLaunchConfigurationType");
		ILaunchConfigurationWorkingCopy workingCopy = null;
		try {
			workingCopy = type.newInstance(null, "testInstance");
		} catch (CoreException e1) {
			throw new SimcontrolInitializationException("Creating eclipse launcher failed", e1);
		}
		// TODO check if minimal necessary set is in Map
		// fill values in the working copy
		for (InitializationMapKeys key : initMap.keySet()) {
			if (key.equals(InitializationMapKeys.INPUT_PATH_KEY)) {
				inputStatePath = initMap.get(key);
			} else if (key.equals(InitializationMapKeys.TOPO_PATH_KEY)) {
				topoPath = initMap.get(key);
			} else if (key.equals(InitializationMapKeys.OUTPUT_PATH_KEY)) {
				outputPath = initMap.get(key);
			} else if (key.equals(InitializationMapKeys.IGNORE_LOC_CON_KEY)) {
				ignoreLogicalConnections = initMap.get(key);
			} else if (key.equals(InitializationMapKeys.ATTACKER_SIMULATION_KEY)) {
				attackerType = AttackerSimulationsTypes.valueOf(initMap.get(key)).getDescription();
			} else if (key.equals(InitializationMapKeys.HACKING_STYLE_KEY)
					&& initMap.get(InitializationMapKeys.ATTACKER_SIMULATION_KEY) != null
					&& AttackerSimulationsTypes.valueOf(initMap.get(
							InitializationMapKeys.ATTACKER_SIMULATION_KEY)) != (AttackerSimulationsTypes.NO_ATTACK_SIMULATION)) {
				hackingStype = HackingStyle.valueOf(initMap.get(key)).getDescription();
			} else if (key.equals(InitializationMapKeys.HACKING_SPEED_KEY)) {
				hackingSpeed = initMap.get(key);
			} else if (key.equals(InitializationMapKeys.TOPO_GENERATION_KEY)) {
				generateTopo = Boolean.valueOf(initMap.get(key));
			} else if (key.equals(InitializationMapKeys.TIME_STEPS_KEY)) {
				timeSteps = Integer.valueOf(initMap.get(key));
			} else if (key.equals(InitializationMapKeys.POWER_MODIFY_KEY)) {
				powerSpecsModificationType = PowerSpecsModificationTypes.valueOf(initMap.get(key)).getDescription();
			} else if (key.equals(InitializationMapKeys.ROOT_NODE_ID_KEY)) {
				rootNode = initMap.get(key);
			}
		}
		if (outputPath == null) {
			outputPath = System.getProperty("java.io.tmpdir");
			outputPath += File.separator + "smargrid" + System.currentTimeMillis();
		}
		reactiveSimControl.init(outputPath);
		if (generateTopo == false) {
			reactiveSimControl.initModelsFromFiles(topoPath, inputStatePath);
		}

		workingCopy.setAttribute(InitializationMapKeys.INPUT_PATH_KEY.getDescription(), inputStatePath);
		workingCopy.setAttribute(InitializationMapKeys.TOPO_PATH_KEY.getDescription(), topoPath);
		workingCopy.setAttribute(InitializationMapKeys.OUTPUT_PATH_KEY.getDescription(), outputPath);
		workingCopy.setAttribute(InitializationMapKeys.IGNORE_LOC_CON_KEY.getDescription(), ignoreLogicalConnections);
		workingCopy.setAttribute(InitializationMapKeys.ATTACKER_SIMULATION_KEY.getDescription(), attackerType);
		if (hackingStype != null) {
			workingCopy.setAttribute(InitializationMapKeys.HACKING_STYLE_KEY.getDescription(), hackingStype);
		}
		workingCopy.setAttribute(InitializationMapKeys.HACKING_SPEED_KEY.getDescription(), hackingSpeed);
		workingCopy.setAttribute(InitializationMapKeys.TOPO_GENERATION_KEY.getDescription(), generateTopo);
		workingCopy.setAttribute(InitializationMapKeys.TIME_STEPS_KEY.getDescription(), timeSteps);
		workingCopy.setAttribute(InitializationMapKeys.POWER_MODIFY_KEY.getDescription(), powerSpecsModificationType);
		workingCopy.setAttribute(InitializationMapKeys.ROOT_NODE_ID_KEY.getDescription(), rootNode);
		workingCopy.setAttribute(InitializationMapKeys.IMPACT_ANALYSIS_SIMULATION_KEY.getDescription(), impactAnalysis);
		workingCopy.setAttribute(InitializationMapKeys.TIME_PROGRESSOR_SIMULATION_KEY.getDescription(), timeProgresser);
		try {
			// create launch configuration
			final ILaunchConfiguration config = workingCopy.doSave();
			reactiveSimControl.loadCustomUserAnalysis(config);
		} catch (InterruptedException e) {
			throw new SimcontrolInitializationException("Simcontrol failed to initialize all simulation components.",
					e);
		} catch (CoreException e) {
			throw new SimcontrolInitializationException("Creating eclipse launcher failed", e);
		}
	}

	@Deprecated
	public void initReactive(String outputPath, String topoPath, String inputStatePath)
			throws RemoteException, SimcontrolException, SimcontrolInitializationException {

		Map<InitializationMapKeys, String> initMap = new HashMap<InitializationMapKeys, String>();
		initMap.put(InitializationMapKeys.INPUT_PATH_KEY, inputStatePath);
		initMap.put(InitializationMapKeys.OUTPUT_PATH_KEY, outputPath);
		initMap.put(InitializationMapKeys.TOPO_PATH_KEY, topoPath);
		initMap.put(InitializationMapKeys.TOPO_GENERATION_KEY, Boolean.toString(true));
		initMap.put(InitializationMapKeys.IGNORE_LOC_CON_KEY, Boolean.toString(false));
		initMap.put(InitializationMapKeys.HACKING_SPEED_KEY, Integer.toString(1));
		initMap.put(InitializationMapKeys.TIME_STEPS_KEY, Integer.toString(1));
		initMap.put(InitializationMapKeys.ROOT_NODE_ID_KEY, "");
		initMap.put(InitializationMapKeys.HACKING_STYLE_KEY, null);
		initMap.put(InitializationMapKeys.ATTACKER_SIMULATION_KEY,
				AttackerSimulationsTypes.NO_ATTACK_SIMULATION.toString());
		initMap.put(InitializationMapKeys.POWER_MODIFY_KEY, PowerSpecsModificationTypes.NO_CHANGE_MODIFIER.toString());
		initConfiguration(initMap);

	}

	@Override
	public Collection<ICTElement> initTopo(SmartGridTopoContainer topo) throws SimcontrolException {
		if (topo == null) {
			LOG.warn("Topo Container is null");
		} else {
			if (state == RmiServerState.ACTIVE) {
				LOG.info("init topo called remotely (Active)");
				BlockingDataExchanger.storeTopoData(topo);
			} else if (state == RmiServerState.REACTIVE) {
				LOG.info("init topo called remotely (ReActive)");
				return reactiveSimControl.initTopo(topo);
			} else {
				LOG.error(ERROR_SERVER_NOT_INITIALIZED);
				throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
			}
		}
		return null;

	}

	/**
	 * Properly unbinds the server from the port.
	 */
	private void shutDownServer() {
		if (registry != null) {
			try {
				registry.unbind("ISimulationController");
				LOG.info("SimControl RMI server shutdown successful");
			} catch (RemoteException e) {
				LOG.error("SimControl RMI server shutdown failed", e);
			} catch (NotBoundException e) {
				LOG.warn("SimControl RMI server shutdown failed: port was unbound. This is unexpected but harmless.",
						e);
			}
			registry = null;
		}
	}

	/**
	 * Binds the server to port 1099
	 * ({@link java.rmi.registry.Registry.REGISTRY_PORT}).
	 */
	private void startServer() {
		try {
			// System.setProperty("java.rmi.server.hostname","192.168.178.45");
			ISimulationController stub = (ISimulationController) UnicastRemoteObject.exportObject(this, 0);
			registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			registry.bind("ISimulationController", stub);
			LOG.info("RMI server running");
		} catch (Exception e) {
			LOG.error("Could not start RMI server", e);
		}
	}

	@Override
	public void terminate() throws SimcontrolException {
		LOG.info("shutDown was called remotely");
		if (state == RmiServerState.NOT_INIT) {
			LOG.warn(ERROR_SERVER_NOT_INITIALIZED);
			throw new SimcontrolException(ERROR_SERVER_NOT_INITIALIZED);
		}
		// shut down if reactive
		else if (state == RmiServerState.REACTIVE) {
			reactiveSimControl.shutDown();
		}
		// To-do here, the active simcontrol could be shutDown if there was a pointer
		// (rendezvous
		// required)

		state = RmiServerState.NOT_INIT;
	}

}