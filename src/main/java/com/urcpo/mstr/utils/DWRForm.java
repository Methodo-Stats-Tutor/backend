package com.urcpo.mstr.utils;

import org.apache.commons.validator.routines.EmailValidator;



public class DWRForm {

    public String validerMel(String valeur) {
     // Get an EmailValidator
        EmailValidator validator = EmailValidator.getInstance();
        // Validate an email address
        boolean isAddressValid = validator.isValid(valeur);
      
        String resultat = isAddressValid ? "Valide" : "Invalide";
        return resultat;
      }
}
