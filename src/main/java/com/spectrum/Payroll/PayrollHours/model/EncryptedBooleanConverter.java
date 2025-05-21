package com.spectrum.Payroll.PayrollHours.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedBooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute == null ? null : EncryptionUtil.encryptBoolean(attribute);
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EncryptionUtil.decryptBoolean(dbData);
    }
}