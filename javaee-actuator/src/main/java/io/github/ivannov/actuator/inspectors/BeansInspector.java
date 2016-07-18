package io.github.ivannov.actuator.inspectors;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author Ivan St. Ivanov.
 */
@RequestScoped
public class BeansInspector {

    @Inject
    private BeanManager beanManager;

    public Set<Bean<?>> getBeans() {
        return beanManager.getBeans(Object.class,new AnnotationLiteral<Any>() {});
    }
}
