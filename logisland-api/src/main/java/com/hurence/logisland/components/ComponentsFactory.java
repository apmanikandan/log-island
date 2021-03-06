package com.hurence.logisland.components;

import com.hurence.logisland.config.ComponentConfiguration;
import com.hurence.logisland.config.LogislandSessionConfiguration;
import com.hurence.logisland.engine.StandardEngineInstance;
import com.hurence.logisland.engine.StreamProcessingEngine;
import com.hurence.logisland.log.AbstractLogParser;
import com.hurence.logisland.log.StandardParserInstance;
import com.hurence.logisland.processor.AbstractEventProcessor;
import com.hurence.logisland.processor.EventProcessor;
import com.hurence.logisland.processor.StandardProcessorInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Created by tom on 04/07/16.
 */
public final class ComponentsFactory {

    private static Logger logger = LoggerFactory.getLogger(ComponentsFactory.class);

    private static final AtomicLong currentId = new AtomicLong(0);


    public static Optional<StandardEngineInstance> getEngineInstance(LogislandSessionConfiguration sessionConf) {
        return sessionConf.getComponents().stream()
                .filter(config -> config.getType().equalsIgnoreCase("engine"))
                .map(ComponentsFactory::getEngineInstance)
                .filter(component -> component != null)
                .findFirst();
    }

    public static List<StandardProcessorInstance> getAllProcessorInstances(LogislandSessionConfiguration sessionConf) {
        return sessionConf.getComponents().stream()
                .filter(config -> config.getType().equalsIgnoreCase("processor"))
                .map(ComponentsFactory::getProcessorInstance)
                .filter(component -> component != null)
                .collect(Collectors.toList());
    }

    public static StandardProcessorInstance getProcessorInstance(ComponentConfiguration configuration) {



        switch (configuration.getType().toLowerCase()) {
            case "processor":

                try {
                    final AbstractEventProcessor processor = (AbstractEventProcessor) Class.forName(configuration.getComponent()).newInstance();
                    final StandardProcessorInstance instance = new StandardProcessorInstance(processor, Long.toString(currentId.incrementAndGet()));


                    configuration.getConfiguration()
                            .entrySet()
                            .stream()
                            .forEach(e -> instance.setProperty(e.getKey(), e.getValue()));

                    logger.info("created processor {}", configuration.getComponent());

                    return instance;

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    logger.error("unable to instanciate processor {} : {}", configuration.getComponent(), e.toString());
                }

                break;

            default:
                logger.error("unsupported component type {}", configuration.getType());
        }
        return null;
    }

    public static StandardEngineInstance getEngineInstance(ComponentConfiguration configuration) {

        switch (configuration.getType().toLowerCase()) {
            case "engine":

                try {
                    final StreamProcessingEngine processor = (StreamProcessingEngine) Class.forName(configuration.getComponent()).newInstance();
                    final StandardEngineInstance engineInstance = new StandardEngineInstance(processor, Long.toString(currentId.incrementAndGet()));


                    configuration.getConfiguration()
                            .entrySet()
                            .stream()
                            .forEach(e -> engineInstance.setProperty(e.getKey(), e.getValue()));

                    logger.info("created engine {}", configuration.getComponent());

                    return engineInstance;

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    logger.error("unable to instanciate engine {} : {}", configuration.getComponent(), e.toString());
                }

                break;

            default:
                logger.error("unsupported component type {}", configuration.getType());
        }
        return null;
    }




    public static List<StandardParserInstance> getAllParserInstances(LogislandSessionConfiguration sessionConf) {
        return sessionConf.getComponents().stream()
                .filter(config -> config.getType().equalsIgnoreCase("parser"))
                .map(ComponentsFactory::getParserInstance)
                .filter(component -> component != null)
                .collect(Collectors.toList());
    }

    public static StandardParserInstance getParserInstance(ComponentConfiguration configuration) {



        switch (configuration.getType().toLowerCase()) {
            case "parser":

                try {
                    final AbstractLogParser parser = (AbstractLogParser) Class.forName(configuration.getComponent()).newInstance();
                    final StandardParserInstance instance = new StandardParserInstance(parser, Long.toString(currentId.incrementAndGet()));


                    configuration.getConfiguration()
                            .entrySet()
                            .stream()
                            .forEach(e -> instance.setProperty(e.getKey(), e.getValue()));

                    logger.info("created parser {}", configuration.getComponent());

                    return instance;

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    logger.error("unable to instanciate parser {} : {}", configuration.getComponent(), e.toString());
                }

                break;

            default:
                logger.error("unsupported component type {}", configuration.getType());
        }
        return null;
    }
}
