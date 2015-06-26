package smartgridsecurity.graphiti;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;

import smartgridsecurity.graphiti.deletefeatures.ConnectionDeleteFeature;
import smartgridsecurity.graphiti.extensionpoint.evaluation.EvaluateContextButtons;
import smartgridsecurity.graphiti.helper.ExtensionPointRegistry;
import smartgridsecurity.graphiti.helper.GraphitiHelper;
import smartgridsecurity.graphiti.patterns.ControlCenterPattern;
import smartgridsecurity.graphiti.patterns.GenericControllerPattern;
import smartgridsecurity.graphiti.patterns.InterComPattern;
import smartgridsecurity.graphiti.patterns.NetworkNodePattern;
import smartgridsecurity.graphiti.patterns.PowerGridNodePattern;
import smartgridsecurity.graphiti.patterns.SmartMeterPattern;
import smartgridsecurity.graphiti.patterns.TextCommentPattern;
import smartgridsecurity.graphiti.patterns.connections.AddPowerConnectionFeature;
import smartgridsecurity.graphiti.patterns.connections.CreatePowerConnectionFeature;
import smartgridsecurity.graphiti.patterns.connections.DeletePowerConnectionFeature;
import smartgridsecurity.graphiti.patterns.connections.LogicalCommunicationPattern;
import smartgridsecurity.graphiti.patterns.connections.PhysicalConnectionPattern;
import smartgridsecurity.graphiti.patterns.connections.RemovePowerConnectionFeature;
import smartgridsecurity.graphiti.resizefeatures.RestrictResizePictogramFeature;
import smartgridtopo.NetworkEntity;
import smartgridtopo.PowerGridNode;

/**
 * This class implements the SGS feature provider.
 * 
 * @author mario
 *
 */
public class SGSFeatureProvider extends DefaultFeatureProviderWithPatterns {

    public SGSFeatureProvider(final IDiagramTypeProvider dtp) {
        super(dtp);
        GraphitiHelper.getInstance().setFeatureProvider(this);
        GraphitiHelper.getInstance().setDiagram(getDiagramTypeProvider().getDiagram());

        this.addPattern(new SmartMeterPattern());
        this.addPattern(new GenericControllerPattern());
        this.addPattern(new InterComPattern());
        this.addPattern(new ControlCenterPattern());
        this.addPattern(new NetworkNodePattern());
        this.addPattern(new PowerGridNodePattern());
        this.addPattern(new TextCommentPattern());
        addContextButtons();
        // atm not used
        // addResizeFeatures();

        // Warning: add new connection also to TextCommentPattern.isMainBusinessObjectApplicable
        this.addConnectionPattern(new PhysicalConnectionPattern());
        this.addConnectionPattern(new LogicalCommunicationPattern());
    }

    /**
     * Retrieves all ContextButtons, which were found by EvaluateContextButtons extension point and
     * adds them to ExtensionPointRegistry.
     */
    private void addContextButtons() {
        ExtensionPointRegistry.getInstance().clearContextButtons();
        List<AbstractCustomFeature> extensionCustomButtons = (new EvaluateContextButtons(this))
                .evaluateFeatureExtension(Platform.getExtensionRegistry());
        ExtensionPointRegistry.getInstance().addContextButtonsToRegistry(extensionCustomButtons);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns#getAddFeature
     * (org.eclipse.graphiti.features.context.IAddContext)
     */
    @Override
    public IAddFeature getAddFeature(IAddContext context) {
        if (context instanceof IAddConnectionContext && context.getNewObject() instanceof Object[]) {
            Object[] objects = (Object[]) context.getNewObject();
            if (objects[0] instanceof NetworkEntity && objects[1] instanceof PowerGridNode) {
                return new AddPowerConnectionFeature(this);
            }
        }
        return super.getAddFeature(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns#
     * getCreateConnectionFeatures()
     */
    @Override
    public ICreateConnectionFeature[] getCreateConnectionFeatures() {
        int length = super.getCreateConnectionFeatures().length;
        ICreateConnectionFeature[] createFeature = new ICreateConnectionFeature[length + 1];
        System.arraycopy(super.getCreateConnectionFeatures(), 0, createFeature, 0, length);
        createFeature[length] = new CreatePowerConnectionFeature(this);
        return createFeature;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns#
     * getRemoveFeature(org.eclipse.graphiti.features.context.IRemoveContext)
     */
    @Override
    public IRemoveFeature getRemoveFeature(IRemoveContext context) {
        if (context.getPictogramElement() instanceof Connection) {
            Connection con = (Connection) context.getPictogramElement();
            NetworkEntity start = getNetworkEntity(con.getStart());
            PowerGridNode end = getPowerGridNode(con.getEnd());

            if (start != null && end != null) {
                return new RemovePowerConnectionFeature(this);
            }
        }
        return super.getRemoveFeature(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns#
     * getDeleteFeature(org.eclipse.graphiti.features.context.IDeleteContext)
     */
    @Override
    public IDeleteFeature getDeleteFeature(IDeleteContext context) {
        if (context.getPictogramElement() instanceof Connection) {
            Connection con = (Connection) context.getPictogramElement();
            NetworkEntity start = getNetworkEntity(con.getStart());
            PowerGridNode end = getPowerGridNode(con.getEnd());

            if (start != null && end != null) {
                return new DeletePowerConnectionFeature(this);
            }
        } else if (context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof NetworkEntity) {
            return new ConnectionDeleteFeature(this);
        }
        return super.getDeleteFeature(context);
    }

    /**
     * Get a {@link Anchor}'s parent interface.
     * 
     * @param anchor
     *            The {@link Anchor}.
     * @return The {@link NetworkEntity}.
     */
    private NetworkEntity getNetworkEntity(Anchor anchor) {
        if (anchor != null) {
            Object object = getBusinessObjectForPictogramElement(anchor.getParent());
            if (object instanceof NetworkEntity) {
                return (NetworkEntity) object;
            }
        }
        return null;
    }

    /**
     * Get a {@link Anchor}'s parent interface.
     * 
     * @param anchor
     *            The {@link Anchor}.
     * @return The {@link PowerGridNode}.
     */
    private PowerGridNode getPowerGridNode(Anchor anchor) {
        if (anchor != null) {
            Object object = getBusinessObjectForPictogramElement(anchor.getParent());
            if (object instanceof PowerGridNode) {
                return (PowerGridNode) object;
            }
        }
        return null;
    }

    @Override
    public ICustomFeature[] getCustomFeatures(ICustomContext context) {
        return super.getCustomFeatures(context);
    }

    @Override
    public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
        return new RestrictResizePictogramFeature(this);
    }

}
