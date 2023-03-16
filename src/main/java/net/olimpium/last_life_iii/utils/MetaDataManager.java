package net.olimpium.last_life_iii.utils;

import fr.skytasul.guardianbeam.Laser;

import java.util.ArrayList;
import java.util.List;

public class MetaDataManager {

	private List<DataManager> dataManagers = new ArrayList<>();
	public MetaDataManager(){}

	public void addDataManager(DataManager dataManager){
		dataManagers.add(dataManager);
	}
	public void removeDataManager(DataManager dataManager){
		dataManagers.add(dataManager);
	}
	public void loadAll(){
		dataManagers.forEach(dataManager -> dataManager.loadDataFromFile());
	}
	public void saveAll(){
		dataManagers.forEach(DataManager::writeToFile);
	}
}
