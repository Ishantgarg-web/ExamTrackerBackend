package com.example.examTracker.validations;

public class PhoneNumberValidator {
    public static boolean validatePhoneNumber(String phoneNumber) {

        if (phoneNumber == null) {
            return true;
        }

        // Remove spaces and hyphens (optional but helpful)
        phoneNumber = phoneNumber.replaceAll("[\\s-]", "");

        // Regex for Indian mobile numbers
        String indianPhoneRegex = "^(\\+91|91|0)?[6-9][0-9]{9}$";

        return phoneNumber.matches(indianPhoneRegex);
    }

}
