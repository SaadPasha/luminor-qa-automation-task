package api.factory;

import api.model.Category;
import api.model.Pet;
import api.model.Tag;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class PetDataFactory {

    private PetDataFactory() {
    }

    public static Pet createPet() {
//        long uniqueId =
//                System.currentTimeMillis() * 1_000
//                        + ThreadLocalRandom.current().nextInt(1_000);

        return Pet.builder()
                .id(generateUniqueId())
                .name("Test Pet " + UUID.randomUUID())
                .photoUrls(List.of("https://example.com/test-pet.jpg"))
                .status("available")
                .build();
    }

    public static Pet createUpdatedPet(Pet existingPet) {
        return existingPet.toBuilder()
                .name("Updated Pet " + UUID.randomUUID())
                .status("sold")
                .build();
    }

    public static Pet createPetWithOptionalFields() {
        return createPet().toBuilder()
                .category(
                        Category.builder()
                                .id(generateUniqueId())
                                .name("Dogs")
                                .build()
                )
                .tags(List.of(
                        Tag.builder()
                                .id(generateUniqueId())
                                .name("friendly")
                                .build(),
                        Tag.builder()
                                .id(generateUniqueId())
                                .name("trained")
                                .build()
                ))
                .build();
    }

    public static Pet createPetWithUpdatedOptionalFields(Pet existingPet) {
        return existingPet.toBuilder()
                .category(
                        Category.builder()
                                .id(generateUniqueId())
                                .name("Working dogs")
                                .build()
                )
                .tags(List.of(
                        Tag.builder()
                                .id(generateUniqueId())
                                .name("service-dog")
                                .build()
                ))
                .build();
    }

    private static long generateUniqueId() {
        return System.currentTimeMillis() * 1_000
                + ThreadLocalRandom.current().nextInt(1_000);
    }
}