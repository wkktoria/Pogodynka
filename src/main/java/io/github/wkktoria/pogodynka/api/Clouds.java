package io.github.wkktoria.pogodynka.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Clouds {
    @SerializedName("all")
    @Expose
    private Integer all;
}
