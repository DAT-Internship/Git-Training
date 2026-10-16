package com.dat.cms.model;

import java.math.BigDecimal;
import java.time.LocalDate;
public record  ContractRow (

        Long contractId,
        String contractNo,
        String description,
        String partyName,
        String departmentName,
        BigDecimal contractValue,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {

}
