package com.relatospapel.ms_books_catalogue.constant;

/**
 * Constantes para la API REST del microservicio de catálogo de libros.
 * Centraliza valores comunes para mantener consistencia en toda la aplicación.
 */
public final class ApiConstants {

  /**
   * Versión actual de la API.
   * Usar en todas las rutas para versionado consistente.
   */
  public static final String API_VERSION = "v1";

  /**
   * Prefijo base para todas las rutas de la API.
   * Formato: /api/{version}/catalogue
   */
  public static final String API_BASE_PATH = "/api/" + API_VERSION + "/catalogue";

  /**
   * Path completo para el recurso de libros.
   */
  public static final String BOOKS_PATH = API_BASE_PATH + "/books";

  /**
   * Path completo para el recurso de categorías.
   */
  public static final String CATEGORIES_PATH = API_BASE_PATH + "/categories";

  /**
   * Versión de la aplicación para documentación.
   */
  public static final String APP_VERSION = "1.0.0";

  /**
   * Nombre de la aplicación.
   */
  public static final String APP_NAME = "MS Books Catalogue";

  /**
   * Descripción del microservicio.
   */
  public static final String APP_DESCRIPTION = "Microservicio para la gestión del catálogo de libros y categorías";

  // Constructor privado para prevenir instanciación
  private ApiConstants() {
    throw new AssertionError("No se puede instanciar la clase ApiConstants");
  }
}
