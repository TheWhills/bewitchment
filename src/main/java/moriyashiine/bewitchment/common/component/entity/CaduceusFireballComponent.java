/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;

public class CaduceusFireballComponent implements Component {
	private boolean fromCaduceus = false;

	@Override
	public void readFromNbt(CompoundTag tag) {
		setFromCaduceus(tag.getBoolean("FromCaduceus"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("FromCaduceus", isFromCaduceus());
	}

	public boolean isFromCaduceus() {
		return fromCaduceus;
	}

	public void setFromCaduceus(boolean fromCaduceus) {
		this.fromCaduceus = fromCaduceus;
	}
}
