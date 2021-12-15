package edu.albert.studycards.resourceserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.albert.studycards.resourceserver.model.dto.CardDto;
import edu.albert.studycards.resourceserver.model.dto.LangPackDto;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import lombok.SneakyThrows;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.*;


/**
 * Util class that generates domain-model instances for testing based on json files.
 *
 * @param <T>
 */
public class ResourceProvider<T> {
    private static final DefaultResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Random RANDOM = new Random(12441252L);

    /**
     * To each resource type mapped its location in resources
     */
    private static final HashMap<Object, String> RSC_TYPE_TO_RSC_LOCATION = new HashMap<>();

    private static final String RSC_EXTENSION = ".json";

    private Class<T> clazz;

    static {
        RSC_TYPE_TO_RSC_LOCATION.put(CardDto.class, "model/Card/Dto/");
        RSC_TYPE_TO_RSC_LOCATION.put(CardPersistent.class, "model/Card/Persistent/");
        RSC_TYPE_TO_RSC_LOCATION.put(LangPackDto.class, "model/LangPack/Dto/");
        RSC_TYPE_TO_RSC_LOCATION.put(LangPackPersistent.class, "model/LangPack/Persistent/");
    }

    public ResourceProvider(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * @param isRandom are instances should be filled with random values
     * @return List with test instances
     */
    public List<T> generateResources(boolean isRandom) {
        List<T> instances = null;
        if (isRandom)
            findRandomRsc();
        else {
            List<Resource> resources = findResources();
            instances = createInstances(resources);
        }
        return instances;
    }

    public T generateResource() {
        final List<Resource> resources = findResources();
        final List<T> instances = createInstances(resources);
        return instances.get(0);
    }

    private List<Resource> findResources() {
        final String dirLocation = RSC_TYPE_TO_RSC_LOCATION.get(clazz);
        return loadResources(dirLocation);
    }

    private void findRandomRsc() {

    }

    @SneakyThrows
    private List<T> createInstances(List<Resource> resources) {
        final List<T> array = new ArrayList<>();
        for (final Resource res : resources) {
            final T t = OBJECT_MAPPER.readValue(res.getFile(), clazz);
            array.add(t);
        }
        return array;
    }

    @SneakyThrows
    private List<Resource> loadResources(final String rscLocation) {
        final int amount = countResources(rscLocation);
        List<Resource> resources = new ArrayList<>(amount);
        for (int i = 1; i <= amount; i++) {
            final String location = rscLocation + clazz.getSimpleName() + i + RSC_EXTENSION;
            final Resource resource = RESOURCE_LOADER.getResource(location);
            resources.add(resource);
        }
        return resources;
    }

    @SneakyThrows
    private int countResources(final String rscLocation) {
        final Resource resource = RESOURCE_LOADER.getResource(rscLocation);
        return Objects.requireNonNull(resource.getFile().list()).length;
    }
}
