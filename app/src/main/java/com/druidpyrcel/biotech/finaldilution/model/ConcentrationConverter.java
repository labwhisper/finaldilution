package com.druidpyrcel.biotech.finaldilution.model;

import de.greenrobot.dao.DaoException;
import de.greenrobot.dao.converter.PropertyConverter;

public class ConcentrationConverter implements PropertyConverter<ConcentrationType, Integer> {
    @Override
    public ConcentrationType convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (ConcentrationType type : ConcentrationType.values()) {
            if (type.getValue() == databaseValue) {
                return type;
            }
        }
        throw new DaoException("Can't convert ConcentrationType from database value: " + databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(ConcentrationType entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        return entityProperty.getValue();
    }
}
