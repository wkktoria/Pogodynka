package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.MissingResourceException;

public class ResourceController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);
    private final ResourceService resourceService;

    public ResourceController(final ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public String getByKey(final String key) {
        try {
            return resourceService.getByKey(key);
        } catch (MissingResourceException e) {
            LOGGER.error("Cannot get resource: {}", key);
            return null;
        }
    }
}
