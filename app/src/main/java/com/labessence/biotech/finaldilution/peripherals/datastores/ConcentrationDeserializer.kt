package com.labessence.biotech.finaldilution.peripherals.datastores

import com.google.gson.*
import com.labessence.biotech.finaldilution.component.concentration.Concentration
import com.labessence.biotech.finaldilution.component.concentration.ConcentrationFactory
import com.labessence.biotech.finaldilution.component.concentration.ConcentrationType
import java.lang.reflect.Type

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 3/4/2018.
 */

internal class ConcentrationDeserializer : JsonDeserializer<Concentration> {


    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Concentration? {
        val type = Gson().fromJson(json.asJsonObject.get("type"), ConcentrationType::class.java)
        return ConcentrationFactory.createConcentration(type,
                json.asJsonObject.get("concentration").asDouble)
    }
}
