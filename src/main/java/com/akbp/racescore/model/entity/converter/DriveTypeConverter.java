package com.akbp.racescore.model.entity.converter;

import com.akbp.racescore.model.enums.DriveType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DriveTypeConverter implements AttributeConverter<DriveType, String> {
    @Override
    public String convertToDatabaseColumn(DriveType driveType) {
        return driveType == null ? null : String.valueOf(driveType.getId());
    }

    @Override
    public DriveType convertToEntityAttribute(String id) {
        return id == null ? null : Stream.of(DriveType.values())
                .filter(v -> v.getId() == Long.valueOf(id)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
