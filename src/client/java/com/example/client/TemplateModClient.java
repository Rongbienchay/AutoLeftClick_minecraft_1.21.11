package com.example.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class TemplateModClient implements ClientModInitializer {

	public static boolean enabled = false;
	public static double delay = 0.1;

	private static long lastAttackTime = 0;
	private static boolean attackPressed = false;

	private static final KeyMapping OPEN_GUI =
			KeyBindingHelper.registerKeyBinding(
					new KeyMapping(
							"key.autoattack.open_gui",
							InputConstants.Type.KEYSYM,
							GLFW.GLFW_KEY_F8,
							KeyMapping.Category.MISC
					)
			);

	// Chuột trái
	private static final InputConstants.Key LEFT_MOUSE =
			InputConstants.Type.MOUSE.getOrCreate(GLFW.GLFW_MOUSE_BUTTON_LEFT);

	@Override
	public void onInitializeClient() {

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			// F8 mở GUI
			while (OPEN_GUI.consumeClick()) {
				client.setScreen(new AutoAttackScreen(client.screen));
			}

			// Nếu Auto Attack tắt
			if (!enabled || client.player == null || client.gameMode == null) {

				if (attackPressed) {
					InputConstants.Key key = LEFT_MOUSE;
					KeyMapping.set(key, false);
					attackPressed = false;
				}

				return;
			}

			long now = System.currentTimeMillis();

			// Nhấn chuột trái
			if (!attackPressed &&
					now - lastAttackTime >= delay * 1000.0) {

				KeyMapping.set(LEFT_MOUSE, true);

				attackPressed = true;
				lastAttackTime = now;
			}

			// Nhả chuột trái ở tick tiếp theo
			else if (attackPressed) {

				KeyMapping.set(LEFT_MOUSE, false);

				attackPressed = false;
			}
		});
	}
}