package net.olimpium.last_life_iii.utils;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataManager {
	private File configFile;

	private final File metaDataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/files.meta");

	private HashMap<File, JSONObject> dataList;
	public DataManager(File file){
		this.configFile = file;
	}

	public void addData(JSONObject data, File file){
		dataList.put(file, data);
		save(data, file);
	}
	public void setData(JSONObject data, File file){
		save(data, file);
		dataList.replace(file, data);
	}
	public JSONObject getData(File file){
		return dataList.get(file);
	}
	/**
	 * Adds an autosave that runs every X time
	 * @param period The time in ticks in witch the autoSave runs
	 */
	public void addAutoSave(int period){
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map.Entry<File, JSONObject> data : dataList.entrySet()){
					File file = data.getKey();
					JSONObject json = data.getValue();
					save(json, file);
				}
			}
		}.runTaskTimer(Last_life_III.getPlugin(), 0, period);

	}
	private void save() throws IOException {
		for (Map.Entry entry : dataList.entrySet()){
			JSONObject json = (JSONObject) entry.getValue();
			File file = (File)entry.getKey();
			save(json, file);
		}

		// Save the metadata
		saveMetadata();
	}
	public void load() throws Exception{
		Scanner scanner = new Scanner(metaDataFile);
		while (scanner.hasNextLine()){
			File file = new File(Last_life_III.getPlugin().getDataFolder() + scanner.nextLine());
			load(file);
		}
	}
	private void save(JSONObject data, File file) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(data.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void saveMetadata() throws IOException {
		FileWriter fileWriter = new FileWriter(metaDataFile);
		for (File file : dataList.keySet()){
			fileWriter.write(file.getPath());
		}
	}
	private void load(File file) throws Exception {
		JSONParser parser = new JSONParser();
		Scanner scanner = new Scanner(file);
		JSONObject jsonObject = (JSONObject) parser.parse(scanner.next());
		dataList.put(file, jsonObject);
	}

}
