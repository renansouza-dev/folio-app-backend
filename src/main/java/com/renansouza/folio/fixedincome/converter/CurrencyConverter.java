package com.renansouza.folio.fixedincome.converter;

import com.renansouza.folio.fixedincome.entities.Currency;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Currency currency) {
        return switch (currency) {
            case BRL -> 0;
            case USD -> 1;
        };
    }

    @Override
    public Currency convertToEntityAttribute(Integer currencyVal) {
        return switch (currencyVal) {
            case 0 -> Currency.BRL;
            case 1 -> Currency.USD;
            default -> throw new IllegalStateException("Unexpected value: " + currencyVal);
        };
    }
}
