package api.tests;

import api.client.PetApiClient;
import api.factory.PetDataFactory;
import api.model.Pet;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@Tag("regression")
@Feature("Pet API")
@Story("Optional pet fields")
class PetOptionalFieldsTest {

    private static final int SUCCESS_STATUS_CODE = 200;

    private final PetApiClient petApiClient = new PetApiClient();

    @Test
    @DisplayName("Pet can be created with category and tags")
    @Description("""
            Verifies that optional category and tags can be submitted
            and are persisted when the pet is retrieved.
            """)
    void shouldCreatePetWithOptionalFields() {
        Pet petRequest = PetDataFactory.createPetWithOptionalFields();
        long petId = petRequest.getId();

        try {
            Response createResponse =
                    petApiClient.createPet(petRequest);

            Allure.step(
                    "Verify that the pet with optional fields was created successfully",
                    () -> assertResponseStatus(
                            createResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            Allure.step(
                    "Verify that the create response contains category and tags",
                    () -> assertOptionalFields(
                            createResponse,
                            petRequest
                    )
            );

            Response retrieveResponse =
                    petApiClient.getPet(petId);

            Allure.step(
                    "Verify that the pet with optional fields can be retrieved",
                    () -> assertResponseStatus(
                            retrieveResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            Allure.step(
                    "Verify that category and tags were persisted",
                    () -> assertOptionalFields(
                            retrieveResponse,
                            petRequest
                    )
            );
        } finally {
            petApiClient.deletePet(petId);
        }
    }

    @Test
    @DisplayName("Optional pet fields can be updated")
    @Description("""
            Verifies that category and tags can be updated
            and that the updated values are persisted.
            """)
    void shouldUpdateOptionalPetFields() {
        Pet originalPet =
                PetDataFactory.createPetWithOptionalFields();

        long petId = originalPet.getId();

        try {
            Response createResponse =
                    petApiClient.createPet(originalPet);

            Allure.step(
                    "Verify that the test pet was created successfully",
                    () -> assertResponseStatus(
                            createResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            Pet updatedPet =
                    PetDataFactory.createPetWithUpdatedOptionalFields(
                            originalPet
                    );

            Response updateResponse =
                    petApiClient.updatePet(updatedPet);

            Allure.step(
                    "Verify that the optional fields were updated successfully",
                    () -> assertResponseStatus(
                            updateResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            Allure.step(
                    "Verify that the update response contains the new optional fields",
                    () -> assertOptionalFields(
                            updateResponse,
                            updatedPet
                    )
            );

            Response retrieveResponse =
                    petApiClient.getPet(petId);

            Allure.step(
                    "Verify that the updated pet can be retrieved",
                    () -> assertResponseStatus(
                            retrieveResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            Allure.step(
                    "Verify that the updated category and tags were persisted",
                    () -> assertOptionalFields(
                            retrieveResponse,
                            updatedPet
                    )
            );
        } finally {
            petApiClient.deletePet(petId);
        }
    }

    private void assertResponseStatus(
            Response response,
            int expectedStatusCode
    ) {
        response.then()
                .log().ifValidationFails()
                .statusCode(expectedStatusCode);
    }

    private void assertOptionalFields(
            Response response,
            Pet expectedPet
    ) {
        response.then()
                .log().ifValidationFails()
                .body(
                        "category.id",
                        equalTo(expectedPet.getCategory().getId())
                )
                .body(
                        "category.name",
                        equalTo(expectedPet.getCategory().getName())
                )
                .body(
                        "tags.id",
                        equalTo(
                                expectedPet.getTags()
                                        .stream()
                                        .map(tag -> tag.getId())
                                        .toList()
                        )
                )
                .body(
                        "tags.name",
                        equalTo(
                                expectedPet.getTags()
                                        .stream()
                                        .map(tag -> tag.getName())
                                        .toList()
                        )
                );
    }
}