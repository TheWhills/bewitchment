/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.particle;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;

public class CauldronBubbleParticle extends TextureSheetParticle {
	public CauldronBubbleParticle(ClientLevel clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ);
		setSize(0.02f, 0.02f);
		quadSize *= random.nextFloat() * 0.3F + 0.3F;
		this.xd *= 0.1;
		this.yd *= 0.1;
		this.zd *= 0.1;
		rCol = (float) velocityX;
		gCol = (float) velocityY;
		bCol = (float) velocityZ;
		lifetime = (int) (4 / (Math.random() * 0.8 + 0.2));
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		if (lifetime-- <= 0) {
			remove();
		} else {
			move(xd, yd, zd);
			xd *= 0.7;
			yd *= 0.7;
			zd *= 0.7;
			if (!(level.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() instanceof WitchCauldronBlock)) {
				remove();
			}
		}
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			CauldronBubbleParticle particle = new CauldronBubbleParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ);
			particle.pickSprite(spriteProvider);
			return particle;
		}
	}
}
