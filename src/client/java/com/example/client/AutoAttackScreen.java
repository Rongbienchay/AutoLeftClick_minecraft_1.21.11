package com.example.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AutoAttackScreen extends Screen {

    private final Screen parent;

    public AutoAttackScreen(Screen parent) {
        super(Component.literal("Auto Attack"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        // ON / OFF
        this.addRenderableWidget(
                Button.builder(
                        getToggleText(),
                        button -> {
                            TemplateModClient.enabled =
                                    !TemplateModClient.enabled;

                            button.setMessage(getToggleText());
                        }
                ).bounds(
                        this.width / 2 - 100,
                        this.height / 2 - 60,
                        200,
                        20
                ).build()
        );

        // Delay slider
        this.addRenderableWidget(
                new DelaySlider(
                        this.width / 2 - 100,
                        this.height / 2 - 20,
                        200,
                        20
                )
        );

        // DONE
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("DONE"),
                        button -> this.onClose()
                ).bounds(
                        this.width / 2 - 100,
                        this.height / 2 + 20,
                        200,
                        20
                ).build()
        );
    }

    private Component getToggleText() {
        return Component.literal(
                "Auto Attack: " +
                        (TemplateModClient.enabled ? "ON" : "OFF")
        );
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        guiGraphics.fill(
                0,
                0,
                this.width,
                this.height,
                0xAA000000
        );

        guiGraphics.drawCenteredString(
                this.font,
                Component.literal("AUTO ATTACK"),
                this.width / 2,
                this.height / 2 - 100,
                0xFFFFFF
        );

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class DelaySlider extends AbstractSliderButton {

        public DelaySlider(
                int x,
                int y,
                int width,
                int height
        ) {
            super(
                    x,
                    y,
                    width,
                    height,
                    Component.literal("Delay: 0.1 s"),
                    getSliderValue()
            );
        }

        private static double getSliderValue() {
            return (TemplateModClient.delay - 0.1) / 4.9;
        }

        @Override
        protected void updateMessage() {

            double delay =
                    0.1 + (this.value * 4.9);

            delay =
                    Math.round(delay * 10.0) / 10.0;

            this.setMessage(
                    Component.literal(
                            String.format(
                                    "Delay: %.1f s",
                                    delay
                            )
                    )
            );
        }

        @Override
        protected void applyValue() {

            double delay =
                    0.1 + (this.value * 4.9);

            delay =
                    Math.round(delay * 10.0) / 10.0;

            TemplateModClient.delay = delay;
        }
    }
}