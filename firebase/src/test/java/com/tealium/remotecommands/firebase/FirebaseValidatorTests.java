 package com.tealium.remotecommands.firebase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FirebaseValidatorTests {

    @Before
    public void setUp() {
        // Reset to default strategy before each test
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REPLACE);
        FirebaseValidator.setGA360Mode(false);
    }

    // Event Name Validation Tests
    
    @Test
    public void validateEventName_ValidName_ReturnsValid() {
        // Valid event name: alphanumeric, starts with letter, <= 40 chars, no reserved prefix
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("valid_event_name");
        
        assertTrue("Valid event name should be accepted", result.isValid);
        assertFalse("Valid event name should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "valid_event_name", result.originalValue);
        assertEquals("Sanitized value should be same as original", "valid_event_name", result.sanitizedValue);
    }

    @Test
    public void validateEventName_NameWithInvalidCharacters_ReplaceStrategy_SanitizesCorrectly() {
        // Event name with invalid characters: space, hyphen, special characters
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REPLACE);
        
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("my-event name!");
        
        assertTrue("Event name with invalid chars should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "my-event name!", result.originalValue);
        assertEquals("Invalid chars should be replaced with underscores", "my_event_name", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Event name sanitized", result.errorMessage);
    }

    @Test
    public void validateEventName_NameWithInvalidCharacters_RemoveStrategy_SanitizesCorrectly() {
        // Event name with invalid characters using remove strategy
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REMOVE);
        
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("my-event name!");
        
        assertTrue("Event name should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "my-event name!", result.originalValue);
        assertEquals("Invalid chars should be removed", "myeventname", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Event name sanitized", result.errorMessage);
    }

    @Test
    public void validateEventName_NameStartingWithDigit_AddsPrefixCorrectly() {
        // Event name starting with digit should be prefixed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("123event");
        
        assertTrue("Event name starting with digit should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "123event", result.originalValue);
        assertEquals("Prefix should be added", "event_123event", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Event name sanitized", result.errorMessage);
    }

    @Test
    public void validateEventName_NameWithFirebasePrefix_RemovesPrefixCorrectly() {
        // Event name with firebase_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("firebase_test_event");
        
        assertTrue("Event name with firebase prefix should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "firebase_test_event", result.originalValue);
        assertEquals("Firebase prefix should be removed", "test_event", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Event name sanitized", result.errorMessage);
    }

    @Test
    public void validateEventName_NameWithGooglePrefix_RemovesPrefixCorrectly() {
        // Event name with google_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("google_test_event");
        
        assertTrue("Event name should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "google_test_event", result.originalValue);
        assertEquals("Google prefix should be removed", "test_event", result.sanitizedValue);
    }

    @Test
    public void validateEventName_NameWithGaPrefix_RemovesPrefixCorrectly() {
        // Event name with ga_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("ga_test_event");
        
        assertTrue("Event name should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "ga_test_event", result.originalValue);
        assertEquals("GA prefix should be removed", "test_event", result.sanitizedValue);
    }

    @Test
    public void validateEventName_NameLongerThan40Chars_TruncatesCorrectly() {
        // Event name longer than 40 characters should be truncated
        String longName = "this_is_a_very_long_event_name_that_exceeds_forty_characters";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName(longName);
        
        assertTrue("Long event name should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", longName, result.originalValue);
        assertEquals("Name should be truncated to 40 chars", 40, result.sanitizedValue.length());
        assertEquals("Truncated name should match expected", "this_is_a_very_long_event_name_that_exce", result.sanitizedValue);
    }

    @Test
    public void validateEventName_OnlyFirebasePrefix_ReturnsInvalid() {
        // Event name that is only a reserved prefix should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("firebase_");
        
        assertFalse("Event name that is only firebase prefix should be invalid", result.isValid);
        assertFalse("Invalid event should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "firebase_", result.originalValue);
        assertEquals("Fallback name should be used", "invalid_event", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix", result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateEventName_OnlyGooglePrefix_ReturnsInvalid() {
        // Event name that is only google_ should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("google_");
        
        assertFalse("Event name that is only google prefix should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_event", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix", result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateEventName_OnlyGaPrefix_ReturnsInvalid() {
        // Event name that is only ga_ should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("ga_");
        
        assertFalse("Event name that is only ga prefix should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_event", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix", result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateEventName_EmptyName_ReturnsInvalid() {
        // Empty event name should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("");
        
        assertFalse("Empty event name should be invalid", result.isValid);
        assertFalse("Invalid event should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "", result.originalValue);
        assertEquals("Fallback name should be used", "invalid_event", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty event name", result.errorMessage);
    }

    @Test
    public void validateEventName_NullName_ReturnsInvalid() {
        // Null event name should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName(null);

        assertFalse("Null event name should be invalid", result.isValid);
        assertFalse("Invalid event should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", null, result.originalValue);
        assertEquals("Fallback name should be used", "invalid_event", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty event name", result.errorMessage);
    }

    @Test
    public void validateEventName_WhitespaceOnlyName_ReturnsInvalid() {
        // Event name with only whitespace should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName("   ");

        assertFalse("Whitespace-only event name should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_event", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty event name", result.errorMessage);
    }

    @Test
    public void validateEventName_ComplexScenario_SanitizesCorrectly() {
        // Complex scenario: firebase prefix + invalid chars + too long
        String complexName = "firebase_my-complex event@name#with$special%chars_that_is_way_too_long_for_firebase_limits";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateEventName(complexName);

        assertTrue("Complex event name should be sanitized", result.isValid);
        assertTrue("Event name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", complexName, result.originalValue);
        assertEquals("Result should be valid length", 40, result.sanitizedValue.length());
        assertTrue("Result should start with letter", Character.isLetter(result.sanitizedValue.charAt(0)));
        assertTrue("Result should not contain invalid chars", result.sanitizedValue.matches("^[a-zA-Z][a-zA-Z0-9_]*$"));
        assertFalse("Result should not contain firebase prefix", result.sanitizedValue.startsWith("firebase_"));
    }

    // Parameter Name Validation Tests

    @Test
    public void validateParameterName_ValidName_ReturnsValid() {
        // Valid parameter name: alphanumeric, starts with letter, <= 40 chars, no reserved prefix
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("valid_param_name");

        assertTrue("Valid parameter name should be accepted", result.isValid);
        assertFalse("Valid parameter name should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "valid_param_name", result.originalValue);
        assertEquals("Sanitized value should be same as original", "valid_param_name", result.sanitizedValue);
    }

    @Test
    public void validateParameterName_NameWithInvalidCharacters_ReplaceStrategy_SanitizesCorrectly() {
        // Parameter name with invalid characters: space, hyphen, special characters
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REPLACE);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("my-param name!");

        assertTrue("Parameter name with invalid chars should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "my-param name!", result.originalValue);
        assertEquals("Invalid chars should be replaced with underscores", "my_param_name", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Parameter name sanitized", result.errorMessage);
    }

    @Test
    public void validateParameterName_NameWithInvalidCharacters_RemoveStrategy_SanitizesCorrectly() {
        // Parameter name with invalid characters using remove strategy
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REMOVE);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("my-param name!");

        assertTrue("Parameter name should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "my-param name!", result.originalValue);
        assertEquals("Invalid chars should be removed", "myparamname", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Parameter name sanitized", result.errorMessage);
    }

    @Test
    public void validateParameterName_NameStartingWithDigit_AddsPrefixCorrectly() {
        // Parameter name starting with digit should be prefixed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("123param");

        assertTrue("Parameter name starting with digit should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "123param", result.originalValue);
        assertEquals("Prefix should be added", "param_123param", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Parameter name sanitized", result.errorMessage);
    }

    @Test
    public void validateParameterName_NameWithFirebasePrefix_RemovesPrefixCorrectly() {
        // Parameter name with firebase_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("firebase_test_param");

        assertTrue("Parameter name with firebase prefix should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "firebase_test_param", result.originalValue);
        assertEquals("Firebase prefix should be removed", "test_param", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "Parameter name sanitized", result.errorMessage);
    }

    @Test
    public void validateParameterName_NameWithGooglePrefix_RemovesPrefixCorrectly() {
        // Parameter name with google_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("google_test_param");

        assertTrue("Parameter name should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "google_test_param", result.originalValue);
        assertEquals("Google prefix should be removed", "test_param", result.sanitizedValue);
    }

    @Test
    public void validateParameterName_NameWithGaPrefix_RemovesPrefixCorrectly() {
        // Parameter name with ga_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("ga_test_param");

        assertTrue("Parameter name should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "ga_test_param", result.originalValue);
        assertEquals("GA prefix should be removed", "test_param", result.sanitizedValue);
    }

    @Test
    public void validateParameterName_NameLongerThan40Chars_TruncatesCorrectly() {
        // Parameter name longer than 40 characters should be truncated
        String longName = "this_is_a_very_long_parameter_name_that_exceeds_forty_characters";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName(longName);

        assertTrue("Long parameter name should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", longName, result.originalValue);
        assertEquals("Name should be truncated to 40 chars", 40, result.sanitizedValue.length());
        assertEquals("Truncated name should match expected", "this_is_a_very_long_parameter_name_that_", result.sanitizedValue);
    }

    @Test
    public void validateParameterName_OnlyFirebasePrefix_ReturnsInvalid() {
        // Parameter name that is only a reserved prefix should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("firebase_");

        assertFalse("Parameter name that is only firebase prefix should be invalid", result.isValid);
        assertFalse("Invalid parameter should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "firebase_", result.originalValue);
        assertEquals("Fallback name should be used", "invalid_param", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix",
                   result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateParameterName_OnlyGooglePrefix_ReturnsInvalid() {
        // Parameter name that is only google_ should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("google_");

        assertFalse("Parameter name that is only google prefix should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_param", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix",
                   result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateParameterName_OnlyGaPrefix_ReturnsInvalid() {
        // Parameter name that is only ga_ should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("ga_");

        assertFalse("Parameter name that is only ga prefix should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_param", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix",
                   result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateParameterName_EmptyName_ReturnsInvalid() {
        // Empty parameter name should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("");

        assertFalse("Empty parameter name should be invalid", result.isValid);
        assertFalse("Invalid parameter should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "", result.originalValue);
        assertEquals("Fallback name should be used", "invalid_param", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty parameter name", result.errorMessage);
    }

    @Test
    public void validateParameterName_NullName_ReturnsInvalid() {
        // Null parameter name should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName(null);

        assertFalse("Null parameter name should be invalid", result.isValid);
        assertFalse("Invalid parameter should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", null, result.originalValue);
        assertEquals("Fallback name should be used", "invalid_param", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty parameter name", result.errorMessage);
    }

    @Test
    public void validateParameterName_WhitespaceOnlyName_ReturnsInvalid() {
        // Parameter name with only whitespace should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName("   ");

        assertFalse("Whitespace-only parameter name should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_param", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty parameter name", result.errorMessage);
    }

    @Test
    public void validateParameterName_ComplexScenario_SanitizesCorrectly() {
        // Complex scenario: firebase prefix + invalid chars + too long
        String complexName = "firebase_my-complex param@name#with$special%chars_that_is_way_too_long_for_firebase_limits";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterName(complexName);

        assertTrue("Complex parameter name should be sanitized", result.isValid);
        assertTrue("Parameter name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", complexName, result.originalValue);
        assertEquals("Result should be valid length", 40, result.sanitizedValue.length());
        assertTrue("Result should start with letter", Character.isLetter(result.sanitizedValue.charAt(0)));
        assertTrue("Result should not contain invalid chars", result.sanitizedValue.matches("^[a-zA-Z][a-zA-Z0-9_]*$"));
        assertFalse("Result should not contain firebase prefix", result.sanitizedValue.startsWith("firebase_"));
    }

    // Parameter Value Validation Tests

    @Test
    public void validateParameterValue_ValueWithin100Chars_ReturnsValid() {
        // Parameter value with exactly 100 characters (standard limit)
        String exactlyHundredChars = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean m";
        assertEquals("Test string should be exactly 100 chars", 100, exactlyHundredChars.length());

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue(exactlyHundredChars);

        assertTrue("Parameter value within 100 chars should be valid", result.isValid);
        assertFalse("Parameter value should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", exactlyHundredChars, result.originalValue);
        assertEquals("Sanitized value should be same as original", exactlyHundredChars, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateParameterValue_ValueOver100Chars_StandardMode_TruncatesCorrectly() {
        // Parameter value with 101 characters (should be truncated to 100 in standard mode)
        String hundredOneChars = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean ma";
        assertEquals("Test string should be exactly 101 chars", 101, hundredOneChars.length());

        // Ensure we're in standard mode (not GA360)
        FirebaseValidator.setGA360Mode(false);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue(hundredOneChars);

        assertTrue("Parameter value should be sanitized", result.isValid);
        assertTrue("Parameter value should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", hundredOneChars, result.originalValue);
        assertEquals("Value should be truncated to 100 chars", 100, result.sanitizedValue.length());
        assertEquals("Truncated value should match expected", hundredOneChars.substring(0, 100), result.sanitizedValue);
        assertEquals("Error message should indicate truncation", "Parameter value truncated to 100 characters", result.errorMessage);
    }

    @Test
    public void validateParameterValue_ValueOver500Chars_GA360Mode_TruncatesCorrectly() {
        // Parameter value with 501 characters (should be truncated to 500 in GA360 mode)
        String fiveHundredOneChars = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus";
        assertEquals("Test string should be exactly 501 chars", 501, fiveHundredOneChars.length());

        // Enable GA360 mode for 500 char limit
        FirebaseValidator.setGA360Mode(true);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue(fiveHundredOneChars);

        assertTrue("Parameter value should be sanitized", result.isValid);
        assertTrue("Parameter value should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", fiveHundredOneChars, result.originalValue);
        assertEquals("Value should be truncated to 500 chars", 500, result.sanitizedValue.length());
        assertEquals("Truncated value should match expected", fiveHundredOneChars.substring(0, 500), result.sanitizedValue);
        assertEquals("Error message should indicate truncation", "Parameter value truncated to 500 characters", result.errorMessage);
    }

    @Test
    public void validateParameterValue_ValueExactly500Chars_GA360Mode_ReturnsValid() {
        // Parameter value with exactly 500 characters (GA360 limit)
        String exactlyFiveHundredChars = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibu";
        assertEquals("Test string should be exactly 500 chars", 500, exactlyFiveHundredChars.length());

        // Enable GA360 mode for 500 char limit
        FirebaseValidator.setGA360Mode(true);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue(exactlyFiveHundredChars);

        assertTrue("Parameter value within 500 chars should be valid", result.isValid);
        assertFalse("Parameter value should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", exactlyFiveHundredChars, result.originalValue);
        assertEquals("Sanitized value should be same as original", exactlyFiveHundredChars, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateParameterValue_NullValue_ReturnsValid() {
        // Null parameter value should be handled gracefully
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue(null);

        assertTrue("Null parameter value should be valid", result.isValid);
        assertFalse("Null value should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be null", null, result.originalValue);
        assertEquals("Sanitized value should be null", null, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateParameterValue_EmptyString_ReturnsValid() {
        // Empty parameter value should be valid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue("");

        assertTrue("Empty parameter value should be valid", result.isValid);
        assertFalse("Empty value should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be empty", "", result.originalValue);
        assertEquals("Sanitized value should be empty", "", result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateParameterValue_ShortValue_ReturnsValid() {
        // Short parameter value should be valid
        String shortValue = "short";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateParameterValue(shortValue);

        assertTrue("Short parameter value should be valid", result.isValid);
        assertFalse("Short value should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", shortValue, result.originalValue);
        assertEquals("Sanitized value should be same as original", shortValue, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateParameterValue_GA360ModeSwitch_BehavesCorrectly() {
        // Test switching between standard and GA360 modes
        String longValue = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis p";
        assertEquals("Test string should be exactly 150 chars", 150, longValue.length());

        // Test in standard mode (100 char limit)
        FirebaseValidator.setGA360Mode(false);
        FirebaseValidator.ValidationResult standardResult = FirebaseValidator.validateParameterValue(longValue);

        assertTrue("Value should be truncated in standard mode", standardResult.isSanitized());
        assertEquals("Value should be truncated to 100 chars in standard mode", 100, standardResult.sanitizedValue.length());

        // Test in GA360 mode (500 char limit)
        FirebaseValidator.setGA360Mode(true);
        FirebaseValidator.ValidationResult ga360Result = FirebaseValidator.validateParameterValue(longValue);

        assertFalse("Value should not be truncated in GA360 mode", ga360Result.isSanitized());
        assertEquals("Value should remain 150 chars in GA360 mode", 150, ga360Result.sanitizedValue.length());
        assertEquals("Original value should be preserved in GA360 mode", longValue, ga360Result.sanitizedValue);
    }

    // User Property Name Validation Tests

    @Test
    public void validateUserPropertyName_ValidName_ReturnsValid() {
        // Valid user property name: alphanumeric, starts with letter, <= 24 chars, no reserved prefix
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("valid_user_prop");

        assertTrue("Valid user property name should be accepted", result.isValid);
        assertFalse("Valid user property name should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "valid_user_prop", result.originalValue);
        assertEquals("Sanitized value should be same as original", "valid_user_prop", result.sanitizedValue);
    }

    @Test
    public void validateUserPropertyName_NameWithInvalidCharacters_ReplaceStrategy_SanitizesCorrectly() {
        // User property name with invalid characters: space, hyphen, special characters
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REPLACE);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("my-user prop!");

        assertTrue("User property name with invalid chars should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "my-user prop!", result.originalValue);
        assertEquals("Invalid chars should be replaced with underscores", "my_user_prop", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "User property name sanitized", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_NameWithInvalidCharacters_RemoveStrategy_SanitizesCorrectly() {
        // User property name with invalid characters using remove strategy
        FirebaseValidator.setInvalidCharStrategy(FirebaseValidator.STRATEGY_REMOVE);

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("my-user prop!");

        assertTrue("User property name should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "my-user prop!", result.originalValue);
        assertEquals("Invalid chars should be removed", "myuserprop", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "User property name sanitized", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_NameStartingWithDigit_AddsPrefixCorrectly() {
        // User property name starting with digit should be prefixed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("123user");

        assertTrue("User property name starting with digit should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "123user", result.originalValue);
        assertEquals("Prefix should be added", "prop_123user", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "User property name sanitized", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_NameWithFirebasePrefix_RemovesPrefixCorrectly() {
        // User property name with firebase_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("firebase_test_user");

        assertTrue("User property name with firebase prefix should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "firebase_test_user", result.originalValue);
        assertEquals("Firebase prefix should be removed", "test_user", result.sanitizedValue);
        assertEquals("Error message should indicate sanitization", "User property name sanitized", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_NameWithGooglePrefix_RemovesPrefixCorrectly() {
        // User property name with google_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("google_test_user");

        assertTrue("User property name should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "google_test_user", result.originalValue);
        assertEquals("Google prefix should be removed", "test_user", result.sanitizedValue);
    }

    @Test
    public void validateUserPropertyName_NameWithGaPrefix_RemovesPrefixCorrectly() {
        // User property name with ga_ prefix should have prefix removed
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("ga_test_user");

        assertTrue("User property name should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "ga_test_user", result.originalValue);
        assertEquals("GA prefix should be removed", "test_user", result.sanitizedValue);
    }

    @Test
    public void validateUserPropertyName_NameLongerThan24Chars_TruncatesCorrectly() {
        // User property name longer than 24 characters should be truncated (not 40!)
        String longName = "this_is_very_long_user_property_name";
        assertEquals("Test string should be longer than 24 chars", 36, longName.length());

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName(longName);

        assertTrue("Long user property name should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", longName, result.originalValue);
        assertEquals("Name should be truncated to 24 chars", 24, result.sanitizedValue.length());
        assertEquals("Truncated name should match expected", "this_is_very_long_user_p", result.sanitizedValue);
    }

    @Test
    public void validateUserPropertyName_OnlyFirebasePrefix_ReturnsInvalid() {
        // User property name that is only a reserved prefix should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("firebase_");

        assertFalse("User property name that is only firebase prefix should be invalid", result.isValid);
        assertFalse("Invalid user property should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "firebase_", result.originalValue);
        assertEquals("Fallback name should be used", "invalid_property", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix", result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateUserPropertyName_OnlyGooglePrefix_ReturnsInvalid() {
        // User property name that is only google_ should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("google_");

        assertFalse("User property name that is only google prefix should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_property", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix", result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateUserPropertyName_OnlyGaPrefix_ReturnsInvalid() {
        // User property name that is only ga_ should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("ga_");

        assertFalse("User property name that is only ga prefix should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_property", result.sanitizedValue);
        assertTrue("Error message should mention reserved prefix", result.errorMessage.contains("Name cannot be only a reserved prefix"));
    }

    @Test
    public void validateUserPropertyName_EmptyName_ReturnsInvalid() {
        // Empty user property name should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("");

        assertFalse("Empty user property name should be invalid", result.isValid);
        assertFalse("Invalid user property should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", "", result.originalValue);
        assertEquals("Fallback name should be used", "invalid_property", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty user property name", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_NullName_ReturnsInvalid() {
        // Null user property name should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName(null);

        assertFalse("Null user property name should be invalid", result.isValid);
        assertFalse("Invalid user property should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", null, result.originalValue);
        assertEquals("Fallback name should be used", "invalid_property", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty user property name", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_WhitespaceOnlyName_ReturnsInvalid() {
        // User property name with only whitespace should be invalid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName("   ");
        
        assertFalse("Whitespace-only user property name should be invalid", result.isValid);
        assertEquals("Fallback name should be used", "invalid_property", result.sanitizedValue);
        assertEquals("Error message should mention empty name", "Empty user property name", result.errorMessage);
    }

    @Test
    public void validateUserPropertyName_ComplexScenario_SanitizesCorrectly() {
        // Complex scenario: firebase prefix + invalid chars + too long (24 char limit)
        String complexName = "firebase_my-complex user@prop#with$special%chars_that_exceeds_limit";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName(complexName);
        
        assertTrue("Complex user property name should be sanitized", result.isValid);
        assertTrue("User property name should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", complexName, result.originalValue);
        assertEquals("Result should be valid length (24 chars)", 24, result.sanitizedValue.length());
        assertTrue("Result should start with letter", Character.isLetter(result.sanitizedValue.charAt(0)));
        assertTrue("Result should not contain invalid chars", result.sanitizedValue.matches("^[a-zA-Z][a-zA-Z0-9_]*$"));
        assertFalse("Result should not contain firebase prefix", result.sanitizedValue.startsWith("firebase_"));
    }

    @Test
    public void validateUserPropertyName_Exactly24Chars_ReturnsValid() {
        // User property name with exactly 24 characters should be valid
        String exactly24Chars = "valid_twenty_four_chars_";
        assertEquals("Test string should be exactly 24 chars", 24, exactly24Chars.length());
        
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyName(exactly24Chars);
        
        assertTrue("24-char user property name should be valid", result.isValid);
        assertFalse("24-char name should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", exactly24Chars, result.originalValue);
        assertEquals("Sanitized value should be same as original", exactly24Chars, result.sanitizedValue);
    }

    // User Property Value Validation Tests

    @Test
    public void validateUserPropertyValue_ValueWithin36Chars_ReturnsValid() {
        // User property value with exactly 36 characters (limit)
        String exactly36Chars = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 36 'a's
        assertEquals("Test string should be exactly 36 chars", 36, exactly36Chars.length());

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyValue(exactly36Chars);

        assertTrue("User property value within 36 chars should be valid", result.isValid);
        assertFalse("User property value should not be sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", exactly36Chars, result.originalValue);
        assertEquals("Sanitized value should be same as original", exactly36Chars, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateUserPropertyValue_ValueOver36Chars_TruncatesCorrectly() {
        // User property value with 37 characters (should be truncated to 36)
        String thirty7Chars = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 37 'a's
        assertEquals("Test string should be exactly 37 chars", 37, thirty7Chars.length());

        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyValue(thirty7Chars);

        assertTrue("User property value should be sanitized", result.isValid);
        assertTrue("User property value should be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", thirty7Chars, result.originalValue);
        assertEquals("Value should be truncated to 36 chars", 36, result.sanitizedValue.length());
        assertEquals("Truncated value should match expected", thirty7Chars.substring(0, 36), result.sanitizedValue);
        assertEquals("Error message should indicate truncation", "User property value truncated to 36 characters", result.errorMessage);
    }

    @Test
    public void validateUserPropertyValue_NullValue_ReturnsValid() {
        // Null user property value should be handled gracefully
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyValue(null);

        assertTrue("Null user property value should be valid", result.isValid);
        assertFalse("Null value should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be null", null, result.originalValue);
        assertEquals("Sanitized value should be null", null, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateUserPropertyValue_EmptyString_ReturnsValid() {
        // Empty user property value should be valid
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyValue("");

        assertTrue("Empty user property value should be valid", result.isValid);
        assertFalse("Empty value should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be empty", "", result.originalValue);
        assertEquals("Sanitized value should be empty", "", result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }

    @Test
    public void validateUserPropertyValue_ShortValue_ReturnsValid() {
        // Short user property value should be valid
        String shortValue = "short";
        FirebaseValidator.ValidationResult result = FirebaseValidator.validateUserPropertyValue(shortValue);

        assertTrue("Short user property value should be valid", result.isValid);
        assertFalse("Short value should not be marked as sanitized", result.isSanitized());
        assertEquals("Original value should be preserved", shortValue, result.originalValue);
        assertEquals("Sanitized value should be same as original", shortValue, result.sanitizedValue);
        assertEquals("Error message should indicate valid", "Valid", result.errorMessage);
    }
} 