package com.example.checkinn_android.core.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests directly ported from CheckInn iOS LoginCredentialValidationTests.
 */
class ValidationUtilTest {

    @Test
    fun testEmailClassification() {
        assertEquals(LoginCredentialKind.Email, "manager@checkinn.app".loginCredentialKind)
        assertEquals(LoginCredentialKind.Email, "desk@".loginCredentialKind)
        assertEquals(LoginCredentialKind.Email, "a@b".loginCredentialKind)
    }

    @Test
    fun testMobileClassification() {
        assertEquals(LoginCredentialKind.Mobile, "9876543210".loginCredentialKind)
        assertEquals(LoginCredentialKind.Mobile, "98765 43210".loginCredentialKind)
        assertEquals(LoginCredentialKind.Mobile, "98765-43210".loginCredentialKind)
    }

    @Test
    fun testUnknownClassification() {
        assertEquals(LoginCredentialKind.Unknown, "frontdesk".loginCredentialKind)
        assertEquals(LoginCredentialKind.Unknown, "".loginCredentialKind)
        assertEquals(LoginCredentialKind.Unknown, "   ".loginCredentialKind)
        assertEquals(LoginCredentialKind.Unknown, "98765 4321a".loginCredentialKind)
    }

    @Test
    fun testNormalizationStripsSpacesForEmail() {
        assertEquals("manager@checkinn.app", "  manager@checkinn.app  ".normalizedLoginCredential)
    }

    @Test
    fun testNormalizationStripsNonDigitsForMobile() {
        assertEquals("9876543210", "98765-43210".normalizedLoginCredential)
        assertEquals("9876543210", "98765 43210".normalizedLoginCredential)
    }

    @Test
    fun testNormalizationTruncatesMobileTo10Digits() {
        assertEquals("9876543210", "987654321099".normalizedLoginCredential)
    }

    @Test
    fun testValidEmailAddresses() {
        assertTrue("manager@checkinn.app".isValidLoginEmailAddress)
        assertTrue("user.name+tag@example.co.in".isValidLoginEmailAddress)
    }

    @Test
    fun testInvalidEmailAddresses() {
        assertFalse("manager@".isValidLoginEmailAddress)
        assertFalse("@checkinn.app".isValidLoginEmailAddress)
        assertFalse("manager@checkinn".isValidLoginEmailAddress)
        assertFalse("manager@.checkinn.app".isValidLoginEmailAddress)
        assertFalse("manager@checkinn.app.".isValidLoginEmailAddress)
        assertFalse("manager @checkinn.app".isValidLoginEmailAddress)
    }

    @Test
    fun testValidIndianMobileNumbers() {
        assertTrue("9876543210".isValidIndianMobileNumber)
        assertTrue("6000000000".isValidIndianMobileNumber)
        assertTrue("7123456789".isValidIndianMobileNumber)
        assertTrue("8000000000".isValidIndianMobileNumber)
    }

    @Test
    fun testInvalidIndianMobileNumbers() {
        assertFalse("987654321".isValidIndianMobileNumber)   // 9 digits
        assertFalse("98765432101".isValidIndianMobileNumber) // 11 digits
        assertFalse("1234567890".isValidIndianMobileNumber)  // starts with 1
        assertFalse("5876543210".isValidIndianMobileNumber)  // starts with 5
        assertFalse("".isValidIndianMobileNumber)
    }
}
