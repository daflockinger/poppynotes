package com.flockinger.poppynotes.notesService.api;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import com.jayway.restassured.response.ResponseOptions;

public class ApiContractTest extends BaseContractTest {

	@Test
	public void validate_apiV1NotesGet_withCorrectRequestNonPaginated_ReturnDefaultCorrect() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).array().contains("pinned").isEqualTo(true);
			assertThatJson(parsedJson).array().contains("lastEdit").matches(".+");
			assertThatJson(parsedJson).array().contains("archived").isNull();
			assertThatJson(parsedJson).array().contains("pinned").isEqualTo(false);
			assertThatJson(parsedJson).array().contains("title").isEqualTo("2-pinned-second");
			assertThatJson(parsedJson).array().contains("title").isEqualTo("1latest");
			assertThatJson(parsedJson).array().contains("id").matches(".+");
			assertThatJson(parsedJson).array().contains("partContent").matches(".+");
			assertThatJson(parsedJson).array().contains("title").isEqualTo("3-pinned-third");
			assertThatJson(parsedJson).array().contains("title").isEqualTo("2second");
	}

	@Test
	public void validate_apiV1NotesGet_withCorrectRequestPaginatedShorArchived_ReturnArchivedCorrect() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes?page=1&items=2&showArchived=true");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).array().contains("pinned").isEqualTo(true);
			assertThatJson(parsedJson).array().contains("lastEdit").matches(".+");
			assertThatJson(parsedJson).array().contains("archived").isNull();
			assertThatJson(parsedJson).array().contains("pinned").isEqualTo(false);
			assertThatJson(parsedJson).array().contains("id").matches(".+");
			assertThatJson(parsedJson).array().contains("partContent").matches(".+");
			assertThatJson(parsedJson).array().contains("title").isEqualTo("a3-pinned-third");
			assertThatJson(parsedJson).array().contains("title").isEqualTo("a1latest");
	}

	@Test
	public void validate_apiV1NotesGet_withCorrectRequestPaginated_ReturnCorrect() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes?page=1&items=2");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).array().contains("pinned").isEqualTo(true);
			assertThatJson(parsedJson).array().contains("lastEdit").matches(".+");
			assertThatJson(parsedJson).array().contains("archived").isNull();
			assertThatJson(parsedJson).array().contains("pinned").isEqualTo(false);
			assertThatJson(parsedJson).array().contains("title").isEqualTo("1latest");
			assertThatJson(parsedJson).array().contains("id").matches(".+");
			assertThatJson(parsedJson).array().contains("title").isEqualTo("3-pinned-third");
			assertThatJson(parsedJson).array().contains("partContent").matches(".+");
	}

	@Test
	public void validate_apiV1NotesGet_withMissingUserId_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	public void validate_apiV1NotesGet_withUserWithoutNotes_ReturnEmpty() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "38748");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
	}

	@Test
	public void validate_apiV1NotesNoteIdDelete_WithMissingUserId_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();

		// when:
			ResponseOptions<?> response = given().spec(request)
					.delete("/api/v1/notes/existingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	public void validate_apiV1NotesNoteIdDelete_withIdFromAnotherUser_ReturnForbidden() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1234");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.delete("/api/v1/notes/existingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(403);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesNoteIdDelete_withNotExistingId_ReturnNotFound() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.delete("/api/v1/notes/nonExista");

		// then:
			assertThat(response.statusCode()).isEqualTo(404);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesNoteIdDelete_withValidIdAndUserId_ReturnOkAndDelete() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.delete("/api/v1/notes/existingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
	}

	@Test
	public void validate_apiV1NotesNoteIdGet_WithMissingUserId_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes/existingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	public void validate_apiV1NotesNoteIdGet_withIdFromAnotherUser_ReturnForbidden() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1234");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes/existingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(403);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesNoteIdGet_withNotExistingId_ReturnNotFound() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes/nonExista");

		// then:
			assertThat(response.statusCode()).isEqualTo(404);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesNoteIdGet_withValidArchivedIdAndUserId_ReturnArchivedNote() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes/aexistingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("title").isEqualTo("a1latest");
			assertThatJson(parsedJson).field("id").matches(".+");
			assertThatJson(parsedJson).field("lastEdit").matches(".+");
			assertThatJson(parsedJson).field("archived").isEqualTo(true);
			assertThatJson(parsedJson).field("content").matches(".+");
	}

	@Test
	public void validate_apiV1NotesNoteIdGet_withValidIdAndUserId_ReturnNote() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.get("/api/v1/notes/existingNoteId");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("id").matches(".+");
			assertThatJson(parsedJson).field("lastEdit").matches(".+");
			assertThatJson(parsedJson).field("title").isEqualTo("1latest");
			assertThatJson(parsedJson).field("archived").isEqualTo(false);
			assertThatJson(parsedJson).field("content").matches(".+");
	}


	
	
	@Test
	public void validate_apiV1NotesPost_withMissingLastEdit_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"pinned\":true,\"userId\":1,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPost_withMissingPinned_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"userId\":1,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPost_withMissingTitle_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"pinned\":true,\"lastEdit\":\"2012-12-12T12:12:12Z\",\"userId\":1,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPost_withMissingUserId_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPost_withMissingInitVector_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
	}
	
	@Test
	public void validate_apiV1NotesPost_withValidNote_ReturnOKAndStore() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1,\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(201);
	}

	@Test
	public void validate_apiV1NotesPut_withMissingLastEdit_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"pinned\":true,\"userId\":1,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPut_withMissingPinned_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"userId\":1,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPut_withMissingTitle_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"pinned\":true,\"lastEdit\":\"2012-12-12T12:12:12Z\",\"userId\":1,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPut_withMissingUserId_ReturnBadRequest() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.post("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(400);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPut_withNonExistingNote_ReturnNotFound() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"id\":\"nonExista\",\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1,\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.put("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(404);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPut_withNoteOfOtherUser_ReturnForbidden() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"id\":\"existingNoteId\",\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1243,\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.put("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(403);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("code").isNull();
			assertThatJson(parsedJson).field("message").matches(".+");
	}

	@Test
	public void validate_apiV1NotesPut_withValidNote_ReturnOKAndStore() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("userId", "1")
					.header("Content-Type", "application/json")
					.body("{\"id\":\"existingNoteId\",\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1,\"initVector\":\"aldk8796\"}");

		// when:
			ResponseOptions<?> response = given().spec(request)
					.put("/api/v1/notes");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
	}
	
}
