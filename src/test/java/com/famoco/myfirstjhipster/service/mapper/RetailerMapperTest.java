package com.famoco.myfirstjhipster.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RetailerMapperTest {

    private RetailerMapper retailerMapper;

    @BeforeEach
    public void setUp() {
        retailerMapper = new RetailerMapperImpl();
    }
}
