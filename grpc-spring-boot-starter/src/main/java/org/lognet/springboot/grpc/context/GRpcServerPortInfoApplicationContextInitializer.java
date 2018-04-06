package org.lognet.springboot.grpc.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class GRpcServerPortInfoApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addApplicationListener((ApplicationListener<GRpcServerInitializedEvent>) event ->
                GRpcServerPortInfoApplicationContextInitializer.this.onApplicationEvent(applicationContext, event)
        );
    }

    private void onApplicationEvent(ConfigurableApplicationContext applicationContext, GRpcServerInitializedEvent event) {
        MutablePropertySources sources = applicationContext.getEnvironment().getPropertySources();
        PropertySource<?> source = sources.get("server.ports");
        if (source == null) {
            source = new MapPropertySource("server.ports", new HashMap<>());
            sources.addFirst(source);
        }
        ((Map<String, Object>) source.getSource()).put("local.grpc.port", event.getServer().getPort());
    }

}
