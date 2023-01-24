package com.renansouza.folio.fixedincome.converter;

import com.renansouza.folio.fixedincome.entities.Forma;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FormaConverter implements AttributeConverter<Forma, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Forma forma) {
        return switch (forma) {
            case PRE_FIXADO -> 0;
            case POS_FIXADO -> 1;
        };
    }

    @Override
    public Forma convertToEntityAttribute(Integer formaVal) {
        return switch (formaVal) {
            case 0 -> Forma.PRE_FIXADO;
            case 1 -> Forma.POS_FIXADO;
            default -> throw new IllegalStateException("Unexpected value: " + formaVal);
        };
    }
}
