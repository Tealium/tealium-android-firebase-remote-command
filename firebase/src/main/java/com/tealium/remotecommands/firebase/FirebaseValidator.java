package com.tealium.remotecommands.firebase;

import java.util.regex.Pattern;

/**
 * Validates Firebase event names, parameter names, and parameter values
 * according to Firebase Analytics requirements.
 * 
 * Handles sanitization of invalid data to ensure Firebase compatibility.
 */
public class FirebaseValidator {

    // Firebase naming validation (same rules for event names and parameter names)
    private static final String NAME_PATTERN = "^(?!firebase_|google_|ga_)[a-zA-Z][a-zA-Z0-9_]{0,39}$";
    private static final int MAX_NAME_LENGTH = 40;
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);
    
    // Firebase User Property validation (different rules)
    private static final String USER_PROPERTY_NAME_PATTERN = "^(?!firebase_|google_|ga_)[a-zA-Z][a-zA-Z0-9_]{0,23}$";
    private static final int MAX_USER_PROPERTY_NAME_LENGTH = 24;
    private static final Pattern USER_PROPERTY_NAME_REGEX = Pattern.compile(USER_PROPERTY_NAME_PATTERN);
    
    // Firebase value length limits
    private static final int MAX_PARAMETER_VALUE_LENGTH_STANDARD = 100;
    private static final int MAX_PARAMETER_VALUE_LENGTH_GA360 = 500;
    private static final int MAX_USER_PROPERTY_VALUE_LENGTH = 36;
    
    // Current parameter value length limit (default: standard GA)
    private static int maxParameterValueLength = MAX_PARAMETER_VALUE_LENGTH_STANDARD;
    
    // Logging constants
    public static final String VALIDATION_TAG = "Tealium-Firebase-Validation";
    public static final String WARNING_PREFIX = "VALIDATION_WARNING: ";
    public static final String ERROR_PREFIX = "VALIDATION_ERROR: ";
    
    // Invalid character handling strategies
    public static final String STRATEGY_REPLACE = "replace";
    public static final String STRATEGY_REMOVE = "remove";
    
    // Current strategy (default: replace invalid chars with underscore)
    private static String invalidCharStrategy = STRATEGY_REPLACE;

    /**
     * Sets the strategy for handling invalid characters in names
     * 
     * @param strategy Either STRATEGY_REPLACE or STRATEGY_REMOVE
     */
    public static void setInvalidCharStrategy(String strategy) {
        if (STRATEGY_REPLACE.equals(strategy) || STRATEGY_REMOVE.equals(strategy)) {
            invalidCharStrategy = strategy;
        }
    }

    /**
     * Sets whether to use GA 360 parameter value length limits (500 chars vs 100)
     * 
     * @param useGA360 true for 500 char limit, false for 100 char limit
     */
    public static void setGA360Mode(boolean useGA360) {
        maxParameterValueLength = useGA360 ? MAX_PARAMETER_VALUE_LENGTH_GA360 : MAX_PARAMETER_VALUE_LENGTH_STANDARD;
    }

    /**
     * Exception thrown when event name cannot be sanitized (e.g., only reserved prefix)
     */
    public static class FirebaseValidationException extends Exception {
        public FirebaseValidationException(String message) {
            super(message);
        }
    }

    /**
     * Result of validation operation containing original and sanitized values
     */
    public static class ValidationResult {
        public final boolean isValid;
        public final String originalValue;
        public final String sanitizedValue;
        public final String errorMessage;

        private ValidationResult(boolean isValid, String originalValue, String sanitizedValue, String errorMessage) {
            this.isValid = isValid;
            this.originalValue = originalValue;
            this.sanitizedValue = sanitizedValue;
            this.errorMessage = errorMessage;
        }

        /**
         * Creates a successful validation result (no changes needed)
         */
        public static ValidationResult valid(String value) {
            return new ValidationResult(true, value, value, "Valid");
        }

        /**
         * Creates a validation result with sanitized value
         */
        public static ValidationResult sanitized(String originalValue, String sanitizedValue, String errorMessage) {
            return new ValidationResult(true, originalValue, sanitizedValue, errorMessage);
        }

        /**
         * Creates an invalid validation result
         */
        public static ValidationResult invalid(String originalValue, String sanitizedValue, String errorMessage) {
            return new ValidationResult(false, originalValue, sanitizedValue, errorMessage);
        }

        /**
         * Checks if the validation result is a sanitized value
         * @return true if the original value is not equal to the sanitized value, false otherwise
         */
        public boolean isSanitized() {
            if (!this.isValid) return false;
            else if (this.originalValue == null) return false;
            else return !this.originalValue.equals(this.sanitizedValue);
        }
    }

    /**
     * Validates and sanitizes Firebase event name
     * Always returns a valid Firebase-compatible event name
     * 
     * @param eventName The event name to validate
     * @return ValidationResult with original and sanitized values
     */
    public static ValidationResult validateEventName(String eventName) {
        return validateName(eventName, "invalid_event", "event_", "Event name");
    }

    /**
     * Validates and sanitizes Firebase parameter name
     * Always returns a valid Firebase-compatible parameter name
     * 
     * @param paramName The parameter name to validate
     * @return ValidationResult with original and sanitized values
     */
    public static ValidationResult validateParameterName(String paramName) {
        return validateName(paramName, "invalid_param", "param_", "Parameter name");
    }

    /**
     * Validates and sanitizes Firebase user property name
     * User properties have different length limits (24 chars vs 40)
     * 
     * @param propertyName The user property name to validate
     * @return ValidationResult with original and sanitized values
     */
    public static ValidationResult validateUserPropertyName(String propertyName) {
        return validateUserPropertyNameInternal(propertyName, "invalid_property", "prop_", "User property name");
    }

    /**
     * Validates and truncates Firebase parameter value if needed
     * Uses current maxParameterValueLength (100 or 500 chars based on GA 360 setting)
     * 
     * @param paramValue The parameter value to validate
     * @return ValidationResult with original and truncated values
     */
    public static ValidationResult validateParameterValue(String paramValue) {
        if (paramValue == null) {
            return ValidationResult.valid(null);
        }
        
        if (paramValue.length() <= maxParameterValueLength) {
            return ValidationResult.valid(paramValue);
        }
        
        String truncated = paramValue.substring(0, maxParameterValueLength);
        return ValidationResult.sanitized(paramValue, truncated, 
            "Parameter value truncated to " + maxParameterValueLength + " characters");
    }

    /**
     * Validates and truncates Firebase user property value if needed (36 char limit)
     * 
     * @param propertyValue The user property value to validate
     * @return ValidationResult with original and truncated values
     */
    public static ValidationResult validateUserPropertyValue(String propertyValue) {
        if (propertyValue == null) {
            return ValidationResult.valid(null);
        }
        
        if (propertyValue.length() <= MAX_USER_PROPERTY_VALUE_LENGTH) {
            return ValidationResult.valid(propertyValue);
        }
        
        String truncated = propertyValue.substring(0, MAX_USER_PROPERTY_VALUE_LENGTH);
        return ValidationResult.sanitized(propertyValue, truncated, 
            "User property value truncated to " + MAX_USER_PROPERTY_VALUE_LENGTH + " characters");
    }



    /**
     * Common validation logic for Firebase names (events and parameters)
     * Always returns a valid Firebase-compatible name
     * 
     * @param name The name to validate
     * @param invalidFallback Fallback name for invalid cases
     * @param sanitizationPrefix Prefix to add if name doesn't start with letter
     * @param nameType Type description for error messages
     * @return ValidationResult with original and sanitized values
     */
    private static ValidationResult validateName(String name, String invalidFallback, 
                                                        String sanitizationPrefix, String nameType) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return ValidationResult.invalid(name, invalidFallback, "Empty " + nameType.toLowerCase());
            }

            // Quick check: if already valid, return as-is
            if (NAME_REGEX.matcher(name).matches()) {
                return ValidationResult.valid(name);
            }

            // Try to sanitize - if it's only a reserved prefix, it's invalid
            String sanitized = sanitizeName(name, sanitizationPrefix);
            
            // Compare and return result
            if (name.equals(sanitized)) {
                return ValidationResult.valid(sanitized);
            } else {
                return ValidationResult.sanitized(name, sanitized, nameType + " sanitized");
            }
        } catch (FirebaseValidationException e) {
            return ValidationResult.invalid(name, invalidFallback, e.getMessage());
        }
    }

    /**
     * Validation logic for Firebase user property names (different length limits)
     * Always returns a valid Firebase-compatible user property name
     * 
     * @param name The user property name to validate
     * @param invalidFallback Fallback name for invalid cases
     * @param sanitizationPrefix Prefix to add if name doesn't start with letter
     * @param nameType Type description for error messages
     * @return ValidationResult with original and sanitized values
     */
    private static ValidationResult validateUserPropertyNameInternal(String name, String invalidFallback, 
                                                                    String sanitizationPrefix, String nameType) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return ValidationResult.invalid(name, invalidFallback, "Empty " + nameType.toLowerCase());
            }

            // Quick check: if already valid, return as-is
            if (USER_PROPERTY_NAME_REGEX.matcher(name).matches()) {
                return ValidationResult.valid(name);
            }

            // Try to sanitize - if it's only a reserved prefix, it's invalid
            String sanitized = sanitizeUserPropertyName(name, sanitizationPrefix);
            
            // Compare and return result
            if (name.equals(sanitized)) {
                return ValidationResult.valid(sanitized);
            } else {
                return ValidationResult.sanitized(name, sanitized, nameType + " sanitized");
            }
        } catch (FirebaseValidationException e) {
            return ValidationResult.invalid(name, invalidFallback, e.getMessage());
        }
    }



    /**
     * Sanitizes Firebase name (event or parameter) to be Firebase-compatible
     * Steps: remove reserved prefixes → replace invalid chars → clean underscores → ensure letter start → truncate
     * 
     * @param name The name to sanitize
     * @param fallbackPrefix Prefix to add if name doesn't start with letter (e.g., "event_", "param_")
     * @return Firebase-compatible name
     * @throws FirebaseValidationException if name is only a reserved prefix
     */
    private static String sanitizeName(String name, String fallbackPrefix) throws FirebaseValidationException {
        
        // Step 1: Remove reserved prefixes
        String result = removeReservedPrefixes(name);
        
        // Step 2: Handle invalid characters based on strategy
        if (STRATEGY_REPLACE.equals(invalidCharStrategy)) {
            // Replace invalid characters with underscore
        result = result.replaceAll("[^a-zA-Z0-9_]", "_");
        } else {
            // Remove invalid characters completely
            result = result.replaceAll("[^a-zA-Z0-9_]", "");
        }
        
        // Step 3: Clean up multiple underscores
        result = result.replaceAll("_+", "_");

        // Step 4: Remove leading/trailing underscores
        result = result.replaceAll("^_+|_+$", "");
        
        // Step 5: Ensure starts with a letter (only if needed after cleanup)
        if (result.isEmpty() || !Character.isLetter(result.charAt(0))) {
            result = fallbackPrefix + result;
        }
        
        // Step 6: Truncate if too long
        if (result.length() > MAX_NAME_LENGTH) {
            result = result.substring(0, MAX_NAME_LENGTH);
        }
        
        // Step 7: Final fallback if somehow still empty
        if (result.isEmpty()) {
            result = fallbackPrefix.replace("_", "") + "_fallback";
        }
        
        return result;
    }

    /**
     * Sanitizes Firebase user property name to be Firebase-compatible (24 char limit)
     * Steps: remove reserved prefixes → replace invalid chars → clean underscores → ensure letter start → truncate
     * 
     * @param name The user property name to sanitize
     * @param fallbackPrefix Prefix to add if name doesn't start with letter (e.g., "prop_")
     * @return Firebase-compatible user property name
     * @throws FirebaseValidationException if name is only a reserved prefix
     */
    private static String sanitizeUserPropertyName(String name, String fallbackPrefix) throws FirebaseValidationException {
        
        // Step 1: Remove reserved prefixes
        String result = removeReservedPrefixes(name);
        
        // Step 2: Handle invalid characters based on strategy
        if (STRATEGY_REPLACE.equals(invalidCharStrategy)) {
            // Replace invalid characters with underscore
        result = result.replaceAll("[^a-zA-Z0-9_]", "_");
        } else {
            // Remove invalid characters completely
            result = result.replaceAll("[^a-zA-Z0-9_]", "");
        }
        
        // Step 3: Clean up multiple underscores
        result = result.replaceAll("_+", "_");

        // Step 4: Remove leading/trailing underscores
        result = result.replaceAll("^_+|_+$", "");
        
        // Step 5: Ensure starts with a letter (only if needed after cleanup)
        if (result.isEmpty() || !Character.isLetter(result.charAt(0))) {
            result = fallbackPrefix + result;
        }
        
        // Step 6: Truncate if too long (24 chars for user properties)
        if (result.length() > MAX_USER_PROPERTY_NAME_LENGTH) {
            result = result.substring(0, MAX_USER_PROPERTY_NAME_LENGTH);
        }
        
        // Step 7: Final fallback if somehow still empty
        if (result.isEmpty()) {
            result = fallbackPrefix.replace("_", "") + "_fallback";
        }
        
        return result;
    }

    /**
     * Removes reserved Firebase prefixes from name
     * 
     * @param name The name to process (event name or parameter name)
     * @return Name without reserved prefixes  
     * @throws FirebaseValidationException if name is only a reserved prefix
     */
    private static String removeReservedPrefixes(String name) throws FirebaseValidationException {        
        String lower = name.toLowerCase();
        String result = name;
        
        if (lower.startsWith("firebase_")) {
            result = name.substring(9); // Remove "firebase_"
        } else if (lower.startsWith("google_")) {
            result = name.substring(7);  // Remove "google_"
        } else if (lower.startsWith("ga_")) {
            result = name.substring(3);  // Remove "ga_"
        }
        
        // If removing prefix left us with empty string, this is invalid input
        if (result.isEmpty()) {
            throw new FirebaseValidationException("Name cannot be only a reserved prefix: " + name);
        }
        
        return result;
    }
} 