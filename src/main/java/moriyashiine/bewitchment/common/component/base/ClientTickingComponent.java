/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

/** Ticked on the logical client after its entity ticks. */
public interface ClientTickingComponent extends Component {
	void clientTick();
}
