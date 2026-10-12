/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.LargeSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class IncenseSmokeParticle extends LargeSmokeParticle {
	public IncenseSmokeParticle(ClientLevel clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
		super(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, spriteProvider);
		rCol = ((float) (Math.random() * 0.2) + 0.8f);
		gCol = ((float) (Math.random() * 0.2) + 0.8f);
		bCol = ((float) (Math.random() * 0.2) + 0.8f);
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			return new IncenseSmokeParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
