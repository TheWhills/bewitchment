/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

@SuppressWarnings("ConstantConditions")
public class DemonScreen extends AbstractContainerScreen<DemonScreenHandler> {
	private static final ResourceLocation HEARTS = new ResourceLocation("textures/gui/icons.png");

	public DemonScreen(DemonScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		for (Slot slot : getMenu().slots) {
			if (slot instanceof DemonScreenHandler.DemonTradeSlot tradeSlot) {
				DemonEntity.DemonTradeOffer offer = tradeSlot.getOffer();
				if (offer != null) {
					drawHearts(context, slot.x, slot.y, offer);
				}
			}
		}
		drawPortrait(context, mouseX, mouseY);
		renderTooltip(context, mouseX, mouseY);
	}

	private void drawPortrait(GuiGraphics context, int mouseX, int mouseY) {
		if (menu.demonMerchant.getDemonTrader() == null) {
			return;
		}
		int x = (width - imageWidth) / 2 + 56;
		int y = (height - imageHeight) / 2 + 16;
		Window window = minecraft.getWindow();
		int scissorX = unscale(x, window.getGuiScaledWidth(), window.getScreenWidth());
		int scissorY = unscale(window.getGuiScaledHeight() - (y + 68), window.getGuiScaledHeight(), window.getScreenHeight());
		int scissorWidth = unscale(64, window.getGuiScaledWidth(), window.getScreenWidth());
		int scissorHeight = unscale(64, window.getGuiScaledHeight(), window.getScreenHeight());
		RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
		context.blit(getBackground(), x, y, 176, 16, 64, 72);
		int height = (int) (menu.demonMerchant.getDemonTrader().getBbHeight() * 55);
		InventoryScreen.renderEntityInInventoryFollowsMouse(context, x + 32, y + height, 50, (float) (x + 32) - mouseX, (float) (y + 105 - 50) - mouseY, menu.demonMerchant.getDemonTrader());
		RenderSystem.disableScissor();
	}

	private static int unscale(int scaled, float windowScaled, int windowReal) {
		return (int) ((scaled / windowScaled) * windowReal);
	}

	@Override
	protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		context.blit(getBackground(), x, y, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
		context.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
	}

	private void drawHearts(GuiGraphics context, int x, int y, DemonEntity.DemonTradeOffer offer) {
		int cost = offer.getCost(menu.demonMerchant);
		int fullGroups = cost / 2;
		int heartX = (this.leftPos + x - 6 - ((cost - 1) / 2 * 4));
		int heartY = this.topPos + y + 18;
		context.pose().pushPose();
		for (int i = 0; i < cost; i++) {
			heartX += 9;
			context.blit(HEARTS, heartX, heartY, 16, 0, 9, 9);
			if (!BWComponents.CONTRACTS_COMPONENT.get(minecraft.player).hasContract(offer.getContract())) {
				if (fullGroups > 0) {
					fullGroups--;
					i++;
					context.blit(HEARTS, heartX, heartY, 52, 0, 9, 9);
				} else {
					context.blit(HEARTS, heartX, heartY, 61, 0, 9, 9);
				}
			} else if (fullGroups > 0) {
				fullGroups--;
				i++;
			}
		}
		context.pose().popPose();
	}

	@Override
	protected void init() {
		super.init();
		titleLabelX = (imageWidth - font.width(title)) / 2;
	}

	private ResourceLocation getBackground() {
		return Bewitchment.id(String.format("textures/gui/demon_trade_%d.png", menu.getOfferCount()));
	}
}
