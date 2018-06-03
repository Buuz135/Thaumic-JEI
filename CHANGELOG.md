# 1.3.1
+ Added a check for items that dont have aspects to dont render in the tooltip

# 1.3.0
+ Updated forge and TC
+ Added a blacklist in the config to remove items from the Aspect From Itemstack checking
+ Aspects in the items tooltips now render in all GUI (Can be disabled in the gui)
+ Added a config option to disable the Aspect From ItemStack cache running every time you run the game. If its disabled it will only run if the file is missing. 
+ All tabs should be localized (TartaricAcid)

# 1.2.4
+ Sorted JEI tabs in a more logical way
+ JEI will now move away in some areas of the Research Table that are outside of the gui

# 1.2.3
+ Readded Aspect from Itemstack, on the first run a file will generate in the config folder that contains all the information for it (You can check the logs to see when is done generating). On the second run the aspects will show in JEI properly and the file will update again. The file is created in another thread so it won't slow down the game start.
+ Made the mod clientside only

# 1.2.2
+ Changed Aspect Ingredient to AspectList Ingredient so the amount can be rendered on top

# 1.2.1
+ Removed Arcane recipes from the normal crafting category and forced latest JEI so it doesn't show an empty page

# 1.2.0
+ Update to 1.12

# 1.1.3
+ Added a small tweak to slow down aspect creation if it is two fast

# 1.1.2
+ Made aspect checking a little bit faster (I dont think so)
+ Added descriptions for TC custom recipes that aren't shown in JEI (Thanks to thePalindrome for the descriptions)
+ Changed some display names

# 1.1.1
+ Changed how items display in the JEI search for the aspect until TC fixes tooltips not showing in not container GUI
+ Forced Tab icons

# 1.1
+ Aspects are now ingredients, they can be searched en JEI, see what 2 aspects make another aspect and what items create that aspect.
+ Arcane Workbench has a clickable area to check recipes and you can transfer recipes from the items in your inventory.
+ Fixed some recipes not working
+ WARNING: Checking what aspects all the items in the game has can be performance heavy but it won't add load time to the pack,, everything is done in a new thread and it might take a while to appear in game. Example: All The Mods 1.10 takes 3 min to check the aspects of all the items