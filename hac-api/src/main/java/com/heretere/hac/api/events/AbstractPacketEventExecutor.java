package com.heretere.hac.api.events;

import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public abstract class AbstractPacketEventExecutor<T extends WrappedPacket> implements Comparable<AbstractPacketEventExecutor<T>> {
    private final Priority priority;
    private final String identifier;
    private final Class<T> wrappedClass;

    protected AbstractPacketEventExecutor(Priority priority, String identifier, PacketReferences.PacketReference<T> reference) {
        this.priority = priority;
        this.identifier = identifier + "_" + reference.getIdentifier();
        this.wrappedClass = reference.getWrappedPacketClass();
    }

    public boolean execute(@NotNull HACPlayer player, @NotNull Object packet) {
        return this.execute(player, this.wrappedClass.cast(packet));
    }

    public void onStop(@NotNull HACPlayer player, @NotNull Object packet) {
        this.onStop(player, this.wrappedClass.cast(packet));
    }

    protected abstract boolean execute(@NotNull HACPlayer player, @NotNull T packet);

    protected abstract void onStop(@NotNull HACPlayer player, @NotNull T packet);

    public Priority getPriority() {
        return this.priority;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Class<T> getWrappedClass() {
        return this.wrappedClass;
    }

    @Override
    public int compareTo(@NotNull AbstractPacketEventExecutor<T> o) {
        return Comparator.comparing((Function<AbstractPacketEventExecutor<T>, Priority>) AbstractPacketEventExecutor::getPriority)
                .thenComparing(AbstractPacketEventExecutor::getIdentifier)
                .compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractPacketEventExecutor<?> that = (AbstractPacketEventExecutor<?>) o;

        return new EqualsBuilder().append(priority, that.priority).append(identifier, that.identifier).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(priority).append(identifier).toHashCode();
    }
}
