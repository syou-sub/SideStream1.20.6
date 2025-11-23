package client.mixin.client;

import client.Client;
import client.alts.Alt;
import client.ui.gui.altmanager.screens.AltManagerScreen;
import client.utils.AlteningUtils;
import client.utils.Logger;
import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen
{
    @Shadow
    protected abstract void refresh();

    @Unique
	private ButtonWidget altsButton;
    @Unique
    private ButtonWidget theAlteningButton;
    @Unique ButtonWidget theAlteningAPIButton;
    @Unique
    private ButtonWidget theAlteningCopyButton;
@Unique
    private String username;
@Unique
    private String token;
	
	protected MixinMultiplayerScreen(Text title)
	{
		super(title);
        title = Text.of(username);
	}
	
	@Inject(at = @At("RETURN"), method = "init")
	private void onInitWidgetsNormal(CallbackInfo ci)
	{
		
		// add AltManager button
		addDrawableChild(altsButton = ButtonWidget
			.builder(Text.literal("Alt Manager"),
				b -> Objects.requireNonNull(client)
					.setScreen(new AltManagerScreen(this)))
			.dimensions(3, 4, 98, 20).build());
        addDrawableChild(theAlteningButton = ButtonWidget
                .builder(Text.literal("TheAltening Clipboard Login"),
                        b -> {
                            try {
                               token = loginWithTheAlteningClipboard();
                            } catch (IOException | UnsupportedFlavorException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .dimensions(100, 4, 150, 20).build());
        addDrawableChild(theAlteningAPIButton = ButtonWidget
                .builder(Text.literal("TheAltening API Login"),
                        b -> {
                            try {
                                token = loginWithTheAlteningAPIClipboard();
                            } catch (IOException | InterruptedException |
                                     URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .dimensions(300, 4, 150, 20).build());
        addDrawableChild(theAlteningCopyButton = ButtonWidget
                .builder(Text.literal("Copy TheAltening Current Token"),
                        b -> {
                    if(token != null) {
                        String text = token;
                        StringSelection selection = new StringSelection(text);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, null);
                    }
                        })
                .dimensions(500, 4, 300, 20).build());
	}
    @Unique
    public String loginWithTheAlteningClipboard() throws IOException, UnsupportedFlavorException {
        //Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // クリップボードの内容を取得
      //  Transferable t = clipboard.getContents(null);
        String text = MinecraftClient.getInstance().keyboard.getClipboard();
        // テキストとして取り出す
        if (text !=null && text.contains("@alt.com")) {
            AlteningUtils.login(text);
            Client.altManager.alts.add(new Alt(text, ""));
            this.refresh();
                return text;
        }
        return null;
    }
    @Unique
    public String loginWithTheAlteningAPIClipboard() throws IOException, InterruptedException, URISyntaxException {
        String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();
        if(!clipboard.contains("api"))
            return null;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://api.thealtening.com/v2/generate?key=" + clipboard)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();
        String[] bodies = response.body().split(",");
        String body = bodies[0].replace("\"", "");
        String[] body2 = body.split(":");
        String token = body2[1];
        Logger.logConsole(token);
        if (token != null && token.contains("@alt.com")) {
            AlteningUtils.login(token);
            Client.altManager.alts.add(new Alt(token, ""));
            this.refresh();
            return token;
        }
        return null;
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX,mouseY,delta);
        context.drawCenteredTextWithShadow(textRenderer,
                "Logged in as:"
                        + MinecraftClient.getInstance().getSession().getUsername()
                        + "    Token: " + token,
                width / 2, 14, 16777215);
    }

	
	@Unique
	public ButtonWidget getAltsButton()
	{
		return altsButton;
	}
    @Unique
    public ButtonWidget getTheAlteningButton()
    {
        return theAlteningButton;
    }
	
	@Unique
	public void setAltsButton(ButtonWidget altsButton)
	{
		this.altsButton = altsButton;
	}
    @Unique
    public void setTheAlteningButton(ButtonWidget theAlteningButton)
    {
        this.theAlteningButton = theAlteningButton;
    }
}
