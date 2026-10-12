/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import moriyashiine.bewitchment.common.registry.BWCommands;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypeInfos.class)
public abstract class ArgumentTypesMixin {
	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(Registry<ArgumentTypeInfo<?, ?>> registry, String id, Class<? extends A> clazz, ArgumentTypeInfo<A, T> serializer) {
		throw new UnsupportedOperationException();
	}

	@Inject(method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("RETURN"))
	private static void bewitchemnt$registerArgumentTypes(Registry<ArgumentTypeInfo<?, ?>> registry, CallbackInfoReturnable<ArgumentTypeInfo<?, ?>> cir) {
		register(registry, "bewitchment:fortune", BWCommands.FortuneArgumentType.class, SingletonArgumentInfo.contextFree(BWCommands.FortuneArgumentType::fortune));
		register(registry, "bewitchment:transformation", BWCommands.TransformationArgumentType.class, SingletonArgumentInfo.contextFree(BWCommands.TransformationArgumentType::transformation));
		register(registry, "bewitchment:contract", BWCommands.ContractArgumentType.class, SingletonArgumentInfo.contextFree(BWCommands.ContractArgumentType::contract));
		register(registry, "bewitchment:curse", BWCommands.CurseArgumentType.class, SingletonArgumentInfo.contextFree(BWCommands.CurseArgumentType::curse));
	}
}
