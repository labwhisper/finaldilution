package com.labessence.biotech.finaldilution.peripherals.datastores;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.labessence.biotech.finaldilution.component.Concentration;
import com.labessence.biotech.finaldilution.component.ConcentrationFactory;
import com.labessence.biotech.finaldilution.component.ConcentrationType;

import java.lang.reflect.Type;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 3/4/2018.
 */

class ConcentrationDeserializer implements JsonDeserializer<Concentration> {


    @Override
    public Concentration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws JsonParseException {
        ConcentrationType type = new Gson().fromJson(json.getAsJsonObject().get("type"), ConcentrationType.class);
        return ConcentrationFactory.createConcentration(type,
                json.getAsJsonObject().get("concentration").getAsDouble());
    }
}
