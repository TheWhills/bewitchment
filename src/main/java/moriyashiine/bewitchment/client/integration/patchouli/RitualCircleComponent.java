/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class RitualCircleComponent implements ICustomComponent {
	transient int x, y;
	final transient ResourceLocation[] circles = new ResourceLocation[2];
	IVariable inner;
	IVariable outer;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		if (circles[1] != null) {
			graphics.blit(circles[1], x, y, 0, 0, 64, 64, 64, 64);
		}
		graphics.blit(circles[0], x + 16, y + 16, 16, 16, 28, 28, 64, 64);
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		circles[0] = Bewitchment.id("textures/gui/patchouli/chalk/" + lookup.apply(inner).asString() + ".png");
		if (!lookup.apply(outer).asString().isEmpty()) {
			circles[1] = Bewitchment.id("textures/gui/patchouli/chalk/" + lookup.apply(outer).asString() + ".png");
		}
	}
}
