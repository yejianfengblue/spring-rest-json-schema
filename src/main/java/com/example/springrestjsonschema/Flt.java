package com.example.springrestjsonschema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Flt {

    @JsonIgnore
    Long id;

    String carrier;

    String fltNum;

    Status status;

//    @JsonCreator
    public Flt(String carrier, String fltNum) {

        this.carrier = carrier;
        this.fltNum = fltNum;
    }

    public static enum Status {

        DRAFT, SUBMITTED;
    }
}
