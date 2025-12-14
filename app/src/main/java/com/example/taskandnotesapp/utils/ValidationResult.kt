package com.example.taskandnotesapp.utils

object ValidationUtils {

    // التحقق من صحة البريد الإلكتروني
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // التحقق من قوة كلمة المرور
    fun isValidPassword(password: String): ValidationResult {
        return when {
            password.length < 6 -> ValidationResult.Error("كلمة المرور يجب أن تكون 6 أحرف على الأقل")
            password.length > 20 -> ValidationResult.Error("كلمة المرور طويلة جداً (20 حرف كحد أقصى)")
            else -> ValidationResult.Success
        }
    }

    // التحقق من اسم المستخدم
    fun isValidUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult.Error("اسم المستخدم مطلوب")
            username.length < 3 -> ValidationResult.Error("اسم المستخدم يجب أن يكون 3 أحرف على الأقل")
            username.length > 20 -> ValidationResult.Error("اسم المستخدم طويل جداً (20 حرف كحد أقصى)")
            !username.matches("^[a-zA-Z0-9._]+$".toRegex()) ->
                ValidationResult.Error("اسم المستخدم يحتوي على أحرف غير مسموحة")
            else -> ValidationResult.Success
        }
    }

    // التحقق من جميع حقول التسجيل
    fun validateRegistration(
        username: String,
        email: String,
        password: String
    ): ValidationResult {
        // التحقق من اسم المستخدم
        val usernameResult = isValidUsername(username)
        if (usernameResult is ValidationResult.Error) {
            return usernameResult
        }

        // التحقق من البريد الإلكتروني
        if (!isValidEmail(email)) {
            return ValidationResult.Error("البريد الإلكتروني غير صحيح")
        }

        // التحقق من كلمة المرور
        val passwordResult = isValidPassword(password)
        if (passwordResult is ValidationResult.Error) {
            return passwordResult
        }

        return ValidationResult.Success
    }

    // التحقق من حقول تسجيل الدخول
    fun validateLogin(email: String, password: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("البريد الإلكتروني مطلوب")
            !isValidEmail(email) -> ValidationResult.Error("البريد الإلكتروني غير صحيح")
            password.isBlank() -> ValidationResult.Error("كلمة المرور مطلوبة")
            else -> ValidationResult.Success
        }
    }
}

// النتيجة المُرجعة من التحقق
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}