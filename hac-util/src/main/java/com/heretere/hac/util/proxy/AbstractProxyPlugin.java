package com.heretere.hac.util.proxy;

public abstract class AbstractProxyPlugin<T extends AbstractVersionProxy> {
    private final String basePackage;
    //
    private Class<T> proxyType;
    private T proxy;

    public AbstractProxyPlugin(String basePackage, Class<T> proxyType) {
        this.basePackage = basePackage;
        this.proxyType = proxyType;
    }
//
//    protected abstract void enable();
//
//    protected abstract void disable();
//
//    @Override
//    protected final void postDependencyEnable() {
//        this.loadVersionProxy();
//
//        this.enable();
//    }
//
//    @Override
//    protected final void postDependencyDisable() {
//        this.disable();
//    }
//
//    private void loadVersionProxy() {
//        Optional<Class<?>> versionProxyClass = VersionProcessor.getVersionProxy(this, this.basePackage);
//
//        versionProxyClass.ifPresent(clazz ->
//        {
//            try {
//                this.proxy = this.proxyType.cast(clazz.getConstructor().newInstance());
//            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                e.printStackTrace(); //TODO: Move to logger
//            }
//        });
//
//        if (!versionProxyClass.isPresent()) {
//            String currentVersion = VersionProcessor.getServerVersionString();
//            Set<String> packagedVersions = VersionProcessor.getPackagedVersions(this);
//
//
//        }
//    }
//
//    public T getProxy() {
//        return proxy;
//    }
}
