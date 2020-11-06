package com.heirteir.hac.util.dependency.types;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.net.URL;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDependency {

    public abstract URL getUrl();

    public abstract String getName();

    @Override
    public abstract String toString();
}
