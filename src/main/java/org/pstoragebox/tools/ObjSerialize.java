package org.pstoragebox.tools;

import java.io.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class ObjSerialize {
    //序列化
    public static String serializeToString(Object obj) throws IOException {
        var byteOut = new ByteArrayOutputStream();
        new ObjectOutputStream(byteOut).writeObject(obj);
        return byteOut.toString(ISO_8859_1);//此处只能是ISO-8859-1,但是不会影响中文使用
    }

    //反序列化
    public static Object deserializeToObject(String str) throws IOException, ClassNotFoundException {
        return new ObjectInputStream(new ByteArrayInputStream(str.getBytes(ISO_8859_1))).readObject();
    }
}
