package com.relatospapel.ms_books_payments.constant;

/**
 * Constantes para la API REST del microservicio de pagos y compras.
 * Centraliza valores comunes para mantener consistencia en toda la aplicacion.
 */
public final class ApiConstants {

  /**
   * Version actual de la API.
   * Usar en todas las rutas para versionado consistente.
   */
  public static final String API_VERSION = "v1";

  /**
   * Prefijo base para todas las rutas de la API.
   * Formato: /api/{version}/payments
   */
  public static final String API_BASE_PATH = "/api/" + API_VERSION + "/payments";

  /**
   * Path completo para el recurso de compras.
   */
  public static final String PURCHASES_PATH = API_BASE_PATH + "/purchases";

  /**
   * Version de la aplicacion para documentacion.
   */
  public static final String APP_VERSION = "1.0.0";

  /**
   * Nombre de la aplicacion.
   */
  public static final String APP_NAME = "MS Books Payments";

  /**
   * Descripcion del microservicio.
   */
  public static final String APP_DESCRIPTION = "Microservicio para la gestion de pagos y compras de libros";

  // Constructor privado para prevenir instanciacion
  private ApiConstants() {
    throw new AssertionError("No se puede instanciar la clase ApiConstants");
  }
}
