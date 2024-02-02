package com.projects.banking.Validators;

import jakarta.validation.Payload;

public @interface ValidUserRegister {
    String message() default "Field Must be Required.";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
