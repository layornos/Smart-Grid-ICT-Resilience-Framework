package smartgrid.simcontrol.test;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import smartgrid.simcontrol.test.rmi.BlockingDataExchanger;

/**
 * This class provides the Delegate for the SimControl Approach of the Smartgrid Analysis'
 *
 * @implements ILaunchConfigurationDelegate
 */
public class SimcontroLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

    private static final Logger LOG = Logger.getLogger(SimcontroLaunchConfigurationDelegate.class);

    private int attempt = 0;
    private int maxAttempts = 5;

    /**
     * {@inheritDoc}
     * <P>
     *
     * Launches an SimController Analysis with the given Launch Configuration
     */
    @Override
    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch, final IProgressMonitor monitor) throws CoreException {

        SimulationController simControl = new SimulationController();
        try {
            simControl.init(configuration);
            simControl.run();
        } catch (InterruptedException e) {
            LOG.info("The simulation was interrupted.");
        } catch (Throwable e) {
            LOG.fatal("An unexpected exception occured.", e);
            BlockingDataExchanger.storeException(e);

            if (attempt < maxAttempts) {
                attempt++;
                LOG.info("Attempting another run (" + (attempt + 1) + '/' + (maxAttempts + 1) + ')');
                this.launch(configuration, mode, launch, monitor);
            } else {
                LOG.info("The maximal amount of attempts failed (" + (maxAttempts + 1) + "). Terminating.");
                throw e;
            }
        }
    }
}
