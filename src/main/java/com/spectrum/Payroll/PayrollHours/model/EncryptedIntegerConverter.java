package com.spectrum.Payroll.PayrollHours.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedIntegerConverter implements AttributeConverter<Integer, String> {
    @Override
    public String convertToDatabaseColumn(Integer attribute) {
        return attribute == null ? null : EncryptionUtil.encryptInt(attribute);
    }

    @Override
    public Integer convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EncryptionUtil.decryptInt(dbData);
    }
}