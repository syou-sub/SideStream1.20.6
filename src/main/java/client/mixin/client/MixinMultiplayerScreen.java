package client.mixin.client;

import client.Client;
import client.alts.Alt;
import client.ui.gui.altmanager.screens.AltManagerScreen;
import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen extends Screen
{
	@Unique
	private ButtonWidget altsButton;
    @Unique
    private ButtonWidget theAlteningButton;
    @Unique ButtonWidget theAlteningAPIButton;

    private String username;
	
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
                               username = loginWithTheAlteningClipboard();
                            } catch (IOException | UnsupportedFlavorException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .dimensions(300, 4, 400, 20).build());
        addDrawableChild(theAlteningAPIButton = ButtonWidget
                .builder(Text.literal("TheAltening API Login"),
                        b -> {
                            try {
                                username = loginWithTheAlteningAPIClipboard();
                            } catch (IOException | InterruptedException |
                                     URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .dimensions(300, 4, 400, 20).build());
	}
    @Unique
    public String loginWithTheAlteningClipboard() throws IOException, UnsupportedFlavorException {
        //Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // クリップボードの内容を取得
      //  Transferable t = clipboard.getContents(null);
        String text = MinecraftClient.getInstance().keyboard.getClipboard();
        // テキストとして取り出す
        if (text !=null &&text.contains("@alt.com")) {
                Alt alt = new Alt(text, "");
                alt.login();
            Client.altManager.alts.add(new Alt(text, ""));
                return text;
        }
        return null;
    }
    @Unique
    public String loginWithTheAlteningAPIClipboard() throws IOException, InterruptedException, URISyntaxException {
        String apiKey = MinecraftClient.getInstance().keyboard.getClipboard();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://api.thealtening.com/v2/generate?key=" + apiKey)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body().replace("\\\"", "");
        String[] sBody = body.split(":");
        String token = sBody[1];
        // テキストとして取り出す
        if (token !=null &&token.contains("@alt.com")) {
            Alt alt = new Alt(token, "");
            alt.login();
            Client.altManager.alts.add(new Alt(token, ""));
            return token;
        }
        return null;
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
