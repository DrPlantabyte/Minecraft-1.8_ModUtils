/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cyano.minecraftutils.t2r;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFileChooser;

/**
 *
 * @author cybergnome
 */
public class ItemMaker {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(true);
		jfc.setDialogTitle("Select block texture files");
		int action = jfc.showOpenDialog(null);
		if(action != JFileChooser.APPROVE_OPTION){
			System.exit(0); // canceled by user
		}
		try{
			File[] files = jfc.getSelectedFiles();
			for(File f : files){
				supportItemIcon(f.toPath());
			}
		}catch(IOException ex){
			Logger.getLogger(ItemMaker.class.getName()).log(Level.SEVERE,"Error",ex);
			System.exit(ex.getClass().hashCode());
		}
	}
	private static final String KEY_MODID = "MODID";
	private static final String KEY_NAME = "ITEMNAME";
	
	private static final String TEMPLATE_ITEMMODEL = "{\n" +
"    \"parent\": \"builtin/generated\",\n" +
"    \"textures\": {\n" +
"        \"layer0\": \"MODID:items/ITEMNAME\"\n" +
"    },\n" +
"    \"display\": {\n" +
"        \"thirdperson\": {\n" +
"            \"rotation\": [ -90, 0, 0 ],\n" +
"            \"translation\": [ 0, 1, -3 ],\n" +
"            \"scale\": [ 0.55, 0.55, 0.55 ]\n" +
"        },\n" +
"        \"firstperson\": {\n" +
"            \"rotation\": [ 0, -135, 25 ],\n" +
"            \"translation\": [ 0, 4, 2 ],\n" +
"            \"scale\": [ 1.7, 1.7, 1.7 ]\n" +
"        }\n" +
"    }\n" +
"}";
	
	private static void supportItemIcon(Path iconFile) throws IOException{
		Path itemModelsDir = Paths.get(iconFile.getParent().getParent().getParent().toString(),"models","item");
		Files.createDirectories(itemModelsDir);

		String blockName = iconFile.getFileName().toString();
		blockName = blockName.substring(0, blockName.lastIndexOf("."));
		String modid = iconFile.getParent().getParent().getParent().getFileName().toString();

		
		Path itemmodel = Paths.get(itemModelsDir.toString(), blockName + ".json");
		if (!Files.exists(itemmodel)) {
			Logger.getLogger(ItemMaker.class.getName()).log(Level.INFO,"Writing file "+itemmodel);
			Files.newBufferedWriter(itemmodel).append(
					TEMPLATE_ITEMMODEL.replace(KEY_NAME, blockName).replace(KEY_MODID, modid))
					.close();
		}
		
	}
	
}
