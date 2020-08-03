package org.thehellnet.lanparty.manager.configuration.mixin;

public interface TestAwareConfiguration {

    boolean runningTest = "true".equals(System.getProperty("org.thehellnet.lanparty.manager.testenabled"));
}
