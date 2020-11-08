package com.heirteir.hac.api;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.google.common.collect.Lists;
import com.heirteir.hac.api.events.*;
import com.heirteir.hac.api.events.packets.wrapper.in.AbilitiesPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.EntityActionPacket;
import com.heirteir.hac.api.events.packets.wrapper.in.FlyingPacket;
import com.heirteir.hac.api.events.packets.wrapper.out.EntityVelocityPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.player.builder.DataManager;
import com.heirteir.hac.api.player.data.DataBuilderTest;
import com.heirteir.hac.api.player.data.DataTest;
import com.heirteir.hac.api.util.reflections.helper.StringHelper;
import com.heirteir.hac.api.util.reflections.types.WrappedClass;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class APITest {
    private static ServerMock server;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testAPI() {
        API api = API.INSTANCE;

        ServerVersion version = api.getReflections().getVersion();

        Assertions.assertTrue(version.greaterThanOrEqual(ServerVersion.MIN));
        Assertions.assertTrue(version.lessThanOrEqual(ServerVersion.MAX));
        Assertions.assertTrue(version.constraint(ServerVersion.MIN, ServerVersion.MAX));
        Assertions.assertEquals(ServerVersion.MAX, version);

        Assertions.assertNotNull(api.getReflections().getHelpers().getHelper(StringHelper.class));

        String testString1 = api.getReflections().getHelpers().getHelper(StringHelper.class).replaceString("%nms%.Entity");
        String testString2 = api.getReflections().getHelpers().getHelper(StringHelper.class).replaceString("%cb%.Entity");

        Assertions.assertNull(api.getReflections().getClassOrNull("dfksjafkdjsakfjdskafsdaf"));

        Assertions.assertEquals("net.minecraft.server.v" + version.getPackageName() + ".Entity", testString1);
        Assertions.assertEquals("org.bukkit.craftbukkit.v" + version.getPackageName() + ".Entity", testString2);

        WrappedClass floatTest = api.getReflections().getClass(float.class);

        Assertions.assertEquals(floatTest.getRaw(), float.class);

        WrappedClass entityPlayer = api.getReflections().getNMSClass("EntityPlayer");
        WrappedClass craftPlayer = api.getReflections().getCBClass("entity.CraftPlayer");

        Assertions.assertEquals("EntityPlayer", entityPlayer.getRaw().getSimpleName());
        Assertions.assertEquals("CraftPlayer", craftPlayer.getRaw().getSimpleName());

        Assertions.assertDoesNotThrow(() -> entityPlayer.getConstructorAtIndex(0));
        Assertions.assertDoesNotThrow(() -> craftPlayer.getConstructorAtIndex(0));

        Assertions.assertDoesNotThrow(() -> entityPlayer.getMethod("getBukkitEntity"));
        Assertions.assertDoesNotThrow(() -> craftPlayer.getMethod("getHandle"));

        Assertions.assertDoesNotThrow(() -> entityPlayer.getFieldByType(int.class, 0));
        Assertions.assertDoesNotThrow(() -> craftPlayer.getFieldByType(int.class, 0));

        Player bukkitPlayer = server.addPlayer();

        DataBuilderTest test = new DataBuilderTest();
        api.getHacPlayerList().getBuilder().registerDataBuilder(DataTest.class, test);

        HACPlayer player = api.getHacPlayerList().getPlayer(bukkitPlayer);
        DataManager dataManager = player.getDataManager();

        Assertions.assertNotNull(dataManager.getData(DataTest.class));

        AbilitiesPacketEventTest abilitiesPacketEventTest = (AbilitiesPacketEventTest) test.getEvents().asList().get(0);
        EntityActionEventTest entityActionEventTest = new EntityActionEventTest();
        EntityVelocityEventTest entityVelocityEventTest = new EntityVelocityEventTest();
        FlyingPacketEventTest flyingPacketEventTest = new FlyingPacketEventTest();

        /* insert randomly to insure it is being properly sorted by the event manager */
        List<AbstractPacketEvent<?>> events = Lists.newArrayList(
                entityActionEventTest,
                entityVelocityEventTest,
                flyingPacketEventTest
        );

        Collections.shuffle(events);

        events.forEach(api.getEventManager()::addEvent);

        events = api.getEventManager().getCurrentEvents();

        Assertions.assertEquals(abilitiesPacketEventTest, events.get(0));
        Assertions.assertEquals(entityActionEventTest, events.get(1));
        Assertions.assertEquals(entityVelocityEventTest, events.get(2));
        Assertions.assertEquals(flyingPacketEventTest, events.get(3));

        /* Reflections testing for latest version */
        api.getEventManager().run(player, AbilitiesPacket.DEFAULT);
        api.getEventManager().run(player, EntityActionPacket.DEFAULT);
        api.getEventManager().run(player, EntityVelocityPacket.DEFAULT);
        api.getEventManager().run(player, FlyingPacket.DEFAULT);

        Assertions.assertTrue(abilitiesPacketEventTest.isRun1());
        Assertions.assertFalse(abilitiesPacketEventTest.isRun2());

        Assertions.assertTrue(entityActionEventTest.isRun1());
        Assertions.assertFalse(entityActionEventTest.isRun2());

        Assertions.assertTrue(entityVelocityEventTest.isRun1());
        Assertions.assertFalse(entityVelocityEventTest.isRun2());

        Assertions.assertTrue(flyingPacketEventTest.isRun1());
        Assertions.assertTrue(flyingPacketEventTest.isRun2());

        Assertions.assertTrue(dataManager.getData(DataTest.class).isRan1());
        Assertions.assertTrue(dataManager.getData(DataTest.class).isRan2());
        Assertions.assertTrue(dataManager.getData(DataTest.class).isRan3());
        Assertions.assertTrue(dataManager.getData(DataTest.class).isRan4());

        api.getHacPlayerList().getBuilder().unregisterDataBuilder(DataTest.class);

        Assertions.assertNull(player.getDataManager().getData(DataTest.class));
    }

}
