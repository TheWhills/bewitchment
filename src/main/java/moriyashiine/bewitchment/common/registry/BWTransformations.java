/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.transformation.VampireTransformation;
import moriyashiine.bewitchment.common.transformation.WerewolfTransformation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class BWTransformations {
	public static final DeferredRegister<Transformation> TRANSFORMATIONS = DeferredRegister.create(BWRegistries.TRANSFORMATION_KEY, Bewitchment.MOD_ID);

	public static final RegistryObject<Transformation> HUMAN = create("human", () -> new Transformation());
	public static final RegistryObject<Transformation> VAMPIRE = create("vampire", () -> new VampireTransformation());
	public static final RegistryObject<Transformation> WEREWOLF = create("werewolf", () -> new WerewolfTransformation());

	private static <T extends Transformation> RegistryObject<T> create(String name, Supplier<T> value) {
		return TRANSFORMATIONS.register(name, value);
	}
}
