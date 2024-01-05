package org.speer.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {

    private boolean success;
    private String message;
    private T data;



}
