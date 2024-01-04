package com.blog_application.blog_application.service;

public class PasswordValidator {

	// Define your password policies
	private static final int MIN_LENGTH = 8;
	private static final int MAX_LENGTH = 20;
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+";

	private static boolean containsDigit(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isDigit(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean containsLowercase(String password) {
		return !password.equals(password.toUpperCase());
	}

	private static boolean containsSpecialCharacter(String password) {
		for (char c : password.toCharArray()) {
			if (SPECIAL_CHARACTERS.contains(String.valueOf(c))) {
				return true;
			}
		}
		return false;
	}

	private static boolean containsUppercase(String password) {
		return !password.equals(password.toLowerCase());
	}

	private static boolean isLengthValid(String password) {
		return password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH;
	}

	public static boolean isValidPassword(String password) {
		return isLengthValid(password) && containsUppercase(password) && containsLowercase(password)
				&& containsDigit(password) && containsSpecialCharacter(password);
	}
}
