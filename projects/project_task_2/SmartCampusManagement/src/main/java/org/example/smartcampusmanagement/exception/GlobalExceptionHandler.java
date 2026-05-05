package org.example.smartcampusmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ─── Handle Event Not Found ─────────────────────

    @ExceptionHandler(EventNotFoundException.class)
    public Object handleEventNotFound(EventNotFoundException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            return buildApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        model.addAttribute("errorTitle", "Event Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 404);
        return "error";
    }

    // ─── Handle Registration Errors ─────────────────

    @ExceptionHandler(RegistrationException.class)
    public Object handleRegistrationError(RegistrationException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            return buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        model.addAttribute("errorTitle", "Registration Error");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 400);
        return "error";
    }

    // ─── Handle Validation Errors (REST) ────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 400);
        body.put("error", "Validation Failed");
        body.put("fieldErrors", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // ─── Handle 404 for resources ───────────────────

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResource(NoResourceFoundException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            return buildApiError(HttpStatus.NOT_FOUND, "Resource not found");
        }
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        model.addAttribute("statusCode", 404);
        return "error";
    }

    // ─── Catch-All ──────────────────────────────────

    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 500);
        return "error";
    }

    // ─── Helpers ────────────────────────────────────

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        return uri.startsWith("/api") || (accept != null && accept.contains("application/json"));
    }

    private ResponseEntity<Map<String, Object>> buildApiError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
