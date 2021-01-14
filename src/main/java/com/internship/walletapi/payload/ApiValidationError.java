package com.internship.walletapi.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiValidationError extends ApiSubError {
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }

  public ApiValidationError(String object, String field, Object rejectedValue, String message) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }


}