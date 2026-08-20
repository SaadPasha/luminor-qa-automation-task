package api.tests;

import api.client.PetApiClient;
import api.factory.PetDataFactory;
import api.model.Pet;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;

class PetCrudFlowTest {

    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int NOT_FOUND_STATUS_CODE = 404;

    private final PetApiClient petApiClient = new PetApiClient();

    @Test
    @Tag("sanity")
    @Tag("regression")
    @Feature("Pet API")
    @Story("Pet CRUD lifecycle")
    @DisplayName("Pet can be created, retrieved, updated and deleted")
    @Description("""
            Verifies the complete pet lifecycle:
            create, retrieve, update, verify persistence, delete,
            and confirm that the deleted pet no longer exists.
            """)
    void shouldCompletePetCrudFlow() {
        Pet petRequest = PetDataFactory.createPet();
        long petId = petRequest.getId();

        boolean cleanupRequired = false;

        try {
            // Create pet
            Response createResponse =
                    petApiClient.createPet(petRequest);

            cleanupRequired =
                    createResponse.statusCode() == SUCCESS_STATUS_CODE;

            step(
                    "Verify that the create-pet response is successful",
                    () -> assertResponseStatus(
                            createResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            step(
                    "Verify that the created pet matches the submitted details",
                    () -> assertPetDetails(createResponse, petRequest)
            );

            // Retrieve created pet
            Response retrieveResponse =
                    petApiClient.getPet(petId);

            step(
                    "Verify that the retrieve-pet response is successful",
                    () -> assertResponseStatus(
                            retrieveResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            step(
                    "Verify that the retrieved pet matches the created pet",
                    () -> assertPetDetails(retrieveResponse, petRequest)
            );

            // Update pet
            Pet updatedPet =
                    PetDataFactory.createUpdatedPet(petRequest);

            Response updateResponse =
                    petApiClient.updatePet(updatedPet);

            step(
                    "Verify that the update-pet response is successful",
                    () -> assertResponseStatus(
                            updateResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            step(
                    "Verify that the update response contains the new pet details",
                    () -> assertPetDetails(updateResponse, updatedPet)
            );

            // Retrieve pet again to verify persistence
            Response retrieveUpdatedResponse =
                    petApiClient.getPet(petId);

            step(
                    "Verify that the updated pet can be retrieved successfully",
                    () -> assertResponseStatus(
                            retrieveUpdatedResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            step(
                    "Verify that the updated pet details were persisted",
                    () -> assertPetDetails(
                            retrieveUpdatedResponse,
                            updatedPet
                    )
            );

            // Delete pet
            Response deleteResponse =
                    petApiClient.deletePet(petId);

            step(
                    "Verify that the delete-pet response is successful",
                    () -> assertResponseStatus(
                            deleteResponse,
                            SUCCESS_STATUS_CODE
                    )
            );

            // Confirm that the pet was deleted
            Response retrieveDeletedResponse =
                    petApiClient.getPet(petId);

            step(
                    "Verify that the deleted pet no longer exists",
                    () -> assertResponseStatus(
                            retrieveDeletedResponse,
                            NOT_FOUND_STATUS_CODE
                    )
            );

            cleanupRequired = false;

        } finally {
            if (cleanupRequired) {
                petApiClient.deletePet(petId);
            }
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

    private void assertPetDetails(
            Response response,
            Pet expectedPet
    ) {
        response.then()
                .log().ifValidationFails()
                .body("id", equalTo(expectedPet.getId()))
                .body("name", equalTo(expectedPet.getName()))
                .body(
                        "photoUrls",
                        equalTo(expectedPet.getPhotoUrls())
                )
                .body("status", equalTo(expectedPet.getStatus()));
    }
}