package com.heretere.hac.api.util.reflections.version;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ServerVersion {
    MIN("1_8_R1"),
    EIGHT_R1("1_8_R1"),
    EIGHT_R2("1_8_R2"),
    EIGHT_R3("1_8_R3"),
    NINE_R1("1_9_R1"),
    NINE_R2("1_9_R2"),
    TEN_R1("1_10_R1"),
    ELEVEN_R1("1_11_R1"),
    TWELVE_R1("1_12_R1"),
    THIRTEEN_R1("1_13_R1"),
    THIRTEEN_R2("1_13_R2"),
    FOURTEEN_R1("1_14_R1"),
    FIFTEEN_R1("1_15_R1"),
    SIXTEEN_R1("1_16_R1"),
    SIXTEEN_R2("1_16_R2"),
    MAX("1_16_R2"),
    INVALID("");

    private final String packageName;

    public static ServerVersion fromPackage(String packageName) {
        return Arrays.stream(ServerVersion.values())
                .filter(version -> version.getPackageName().equals(packageName))
                .findFirst()
                .orElse(ServerVersion.INVALID);
    }

    public boolean greaterThanOrEqual(ServerVersion version) {
        return this.ordinal() >= version.ordinal();
    }

    public boolean lessThanOrEqual(ServerVersion version) {
        return this.ordinal() <= version.ordinal();
    }

    public boolean constraint(ServerVersion min, ServerVersion max) {
        return this.greaterThanOrEqual(min) && this.lessThanOrEqual(max);
    }
}
