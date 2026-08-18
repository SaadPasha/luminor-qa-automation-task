package api.client;

import api.model.Pet;
import config.TestConfig;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class PetApiClient {

    private final RequestSpecification requestSpecification =
            given()
                    .baseUri(TestConfig.apiBaseUrl())
                    .contentType(JSON)
                    .accept(JSON)
                    .filter(new AllureRestAssured());

    @Step("Create a new pet using POST /pet")
    public Response createPet(Pet pet) {
        return given()
                .spec(requestSpecification)
                .body(pet)
                .when()
                .post("/pet");
    }

    @Step("Retrieve pet with ID {petId}")
    public Response getPet(long petId) {
        return given()
                .spec(requestSpecification)
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}");
    }

    @Step("Update pet details using PUT /pet")
    public Response updatePet(Pet pet) {
        return given()
                .spec(requestSpecification)
                .body(pet)
                .when()
                .put("/pet");
    }

    @Step("Delete pet with ID {petId}")
    public Response deletePet(long petId) {
        return given()
                .spec(requestSpecification)
                .pathParam("petId", petId)
                .when()
                .delete("/pet/{petId}");
    }
}