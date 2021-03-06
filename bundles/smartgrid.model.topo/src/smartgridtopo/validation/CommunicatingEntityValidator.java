/**
 *
 * $Id$
 */
package smartgridtopo.validation;

import org.eclipse.emf.common.util.EList;

import smartgridtopo.LogicalCommunication;

/**
 * A sample validator interface for {@link smartgridtopo.CommunicatingEntity}. This doesn't really
 * do anything, and it's not a real EMF artifact. It was generated by the
 * org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can
 * be extended. This can be disabled with -vmargs
 * -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface CommunicatingEntityValidator {
    boolean validate();

    boolean validateCommunicatesBy(EList<LogicalCommunication> value);
}
