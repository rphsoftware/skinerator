package space.rph.skinerator;

import java.util.UUID;

public class CustomSkinDescriptor {
    public String signature;
    public UUID owner;
    public String data;

    public CustomSkinDescriptor(String signature, UUID owner, String data) {
        this.signature = signature;
        this.owner = owner;
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CustomSkinDescriptor{" +
                "signature='" + signature + '\'' +
                ", owner=" + owner +
                ", data='" + data + '\'' +
                '}';
    }
}
