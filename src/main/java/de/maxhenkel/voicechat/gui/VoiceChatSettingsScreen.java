package de.maxhenkel.voicechat.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.widgets.*;
import de.maxhenkel.voicechat.voice.client.Client;
import de.maxhenkel.voicechat.voice.common.Utils;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class VoiceChatSettingsScreen extends VoiceChatScreenBase implements MicTestButton.MicListener {

    private static final Identifier TEXTURE = new Identifier(Voicechat.MODID, "textures/gui/gui_voicechat_settings.png");

    private double micValue;

    private VoiceActivationSlider voiceActivationSlider;

    public VoiceChatSettingsScreen() {
        super(new TranslatableText("gui.voicechat.voice_chat_settings.title"), 248, 226);
    }

    @Override
    protected void init() {
        super.init();

        voiceActivationSlider = new VoiceActivationSlider(guiLeft + 10, guiTop + 95, xSize - 20, 20);
        addButton(new VoiceSoundSlider(guiLeft + 10, guiTop + 20, xSize - 20, 20));
        addButton(new MicAmplificationSlider(guiLeft + 10, guiTop + 45, xSize - 20, 20));
        addButton(new MicActivationButton(guiLeft + 10, guiTop + 70, xSize - 20, 20, voiceActivationSlider));
        addButton(voiceActivationSlider);
        Client c = VoicechatClient.CLIENT.getClient();
        if (c != null) {
            addButton(new MicTestButton(guiLeft + 10, guiTop + 145, xSize - 20, 20, this, c));
        }
        addButton(new ButtonWidget(guiLeft + 10, guiTop + 170, xSize - 20, 20, new TranslatableText("message.voicechat.adjust_volumes"), button -> {
            client.openScreen(new AdjustVolumeScreen(this, VoicechatClient.CLIENT.getPlayerStateManager().getPlayerStates()));
        }));
        addButton(new ButtonWidget(guiLeft + 10, guiTop + 195, xSize / 2 - 15, 20, new TranslatableText("message.voicechat.select_microphone"), button -> {
            client.openScreen(new SelectMicrophoneScreen(this));
        }));
        addButton(new ButtonWidget(guiLeft + xSize / 2 + 6, guiTop + 195, xSize / 2 - 15, 20, new TranslatableText("message.voicechat.select_speaker"), button -> {
            client.openScreen(new SelectSpeakerScreen(this));
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        client.getTextureManager().bindTexture(TEXTURE);
        drawTexture(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);

        drawTexture(matrixStack, guiLeft + 10, guiTop + 120, 0, 244, xSize - 20, 20, 512, 512);
        drawTexture(matrixStack, guiLeft + 11, guiTop + 121, 0, 226, (int) ((xSize - 18) * micValue), 18, 512, 512);

        int pos = (int) ((xSize - 20) * Utils.dbToPerc(VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.get()));

        drawTexture(matrixStack, guiLeft + 10 + pos, guiTop + 120, 0, 244, 1, 20, 512, 512);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Text title = new TranslatableText("gui.voicechat.voice_chat_settings.title");
        int titleWidth = textRenderer.getWidth(title);
        textRenderer.draw(matrixStack, title.asOrderedText(), (float) (guiLeft + (xSize - titleWidth) / 2), guiTop + 7, FONT_COLOR);
    }

    @Override
    public void onMicValue(double perc) {
        this.micValue = perc;
    }
}