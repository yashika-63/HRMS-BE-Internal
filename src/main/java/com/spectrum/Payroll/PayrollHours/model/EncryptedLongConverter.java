package com.spectrum.Payroll.PayrollHours.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedLongConverter implements AttributeConverter<Long, String> {
    @Override
    public String convertToDatabaseColumn(Long attribute) {
        return attribute == null ? null : EncryptionUtil.encryptLong(attribute);
    }

    @Override
    public Long convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EncryptionUtil.decryptLong(dbData);
    }
}
