package smartgrid.simcontrol.test.ui.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import smartgrid.simcontrol.test.ui.EclipseUiHelper;


public class InterruptButtonHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) {
//        RmiServer.resetState();
//        BlockingKritisDataExchanger.freeAll();
//    	//TODO
//
//        /* This code shows a popup that is not really necessary anymore */
//        boolean threadsFreed = BlockingKritisDataExchanger.freeAll();
//        String message;
//        if (threadsFreed) {
//            message = "Threads were freed from the synchronization.";
//        } else {
//            message = "There were no threads waiting.";
//        }
//        Shell shell = HandlerUtil.getActiveShell(event);
//        EclipseUiHelper.prompt(message, shell);
        return null;
    }
}
