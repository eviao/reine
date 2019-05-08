package cn.eviao.reine.codec;

import cn.eviao.reine.bean.definition.Reine;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class ReineMessageCodec implements MessageCodec<Reine, Reine> {

    @Override
    public void encodeToWire(Buffer buffer, Reine reine) {
        JsonObject jsonToEncode = reine.toJson();
        String jsonToStr = jsonToEncode.encode();
        int length = jsonToStr.getBytes().length;

        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public Reine decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        String jsonStr = buffer.getString(pos += 4, pos += length);
        JsonObject json = new JsonObject(jsonStr);

        return Reine.of(json);
    }

    @Override
    public Reine transform(Reine reine) {
        return reine;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
