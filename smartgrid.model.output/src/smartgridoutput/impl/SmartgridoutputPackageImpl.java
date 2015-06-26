/**
 */
package smartgridoutput.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import smartgridoutput.Cluster;
import smartgridoutput.Destroyed;
import smartgridoutput.EntityState;
import smartgridoutput.NoPower;
import smartgridoutput.NoUplink;
import smartgridoutput.Offline;
import smartgridoutput.On;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridoutput.SmartgridoutputFactory;
import smartgridoutput.SmartgridoutputPackage;
import smartgridtopo.SmartgridtopoPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SmartgridoutputPackageImpl extends EPackageImpl implements SmartgridoutputPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass scenarioResultEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass entityStateEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass onlineEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass destroyedEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass noPowerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass noUplinkEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass offlineEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass clusterEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass onEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see smartgridoutput.SmartgridoutputPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SmartgridoutputPackageImpl() {
        super(eNS_URI, SmartgridoutputFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link SmartgridoutputPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SmartgridoutputPackage init() {
        if (isInited) return (SmartgridoutputPackage)EPackage.Registry.INSTANCE.getEPackage(SmartgridoutputPackage.eNS_URI);

        // Obtain or create and register package
        SmartgridoutputPackageImpl theSmartgridoutputPackage = (SmartgridoutputPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SmartgridoutputPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SmartgridoutputPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        SmartgridtopoPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theSmartgridoutputPackage.createPackageContents();

        // Initialize created meta-data
        theSmartgridoutputPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theSmartgridoutputPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(SmartgridoutputPackage.eNS_URI, theSmartgridoutputPackage);
        return theSmartgridoutputPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getScenarioResult() {
        return scenarioResultEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getScenarioResult_EntityStates() {
        return (EReference)scenarioResultEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getScenarioResult_Clusters() {
        return (EReference)scenarioResultEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getScenarioResult_Scenario() {
        return (EReference)scenarioResultEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEntityState() {
        return entityStateEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEntityState_Owner() {
        return (EReference)entityStateEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOnline() {
        return onlineEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDestroyed() {
        return destroyedEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNoPower() {
        return noPowerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNoUplink() {
        return noUplinkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOffline() {
        return offlineEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCluster() {
        return clusterEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCluster_ControlCenterCount() {
        return (EAttribute)clusterEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCluster_SmartMeterCount() {
        return (EAttribute)clusterEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCluster_HasEntities() {
        return (EReference)clusterEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOn() {
        return onEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOn_BelongsToCluster() {
        return (EReference)onEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOn_IsHacked() {
        return (EAttribute)onEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SmartgridoutputFactory getSmartgridoutputFactory() {
        return (SmartgridoutputFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        scenarioResultEClass = createEClass(SCENARIO_RESULT);
        createEReference(scenarioResultEClass, SCENARIO_RESULT__ENTITY_STATES);
        createEReference(scenarioResultEClass, SCENARIO_RESULT__CLUSTERS);
        createEReference(scenarioResultEClass, SCENARIO_RESULT__SCENARIO);

        entityStateEClass = createEClass(ENTITY_STATE);
        createEReference(entityStateEClass, ENTITY_STATE__OWNER);

        onlineEClass = createEClass(ONLINE);

        destroyedEClass = createEClass(DESTROYED);

        noPowerEClass = createEClass(NO_POWER);

        noUplinkEClass = createEClass(NO_UPLINK);

        offlineEClass = createEClass(OFFLINE);

        clusterEClass = createEClass(CLUSTER);
        createEAttribute(clusterEClass, CLUSTER__CONTROL_CENTER_COUNT);
        createEAttribute(clusterEClass, CLUSTER__SMART_METER_COUNT);
        createEReference(clusterEClass, CLUSTER__HAS_ENTITIES);

        onEClass = createEClass(ON);
        createEReference(onEClass, ON__BELONGS_TO_CLUSTER);
        createEAttribute(onEClass, ON__IS_HACKED);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        SmartgridtopoPackage theSmartgridtopoPackage = (SmartgridtopoPackage)EPackage.Registry.INSTANCE.getEPackage(SmartgridtopoPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        onlineEClass.getESuperTypes().add(this.getOn());
        destroyedEClass.getESuperTypes().add(this.getEntityState());
        destroyedEClass.getESuperTypes().add(this.getOffline());
        noPowerEClass.getESuperTypes().add(this.getEntityState());
        noPowerEClass.getESuperTypes().add(this.getOffline());
        noUplinkEClass.getESuperTypes().add(this.getOffline());
        noUplinkEClass.getESuperTypes().add(this.getOn());
        onEClass.getESuperTypes().add(this.getEntityState());

        // Initialize classes, features, and operations; add parameters
        initEClass(scenarioResultEClass, ScenarioResult.class, "ScenarioResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getScenarioResult_EntityStates(), this.getEntityState(), null, "EntityStates", null, 0, -1, ScenarioResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getScenarioResult_Clusters(), this.getCluster(), null, "Clusters", null, 0, -1, ScenarioResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getScenarioResult_Scenario(), theSmartgridtopoPackage.getScenario(), null, "Scenario", null, 0, 1, ScenarioResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(entityStateEClass, EntityState.class, "EntityState", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEntityState_Owner(), theSmartgridtopoPackage.getNetworkEntity(), null, "Owner", null, 1, 1, EntityState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(onlineEClass, Online.class, "Online", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(destroyedEClass, Destroyed.class, "Destroyed", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(noPowerEClass, NoPower.class, "NoPower", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(noUplinkEClass, NoUplink.class, "NoUplink", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(offlineEClass, Offline.class, "Offline", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(clusterEClass, Cluster.class, "Cluster", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCluster_ControlCenterCount(), ecorePackage.getEInt(), "ControlCenterCount", null, 0, 1, Cluster.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCluster_SmartMeterCount(), ecorePackage.getEInt(), "SmartMeterCount", null, 0, 1, Cluster.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCluster_HasEntities(), this.getOn(), this.getOn_BelongsToCluster(), "HasEntities", null, 0, -1, Cluster.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(onEClass, On.class, "On", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOn_BelongsToCluster(), this.getCluster(), this.getCluster_HasEntities(), "BelongsToCluster", null, 0, 1, On.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOn_IsHacked(), ecorePackage.getEBoolean(), "IsHacked", "false", 1, 1, On.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);
    }

} //SmartgridoutputPackageImpl
