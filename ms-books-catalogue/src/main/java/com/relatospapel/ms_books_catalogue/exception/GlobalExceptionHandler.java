package com.relatospapel.ms_books_catalogue.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Manejador global de excepciones para la API REST.
 * 
 * <p>Implementa las mejores prácticas REST usando códigos HTTP apropiados:
 * <ul>
 *   <li><b>2xx</b>: Respuestas exitosas (manejadas en controladores)</li>
 *   <li><b>4xx</b>: Errores del cliente (datos inválidos, recursos no encontrados, etc.)</li>
 *   <li><b>5xx</b>: Errores del servidor (excepciones no controladas)</li>
 * </ul>
 * 
 * <p>Todos los errores retornan una estructura JSON consistente con:
 * <ul>
 *   <li>timestamp: Marca de tiempo del error</li>
 *   <li>status: Código HTTP numérico</li>
 *   <li>error: Nombre del error HTTP</li>
 *   <li>message: Descripción detallada del error</li>
 *   <li>errors: (opcional) Mapa de errores de validación por campo</li>
 * </ul>
 * 
 * @author Relatos de Papel
 * @version 1.0.0
 * @see <a href="https://tools.ietf.org/html/rfc7231">RFC 7231 - HTTP/1.1 Semantics</a>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Maneja errores 404 - NOT FOUND
   * Cuando un recurso no existe en la base de datos
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> handleNotFound(NotFoundException ex) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  /**
   * Maneja errores 400 - BAD REQUEST
   * Cuando los datos de entrada no pasan las validaciones (@Valid)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, Object> body = base(HttpStatus.BAD_REQUEST);
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
    body.put("message", "Error de validación en los datos de entrada");
    body.put("errors", errors);
    return ResponseEntity.badRequest().body(body);
  }

  /**
   * Maneja errores 400 - BAD REQUEST
   * Cuando falta un parámetro requerido en la petición
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
    return build(HttpStatus.BAD_REQUEST, 
        "Parámetro requerido ausente: " + ex.getParameterName());
  }

  /**
   * Maneja errores 400 - BAD REQUEST
   * Cuando un parámetro tiene un tipo incorrecto (ej: String en lugar de Integer)
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String message = String.format("El parámetro '%s' debe ser de tipo %s", 
        ex.getName(), 
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");
    return build(HttpStatus.BAD_REQUEST, message);
  }

  /**
   * Maneja errores 400 - BAD REQUEST
   * Cuando el cuerpo de la petición no se puede parsear (JSON inválido)
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleMessageNotReadable(HttpMessageNotReadableException ex) {
    return build(HttpStatus.BAD_REQUEST, 
        "Cuerpo de la petición mal formado o ilegible. Verifique el formato JSON");
  }

  /**
   * Maneja errores 404 - NOT FOUND
   * Cuando no existe un handler para la ruta solicitada
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException ex) {
    return build(HttpStatus.NOT_FOUND, 
        "No se encontró el endpoint: " + ex.getRequestURL());
  }

  /**
   * Maneja errores 405 - METHOD NOT ALLOWED
   * Cuando se usa un método HTTP no soportado en un endpoint (ej: PUT en lugar de POST)
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    return build(HttpStatus.METHOD_NOT_ALLOWED, 
        "Método HTTP no soportado: " + ex.getMethod() + 
        ". Métodos permitidos: " + String.join(", ", ex.getSupportedMethods()));
  }

  /**
   * Maneja errores 415 - UNSUPPORTED MEDIA TYPE
   * Cuando el Content-Type de la petición no es soportado
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<?> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
    return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
        "Tipo de contenido no soportado: " + ex.getContentType() + 
        ". Tipos soportados: " + ex.getSupportedMediaTypes());
  }

  /**
   * Maneja errores 409 - CONFLICT
   * Cuando hay conflictos de integridad en la base de datos (ej: duplicados, violación de constraints)
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    String message = "Conflicto de integridad de datos. ";
    if (ex.getMessage().contains("unique") || ex.getMessage().contains("duplicate")) {
      message += "El recurso ya existe o viola una restricción de unicidad";
    } else if (ex.getMessage().contains("foreign key")) {
      message += "Violación de clave foránea. Verifique las relaciones entre recursos";
    } else {
      message += "Operación no permitida por restricciones de la base de datos";
    }
    return build(HttpStatus.CONFLICT, message);
  }

  /**
   * Maneja errores 500 - INTERNAL SERVER ERROR
   * Cualquier excepción no manejada específicamente (fallback)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneric(Exception ex) {
    // En producción, no exponer detalles internos del error
    // Solo loguear internamente y devolver mensaje genérico
    return build(HttpStatus.INTERNAL_SERVER_ERROR, 
        "Error interno del servidor. Por favor, contacte al administrador");
  }

  /**
   * Construye la respuesta de error con un mensaje
   */
  private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
    Map<String, Object> body = base(status);
    body.put("message", message);
    return ResponseEntity.status(status).body(body);
  }

  /**
   * Crea la estructura base de la respuesta de error
   */
  private Map<String, Object> base(HttpStatus status) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    return body;
  }
}
