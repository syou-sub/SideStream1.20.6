/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package client.event.listeners;


import client.event.Event;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

public class EventReceiveMessage extends Event<EventReceiveMessage> {

    public Text message;
    public MessageIndicator indicator;
    public boolean modified;
    public int id;

    public EventReceiveMessage(Text message, MessageIndicator indicator, int id)
    {
        this.message = message;
        this.indicator = indicator;
        this.modified = false;
        this.id = id;
    }

    public Text getMessage() {
        return message;
    }
    public String getMessageLiteralString(){
        return message.getLiteralString();
    }
    public String getMessageString(){
        return message.getString();
    }

    public MessageIndicator getIndicator() {
        return indicator;
    }

    public void setMessage(Text message) {
        this.message = message;
        this.modified = true;
    }

    public void setIndicator(MessageIndicator indicator) {
        this.indicator = indicator;
        this.modified = true;
    }

    public boolean isModified() {
        return modified;
    }
}
