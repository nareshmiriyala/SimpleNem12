package au.com.redenergy.parser;

import au.com.redenergy.csv.Reader;
import au.com.redenergy.validator.Validator;

/**
 * Created by nmiriyal on 16/02/2017.
 */
public final class SimpleNem12ParserImplBuilder {
    private Reader csvReader;
    private Validator nmiValidator;
    private Validator qualityValidator;
    private Validator energyUnitValidator;

    private SimpleNem12ParserImplBuilder() {
    }

    public static SimpleNem12ParserImplBuilder aSimpleNem12ParserImpl() {
        return new SimpleNem12ParserImplBuilder();
    }

    public SimpleNem12ParserImplBuilder withCsvReader(Reader csvReader) {
        this.csvReader = csvReader;
        return this;
    }

    public SimpleNem12ParserImplBuilder withNmiValidator(Validator nmiValidator) {
        this.nmiValidator = nmiValidator;
        return this;
    }

    public SimpleNem12ParserImplBuilder withQualityValidator(Validator qualityValidator) {
        this.qualityValidator = qualityValidator;
        return this;
    }

    public SimpleNem12ParserImplBuilder withEnergyUnitValidator(Validator energyUnitValidator) {
        this.energyUnitValidator = energyUnitValidator;
        return this;
    }

    public SimpleNem12ParserImpl build() {
        SimpleNem12ParserImpl simpleNem12ParserImpl = new SimpleNem12ParserImpl();
        simpleNem12ParserImpl.setCsvReader(csvReader);
        simpleNem12ParserImpl.setNmiValidator(nmiValidator);
        simpleNem12ParserImpl.setQualityValidator(qualityValidator);
        simpleNem12ParserImpl.setEnergyUnitValidator(energyUnitValidator);
        return simpleNem12ParserImpl;
    }
}
