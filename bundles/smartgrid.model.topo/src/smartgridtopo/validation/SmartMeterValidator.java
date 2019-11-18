/**
 *
 * $Id$
 */
package smartgridtopo.validation;

import smartgridtopo.SmartMeterType;

/**
 * A sample validator interface for {@link smartgridtopo.SmartMeter}. This doesn't really do
 * anything, and it's not a real EMF artifact. It was generated by the
 * org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can
 * be extended. This can be disabled with -vmargs
 * -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface SmartMeterValidator {
    boolean validate();

    boolean validateIsA(SmartMeterType value);

    boolean validateAggregation(int value);
}