package smartgrid.graphiti.customfeatures;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import smartgridinput.ScenarioState;
import smartgridsecurity.graphiti.extensionpoint.definition.IDeleteFeatureResolver;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;

public class InputDeleteFeature implements IDeleteFeatureResolver {

    @Override
    public void deleteBusinessObjects(final List<EObject> boFromDiagram, final EObject owner,
            final TransactionalEditingDomain domain) {
        ScenarioState state = null;
        for (final EObject ob : boFromDiagram) {
            if (ob instanceof ScenarioState) {
                state = (ScenarioState) ob;
            }
        }
        if (state != null) {
            int position = -1;
            if (owner instanceof PowerGridNode) {
                for (int i = 0; i < state.getPowerStates().size(); i++) {
                    if (state.getPowerStates().get(i).getOwner().getId() == ((PowerGridNode) owner).getId()) {
                        position = i;
                        break;
                    }
                }
                if (position > -1) {
                    this.removeElement(false, state, position, domain);
                }
            } else if (owner instanceof NetworkEntity) {
                for (int i = 0; i < state.getEntityStates().size(); i++) {
                    if (state.getEntityStates().get(i).getOwner().getId() == ((NetworkEntity) owner).getId()) {
                        position = i;
                        break;
                    }
                }
                if (position > -1) {
                    this.removeElement(true, state, position, domain);
                }
            }
        }
    }

    private void removeElement(final boolean networkEntity, final ScenarioState state, final int position,
            final TransactionalEditingDomain domain) {
        domain.getCommandStack().execute(new RecordingCommand(domain) {
            @Override
            protected void doExecute() {
                if (networkEntity) {
                    state.getEntityStates().remove(position);
                } else {
                    state.getPowerStates().remove(position);
                }
            }
        });
    }

}
