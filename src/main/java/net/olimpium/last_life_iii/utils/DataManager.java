package net.olimpium.last_life_iii.utils;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataManager {
	private final String fileName;
	private JSONObject jsonData;

	public DataManager(String fileName) {
		this.fileName = fileName;
		this.jsonData = new JSONObject();
	}

	public void setData(JSONObject newData) {
		jsonData = newData;
		writeToFile();
	}

	public void changeData(String key, Object value) {

		if (jsonData.containsKey(key)) {
			jsonData.replace(key, value);
		} else {
			jsonData.put(key, value);
		}
		writeToFile();
	}

	public void writeToFile() {
		try {
			if (!new File(fileName).exists()) new File(fileName).createNewFile();
		} catch (Exception e){
			e.printStackTrace();
		}

		try (FileWriter fileWriter = new FileWriter(fileName)) {
			fileWriter.write(compress(jsonData.toJSONString()));
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadDataFromFile() {
		try {
			String fileContents = decompress(new String(Files.readAllBytes(Paths.get(fileName))));
			JSONParser parser = new JSONParser();
			System.out.println("PATH: " + fileName);
			System.out.println("LOADED: " + fileContents);

			this.jsonData = (JSONObject) parser.parse(fileContents);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	public JSONObject getData() {
		return jsonData;
	}

	public static String beautify(String input) {
		int tabCount = 0;

		StringBuilder inputBuilder = new StringBuilder();
		char[] inputChar = input.toCharArray();

		for (int i = 0; i < inputChar.length; i++) {
			String charI = String.valueOf(inputChar[i]);
			if (charI.equals("}") || charI.equals("]")) {
				tabCount--;
				if (!String.valueOf(inputChar[i - 1]).equals("[") && !String.valueOf(inputChar[i - 1]).equals("{"))
					inputBuilder.append(newLine(tabCount));
			}
			inputBuilder.append(charI);

			if (charI.equals("{") || charI.equals("[")) {
				tabCount++;
				if (String.valueOf(inputChar[i + 1]).equals("]") || String.valueOf(inputChar[i + 1]).equals("}"))
					continue;

				inputBuilder.append(newLine(tabCount));
			}

			if (charI.equals(",")) {
				inputBuilder.append(newLine(tabCount));
			}
		}

		return inputBuilder.toString();
	}
	private static String newLine(int tabCount) {
		StringBuilder builder = new StringBuilder();

		builder.append("\n");
		for (int j = 0; j < tabCount; j++)
			builder.append("  ");

		return builder.toString();
	}

	public static String compress(String str) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
			gzipOutputStream.write(str.getBytes());
		}
		return Base64.getEncoder().encodeToString(outputStream.toByteArray());
	}

	public static String decompress(String compressedStr) throws IOException {
		byte[] compressedData = Base64.getDecoder().decode(compressedStr);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gzipInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		}
		return new String(outputStream.toByteArray());
	}
}