package smartgrid.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.test.baselib.coupling.IImpactAnalysis;
import smartgrid.simcontrol.test.baselib.coupling.ISimulationComponent;
import smartgrid.simcontrol.test.baselib.coupling.ITerminationCondition;
import smartgrid.simcontrol.test.baselib.coupling.ITimeProgressor;

public class TestSimulationExtensionPointHelper {

    private static final IExtensionRegistry registry = RegistryFactory.getRegistry();

    private TestSimulationExtensionPointHelper() {
    }


    public static List<IAttackerSimulation> getAttackerSimulationExtensions() throws CoreException {
        final List<IAttackerSimulation> list = new ArrayList<>();
        final IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.ATTACKER_SIMULATION_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof IAttackerSimulation) {
                list.add((IAttackerSimulation) o);
            }
        }
        return list;
    }

    public static List<ITerminationCondition> getTerminationConditionExtensions() throws CoreException {
        final List<ITerminationCondition> list = new ArrayList<>();
        final IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.TERMINATION_CONDITION_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof ITerminationCondition) {
                list.add((ITerminationCondition) o);
            }
        }
        return list;
    }

    public static List<ITimeProgressor> getProgressorExtensions() throws CoreException {
        final List<ITimeProgressor> list = new ArrayList<>();
        final IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.TIME_PROGRESSOR_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof ITimeProgressor) {
                list.add((ITimeProgressor) o);
            }
        }
        return list;
    }

    public static List<IImpactAnalysis> getImpactAnalysisExtensions() throws CoreException {
        final List<IImpactAnalysis> list = new ArrayList<>();
        final IConfigurationElement[] elements = registry
                .getConfigurationElementsFor(Constants.IMPACT_ANALYSIS_EXTENSION_POINT);
        for (final IConfigurationElement element : elements) {
            final Object o = element.createExecutableExtension("class");
            if (o instanceof IImpactAnalysis) {
                list.add((IImpactAnalysis) o);
            }
        }
        return list;
    }


    public static boolean isExtensionSelected(final ILaunchConfiguration launchConfig,
            final ISimulationComponent simComponent, final String key) throws CoreException {
        return launchConfig.getAttribute(key, "").equals(simComponent.getName());
    }

    public static <T extends ISimulationComponent> T findExtension(final ILaunchConfiguration launchConfig,
            final List<T> simComponents, final String key, final Class<T> type) throws CoreException {
        for (final T simComponent : simComponents) {
            if (isExtensionSelected(launchConfig, simComponent, key)) {
                return simComponent;
            }
        }
        return null;
    }
}
