package smartgrid.simcontrol.rmi;

import java.util.Map;

import org.apache.log4j.Logger;

import smartgrid.simcontrol.coupling.PowerSpec;
import smartgrid.simcontrol.coupling.TopologyContainer;

/**
 * This is basically an exchanger for two different datatypes.
 * 
 * @author Misha
 */
public class BlockingKritisDataExchanger {

    private static final Logger LOG = Logger.getLogger(BlockingKritisDataExchanger.class);

    private static Map<String, Map<String, PowerSpec>> bufferedDemand;
    private static Thread kritisThread;

    private static Map<String, Map<String, Double>> bufferedPower;
    private static Thread couplingThread;

    private static Exception storedException;

    public static synchronized Map<String, Map<String, PowerSpec>> passDataToKritisSim(Map<String, Map<String, Double>> power) throws InterruptedException {
        assert bufferedPower == null;

        // provide own data
        bufferedPower = power;
        couplingThread = Thread.currentThread();
        BlockingKritisDataExchanger.class.notifyAll();

        // wait for data
        while (bufferedDemand == null) {
            LOG.info("SimControl is waiting for data of Kritis simulation.");
            BlockingKritisDataExchanger.class.wait();
        }

        // consume data
        Map<String, Map<String, PowerSpec>> tempDemand = bufferedDemand;
        bufferedDemand = null;
        couplingThread = null;
        LOG.info("SimControl has finished its exchange.");
        return tempDemand;
    }

    public static synchronized Map<String, Map<String, Double>> getDataFromCoupling(Map<String, Map<String, PowerSpec>> demand) throws Exception {
        assert bufferedDemand == null;

        hasExceptionOccured();

        // provide own data
        bufferedDemand = demand;
        kritisThread = Thread.currentThread();
        BlockingKritisDataExchanger.class.notifyAll();

        // wait for data
        while (bufferedPower == null) {
            hasExceptionOccured();
            LOG.info("The Kritis simulation is waiting for data of SimControl.");
            BlockingKritisDataExchanger.class.wait();
        }

        hasExceptionOccured();

        // consume data
        Map<String, Map<String, Double>> tempPower = bufferedPower;
        bufferedPower = null;
        kritisThread = null;
        LOG.info("The Kritis simulation has finished its exchange.");
        return tempPower;
    }

    private static void hasExceptionOccured() throws Exception {
        if (storedException != null) {
            LOG.info("Passing exception to KRITIS simulation.");
            throw storedException;
        }
    }

    public static synchronized void storeException(Exception e) {
        storedException = e;
        BlockingKritisDataExchanger.class.notifyAll();
    }

    private static TopologyContainer bufferedTopoData;

    public static synchronized void storeGeoData(TopologyContainer topoData) {
        if (bufferedTopoData != null) {
            LOG.error("There was already geo data present. This data is now overwritten.");
        }
        BlockingKritisDataExchanger.bufferedTopoData = topoData;
        BlockingKritisDataExchanger.class.notifyAll();
    }

    public static synchronized TopologyContainer getTopoData() throws InterruptedException {
        couplingThread = Thread.currentThread();

        // wait for data
        while (bufferedTopoData == null) {
            LOG.info("SimControl is waiting for topo data from the Kritis simulation.");
            BlockingKritisDataExchanger.class.wait();
        }

        // consume data
        TopologyContainer tempGeoData = bufferedTopoData;
        bufferedTopoData = null;
        couplingThread = null;
        LOG.info("SimControl got geo data from the Kritis simulation.");
        return tempGeoData;
    }

    public static synchronized boolean freeAll() {

        boolean threadFreed = couplingThread != null || kritisThread != null;

        // free coupling thread
        if (couplingThread != null) {
            couplingThread.interrupt();
            couplingThread = null;
            LOG.info("The SimControl thread was interrupted and freed from the data exchange sync.");
        }

        if (kritisThread != null) {
            // only free kritisThread if there was no exception (the KRITIS thread will be freed by the exception throw)
            if (storedException == null) {
                kritisThread.interrupt();
                LOG.info("The Kritis simulation thread was interrupted and freed from the data exchange sync.");
            }
            kritisThread = null;
        }

        // clear all buffered data
        bufferedDemand = null;
        bufferedPower = null;
        bufferedTopoData = null;
        storedException = null;

        return threadFreed;
    }
}
