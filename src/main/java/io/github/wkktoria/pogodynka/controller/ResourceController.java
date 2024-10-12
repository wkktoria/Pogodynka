package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.service.ResourceService;
import org.apache.commons.text.WordUtils;
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

    public String getByKey(final String key, final Case resultCase) {
        try {
            switch (resultCase) {
                case SENTENCE_CASE -> {
                    var result = resourceService.getByKey(key);
                    return result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
                }
                case LOWER_CASE -> {
                    return resourceService.getByKey(key).toLowerCase();
                }
                case UPPER_CASE -> {
                    return resourceService.getByKey(key).toUpperCase();
                }
                case CAPITALIZED_CASE -> {
                    return WordUtils.capitalizeFully(resourceService.getByKey(key));
                }
                default -> {
                    return resourceService.getByKey(key);
                }
            }
        } catch (MissingResourceException e) {
            LOGGER.error("Cannot get resource: {}", key);
            return null;
        }
    }

    public enum Case {
        SENTENCE_CASE,
        LOWER_CASE,
        UPPER_CASE,
        CAPITALIZED_CASE
    }
}
