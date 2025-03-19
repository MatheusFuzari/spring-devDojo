package com.exemple.dev_dojo;

import java.util.List;

public record ValidationErrorMessage(int status, List<String> message) {
}
