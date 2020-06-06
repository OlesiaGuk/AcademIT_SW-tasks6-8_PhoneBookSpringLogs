package ru.academits.model;

public class ContactValidation {
    private boolean valid;
    private String error;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        ContactValidation contactValidation = (ContactValidation) o;

        if (error != null) {
            return valid == contactValidation.valid && error.equals(contactValidation.error);
        }

        return valid == contactValidation.valid;
    }
}