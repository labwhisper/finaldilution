package com.druidpyrcel.biotech.finaldilutiondaogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class FinalDilutionDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.druidpyrcel.biotech.finaldilution.model");
        schema.enableKeepSectionsByDefault();
        addTables(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addTables(Schema schema) {
        Entity solution = addSolution(schema);
        Entity compound = addCompound(schema);
        Entity concentration = addConcentration(schema);
        Entity component = addComponent(schema);

        Property solutionId = component.addStringProperty("solutionName").notNull().getProperty();
        Property compoundId = component.addStringProperty("compoundShortName").notNull().getProperty();
        Property desConcId = component.addLongProperty("desConcId").notNull().getProperty();
        Property availConcId = component.addLongProperty("availConcId").getProperty();

        component.addToOne(solution, solutionId);
        component.addToOne(compound, compoundId);
        component.addToOne(concentration, desConcId, "desiredConcentration");
        component.addToOne(concentration, availConcId, "availableConcentration");
        ToMany solutionToComponents = solution.addToMany(component, solutionId);
        solutionToComponents.setName("components");
        //TODO add order Asc by Compound short name
    }

    private static Entity addSolution(Schema schema) {
        Entity solution = schema.addEntity("Solution");
        solution.addStringProperty("name").primaryKey();
        solution.addDoubleProperty("volume");
        return solution;
    }

    private static Entity addCompound(Schema schema) {
        Entity compound = schema.addEntity("Compound");
        compound.addStringProperty("shortName").primaryKey();
        compound.addDoubleProperty("molarMass");
        return compound;
    }

    private static Entity addConcentration(Schema schema) {
        Entity concentration = schema.addEntity("Concentration");
        concentration.addIdProperty().primaryKey();
        concentration.addDoubleProperty("amount");
        concentration.addIntProperty("type").customType("com.druidpyrcel.biotech.finaldilution.model.Concentration.ConcentrationType", "com.druidpyrcel.biotech.finaldilution.model.Concentration.ConcentrationConverter"
        );
        return concentration;
    }

    private static Entity addComponent(Schema schema) {
        Entity component = schema.addEntity("Component");
        component.addBooleanProperty("fromStock");
        return component;
    }

}
