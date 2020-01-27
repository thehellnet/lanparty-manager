package org.thehellnet.lanparty.manager.model.persistence.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface Visibility {

    enum Size {
        EXTRA_SMALL("xs"),
        SMALL("xm"),
        MEDIUM("md"),
        LARGE("lg"),
        EXTRA_LARGE("xl");

        private final String tag;

        Size(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return tag;
        }
    }

    Size value() default Size.EXTRA_SMALL;
}
