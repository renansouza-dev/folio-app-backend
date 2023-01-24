package com.renansouza.folio.fixedincome.converter;

import com.renansouza.folio.fixedincome.entities.Index;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IndexConverter implements AttributeConverter<Index, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Index index) {
        return switch (index) {
            case CDI -> 0;
            case IPCA -> 1;
            case IGPM -> 2;
        };
    }

    @Override
    public Index convertToEntityAttribute(Integer indexVal) {
        return switch (indexVal) {
            case 0 -> Index.CDI;
            case 1 -> Index.IPCA;
            case 2 -> Index.IGPM;
            default -> throw new IllegalStateException("Unexpected value: " + indexVal);
        };
    }
}
