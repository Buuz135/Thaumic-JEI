package thaumcraft.api.casters;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class FocusUpgradeType {

	public static FocusUpgradeType[] types = new FocusUpgradeType[20];
	
	public short id;
	
	public ResourceLocation icon;
	
	public String name;
	
	public String text;

	private static int lastID = 0; 

	public FocusUpgradeType(ResourceLocation icon, String name, String text) {
		this.id = (short) lastID;	
		lastID++;
		this.icon = icon;
		this.name = name;
		this.text = text;
		
		// allocate space
		if (id>=types.length) {
			FocusUpgradeType[] temp = new FocusUpgradeType[id+1];
			System.arraycopy(types, 0, temp, 0, types.length);
			types = temp;
		}
		
		types[id] = this;
	}	
	
	public String getLocalizedName() {
		return I18n.translateToLocal(name);
	}
	
	public String getLocalizedText() {
		return I18n.translateToLocal(text);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FocusUpgradeType) {
			return this.id == ((FocusUpgradeType)obj).id;
		} else return false;
	}

	// basic upgrade types
	public static FocusUpgradeType potency = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/potency.png"),
			"focus.upgrade.potency.name","focus.upgrade.potency.text");
	
	public static FocusUpgradeType frugal = new FocusUpgradeType(
			new ResourceLocation("thaumcraft", "textures/foci/frugal.png"),
			"focus.upgrade.frugal.name","focus.upgrade.frugal.text");
	
	public static FocusUpgradeType treasure = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/treasure.png"),
			"focus.upgrade.treasure.name","focus.upgrade.treasure.text");
	
	public static FocusUpgradeType enlarge = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/enlarge.png"),
			"focus.upgrade.enlarge.name","focus.upgrade.enlarge.text");

	public static FocusUpgradeType alchemistsfire = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/alchemistsfire.png"),
			"focus.upgrade.alchemistsfire.name","focus.upgrade.alchemistsfire.text");
	
	public static FocusUpgradeType alchemistsfrost = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/alchemistsfrost.png"),
			"focus.upgrade.alchemistsfrost.name","focus.upgrade.alchemistsfrost.text");
	
	public static FocusUpgradeType architect = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/architect.png"),
			"focus.upgrade.architect.name","focus.upgrade.architect.text");
	
	public static FocusUpgradeType extend = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/extend.png"),
			"focus.upgrade.extend.name","focus.upgrade.extend.text");
	
	public static FocusUpgradeType silktouch = new FocusUpgradeType( 
			new ResourceLocation("thaumcraft", "textures/foci/silktouch.png"),
			"focus.upgrade.silktouch.name","focus.upgrade.silktouch.text");
	
	
}
