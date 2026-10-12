/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

/**
 * Client-side halves of Bewitchment's server-to-client packets. Only ever loaded on the
 * physical client: the packets reach these through {@code DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ...)},
 * and handlers run on the client main thread (registered with {@code consumerMainThread}).
 */
public final class ClientPacketHandlers {
	private ClientPacketHandlers() {
	}

	enum Particles {
		EXPLOSION(ParticleTypes.EXPLOSION, 8, true),
		PORTAL(ParticleTypes.PORTAL, 32, true),
		SMOKE(ParticleTypes.SMOKE, 32, true),
		SPECTERBANGLE(ParticleTypes.SMOKE, 1, false);

		final ParticleOptions particle;
		final int count;
		final boolean randomY;

		Particles(ParticleOptions particle, int count, boolean randomY) {
			this.particle = particle;
			this.count = count;
			this.randomY = randomY;
		}
	}

	static void spawnParticlesAround(int entityId, Particles kind) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) {
			return;
		}
		Entity entity = level.getEntity(entityId);
		if (entity != null) {
			for (int i = 0; i < kind.count; i++) {
				level.addParticle(kind.particle, entity.getRandomX(1), kind.randomY ? entity.getRandomY() : entity.getY(), entity.getRandomZ(1), 0, 0, 0);
			}
		}
	}

	static void syncContracts(SyncContractsPacket packet) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null) {
			return;
		}
		BWComponents.CONTRACTS_COMPONENT.maybeGet(client.player).ifPresent(contractsComponent -> {
			contractsComponent.getContracts().clear();
			ListTag contractsList = packet.contracts().getList("Contracts", Tag.TAG_COMPOUND);
			for (int i = 0; i < contractsList.size(); i++) {
				CompoundTag contractCompound = contractsList.getCompound(i);
				contractsComponent.addContract(new Contract.Instance(BWRegistries.CONTRACT.get(new ResourceLocation(contractCompound.getString("Contract"))), contractCompound.getInt("Duration"), contractCompound.getInt("Cost")));
			}
		});
	}

	static void syncDemonTrades(SyncDemonTradesPacket packet) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.level == null) {
			return;
		}
		AbstractContainerMenu menu = client.player.containerMenu;
		if (packet.syncId() == menu.containerId && menu instanceof DemonScreenHandler demonMenu) {
			demonMenu.demonMerchant.setCurrentCustomer(client.player);
			demonMenu.demonMerchant.setOffersClientside(packet.offers());
			demonMenu.demonMerchant.setDemonTraderClientside((LivingEntity) client.level.getEntity(packet.traderId()));
			demonMenu.demonMerchant.setDiscountClientside(packet.discount());
		}
	}

	static void syncHornedSpear(SyncHornedSpearPacket packet) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level != null && level.getEntity(packet.entityId()) instanceof HornedSpearEntity spear) {
			spear.spear = packet.spear();
		}
	}

	static void syncPoppetShelf(SyncPoppetShelfPacket packet) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) {
			return;
		}
		NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
		if (!packet.inventory().isEmpty()) {
			ContainerHelper.loadAllItems(packet.inventory(), inventory);
		}
		if (level.getBlockEntity(packet.pos()) instanceof PoppetShelfBlockEntity poppetShelf) {
			poppetShelf.clientInventory = inventory;
		}
	}
}
