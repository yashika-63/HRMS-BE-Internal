package com.spectrum.Payroll.PayrollHours.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedDoubleConverter implements AttributeConverter<Double, String> {
    @Override
    public String convertToDatabaseColumn(Double attribute) {
        return attribute == null ? null : EncryptionUtil.encryptDouble(attribute);
    }

    @Override
    public Double convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EncryptionUtil.decryptDouble(dbData);
    }
}
