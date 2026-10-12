/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

/** Ticked on the logical server after its entity ticks. */
public interface ServerTickingComponent extends Component {
	void serverTick();
}
