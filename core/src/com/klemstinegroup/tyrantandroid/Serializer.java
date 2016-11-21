package com.klemstinegroup.tyrantandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Serializer {
	public static boolean serialize(String path, Object o) {
		FileHandle file = Gdx.files.external(path);
		OutputStream os = file.write(false);
		try {
			ObjectOutputStream oo = new ObjectOutputStream(os);
			oo.writeObject(o);
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void delete(String path) {
		FileHandle file1 = Gdx.files.external(path);
		file1.delete();
	}

	public static Object deserialize(String path, boolean internal) {
		FileHandle file1 = null;
		if (internal)
			file1 = Gdx.files.internal(path);
		else
			file1 = Gdx.files.external(path);
		InputStream os1 = file1.read();
		try {
			ObjectInputStream oo = new ObjectInputStream(os1);
			Object o = oo.readObject();
			os1.close();
			return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static boolean writeText(String path, String text) {
		FileHandle file = Gdx.files.external(path);
		OutputStream os = file.write(false);
		OutputStreamWriter ow = new OutputStreamWriter(os);

		try {
			ow.write(text);
			ow.close();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

}
