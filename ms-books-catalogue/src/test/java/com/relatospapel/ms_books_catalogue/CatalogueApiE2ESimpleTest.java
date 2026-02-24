package com.relatospapel.ms_books_catalogue;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

/**
 * Pruebas E2E (End-to-End) simples para la API REST del microservicio ms-books-catalogue.
 * 
 * <p>Estas pruebas verifican los flujos principales de:
 * <ul>
 *   <li>Gestión de categorías (crear y listar)</li>
 *   <li>Gestión de libros (crear, listar, eliminar)</li>
 *   <li>Búsqueda/filtrado de libros</li>
 *   <li>Validaciones negativas</li>
 * </ul>
 * 
 * <p><b>Requisitos previos:</b>
 * <ul>
 *   <li>El microservicio debe estar ejecutándose en el puerto 8081</li>
 *   <li>La base de datos debe estar disponible y limpia</li>
 *   <li>Por defecto, se usa http://localhost:8081</li>
 *   <li>Configurar con -DbaseUrl para cambiar la URL</li>
 * </ul>
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("Catálogo de Libros")
public class CatalogueApiE2ESimpleTest {

  private static String category1Id;
  private static String category2Id;
  private static String book1Id;
  private static String book2Id;
  private static String baseUrl;
  private static final String UNIQUE_SUFFIX = String.valueOf(System.currentTimeMillis());

  @BeforeAll
  @Step("Configurar base URL y REST Assured")
  @SuppressWarnings("unused")
  static void setUp() {
    baseUrl = System.getProperty("baseUrl", "http://localhost:8081");
    RestAssured.baseURI = baseUrl;
    RestAssured.filters(new AllureRestAssured());
  }

  @Test
  @Order(1)
  @Feature("Categorías")
  @Description("Flujo completo de categorías: crear 2 categorías y listarlas")
  void category_flow() {
    // Crear primera categoría
    Map<String, Object> category1 = new HashMap<>();
    category1.put("name", "Ciencia Ficción Test " + UNIQUE_SUFFIX);
    category1.put("description", "Libros de ciencia ficción y futurismo");
    
    category1Id = given()
        .contentType(ContentType.JSON)
        .body(category1)
      .when()
        .post("/api/v1/catalogue/categories")
      .then()
        .statusCode(201)
        .body("name", equalTo("Ciencia Ficción Test " + UNIQUE_SUFFIX))
        .body("description", equalTo("Libros de ciencia ficción y futurismo"))
        .body("id", notNullValue())
      .extract()
        .path("id");
    
    // Crear segunda categoría
    Map<String, Object> category2 = new HashMap<>();
    category2.put("name", "Fantasía Test " + UNIQUE_SUFFIX);
    category2.put("description", "Libros de fantasía épica");
    
    category2Id = given()
        .contentType(ContentType.JSON)
        .body(category2)
      .when()
        .post("/api/v1/catalogue/categories")
      .then()
        .statusCode(201)
        .body("name", equalTo("Fantasía Test " + UNIQUE_SUFFIX))
        .body("id", notNullValue())
      .extract()
        .path("id");
    
    // Listar todas las categorías y verificar que aparecen
    given()
      .when()
        .get("/api/v1/catalogue/categories")
      .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(2)))
        .body("id", hasItem(category1Id))
        .body("id", hasItem(category2Id));
  }

  @Test
  @Order(2)
  @Feature("Libros")
  @Description("Flujo completo de libros: crear 2 libros, listar, eliminar 1 y verificar")
  void book_flow() {
    // Crear primer libro con ISBN único
    Map<String, Object> book1 = new HashMap<>();
    book1.put("title", "Dune Test " + UNIQUE_SUFFIX);
    book1.put("author", "Frank Herbert");
    book1.put("isbn", "ISBN-DUNE-" + UNIQUE_SUFFIX);
    book1.put("rating", 5);
    book1.put("visible", true);
    book1.put("stock", 10);
    book1.put("publicationDate", "1965-08-01");
    book1.put("categoryId", category1Id);
    
    book1Id = given()
        .contentType(ContentType.JSON)
        .body(book1)
      .when()
        .post("/api/v1/catalogue/books")
      .then()
        .statusCode(201)
        .body("title", equalTo("Dune Test " + UNIQUE_SUFFIX))
        .body("author", equalTo("Frank Herbert"))
        .body("id", notNullValue())
      .extract()
        .path("id");
    
    // Crear segundo libro
    Map<String, Object> book2 = new HashMap<>();
    book2.put("title", "El Señor de los Anillos Test " + UNIQUE_SUFFIX);
    book2.put("author", "J.R.R. Tolkien");
    book2.put("isbn", "ISBN-LOTR-" + UNIQUE_SUFFIX);
    book2.put("rating", 5);
    book2.put("visible", true);
    book2.put("stock", 15);
    book2.put("publicationDate", "1954-07-29");
    book2.put("categoryId", category2Id);
    
    book2Id = given()
        .contentType(ContentType.JSON)
        .body(book2)
      .when()
        .post("/api/v1/catalogue/books")
      .then()
        .statusCode(201)
        .body("title", equalTo("El Señor de los Anillos Test " + UNIQUE_SUFFIX))
        .body("author", equalTo("J.R.R. Tolkien"))
        .body("id", notNullValue())
      .extract()
        .path("id");
    
    // Listar todos los libros y verificar que aparecen ambos
    given()
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(2)))
        .body("id", hasItem(book1Id))
        .body("id", hasItem(book2Id));
    
    // Eliminar el primer libro
    given()
      .when()
        .delete("/api/v1/catalogue/books/" + book1Id)
      .then()
        .statusCode(204);
    
    // Listar otra vez y verificar que el eliminado ya no aparece
    given()
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("id", not(hasItem(book1Id)))
        .body("id", hasItem(book2Id));
  }

  @Test
  @Order(3)
  @Feature("Búsqueda")
  @Description("Verificar endpoint de búsqueda/filtrado de libros por diferentes criterios")
  void search_flow() {
    // Crear un libro adicional para las pruebas de búsqueda
    Map<String, Object> book3 = new HashMap<>();
    book3.put("title", "1984 Test " + UNIQUE_SUFFIX);
    book3.put("author", "George Orwell");
    book3.put("isbn", "ISBN-1984-" + UNIQUE_SUFFIX);
    book3.put("rating", 4);
    book3.put("visible", true);
    book3.put("stock", 20);
    book3.put("publicationDate", "1949-06-08");
    book3.put("categoryId", category1Id);
    
    String book3Id = given()
        .contentType(ContentType.JSON)
        .body(book3)
      .when()
        .post("/api/v1/catalogue/books")
      .then()
        .statusCode(201)
      .extract()
        .path("id");
    
    // Búsqueda por título (debe encontrar "1984")
    given()
        .queryParam("title", "1984")
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(1)))
        .body("id", hasItem(book3Id));
    
    // Búsqueda por autor (debe encontrar "George Orwell")
    given()
        .queryParam("author", "Orwell")
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(1)))
        .body("author", hasItem(containsString("Orwell")))
        .body("id", hasItem(book3Id));
    
    // Búsqueda por categoría (Ciencia Ficción, debe incluir "1984" pero NO "El Señor de los Anillos")
    given()
        .queryParam("categoryId", category1Id)
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("id", hasItem(book3Id))
        .body("id", not(hasItem(book2Id))); 
    
    // Búsqueda por ISBN exacto
    given()
        .queryParam("isbn", "ISBN-1984-" + UNIQUE_SUFFIX)
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(1)))
        .body("id", hasItem(book3Id));
    
    // Búsqueda por rating
    given()
        .queryParam("rating", 4)
      .when()
        .get("/api/v1/catalogue/books")
      .then()
        .statusCode(200)
        .body("rating", everyItem(equalTo(4)));
    
    // Limpiar: eliminar el libro adicional creado
    given()
      .when()
        .delete("/api/v1/catalogue/books/" + book3Id)
      .then()
        .statusCode(204);
  }

  @Test
  @Order(4)
  @Feature("Validaciones")
  @Description("Verificar validaciones negativas: crear categoría y libro sin campos obligatorios")
  void negative_validation() {
    // Intentar crear categoría sin nombre (campo obligatorio)
    Map<String, Object> invalidCategory = new HashMap<>();
    invalidCategory.put("description", "Categoría sin nombre");
    // name está ausente
    
    given()
        .contentType(ContentType.JSON)
        .body(invalidCategory)
      .when()
        .post("/api/v1/catalogue/categories")
      .then()
        .statusCode(anyOf(equalTo(400), equalTo(422)));
    
    // Intentar crear categoría con nombre vacío
    Map<String, Object> emptyNameCategory = new HashMap<>();
    emptyNameCategory.put("name", "");
    emptyNameCategory.put("description", "Categoría con nombre vacío");
    
    given()
        .contentType(ContentType.JSON)
        .body(emptyNameCategory)
      .when()
        .post("/api/v1/catalogue/categories")
      .then()
        .statusCode(anyOf(equalTo(400), equalTo(422)));
    
    // Intentar crear libro sin título (campo obligatorio)
    Map<String, Object> invalidBook = new HashMap<>();
    invalidBook.put("author", "Autor Test");
    invalidBook.put("isbn", "123456789");
    invalidBook.put("rating", 3);
    invalidBook.put("visible", true);
    invalidBook.put("stock", 5);
    // title está ausente
    
    given()
        .contentType(ContentType.JSON)
        .body(invalidBook)
      .when()
        .post("/api/v1/catalogue/books")
      .then()
        .statusCode(anyOf(equalTo(400), equalTo(422)));
    
    // Intentar crear libro sin autor (campo obligatorio)
    Map<String, Object> noAuthorBook = new HashMap<>();
    noAuthorBook.put("title", "Libro sin autor");
    noAuthorBook.put("isbn", "123456789");
    noAuthorBook.put("rating", 3);
    noAuthorBook.put("visible", true);
    noAuthorBook.put("stock", 5);
    // author está ausente
    
    given()
        .contentType(ContentType.JSON)
        .body(noAuthorBook)
      .when()
        .post("/api/v1/catalogue/books")
      .then()
        .statusCode(anyOf(equalTo(400), equalTo(422)));
    
    // Intentar crear libro con rating fuera de rango (debe ser 1-5)
    Map<String, Object> invalidRatingBook = new HashMap<>();
    invalidRatingBook.put("title", "Libro con rating inválido");
    invalidRatingBook.put("author", "Autor Test");
    invalidRatingBook.put("isbn", "123456789");
    invalidRatingBook.put("rating", 10); // rating > 5
    invalidRatingBook.put("visible", true);
    invalidRatingBook.put("stock", 5);
    
    given()
        .contentType(ContentType.JSON)
        .body(invalidRatingBook)
      .when()
        .post("/api/v1/catalogue/books")
      .then()
        .statusCode(anyOf(equalTo(400), equalTo(422)));
  }

  @AfterAll
  @Step("Limpieza de datos de prueba")
  @SuppressWarnings("unused")
  static void cleanup() {
    // Intentar eliminar el libro 2 si aún existe (book1 ya fue eliminado en book_flow)
    if (book2Id != null) {
      try {
        given()
          .when()
            .delete("/api/v1/catalogue/books/" + book2Id)
          .then()
            .statusCode(anyOf(equalTo(204), equalTo(404)));
      } catch (Exception e) {
        // Silenciar excepciones de limpieza
      }
    }
  }
}
